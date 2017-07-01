package com.multiplus.bot.context.credential;

import java.util.Objects;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.auth.AWSCredentials;
import com.multiplus.bot.configuration.ApplicationConfiguration;
import com.multiplus.bot.configuration.ConfigurationEntity;
import com.multiplus.bot.context.aws.credential.AWSCredentialContext;

public class AWSCredentialComponentTest {

	@Test
	public void testCredentialFetching() {
		ConfigurationEntity configEntity = new ConfigurationEntity();
		ApplicationConfiguration configuration = new ApplicationConfiguration(configEntity);
		AWSCredentialContext wrappedCredentials = new AWSCredentialContext(configuration);
		AWSCredentials credentials = wrappedCredentials.getCredentials();
		Assert.assertTrue(Objects.nonNull(credentials));
	}
}
