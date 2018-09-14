package com.camargo.oauth.connect.config;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedPrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
public class FiltersConfiguration {

	@Bean
	public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(
			final OAuth2ClientContextFilter filter) {
		final FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
		registration.setFilter(filter);
		registration.setOrder(-100);
		return registration;
	}

	@Bean
	@ConfigurationProperties("github")
	public ClientResources githubClientResources() {
		return new ClientResources();
	}

	@Bean
	@ConfigurationProperties("facebook")
	public ClientResources facebookClientResources() {
		return new ClientResources();
	}

	@Bean
	@ConfigurationProperties("google")
	public ClientResources googleClientResources() {
		return new ClientResources();
	}

	@Bean
	public Filter githubFilter(final OAuth2ClientContext oauth2ClientContext,
			final ClientResources githubClientResources) {
		return createSocialFilter(oauth2ClientContext, githubClientResources, "/login/github",
				new FixedPrincipalExtractor(), new SavedRequestAwareAuthenticationSuccessHandler());
	}

	@Bean
	public Filter facebookFilter(final OAuth2ClientContext oauth2ClientContext,
			final ClientResources facebookClientResources) {
		return createSocialFilter(oauth2ClientContext, facebookClientResources, "/login/facebook",
				new FixedPrincipalExtractor(), new SavedRequestAwareAuthenticationSuccessHandler());
	}

	@Bean
	public Filter googleFilter(final OAuth2ClientContext oauth2ClientContext,
			final ClientResources googleClientResources) {
		return createSocialFilter(oauth2ClientContext, googleClientResources, "/login/google",
				new GooglePrincipalExtractor(), new SavedRequestAwareAuthenticationSuccessHandler());
	}

	private Filter createSocialFilter(final OAuth2ClientContext oauth2ClientContext, final ClientResources clientResources,
			final String path, final PrincipalExtractor principalExtractor,
			final AuthenticationSuccessHandler authenticationSuccessHandler) {
		final OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationFilter = new OAuth2ClientAuthenticationProcessingFilter(
				path);
		final OAuth2RestTemplate oAuth2RestTemplate = new OAuth2RestTemplate(clientResources.getClient(), oauth2ClientContext);
		oAuth2ClientAuthenticationFilter.setRestTemplate(oAuth2RestTemplate);

		final UserInfoTokenServices tokenServices = new UserInfoTokenServices(clientResources.getResource().getUserInfoUri(),
				clientResources.getClient().getClientId());
		tokenServices.setRestTemplate(oAuth2RestTemplate);
		tokenServices.setPrincipalExtractor(principalExtractor);
		oAuth2ClientAuthenticationFilter.setTokenServices(tokenServices);

		oAuth2ClientAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
		return oAuth2ClientAuthenticationFilter;
	}
}
