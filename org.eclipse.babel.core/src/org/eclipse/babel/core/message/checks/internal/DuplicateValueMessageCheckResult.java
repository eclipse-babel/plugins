package org.eclipse.babel.core.message.checks.internal;

import org.eclipse.babel.core.message.checks.IMessageCheck;
import org.eclipse.babel.core.message.checks.MessageCheckResult;

public class DuplicateValueMessageCheckResult extends MessageCheckResult {

	private String[] duplicateKeys;

	public DuplicateValueMessageCheckResult(String[] duplicateKeys, IMessageCheck messageCheck) {
		super("", messageCheck);
		this.duplicateKeys = duplicateKeys;
	}

	public String[] getDuplicateKeys() {
		return this.duplicateKeys;
	}
	
}
