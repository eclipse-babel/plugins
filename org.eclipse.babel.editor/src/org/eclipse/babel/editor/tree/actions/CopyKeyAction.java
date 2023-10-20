package org.eclipse.babel.editor.tree.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.babel.core.message.tree.IKeyTreeNode;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.babel.editor.util.ClipboardUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class CopyKeyAction extends AbstractTreeAction {

	public CopyKeyAction(AbstractMessagesEditor editor, TreeViewer treeViewer) {
		super(editor, treeViewer);

		setText(MessagesEditorPlugin.getString("key.copy")); //$NON-NLS-1$

		ImageDescriptor copyImageDescriptor = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY);
		ImageDescriptor disabledCopyImageDescriptor = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED);

		setImageDescriptor(copyImageDescriptor);
		setDisabledImageDescriptor(disabledCopyImageDescriptor);
	}

	@Override
	public void runWithEvent(Event event) {
        List<IKeyTreeNode> keyTreeNodes = getKeyTreeNodesFromSelection();

        /* OK, so we have a bunch of keys. Now we need to put them on the clipboard */
        List<String> allKeys = new ArrayList<String>();
        for(IKeyTreeNode keyTreeNode : keyTreeNodes) {
        	allKeys.add(keyTreeNode.getMessageKey());
        }

        String clipboardContent = ClipboardUtil.serializeKeys(getBundleGroup(), allKeys.toArray(new String[0]));
        ClipboardUtil.putOnClipboard(event.display, clipboardContent);
	}


	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return super.getImageDescriptor();
	}

}