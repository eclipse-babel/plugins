package org.eclipse.babel.core.message.checks;

import java.util.Locale;

import org.eclipse.babel.core.message.IMessage;
/**
 * A result of a test performed by IMessageCheck
 */
public interface IMessageCheckResult {
	/**
	 * Textual presentation of the check result. Used in UI
	 */
	String getText();

	/**
	 * The key that was tested by IMessageCheck
	 */
	String getKey();

	/**
	 * The locale that was tested by IMessageCheck
	 */
	Locale getLocale();
	
	/**
	 * The IMessage that was tested, this value can be null
	 */
	IMessage getMessage();

	/**
	 * The IMessageCheck that was used when testing
	 */
	IMessageCheck getMessageCheck();
}
