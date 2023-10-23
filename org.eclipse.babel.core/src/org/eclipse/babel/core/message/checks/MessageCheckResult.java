package org.eclipse.babel.core.message.checks;

import java.util.Locale;

import org.eclipse.babel.core.message.IMessage;

public class MessageCheckResult implements IMessageCheckResult {

	public static final IMessageCheckResult OK = new MessageCheckResult("", null, null, null, null); //$NON-NLS-1$

	private String text;
	private IMessageCheck messageCheck;

	private String key;

	private Locale locale;

	private IMessage message;

	public MessageCheckResult(String text, String key, Locale locale, IMessage message, IMessageCheck messageCheck) {
		this.text = text;
		this.key = key;
		this.locale = locale;
		this.message = message;
		this.messageCheck = messageCheck;
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public IMessageCheck getMessageCheck() {
		return this.messageCheck;
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public Locale getLocale() {
		return this.locale;
	}

	@Override
	public IMessage getMessage() {
		return this.message;
	}

}
