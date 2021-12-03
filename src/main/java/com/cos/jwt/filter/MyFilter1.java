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

public class MyFilter1 implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		System.out.println("req.getMethod() : "+req.getMethod());
		
		// 토큰 : 코스  (만들었다고 가정함.)
		System.out.println("필터1");
		/*
		if (req.getMethod().equals("POST")) {
			System.out.println("POST요청됨");
			String headerAuth = req.getHeader("Authorization");
			System.out.println(headerAuth);	
			if(headerAuth.equals("cos")) {
				chain.doFilter(req, res);
				}else {
					PrintWriter outPrintWriter = res.getWriter();
					outPrintWriter.println("인증 안됨");
					chain.doFilter(req, res);
				}
		}else {
			chain.doFilter(req, res);
		}	
	
		*/
		chain.doFilter(req, res);
	}
}
