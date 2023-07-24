package org.eclipse.babel.editor.tree.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.babel.core.message.tree.IKeyTreeNode;
import org.eclipse.babel.core.message.tree.internal.KeyTreeNode;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.babel.editor.util.ClipboardUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
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

	static protected List<IKeyTreeNode> getAllKeys(IKeyTreeNode keyTreeNode) {
		List<IKeyTreeNode> treeNodes = new ArrayList<IKeyTreeNode>();
		if (keyTreeNode != null) {
			if (keyTreeNode.isUsedAsKey()) {
				treeNodes.add(keyTreeNode);
			}
			for (IKeyTreeNode childKeyNode : keyTreeNode.getChildren()) {
				List<IKeyTreeNode> allChildKeys = getAllKeys(childKeyNode);
				treeNodes.addAll(allChildKeys);
			}
		}

		return treeNodes;
	}

	@Override
	public void runWithEvent(Event event) {
        List<IKeyTreeNode> keyTreeNodes = this.getKeyTreeNodesFromSelection();

        /* OK, so we have a bunch of keys. Now we need to put them on the clipboard */
        List<String> allKeys = new ArrayList<String>();
        for(IKeyTreeNode keyTreeNode : keyTreeNodes) {
        	allKeys.add(keyTreeNode.getMessageKey());
        }

        String clipboardContent = ClipboardUtil.serializeKeys(getBundleGroup(), allKeys.toArray(new String[0]));
        ClipboardUtil.putOnClipboard(event.display, clipboardContent);
	}

	protected List<IKeyTreeNode> getKeyTreeNodesFromSelection()
	{
        IStructuredSelection selection = (IStructuredSelection) this.treeViewer.getSelection();

        /* Retrieve all the IKeyTreeNode:s that "is used as key" */
        List<IKeyTreeNode> keyTreeNodes = new ArrayList<IKeyTreeNode>();
        for(Object object : selection.toList()) {
        	if(object instanceof KeyTreeNode) {
				List<IKeyTreeNode> allChildKeys = getAllKeys((KeyTreeNode) object);
				keyTreeNodes.addAll(allChildKeys);
        	}
        }
        
        return keyTreeNodes;
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return super.getImageDescriptor();
	}

}