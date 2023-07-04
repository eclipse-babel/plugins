package org.eclipse.babel.editor.util;

import java.nio.charset.Charset;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

public class TranslationTransfer extends ByteArrayTransfer {

	private static TranslationTransfer instance = null;

	private static final String RESOURCE_BUNDLE__TRANSLATION = "resourcebundle-translations"; //$NON-NLS-1$
	private static final int RESOURCE_BUNDLE__TRANSLATION__ID = registerType(RESOURCE_BUNDLE__TRANSLATION);

	private TranslationTransfer() {
	}

	public synchronized static TranslationTransfer getInstance() {
		if(TranslationTransfer.instance == null) {
			TranslationTransfer.instance = new TranslationTransfer();
		}
		return TranslationTransfer.instance;
	}

	@Override
	protected void javaToNative(Object object, TransferData transferData) {
			String content = (String)object;
			byte[] bytes = content.getBytes(Charset.forName("UTF-8"));
			super.javaToNative(bytes, transferData);
	}

	@Override
	protected Object nativeToJava(TransferData transferData) {

		byte[] bytes = (byte[]) super.nativeToJava(transferData);
		String content = new String(bytes, Charset.forName("UTF-8"));
		return content;
	}
	
	@Override
	protected int[] getTypeIds() {
		return new int[] { RESOURCE_BUNDLE__TRANSLATION__ID };
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] { RESOURCE_BUNDLE__TRANSLATION };
	}
}
