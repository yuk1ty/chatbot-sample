package com.multiplus.bot.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@LineMessageHandler
public class DefaultMessageHandler {

	private static final Log LOGGER = LogFactory.getLog(ConversationHandler.class);
	
	/**
	 * Handling not supporting messages
	 * @param event {@link Event}
	 */
	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		// not in use
		LOGGER.debug("event: " + event);
	}
}
