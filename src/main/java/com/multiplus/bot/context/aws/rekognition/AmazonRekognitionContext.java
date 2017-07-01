package com.multiplus.bot.context.aws.rekognition;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.multiplus.bot.context.aws.credential.AWSCredentialContext;

import okhttp3.ResponseBody;

public abstract class AmazonRekognitionContext {
	
	protected final AmazonRekognition rekognitionClient;
	
	protected AmazonRekognitionContext(AWSCredentialContext credentialContext) {
		this.rekognitionClient = AmazonRekognitionClientBuilder.standard() //
				.withRegion(Regions.US_WEST_2) //
				.withCredentials(new AWSStaticCredentialsProvider(credentialContext.getCredentials())) //
				.build();
	}
	
	/**
	 * Get Amazon Rekognition Client Class.
	 * @return {@link AmazonRekognition}
	 */
	public AmazonRekognition getRekognition() {
		return rekognitionClient;
	}
	
	// TODO Type, Arguments, etc...
	public abstract AmazonRekognition rekognite(ResponseBody content);
}
