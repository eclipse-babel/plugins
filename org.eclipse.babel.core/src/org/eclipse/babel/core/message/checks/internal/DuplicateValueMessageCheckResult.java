package org.eclipse.babel.core.message.checks.internal;

import java.text.MessageFormat;
import java.util.Locale;

import org.eclipse.babel.core.message.IMessage;
import org.eclipse.babel.core.message.checks.IMessageCheck;
import org.eclipse.babel.core.message.checks.MessageCheckResult;

public class DuplicateValueMessageCheckResult extends MessageCheckResult {

	private String[] duplicateKeys;

	public DuplicateValueMessageCheckResult(String[] duplicateKeys, String key, Locale locale, IMessage message, IMessageCheck messageCheck) {
		super("", key, locale, message, messageCheck);
		this.duplicateKeys = duplicateKeys;
	}

	public String[] getDuplicateKeys() {
		return this.duplicateKeys;
	}
	
	@Override
	public String getText() {
		String introduction = MessageFormat.format( "The key \"{0}\" has the same value as the following key(s):\n", this.getKey());

        StringBuilder buf = new StringBuilder(introduction);

        if (this.duplicateKeys != null) { // keys = duplicated values
        	for( String duplKey : this.duplicateKeys ) {
                buf.append("\t- ");
                buf.append(duplKey);
                buf.append("\n");
        	}
        }

        return buf.toString();
	}
	
}
