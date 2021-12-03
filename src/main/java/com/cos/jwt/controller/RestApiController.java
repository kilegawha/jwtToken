package com.cos.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// @CrossOrigin 어노테이션을 사용해도 되지만, @CrossOrigin은 인증이 필요한 요청을  다 거부한다.
// 로그인을 해야만 하는 요청은 @CrossOrigin를 사용한다고 해결이 안된다.
@RequiredArgsConstructor
@RestController
public class RestApiController {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	@GetMapping("home")
	public String home() {
		return "<h1>home</h1>";
	}
	
	@PostMapping("token")
	public String token() {
		return "<h1>token</h1>";
	}
	@PostMapping("join")
	public String join(@RequestBody User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRoles("ROLE_USER"); 
		userRepository.save(user);
		return "회원가입 완료";
	}
	
	//user권한만 접근가능
	@GetMapping("/api/v1/user")
	public String user(Authentication authentication) {
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		System.out.println("principal : "+principal.getUser().getId());
		System.out.println("principal : "+principal.getUser().getUsername());
		System.out.println("principal : "+principal.getUser().getPassword());
		return "user";
	}
	
	//manager권한과 admin권한만 접근가능
	@GetMapping("/api/v1/manager")
	public String manager() {
		return "manager";
	}
	
	//admin권한만 접근가능
	@GetMapping("/api/v1/admin")
	public String admin() {
		return "admin";
	}
}
