package org.eclipse.babel.core.message.checks;

public class MessageCheckResult implements IMessageCheckResult {

	public static final IMessageCheckResult OK = new MessageCheckResult("", null);//$NON-NLS-1$

	private String text;
	private IMessageCheck messageCheck;

	public MessageCheckResult(String text, IMessageCheck messageCheck) {
		this.text = text;
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

}
