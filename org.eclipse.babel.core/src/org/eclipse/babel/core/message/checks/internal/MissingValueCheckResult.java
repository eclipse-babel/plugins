package org.eclipse.babel.core.message.checks.internal;

import java.text.MessageFormat;
import java.util.Locale;

import org.eclipse.babel.core.message.IMessage;
import org.eclipse.babel.core.message.checks.IMessageCheck;
import org.eclipse.babel.core.message.checks.MessageCheckResult;

public class MissingValueCheckResult extends MessageCheckResult {

	public MissingValueCheckResult(String key, Locale locale, IMessage message, IMessageCheck messageCheck) {
		super("", key, locale, message, messageCheck);
	}

	@Override
	public String getText() {
		return MessageFormat.format( "The key \"{0}\" missing a value", this.getKey());
	}
}
