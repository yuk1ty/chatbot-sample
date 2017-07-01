package com.multiplus.bot.context.aws.rekognition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.multiplus.bot.context.aws.credential.AWSCredentialContext;

@Component
public class AmazonRekognitionContext {
	
	private final AmazonRekognition rekognitionClient;
	
	/**
	 * Constructor.
	 * @param credentialComponent {@link AWSCredentialContext}
	 */
	@Autowired
	public AmazonRekognitionContext(AWSCredentialContext credentialComponent) {
		this.rekognitionClient = AmazonRekognitionClientBuilder.standard() //
				.withRegion(Regions.US_EAST_2) //
				.withCredentials(new AWSStaticCredentialsProvider(credentialComponent.getCredentials())) //
				.build();
	}
	
	/**
	 * Get Amazon Rekognition Client Class.
	 * @return {@link AmazonRekognition}
	 */
	public AmazonRekognition getRekognition() {
		return rekognitionClient;
	}
}
