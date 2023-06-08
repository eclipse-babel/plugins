package org.eclipse.babel.editor.tree.actions;

import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.babel.editor.util.ClipboardUtil;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Event;

public class PasteKeyAction extends AbstractTreeAction {

	public PasteKeyAction(AbstractMessagesEditor editor, TreeViewer treeViewer) {
		super(editor, treeViewer);

        setText(MessagesEditorPlugin.getString("key.paste")); //$NON-NLS-1$
        setImageDescriptor(UIUtils.getImageDescriptor(UIUtils.IMAGE_ADD));
	}

	@Override
	public void runWithEvent(Event event) {
		MessagesBundleGroup bundleGroup = getBundleGroup();
        String clipboardContent = ClipboardUtil.getFromClipboard(event.display);
        if(clipboardContent != null) {
        	ClipboardUtil.deserializeKeys(bundleGroup, clipboardContent);
        }
	}
}
