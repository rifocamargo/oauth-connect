package com.camargo.oauth.connect.config;

import java.util.Arrays;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.filter.CompositeFilter;

@Configuration
@EnableOAuth2Client
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class })
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private Filter facebookFilter;

	@Autowired
	private Filter githubFilter;

	@Autowired
	private Filter googleFilter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.antMatcher("/**")
			.authorizeRequests()
				.antMatchers("/", "/login**", "/webjars/**", "/error**", "/css/**", "/images/**", "/js/**")
					.permitAll()
				.anyRequest()
					.authenticated()
					.and()
						.exceptionHandling()
						.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
					.and()
						.logout()
							.logoutSuccessUrl("/")
						.permitAll()
					.and()
						.csrf()
							.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
					.and()
						.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
		// @formatter:on
	}

	public Filter ssoFilter() {
		final CompositeFilter filter = new CompositeFilter();
		filter.setFilters(Arrays.asList(this.facebookFilter, this.githubFilter, this.googleFilter));
		return filter;
	}
}
