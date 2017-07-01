package com.multiplus.bot.handlers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.multiplus.bot.context.aws.rekognition.AmazonRekognitionContext;
import com.multiplus.bot.context.line.messaging.LineMessagingContext;

import okhttp3.ResponseBody;
import retrofit2.Response;

@LineMessageHandler
public class FaceEmotionHandler {

	private final LineMessagingContext lineMessagingContext;
	
	private final AmazonRekognitionContext rekognitionContext;
	
	@Autowired
	public FaceEmotionHandler(LineMessagingContext lineMessagingContext, AmazonRekognitionContext rekognitionContext) {
		this.lineMessagingContext = lineMessagingContext;
		this.rekognitionContext = rekognitionContext;
	}
	
	/**
	 * Handling messages with images via Face Emotion API
	 * 
	 * @param event {@link MessageEvent}
	 * @return {@link TextMessage}
	 */
	@EventMapping
	public TextMessage handleImageMessage(MessageEvent<ImageMessageContent> event) {
		try {
			Response<ResponseBody> res = lineMessagingContext.getLineMessagingService()
					.getMessageContent(event.getMessage().getId())
					.execute();
			
			if (res.isSuccessful()) {
				return executeEmotionalRecognition(res.body());
			} else {
				return new TextMessage("Sorry, can't read your image file.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new TextMessage("Sorry, unexpected internal error has been occurerd.");
		}
	}
	
	private TextMessage executeEmotionalRecognition(ResponseBody content) {
		return new TextMessage("Successfully to read the image file.");
	}
}
