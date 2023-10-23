package org.eclipse.babel.core.message.checks.internal;

import org.eclipse.babel.core.message.checks.IMessageCheck;
import org.eclipse.babel.core.message.checks.MessageCheckResult;

public class MissingValueCheckResult extends MessageCheckResult {

	public MissingValueCheckResult(IMessageCheck messageCheck) {
		super("", messageCheck);
	}

}
