package com.multiplus.bot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfiguration {

	private final ConfigurationEntity configuration;
	
	@Autowired
	public ApplicationConfiguration(ConfigurationEntity configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * Get credential access key for AWS.
	 * @return access key
	 */
	public String getCredentialAccessKey() {
		return configuration.getAccessKey();
	}
	
	/**
	 * Get credential secret key for AWS.
	 * @return secret key
	 */
	public String getCredentialSecretKey() {
		return configuration.getSecretKey();
	}
}
