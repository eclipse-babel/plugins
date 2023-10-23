package org.eclipse.babel.core.message.checks.internal;

import java.text.MessageFormat;
import java.util.Locale;

import org.eclipse.babel.core.message.IMessage;
import org.eclipse.babel.core.message.checks.IMessageCheck;
import org.eclipse.babel.core.message.checks.MessageCheckResult;

public class SimilarValueMessageCheckResult extends MessageCheckResult {

	private String[] similarValueKeys;

	public SimilarValueMessageCheckResult(String[] similarValueKeys, String key, Locale locale, IMessage message,  IMessageCheck messageCheck) {
		super("", key, locale, message, messageCheck);
		this.similarValueKeys = similarValueKeys;
	}

	public String[] getSimilarMessageKeys() {
		return similarValueKeys;
	}

	@Override
	public String getText() {
		String introduction = MessageFormat.format( "The key \"{0}\" has a simular value as the following key(s):\n", this.getKey());

        StringBuilder buf = new StringBuilder(introduction);

        if (this.similarValueKeys != null) { // keys = duplicated values
        	for( String duplKey : this.similarValueKeys ) {
                buf.append("\t- ");
                buf.append(duplKey);
                buf.append("\n");
        	}
        }

        return buf.toString();
	}
}
