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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.core.message.tree.IKeyTreeNode;
import org.eclipse.babel.core.message.tree.internal.AbstractKeyTreeModel;
import org.eclipse.babel.core.message.tree.internal.KeyTreeNode;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Pascal Essiembre
 * 
 */
public abstract class AbstractTreeAction extends Action {

    protected final TreeViewer treeViewer;
    protected final AbstractMessagesEditor editor;
    private ISelectionChangedListener selectionChangedListener = null;
    /**
     * 
     */
    protected AbstractTreeAction(AbstractMessagesEditor editor,
            TreeViewer treeViewer) {
        super();
        this.treeViewer = treeViewer;
        this.editor = editor;
        this.hookListeners();
    }

    /**
     * 
     */
    protected AbstractTreeAction(AbstractMessagesEditor editor,
            TreeViewer treeViewer, int style) {
        super("", style);
        this.treeViewer = treeViewer;
        this.editor = editor;
        this.hookListeners();
    }

    private void hookListeners()
    {
    	this.selectionChangedListener = new ViewerSelectionListener();
    	this.treeViewer.addSelectionChangedListener(this.selectionChangedListener);
    }

    protected void selectionChanged(IStructuredSelection selection) { }
    
    protected KeyTreeNode getNodeSelection() {
        IStructuredSelection selection = (IStructuredSelection) treeViewer
                .getSelection();
        return (KeyTreeNode) selection.getFirstElement();
    }

	protected List<IKeyTreeNode> getKeyTreeNodesFromSelection()
	{
        IStructuredSelection selection = (IStructuredSelection) this.treeViewer.getSelection();

        /* Retrieve all the IKeyTreeNode:s that "is used as key" */
        List<IKeyTreeNode> keyTreeNodes = new ArrayList<>();
        for(Object object : selection.toList()) {
        	if(object instanceof KeyTreeNode keyTreeNode) {
				List<IKeyTreeNode> allChildKeys = getAllKeys(keyTreeNode);
				keyTreeNodes.addAll(allChildKeys);
        	}
        }
        
        return keyTreeNodes;
	}

	protected static List<IKeyTreeNode> getAllKeys(IKeyTreeNode keyTreeNode) {
		List<IKeyTreeNode> treeNodes = new ArrayList<>();
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
	
    protected IKeyTreeNode[] getBranchNodes(IKeyTreeNode node) {
        return ((AbstractKeyTreeModel) treeViewer.getInput()).getBranch((KeyTreeNode)node);
    }

    protected ITreeContentProvider getContentProvider() {
        return (ITreeContentProvider) treeViewer.getContentProvider();
    }

    protected MessagesBundleGroup getBundleGroup() {
        return editor.getBundleGroup();
    }

    protected TreeViewer getTreeViewer() {
        return treeViewer;
    }

    protected AbstractMessagesEditor getEditor() {
        return editor;
    }

    protected Shell getShell() {
        return treeViewer.getTree().getShell();
    }
    
    class ViewerSelectionListener implements ISelectionChangedListener {

		@Override
		public void selectionChanged(SelectionChangedEvent event) {
			AbstractTreeAction.this.selectionChanged((IStructuredSelection) event.getSelection());
			
		}
    }
}
