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
package org.eclipse.babel.editor.tree.internal;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.babel.core.message.tree.IKeyTreeNode;
import org.eclipse.babel.core.message.tree.TreeType;
import org.eclipse.babel.core.message.tree.internal.AbstractKeyTreeModel;
import org.eclipse.babel.core.message.tree.internal.IKeyTreeModelListener;
import org.eclipse.babel.core.message.tree.internal.KeyTreeNode;
import org.eclipse.babel.editor.IMessagesEditorChangeListener;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.internal.MessagesEditorChangeAdapter;
import org.eclipse.babel.editor.internal.MessagesEditorMarkers;
import org.eclipse.babel.editor.tree.IKeyTreeContributor;
import org.eclipse.babel.editor.tree.actions.AddKeyAction;
import org.eclipse.babel.editor.tree.actions.CopyKeyAction;
import org.eclipse.babel.editor.tree.actions.CutKeyAction;
import org.eclipse.babel.editor.tree.actions.DeleteKeyAction;
import org.eclipse.babel.editor.tree.actions.PasteKeyAction;
import org.eclipse.babel.editor.tree.actions.RefactorKeyAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;

/**
 * @author Pascal Essiembre
 *
 */
public class KeyTreeContributor implements IKeyTreeContributor {
	
	private static final Logger LOGGER = Logger.getLogger(KeyTreeContributor.class.getName());

    private AbstractMessagesEditor editor;
    private AbstractKeyTreeModel treeModel;
    private TreeType treeType;

    /**
     *
     */
    public KeyTreeContributor(final AbstractMessagesEditor editor) {
        super();
        this.editor = editor;
        this.treeModel = new AbstractKeyTreeModel(editor.getBundleGroup());
        this.treeType = TreeType.Tree;
    }

    /**
     *
     */
    public void contribute(final TreeViewer treeViewer) {

        KeyTreeContentProvider contentProvider = new KeyTreeContentProvider(
                treeType);
        treeViewer.setContentProvider(contentProvider);
        ColumnViewerToolTipSupport.enableFor(treeViewer);
        treeViewer.setLabelProvider(new KeyTreeLabelProvider(editor, treeModel,
                contentProvider));
        if (treeViewer.getInput() == null)
            treeViewer.setUseHashlookup(true);

        ViewerFilter onlyUnusedAndMissingKeysFilter = new OnlyUnsuedAndMissingKey();
        ViewerFilter[] filters = { onlyUnusedAndMissingKeysFilter };
        treeViewer.setFilters(filters);

        // IKeyBindingService service = editor.getSite().getKeyBindingService();
        // service.setScopes(new
        // String[]{"org.eclilpse.babel.editor.editor.tree"});

        contributeActions(treeViewer);

        contributeKeySync(treeViewer);

        contributeModelChanges(treeViewer);

        contributeDoubleClick(treeViewer);

        contributeMarkers(treeViewer);

        // Set input model
        treeViewer.setInput(treeModel);
        treeViewer.expandAll();

        treeViewer.setColumnProperties(new String[] { "column1" });
        treeViewer.setCellEditors(new CellEditor[] { new TextCellEditor(
                treeViewer.getTree()) });
    }

    private class OnlyUnsuedAndMissingKey extends ViewerFilter implements
            AbstractKeyTreeModel.IKeyTreeNodeLeafFilter {

        /*
         * (non-Javadoc)
         *
         * @see
         * org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers
         * .Viewer, java.lang.Object, java.lang.Object)
         */
        public boolean select(Viewer viewer, Object parentElement,
                Object element) {
            if (editor.isShowOnlyUnusedAndMissingKeys() == IMessagesEditorChangeListener.SHOW_ALL
                    || !(element instanceof KeyTreeNode)) {
                // no filtering. the element is displayed by default.
                return true;
            }
            if (editor.getI18NPage() != null
                    && editor.getI18NPage().isKeyTreeVisible()) {
                return editor.getKeyTreeModel().isBranchFiltered(this,
                        (KeyTreeNode) element);
            } else {
                return isFilteredLeaf((KeyTreeNode) element);
            }
        }

        /**
         * @param node
         * @return true if this node should be in the filter. Does not navigate
         *         the tree of KeyTreeNode. false unless the node is a missing
         *         or unused key.
         */
        public boolean isFilteredLeaf(IKeyTreeNode node) {
            MessagesEditorMarkers markers = KeyTreeContributor.this.editor
                    .getMarkers();
            String key = node.getMessageKey();
            boolean missingOrUnused = markers.isMissingOrUnusedKey(key);
            if (!missingOrUnused) {
                return false;
            }
            switch (editor.isShowOnlyUnusedAndMissingKeys()) {
            case IMessagesEditorChangeListener.SHOW_ONLY_MISSING_AND_UNUSED:
                return missingOrUnused;
            case IMessagesEditorChangeListener.SHOW_ONLY_MISSING:
                return !markers.isUnusedKey(key, missingOrUnused);
            case IMessagesEditorChangeListener.SHOW_ONLY_UNUSED:
                return markers.isUnusedKey(key, missingOrUnused);
            default:
                return false;
            }
        }

    }

    /**
     * Contributes markers.
     *
     * @param treeViewer
     *            tree viewer
     */
    private void contributeMarkers(final TreeViewer treeViewer) {
        editor.getMarkers().addObserver(new Observer() {
            public void update(Observable o, Object arg) {
                Display display = treeViewer.getTree().getDisplay();
                // [RAP] only refresh tree viewer in this UIThread
                if (display.equals(Display.getCurrent())) {
                    display.asyncExec(new Runnable() {
                    public void run() {
                    	if(treeViewer.getControl().isDisposed() == false) {
                    		treeViewer.refresh();
                    	}
                    }
                });
            }
            }
        });
        // editor.addChangeListener(new MessagesEditorChangeAdapter() {
        // public void editorDisposed() {
        // editor.getMarkers().clear();
        // }
        // });

        // final IMarkerListener markerListener = new IMarkerListener() {
        // public void markerAdded(IMarker marker) {
        // PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable () {
        // public void run() {
        // if (!PlatformUI.getWorkbench().getDisplay().isDisposed()) {
        // treeViewer.refresh(true);
        // }
        // }
        // });
        // }
        // public void markerRemoved(IMarker marker) {
        // PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable () {
        // public void run() {
        // if (!PlatformUI.getWorkbench().getDisplay().isDisposed()) {
        // treeViewer.refresh(true);
        // }
        // }
        // });
        // }
        // };
        // editor.getMarkerManager().addMarkerListener(markerListener);
        // editor.addChangeListener(new MessagesEditorChangeAdapter() {
        // public void editorDisposed() {
        // editor.getMarkerManager().removeMarkerListener(markerListener);
        // }
        // });
    }

    /**
     * Contributes double-click support, expanding/collapsing nodes.
     *
     * @param treeViewer
     *            tree viewer
     */
    private void contributeDoubleClick(final TreeViewer treeViewer) {
        treeViewer.getTree().addMouseListener(new MouseAdapter() {
            public void mouseDoubleClick(MouseEvent event) {
                IStructuredSelection selection = (IStructuredSelection) treeViewer
                        .getSelection();
                Object element = selection.getFirstElement();
                if (treeViewer.isExpandable(element)) {
                    if (treeViewer.getExpandedState(element)) {
                        treeViewer.collapseToLevel(element, 1);
                    } else {
                        treeViewer.expandToLevel(element, 1);
                    }
                }
            }
        });
    }

    /**
     * Contributes key synchronization between editor and tree selected keys.
     *
     * @param treeViewer
     *            tree viewer
     */
    private void contributeModelChanges(final TreeViewer treeViewer) {
        final IKeyTreeModelListener keyTreeListener = new IKeyTreeModelListener() {
            // TODO be smarter about refreshes.
            public void nodeAdded(KeyTreeNode node) {
                Display.getDefault().asyncExec(new Runnable() {
                    public void run() {
                        if (!editor.getI18NPage().isDisposed()) {
                        treeViewer.refresh(true);
                    }
                    }
                });
            };

            // public void nodeChanged(KeyTreeNode node) {
            // treeViewer.refresh(true);
            // };
            public void nodeRemoved(KeyTreeNode node) {
                Display.getDefault().asyncExec(new Runnable() {
                    public void run() {
                        if (!editor.getI18NPage().isDisposed()) {
                        treeViewer.refresh(true);
                    }
                    }
                });
            };
        };
        treeModel.addKeyTreeModelListener(keyTreeListener);
        editor.addChangeListener(new MessagesEditorChangeAdapter() {
            public void keyTreeModelChanged(AbstractKeyTreeModel oldModel,
                    AbstractKeyTreeModel newModel) {
                oldModel.removeKeyTreeModelListener(keyTreeListener);
                newModel.addKeyTreeModelListener(keyTreeListener);
                treeViewer.setInput(newModel);
                treeViewer.refresh();
            }

            public void showOnlyUnusedAndMissingChanged(int hideEverythingElse) {
                treeViewer.refresh();
            }
        });
    }

    /**
     * Contributes key synchronization between editor and tree selected keys.
     *
     * @param treeViewer
     *            tree viewer
     */
    private void contributeKeySync(final TreeViewer treeViewer) {
        // changes in tree selected key update the editor
    	treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();

                String selectedMessageKey = null;
                if (selection != null ) {
                	if ( selection.size() == 1) {
                        KeyTreeNode node = (KeyTreeNode) selection
                                .getFirstElement();
                        LOGGER.log(Level.INFO, "viewer key/hash:"
                                + node.getMessageKey() + "/" + node.hashCode());
                        selectedMessageKey = node.getMessageKey();
                	}
                }

                editor.setSelectedKey(selectedMessageKey);
				
			}
		});
        // changes in editor selected key updates the tree
        editor.addChangeListener(new MessagesEditorChangeAdapter() {
            public void selectedKeyChanged(String oldKey, String newKey) {
                ITreeContentProvider provider = (ITreeContentProvider) treeViewer
                        .getContentProvider();
                if (provider != null) { // alst workaround
                    KeyTreeNode node = findKeyTreeNode(provider,
                            provider.getElements(null), newKey);

                    // String[] test = newKey.split("\\.");
                    // treeViewer.setSelection(new StructuredSelection(test),
                    // true);

                    if (node != null) {
                        treeViewer.setSelection(new StructuredSelection(node),
                                true);
                        treeViewer.getTree().showSelection();
                    }
                }
            }
        });
    }

    /**
     * Contributes actions to the tree.
     *
     * @param treeViewer
     *            tree viewer
     */
    private void contributeActions(final TreeViewer treeViewer) {
        Tree tree = treeViewer.getTree();

        // Add menu
        MenuManager menuManager = new MenuManager();

        // Add
        final IAction addAction = new AddKeyAction(editor, treeViewer);
        menuManager.add(addAction);
        
        // Refactor
        final IAction refactorAction = new RefactorKeyAction(editor, treeViewer);
        menuManager.add(refactorAction);

        // Separator
        menuManager.add(new Separator());

        // Cut
        final IAction cutAction = new CutKeyAction(editor, treeViewer);
        menuManager.add(cutAction);

        // Copy
        final IAction copyAction = new CopyKeyAction(editor, treeViewer);
        menuManager.add(copyAction);

        
        // Paste
        final IAction pasteAction = new PasteKeyAction(editor, treeViewer);
        menuManager.add(pasteAction);

        
        // Delete
        final IAction deleteAction = new DeleteKeyAction(editor, treeViewer);
        menuManager.add(deleteAction);

        menuManager.update(true);
        menuManager.addMenuListener(new IMenuListener() {
			
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				// TODO Auto-generated method stub
				IStructuredSelection selection = (IStructuredSelection) treeViewer
		                .getSelection();
		        KeyTreeNode node = (KeyTreeNode) selection.getFirstElement();
				refactorAction.setEnabled(node == null || node.getChildren().length == 0);
			}
		});
        Menu menu = menuManager.createContextMenu(tree);
        tree.setMenu(menu);

        // Bind actions to tree
        tree.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent event) {
                if (event.character == SWT.DEL) {
                    deleteAction.run();
                }
            }
        });
    }

    private KeyTreeNode findKeyTreeNode(ITreeContentProvider provider,
            Object[] nodes, String key) {
        for (int i = 0; i < nodes.length; i++) {
            KeyTreeNode node = (KeyTreeNode) nodes[i];
            if (node.getMessageKey().equals(key)) {
                return node;
            }
            node = findKeyTreeNode(provider, provider.getChildren(node), key);
            if (node != null) {
                return node;
            }
        }
        return null;
    }

    public IKeyTreeNode getKeyTreeNode(String key) {
        return getKeyTreeNode(key, null);
    }

    // TODO, think about a hashmap
    private IKeyTreeNode getKeyTreeNode(String key, IKeyTreeNode node) {
        if (node == null) {
            for (IKeyTreeNode ktn : treeModel.getRootNodes()) {
                String id = ktn.getMessageKey();
                if (key.equals(id)) {
                    return ktn;
                } else {
                    getKeyTreeNode(key, ktn);
                }
            }
        } else {
            for (IKeyTreeNode ktn : node.getChildren()) {
                String id = ktn.getMessageKey();
                if (key.equals(id)) {
                    return ktn;
                } else {
                    getKeyTreeNode(key, ktn);
                }
            }
        }
        return null;
    }

    public IKeyTreeNode[] getRootKeyItems() {
        return treeModel.getRootNodes();
    }

}
