package org.eclipse.babel.editor.tree.actions;

import java.util.List;

import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.core.message.tree.IKeyTreeNode;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Event;

public class CutKeyAction extends CopyKeyAction {

	public CutKeyAction(AbstractMessagesEditor editor, TreeViewer treeViewer) {
		super(editor, treeViewer);

		setText(MessagesEditorPlugin.getString("key.cut")); //$NON-NLS-1$
		setImageDescriptor(UIUtils.getImageDescriptor(UIUtils.IMAGE_ADD));
	}

	@Override
	public void runWithEvent(Event event) {
		/* Put keys on the clipboard */
		super.runWithEvent(event);

		/* Delete keys */
		MessagesBundleGroup messagesBundleGroup = getBundleGroup();
		List<IKeyTreeNode> keyTreeNodes = this.getKeyTreeNodesFromSelection();
		for (IKeyTreeNode keyTreeNode : keyTreeNodes) {
			messagesBundleGroup.removeMessages(keyTreeNode.getMessageKey());
		}
	}

}