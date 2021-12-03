package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.CorsFilter;

import com.cos.jwt.config.jwt.JwtAuthenticationFilter;
import com.cos.jwt.config.jwt.JwtAuthorizationFilter;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration  //Ioc할 수 있게 해줌
@EnableWebSecurity	// security를 활성화해준다.
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final CorsFilter corsFilter;
	private final UserRepository userRepository;
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	@Override
	protected void configure(HttpSecurity http) throws Exception {
	//	http.addFilterBefore(new MyFilter1(), BasicAuthenticationFilter.class);
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  //session을 사용하지 않겠다
		.and()
		.addFilter(corsFilter)   // @CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
		.formLogin().disable()  //form 태그를 만들어서 로그인하는 것을 사용하지않는다.
		.httpBasic().disable()	//기본적인 http로그인방법을 사용하지 않겠다.
		.addFilter(new JwtAuthenticationFilter(authenticationManager()))	// JwtAuthenticationFilter가 꼭 전달해야하는 파라미터(AuthenticationManager)가 있다.
		.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))  // 그 이유는 AuthenticationManager를 통해서 로그인을 진행하기 때문이다.
		.authorizeRequests()		//jwt를 사용하려면 이게 기본이다
		.antMatchers("/api/v1/user/**")
		.access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		.antMatchers("/api/v1/manager/**")
		.access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
		.antMatchers("/api/v1/admin/**")
		.access("hasRole('ROLE_ADMIN')")
		.anyRequest().permitAll();  //다른 요청은 전부다 권한없이 들어갈 수 있다.
	}
}
