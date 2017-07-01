package com.multiplus.bot.handlers;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@LineMessageHandler
public class ConversationHandler {
	
	/**
	 * Handling messages by text format via conversation API.
	 * 
	 * @param event {@link MessageEvent}
	 * @return {@link TextMessage}
	 */
	@EventMapping
	public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		return new TextMessage(event.getMessage().getText());
	}
}
