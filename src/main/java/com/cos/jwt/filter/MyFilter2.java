package com.cos.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter2 implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		System.out.println("req.getMethod() : " + req.getMethod());

		// 토큰 cos 이걸 만들어줘야함. .id, pw 정상적으로 들어와서 로그인이 완료되면 토큰을
		// 만들어주고 그 걸 응답해준다.
		// 요청할 때마다header 에 Authorization에 value값으로 토큰을 가지고 온다.
		// 그때만큼토큰이 넘어오면 이 이 토큰이 내가 바로 만든 토큰이 맞는지만 검증하면 됨
		System.out.println("필터2");
		
		/*
		if (req.getMethod().equals("POST")) {
			System.out.println("POST요청됨");
			String headerAuth = req.getHeader("Authorization");
			System.out.println(headerAuth);
			if (headerAuth.equals("cos")) {
				chain.doFilter(req, res);
			} else {
				PrintWriter outPrintWriter2 = res.getWriter();
				outPrintWriter2.println("인증 안됨");
				chain.doFilter(req, res);
			}
		}else {
			chain.doFilter(req, res);
		}
		*/
		chain.doFilter(req, res);
	}
}
