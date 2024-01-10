package org.eclipse.babel.editor.tree.internal;

import org.eclipse.babel.core.message.tree.IKeyTreeNode;
import org.eclipse.babel.core.message.tree.internal.AbstractKeyTreeModel;
import org.eclipse.babel.core.message.tree.internal.KeyTreeNode;
import org.eclipse.babel.editor.IMessagesEditorChangeListener;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.internal.MessagesEditorMarkers;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class OnlyUnsuedAndMissingKey extends ViewerFilter implements
        AbstractKeyTreeModel.IKeyTreeNodeLeafFilter {

    /**
     * one of the SHOW_* constants defined in the
     * {@link IMessagesEditorChangeListener}
     */
    private int showOnlyMissingAndUnusedKeys = IMessagesEditorChangeListener.SHOW_ALL;
	
    private AbstractMessagesEditor editor;

    public OnlyUnsuedAndMissingKey(AbstractMessagesEditor editor) {
    	this.editor = editor;
    }
    
    /*	
     * (non-Javadoc)
     *
     * @see
     * org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers
     * .Viewer, java.lang.Object, java.lang.Object)
     */
    public boolean select(Viewer viewer, Object parentElement,
            Object element) {

        if (this.isShowOnlyUnusedAndMissingKeys() == IMessagesEditorChangeListener.SHOW_ALL
                || !(element instanceof KeyTreeNode)) {
            // no filtering. the element is displayed by default.
            return true;
        }
        if (this.editor.getI18NPage() != null
                && this.editor.getI18NPage().isKeyTreeVisible()) {
            return this.editor.getKeyTreeModel().isBranchFiltered(this,
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
        MessagesEditorMarkers markers = this.editor.getMarkers();
        String key = node.getMessageKey();
        boolean missingOrUnused = markers.isMissingOrUnusedKey(key);
        if (!missingOrUnused) {
            return false;
        }
        switch (this.isShowOnlyUnusedAndMissingKeys()) {
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

    /**
     * @return true when only unused and missing keys should be displayed. false by default.
     */
	public int isShowOnlyUnusedAndMissingKeys() {
		return this.showOnlyMissingAndUnusedKeys;
	}

    public void setShowOnlyUnusedMissingKeys(int showFlag) {
        this.showOnlyMissingAndUnusedKeys = showFlag;
        this.editor.notifyChange(showFlag);
    }
	
}