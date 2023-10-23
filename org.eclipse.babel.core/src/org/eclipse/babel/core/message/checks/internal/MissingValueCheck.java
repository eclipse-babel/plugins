/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.core.message.checks.internal;

import java.util.Locale;

import org.eclipse.babel.core.message.IMessage;
import org.eclipse.babel.core.message.IMessagesBundleGroup;
import org.eclipse.babel.core.message.checks.IMessageCheck;
import org.eclipse.babel.core.message.checks.IMessageCheckResult;
import org.eclipse.babel.core.message.checks.MessageCheckResult;

/**
 * Visitor for finding if a key has at least one corresponding bundle entry with
 * a missing value.
 * 
 * @author Pascal Essiembre (pascal@essiembre.com)
 */
public class MissingValueCheck implements IMessageCheck {

	/** The singleton */
	public static final MissingValueCheck INSTANCE = new MissingValueCheck();

	/**
	 * Constructor.
	 */
	private MissingValueCheck() {
		super();
	}

	/**
	 * @see org.eclipse.babel.core.message.internal.checks.IMessageCheck#checkKey(org.eclipse.babel.core.message.internal.MessagesBundleGroup,
	 *      String, Locale, org.eclipse.babel.core.message.internal.Message)
	 */
	public IMessageCheckResult checkKey(IMessagesBundleGroup messagesBundleGroup, String key, Locale locale,
			IMessage message) {
		if (message == null || message.getValue() == null || message.getValue().length() == 0) {
			return new MissingValueCheckResult(key, locale, message, this);
		}
		return MessageCheckResult.OK;
	}

}
