package com.multiplus.bot.context.aws.credential;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.multiplus.bot.configuration.ApplicationConfiguration;

@Component
public class AWSCredentialContext {

	private final AWSCredentials credentials;

	@Autowired
	public AWSCredentialContext(ApplicationConfiguration configuration) {
		this.credentials = new BasicAWSCredentials( //
				configuration.getCredentialAccessKey(), configuration.getCredentialSecretKey());
	}

	/**
	 * Get AWS Credential.
	 * 
	 * @return {@link AWSCredentials}
	 */
	public AWSCredentials getCredentials() {
		return credentials;
	}
}
