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
public class FlatModelAction extends AbstractTreeAction {

    /**
     * @param editor
     * @param treeViewer
     */
    public FlatModelAction(AbstractMessagesEditor editor, TreeViewer treeViewer) {
        super(editor, treeViewer, IAction.AS_RADIO_BUTTON);
        setText(MessagesEditorPlugin.getString("key.layout.flat")); //$NON-NLS-1$
        setImageDescriptor(BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_LAYOUT_FLAT));
        setDisabledImageDescriptor(BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_LAYOUT_FLAT));
        setToolTipText("Display in a list"); // TODO put tooltip
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        KeyTreeContentProvider contentProvider = (KeyTreeContentProvider) treeViewer
                .getContentProvider();
        contentProvider.setTreeType(TreeType.Flat);
    }
}
