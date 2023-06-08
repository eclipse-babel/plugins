/*******************************************************************************
 * Copyright (c) 2012 Alexej Strelzow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexej Strelzow - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.editor.tree.actions;

import org.eclipse.babel.core.message.manager.RBManager;
import org.eclipse.babel.core.message.tree.internal.KeyTreeNode;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * An action, which triggers the refactoring of message keys.
 *
 * @author Alexej Strelzow
 */
public class RefactorKeyAction extends AbstractTreeAction {

    /**
     * Constructor.
     *
     * @param editor
     *            The {@link MessagesEditor}
     * @param treeViewer
     *            The {@link TreeViewer}
     */
    public RefactorKeyAction(AbstractMessagesEditor editor,
            TreeViewer treeViewer) {
        super(editor, treeViewer);
        setText(MessagesEditorPlugin.getString("key.rename") + " ..."); //$NON-NLS-1$
        setImageDescriptor(UIUtils
                .getImageDescriptor(UIUtils.IMAGE_REFACTORING));
        setToolTipText("Rename the selected key");
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        KeyTreeNode node = getNodeSelection();

        String key = node.getMessageKey();
        String bundleId = node.getMessagesBundleGroup().getResourceBundleId();
        String projectName = node.getMessagesBundleGroup().getProjectName();

        RBManager.getRefactorService().openRefactorDialog(projectName,
                bundleId, key, null);

    }
    
    @Override
    protected void selectionChanged(IStructuredSelection selection) {
    	/* Re-factoring is only supported if only one key is selected. */
    	this.setEnabled(selection != null && selection.equals(StructuredSelection.EMPTY) == false && selection.size() == 1);
    }
    
}
