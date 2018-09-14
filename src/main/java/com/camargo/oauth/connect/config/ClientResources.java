package com.camargo.oauth.connect.config;

import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

public class ClientResources {
	@NestedConfigurationProperty
	private AuthorizationCodeResourceDetails client;

	@NestedConfigurationProperty
	private ResourceServerProperties resource;

	public AuthorizationCodeResourceDetails getClient() {
		return client;
	}

	public ResourceServerProperties getResource() {
		return resource;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(AuthorizationCodeResourceDetails client) {
		this.client = client;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(ResourceServerProperties resource) {
		this.resource = resource;
	}

}
