package com.multiplus.bot.context.line.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.spring.boot.LineBotAutoConfiguration;

@Component
public class LineMessagingContext {

	private final LineMessagingService lineMessagingService;

	@Autowired
	public LineMessagingContext(LineBotAutoConfiguration lineBotAutoConfiguration) {
		this.lineMessagingService = LineMessagingServiceBuilder.create(lineBotAutoConfiguration.channelTokenSupplier()).build();
	}

	/**
	 * Get {@link LineMessagingService}.
	 * @return {@link LineMessageingService}
	 */
	public LineMessagingService getLineMessagingService() {
		return lineMessagingService;
	}
}
