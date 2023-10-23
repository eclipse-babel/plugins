package org.eclipse.babel.core.message.checks.internal;

import org.eclipse.babel.core.message.checks.IMessageCheck;
import org.eclipse.babel.core.message.checks.MessageCheckResult;

public class SimilarValueMessageCheckResult extends MessageCheckResult {

	private String[] similarValueKeys;

	public SimilarValueMessageCheckResult(String[] similarValueKeys, IMessageCheck messageCheck) {
		super("", messageCheck);
		this.similarValueKeys = similarValueKeys;
	}

	public String[] getSimilarMessageKeys() {
		return similarValueKeys;
	}

}
