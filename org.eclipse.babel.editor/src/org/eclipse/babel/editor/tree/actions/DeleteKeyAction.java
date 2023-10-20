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
package org.eclipse.babel.editor.tree.actions;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.core.message.tree.IKeyTreeNode;
import org.eclipse.babel.core.message.tree.internal.KeyTreeNode;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

/**
 * @author Pascal Essiembre
 * 
 */
public class DeleteKeyAction extends AbstractTreeAction implements IWorkbenchAction {

    /**
     * 
     */
    public DeleteKeyAction(AbstractMessagesEditor editor, TreeViewer treeViewer) {
        super(editor, treeViewer);
        setText(MessagesEditorPlugin.getString("key.delete")); //$NON-NLS-1$
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
                .getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
        setToolTipText("TODO put something here"); // TODO put tooltip
        // setActionDefinitionId("org.eclilpse.babel.editor.editor.tree.delete");
        // setActionDefinitionId("org.eclipse.ui.edit.delete");
        // editor.getSite().getKeyBindingService().registerAction(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        List<IKeyTreeNode> selectedNodes = getKeyTreeNodesFromSelection();
        String msgHead = null;
        String msgBody = null;

        Set<IKeyTreeNode> nodesToDelete = new HashSet<>();
       
        for ( IKeyTreeNode node : selectedNodes ) {
        	nodesToDelete.add(node);
        	Collections.addAll(nodesToDelete, getBranchNodes(node));
        }
        
        if (nodesToDelete.size() > 1) {
            msgHead = MessagesEditorPlugin
                    .getString("dialog.delete.head.multiple"); //$NON-NLS-1$
            msgBody = MessagesEditorPlugin.getString(
                    "dialog.delete.body.multiple");//$NON-NLS-1$ 
        } else if ( nodesToDelete.size() == 1) {
        	IKeyTreeNode key = nodesToDelete.iterator().next();
            msgHead = MessagesEditorPlugin
                    .getString("dialog.delete.head.single"); //$NON-NLS-1$
            msgBody = MessagesEditorPlugin.getString(
                    "dialog.delete.body.single", key.getMessageKey()); //$NON-NLS-1$
        }
        MessageBox msgBox = new MessageBox(getShell(), SWT.ICON_QUESTION
                | SWT.OK | SWT.CANCEL);
        msgBox.setMessage(msgBody);
        msgBox.setText(msgHead);
        if (msgBox.open() == SWT.OK) {
            MessagesBundleGroup messagesBundleGroup = getBundleGroup();
            for (IKeyTreeNode nodeToDelete : nodesToDelete) {
                 messagesBundleGroup.removeMessages(nodeToDelete.getMessageKey());
            }
        }
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		System.out.println("Dispose");
	}

}
