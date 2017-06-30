package com.multiplus.bot;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
public class LineBotApplication {
	
	private static final Log LOGGER = LogFactory.getLog(LineBotApplication.class);
	
	/**
	 * application bootstrap point.
	 * @param args args
	 */
	public static void main(String[] args) {
		SpringApplication.run(LineBotApplication.class, args);
	}
	
	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		return new TextMessage(event.getMessage().getText());
	}
	
	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		// not in use
		LOGGER.debug("event: " + event);
	}
}
