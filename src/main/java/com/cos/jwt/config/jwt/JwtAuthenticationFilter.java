package com.cos.jwt.config.jwt;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;


//스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음
// /login 요청해서 username,password전송하면(post)
// UsernamePasswordAuthenticationFilter 동작을 함.
// security설정에서 .formLogin().disable()했기 때문에 로그인을 하게 되면 동작을 하지 않는다.
// 하지만 다시 동작하게 하려면 Security설정에서 다시 필터등록을 하면된다.

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;
	
	// /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter : 로그인 시도중 ");
		
		// 1. username, password받아서
		try {
	//		System.out.println("request.getInputStream() : " + request.getInputStream());  //request.getInputStream() 안에 id, pw가 담겨있다.
//			BufferedReader br = request.getReader();
//			String input = null;
//			while((input = br.readLine()) != null) {
//				System.out.println(input);
//			}
			//ObjectMapper가 json데이터를 파싱해준다.
			ObjectMapper om = new ObjectMapper(); 
			User user = om.readValue(request.getInputStream(), User.class);
			System.out.println("ObjectMapper 방식 : " + user);
			// 토큰만들기
			//form로그인하면 자동으로 다 해주지만 우리가 직접해야 하기때문에 토큰만들어야한다.
			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		
			//PrinciaplDetaisService의 loadUserByUsername()함수가 실행됨..
			// DB에 있는 username과 password가 일치한다.
			Authentication authentication = 
					authenticationManager.authenticate(authenticationToken);
						
		// authentication 객체가 session영역에 저장됨 => 로그인이 되었다는 뜻
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("JwtAuthenticationFilter PrincipalDetails" + principalDetails.getUser().getUsername());	//로그인 정상적으로 됨
		System.out.println("==================================");
		// authentication 객체가 session 영역에 저장을 해야하고 그방법이 return해주면 됨
		// 리턴의 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는거임
		// 굳이 JWT토큰을 사용하면서 세션을 만들 이유가 없음. 근데 단지 권한 처리때문에 session을 넣어준다.
		
		return authentication;
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("=====================================================");
		// 2. 정상인지 로그인 시도를 해보는 것 authenticationManager로 로그인 시도를 하면
		// PrincipalDetaisService가 호출이되고 loadUserByUsername()함수가 실행됨.
		return null;
		
		// 3. PrincipalDetails를 세션에 담고 (세션에 PrincipalDetails를 담지 않으면 권한 관리가 안된다.)
		
		// 4. JWT토큰을 만들어서 응답해주면 됨.
	}
	// attemptAuthentication실행 후 인증이 정상적으로 되었으면 successfulAuthentication 함수가 실행된다.
	//JWT토큰을 만들어서 request요청한 사용자에게 JWT토큰을 response해주면 됨.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
		
		// RSA방식은 아니고 Hash암호방식
		String jwtToken = JWT.create()
				.withSubject(principalDetails.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10)))
				.withClaim("id", principalDetails.getUser().getId())
				.withClaim("username", principalDetails.getUser().getUsername())
				.sign(Algorithm.HMAC512("cos"));
					
		System.out.println("successfulAuthentication 실행됨 : 인증이 완료되었다는 뜻");
		response.addHeader("Authorization", "Bearer " + jwtToken);
	}
}
