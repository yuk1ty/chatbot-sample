package com.multiplus.bot.context.aws.rekognition;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Smile;
import com.amazonaws.util.IOUtils;
import com.multiplus.bot.context.aws.credential.AWSCredentialContext;
import com.multiplus.bot.model.entity.aws.result.smile.FaceDetectionResult;
import com.multiplus.bot.model.exception.aws.smile.FaceDetectionContextException;

import okhttp3.ResponseBody;

@Component
public class FaceDetectionRekognitionContext extends AmazonRekognitionContext<FaceDetectionResult> {

	@Autowired
	public FaceDetectionRekognitionContext(AWSCredentialContext credentialContext) {
		super(credentialContext);
	}

	/** {@inheritDoc} */
	@Override
	public FaceDetectionResult rekognite(ResponseBody content) {
		try {
			DetectFacesResult detectFacesResult = callDetectFaces(content);
			Smile detectedSmile = detectFacesResult.getFaceDetails().stream() //
					.map(FaceDetail::getSmile) //
					.filter(Objects::nonNull) //
					.findFirst() //
					.orElse(null);

			return handleMessage(detectedSmile);
		} catch (FaceDetectionContextException e) {
			return new FaceDetectionResult(e.getMessage(), e);
		}
	}

	private DetectFacesResult callDetectFaces(ResponseBody content)
			throws FaceDetectionContextException {
		try {
			DetectFacesRequest request = new DetectFacesRequest()
					.withImage(new Image().withBytes(getBufferedImage(content))) //
					.withAttributes(Attribute.ALL);
			return rekognitionClient.detectFaces(request);
		} catch (IOException | AmazonRekognitionException e) {
			e.printStackTrace();
			throw new FaceDetectionContextException(e.getMessage());
		}
	}

	private ByteBuffer getBufferedImage(ResponseBody content) throws IOException {
		ByteBuffer imageBytes;
		try (InputStream inputStream = content.byteStream()) {
			imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
		}
		return imageBytes;
	}

	private FaceDetectionResult handleMessage(Smile smile) throws FaceDetectionContextException {
		if (Objects.isNull(smile)) {
			return new FaceDetectionResult("Can't detect a face in your photo, because of the detected smile is null.");
		}
		return smile.isValue() ? new FaceDetectionResult("Smiling!") : new FaceDetectionResult("Not smiling...");
	}
}
