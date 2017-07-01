package com.multiplus.bot.context.aws.rekognition;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.services.rekognition.model.FaceDetail;
import com.amazonaws.services.rekognition.model.FaceRecord;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.IndexFacesRequest;
import com.amazonaws.services.rekognition.model.IndexFacesResult;
import com.amazonaws.services.rekognition.model.Smile;
import com.amazonaws.util.IOUtils;
import com.multiplus.bot.context.aws.credential.AWSCredentialContext;
import com.multiplus.bot.domain.aws.result.smile.SmileResult;
import com.multiplus.bot.exception.smile.SmilingDetectionContextException;

import okhttp3.ResponseBody;

@Component
public class SimlingDetectionRekognitionContext extends AmazonRekognitionContext<SmileResult> {

	private static final String COLLECTION_ID = "collection-id";

	@Autowired
	public SimlingDetectionRekognitionContext(AWSCredentialContext credentialContext) {
		super(credentialContext);
	}

	@Override
	public SmileResult rekognite(ResponseBody content) {
		try {
			IndexFacesResult indexFacesResult = callIndexFaces(content);

			List<Smile> smiles = indexFacesResult.getFaceRecords().stream() //
					.map(FaceRecord::getFaceDetail) //
					.map(FaceDetail::getSmile) //
					.collect(Collectors.toList());

			if (smiles.size() > 1) {
				throw new SmilingDetectionContextException( //
						"Sorry, this application can't handle more than one person's faces.");
			}

			Smile detectedSmile = smiles.stream() //
					.findFirst() //
					.orElseThrow(() -> new SmilingDetectionContextException("No face found."));
			return handleMessage(detectedSmile);
		} catch (IOException | SmilingDetectionContextException e) {
			return new SmileResult(e.getMessage(), e);
		}
	}

	private IndexFacesResult callIndexFaces(ResponseBody content) throws IOException {
		IndexFacesRequest indexFacesRequest = new IndexFacesRequest() //
				.withImage(new Image().withBytes(getBufferedImage(content))) //
				.withCollectionId(COLLECTION_ID);
		return rekognitionClient.indexFaces(indexFacesRequest);
	}

	private ByteBuffer getBufferedImage(ResponseBody content) throws IOException {
		ByteBuffer imageBytes;
		try (InputStream inputStream = content.byteStream()) {
			imageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
		}
		return imageBytes;
	}
	
	private SmileResult handleMessage(Smile smile) throws SmilingDetectionContextException {
		if (Objects.isNull(smile)) {
			throw new SmilingDetectionContextException("No face found.");
		}
		
		return smile.isValue() ? new SmileResult("Smiling!") : new SmileResult("Not smiling...");
	}
}
