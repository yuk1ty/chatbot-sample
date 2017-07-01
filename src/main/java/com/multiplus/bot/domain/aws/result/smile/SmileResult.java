package com.multiplus.bot.domain.aws.result.smile;

import java.util.Objects;

public class SmileResult {

	private final String resultMessage;

	private final Throwable exception;

	public SmileResult(String resultMessage) {
		this.resultMessage = Objects.requireNonNull(resultMessage);
		this.exception = null;
	}
	
	public SmileResult(String resultMessage, Throwable exception) {
		this.resultMessage = Objects.requireNonNull(resultMessage);
		this.exception = exception;
	}

	public String getResultMessage() {
		return handleMessage(resultMessage);
	}

	private String handleMessage(String resultMessage) {
		if (Objects.nonNull(exception)) {
			exception.printStackTrace();
			return "Unexpected Error has been occured: " + resultMessage;
		}
		
		return resultMessage;
	}
}
