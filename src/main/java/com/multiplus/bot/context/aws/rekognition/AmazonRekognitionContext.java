package com.multiplus.bot.context.aws.rekognition;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.util.IOUtils;
import com.multiplus.bot.context.aws.credential.AWSCredentialContext;

import okhttp3.ResponseBody;

public abstract class AmazonRekognitionContext<T> {

	protected final AmazonRekognition rekognitionClient;

	protected AmazonRekognitionContext(AWSCredentialContext credentialContext) {
		this.rekognitionClient = AmazonRekognitionClientBuilder.standard() //
				.withRegion(Regions.US_WEST_2) //
				.withCredentials(new AWSStaticCredentialsProvider(credentialContext.getCredentials())) //
				.build();
	}

	protected ByteBuffer getBufferedImage(ResponseBody content) throws IOException {
		ByteBuffer imageBytes;
		try (InputStream inputStream = content.byteStream()) {
			imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
		}
		return imageBytes;
	}
	
	/**
	 * execute recognition.
	 * 
	 * @param content
	 *            {@link ResponseBody}
	 * @return each messaging result dtos.
	 */
	public abstract T rekognite(ResponseBody content);
}
