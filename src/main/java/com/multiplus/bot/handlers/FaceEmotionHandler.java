package com.multiplus.bot.handlers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.util.IOUtils;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.multiplus.bot.context.aws.rekognition.SimlingDetectionRekognitionContext;
import com.multiplus.bot.context.line.messaging.LineMessagingContext;

import okhttp3.ResponseBody;
import retrofit2.Response;

@LineMessageHandler
public class FaceEmotionHandler {

	private final LineMessagingContext lineMessagingContext;

	private final SimlingDetectionRekognitionContext rekognitionContext;

	@Autowired
	public FaceEmotionHandler(LineMessagingContext lineMessagingContext, SimlingDetectionRekognitionContext rekognitionContext) {
		this.lineMessagingContext = lineMessagingContext;
		this.rekognitionContext = rekognitionContext;
	}

	/**
	 * Handling messages with images via Face Emotion API
	 * 
	 * @param event
	 *            {@link MessageEvent}
	 * @return {@link TextMessage}
	 */
	@EventMapping
	public TextMessage handleImageMessage(MessageEvent<ImageMessageContent> event) {
		try {
			Response<ResponseBody> res = lineMessagingContext.getLineMessagingService()
					.getMessageContent(event.getMessage().getId()).execute();

			if (res.isSuccessful()) {
				return executeEmotionalRecognition(res.body());
			} else {
				return new TextMessage("Sorry, can't read your image file.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new TextMessage("Sorry, file I/O error has been occurerd.");
		} catch (AmazonRekognitionException e) {
			e.printStackTrace();
			return new TextMessage("Sorry, unexpected error has been occured.");
		}
	}

	private TextMessage executeEmotionalRecognition(ResponseBody content) throws IOException, AmazonRekognitionException {
		rekognitionContext.rekognite(content);
		return new TextMessage("Successfully to read the image file.");
	}	
}
