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

import org.eclipse.babel.core.message.tree.TreeType;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.babel.editor.tree.internal.KeyTreeContentProvider;
import org.eclipse.babel.editor.util.BabelSharedImages;
import org.eclipse.babel.editor.util.IBabelSharedImages;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * @author Pascal Essiembre
 * 
 */
public class TreeModelAction extends AbstractTreeAction {

    /**
     * @param editor
     * @param treeViewer
     */
    public TreeModelAction(AbstractMessagesEditor editor, TreeViewer treeViewer) {
        super(editor, treeViewer, IAction.AS_RADIO_BUTTON);
        setText(MessagesEditorPlugin.getString("key.layout.tree")); //$NON-NLS-1$
        setImageDescriptor(BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_LAYOUT_HIERARCHICAL));
        setToolTipText("Display as in a Tree"); // TODO put tooltip
        setChecked(true);
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        KeyTreeContentProvider contentProvider = (KeyTreeContentProvider) treeViewer
                .getContentProvider();
        
        TreeType currentTreeType = contentProvider.getTreeType();
        TreeType newTreeType = currentTreeType == TreeType.Tree ? TreeType.Flat : TreeType.Tree;	// Toggle tree type.
        contentProvider.setTreeType(newTreeType);
        setChecked(newTreeType == TreeType.Tree);
    }
}
