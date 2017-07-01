package com.multiplus.bot.context.aws.rekognition;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.util.IOUtils;
import com.multiplus.bot.context.aws.credential.AWSCredentialContext;

import okhttp3.ResponseBody;

@Component
public class SimlingDetectionRekognitionContext extends AmazonRekognitionContext {

	@Autowired
	public SimlingDetectionRekognitionContext(AWSCredentialContext credentialContext) {
		super(credentialContext);
	}

	@Override
	public AmazonRekognition rekognite(ResponseBody content) {
		try {
			DetectLabelsRequest request = new DetectLabelsRequest()
					.withImage(new Image().withBytes(getBufferedImage(content))) //
					.withMaxLabels(10) //
					.withMinConfidence(77F);
			
			DetectLabelsResult result = super.getRekognition().detectLabels(request);
			List<Label> labels = result.getLabels();
			for (Label label : labels) {
				System.out.println(label.getName() + ": " + label.getConfidence().toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return super.getRekognition();
	}
	
	private ByteBuffer getBufferedImage(ResponseBody content) throws IOException {
		ByteBuffer imageBytes;
		try (InputStream inputStream = content.byteStream()) {
			imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
		}

		return imageBytes;
	}
}
