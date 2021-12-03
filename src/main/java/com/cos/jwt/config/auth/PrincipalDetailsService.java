package com.cos.jwt.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;
// http://localhost8080/login  로그인 요청올 때 loadUserByUsername가 동작한다.
// security설정에서 .formLogin().disable()했기 때문에 로그인을 하게 되면 동작을 하지 않는다.
// 직접PrincipalDetaisService를 때려주는 Filter를 만들어야 한다.
//   /login 요청올 때 동작하는 이유는 스프링 시큐리티가 기본으로 로그인요청주소가 /login이기 때문이다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{

	private final UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService의 loadUserByUsername 실행됨");
		User userEntity = userRepository.findByUsername(username);
		System.out.println("loadUserByUsername userEntity : " + userEntity);
		return new PrincipalDetails(userEntity);
	}
	

}
