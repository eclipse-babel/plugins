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
package org.eclipse.babel.editor.i18n;

import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.tree.actions.CollapseAllAction;
import org.eclipse.babel.editor.tree.actions.ExpandAllAction;
import org.eclipse.babel.editor.tree.actions.TreeModelAction;
import org.eclipse.babel.editor.tree.internal.KeyTreeContributor;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * Tree for displaying and navigating through resource bundle keys.
 * 
 * @author Pascal Essiembre
 */
public class SideNavComposite extends Composite {

    /** Key Tree Viewer. */
    private TreeViewer treeViewer;

    private AbstractMessagesEditor editor;

    private SideNavTextBoxComposite textBoxComp;

    /**
     * Constructor.
     * 
     * @param parent
     *            parent composite
     * @param keyTree
     *            key tree
     */
    public SideNavComposite(Composite parent,
            final AbstractMessagesEditor editor) {
        super(parent, SWT.NONE);
        this.editor = editor;
        setLayout(new GridLayout(1, true));
        
        FormToolkit toolKit = new FormToolkit(this.getDisplay());
        Section messageKeySection = toolKit.createSection(this, Section.TITLE_BAR | Section.EXPANDED );
        messageKeySection.setText("Message keys");
        // Create a toolbar.
        ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
        ToolBar toolBar = toolBarMgr.createControl(messageKeySection);

        messageKeySection.setTextClient(toolBar);

        messageKeySection.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        
        this.treeViewer = new TreeViewer(messageKeySection, SWT.SINGLE | SWT.BORDER
                | SWT.V_SCROLL | SWT.H_SCROLL);

        // createTopSection();
        createKeyTree();
        
        messageKeySection.setClient(this.treeViewer.getControl());

        toolBarMgr.add(new TreeModelAction(this.editor, this.treeViewer));
        toolBarMgr.add(new Separator());
        toolBarMgr.add(new ExpandAllAction(this.editor, this.treeViewer));
        toolBarMgr.add(new CollapseAllAction(this.editor, this.treeViewer));

        toolBarMgr.add(new FilterDropDown(this.editor, this.treeViewer));

        toolBarMgr.update(true);

        // TODO have two toolbars, one left-align, and one right, with drop
        // down menu
        // initListener();
        textBoxComp = new SideNavTextBoxComposite(this, editor);
        textBoxComp.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
    }

    
    
    // private void initListener() {
    // IResourceChangeListener listener = new IResourceChangeListener() {
    //
    // public void resourceChanged(IResourceChangeEvent event) {
    // if (!Boolean.valueOf(System.getProperty("dirty"))) {
    // createKeyTree();
    // }
    // }
    // };
    //
    // ResourcesPlugin.getWorkspace().addResourceChangeListener(listener,
    // IResourceChangeEvent.PRE_DELETE | IResourceChangeEvent.POST_CHANGE);
    //
    // }

    /**
     * Gets the tree viewer.
     * 
     * @return tree viewer
     */
    public TreeViewer getTreeViewer() {
        return treeViewer;
    }

    /**
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    public void dispose() {
        super.dispose();
    }

    /**
     * Creates the middle (tree) section of this composite.
     */
    private void createKeyTree() {
        KeyTreeContributor treeContributor = new KeyTreeContributor(editor);
        treeContributor.contribute(treeViewer);

    }

    public SideNavTextBoxComposite getSidNavTextBoxComposite() {
        return textBoxComp;
    }
}
