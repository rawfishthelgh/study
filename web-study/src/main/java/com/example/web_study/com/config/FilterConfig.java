package com.example.web_study.com.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.web_study.com.filter.JwtAuthFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

	private final JwtAuthFilter jwtAuthFilter;

	@Bean
	public FilterRegistrationBean<JwtAuthFilter> jwtFilter() {
		FilterRegistrationBean<JwtAuthFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(jwtAuthFilter);
		registration.addUrlPatterns("/api/*");
		registration.addUrlPatterns("/admin/*");
		registration.setOrder(1);
		return registration;
	}
}

