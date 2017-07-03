package com.multiplus.bot.context.aws.rekognition;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.rekognition.model.AgeRange;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.Attribute;
import com.amazonaws.services.rekognition.model.DetectFacesRequest;
import com.amazonaws.services.rekognition.model.DetectFacesResult;
import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.Gender;
import com.amazonaws.services.rekognition.model.GenderType;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Smile;
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

			AgeRange detectedAgeRange = detectFacesResult.getFaceDetails().stream() //
					.map(FaceDetail::getAgeRange) //
					.filter(Objects::nonNull) //
					.findFirst() //
					.orElse(null);

			Gender detectedGender = detectFacesResult.getFaceDetails().stream() //
					.map(FaceDetail::getGender) //
					.filter(Objects::nonNull) //
					.findFirst() //
					.orElse(null);

			return handleMessage(detectedSmile, detectedAgeRange, detectedGender);
		} catch (FaceDetectionContextException e) {
			return new FaceDetectionResult(e.getMessage(), e);
		}
	}

	private DetectFacesResult callDetectFaces(ResponseBody content) throws FaceDetectionContextException {
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

	private FaceDetectionResult handleMessage(Smile smile, AgeRange ageRange, Gender gender)
			throws FaceDetectionContextException {
		if (Objects.isNull(smile) || Objects.isNull(ageRange) || Objects.isNull(gender)) {
			return new FaceDetectionResult("Can't detect a face in your photo, because of the detected smile is null.");
		}
		
		StringBuilder messageBuilder = new StringBuilder();
		
		messageBuilder.append(buildGenderMessage(gender));
		messageBuilder.append(buildSmileMessage(smile));
		messageBuilder.append(buildAgeRangeMessage(ageRange));
		
		return new FaceDetectionResult(messageBuilder.toString());
	}
	
	private StringBuilder buildGenderMessage(Gender gender) {
		StringBuilder messageBuilder = new StringBuilder();
		
		switch (GenderType.fromValue(gender.getValue().toUpperCase())) {
		case MALE:
			messageBuilder.append("He is ");
			break;
		case FEMALE:
			messageBuilder.append("She is ");
			break;
		default:
			break;
		}
		
		return messageBuilder;
	}
	
	private StringBuilder buildSmileMessage(Smile smile) {
		StringBuilder messageBuilder = new StringBuilder();
		
		if (smile.isValue()) {
			messageBuilder.append("smiling and ");
		} else {
			messageBuilder.append("not smiling and ");
		}
		
		return messageBuilder;
	}
	
	private StringBuilder buildAgeRangeMessage(AgeRange ageRange) {
		StringBuilder messageBuilder = new StringBuilder();
		messageBuilder.append(ageRange.getLow());
		messageBuilder.append(" to ");
		messageBuilder.append(ageRange.getHigh());
		messageBuilder.append(" years old range.");
		return messageBuilder;
	}
}
