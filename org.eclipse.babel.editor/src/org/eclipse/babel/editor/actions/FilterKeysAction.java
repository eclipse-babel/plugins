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
package org.eclipse.babel.editor.actions;

import org.eclipse.babel.editor.IMessagesEditorChangeListener;
import org.eclipse.babel.editor.tree.internal.OnlyUnsuedAndMissingKey;
import org.eclipse.babel.editor.util.BabelSharedImages;
import org.eclipse.babel.editor.util.IBabelSharedImages;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * 
 * @author Hugues Malphettes
 */
public class FilterKeysAction extends Action {

    private TreeViewer treeViewer;
    private final int flagToSet;

    /**
     * @param flagToSet
     *            The flag that will be set on unset
     */
    public FilterKeysAction(int flagToSet) {
    	this(null, flagToSet);
    }

    /**
     * @param treeViewer The TreeViewer to interact with
     * @param flagToSet
     *            The flag that will be set on unset
     */
    public FilterKeysAction(TreeViewer treeViewer, int flagToSet) {
        super("", IAction.AS_RADIO_BUTTON);
        this.treeViewer = treeViewer;
        this.flagToSet = flagToSet;
        update();
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
    	
    	OnlyUnsuedAndMissingKey keyFilter = UIUtils.getFilter(this.treeViewer, OnlyUnsuedAndMissingKey.class);
    	if ( keyFilter != null ) {
            if (keyFilter.isShowOnlyUnusedAndMissingKeys() != flagToSet) {
            	keyFilter.setShowOnlyUnusedMissingKeys(flagToSet);
            } else {
            	keyFilter.setShowOnlyUnusedMissingKeys(IMessagesEditorChangeListener.SHOW_ALL);
            }
    	}
    }

    public void update() {
    	super.setEnabled(this.treeViewer != null);

    	boolean checked = false;
        if (this.treeViewer != null) {
        	OnlyUnsuedAndMissingKey keyFilter = UIUtils.getFilter(this.treeViewer, OnlyUnsuedAndMissingKey.class);
        	checked = keyFilter != null && keyFilter.isShowOnlyUnusedAndMissingKeys() == flagToSet;
        }

    	setChecked(checked);

        setText(getTextInternal());
        setToolTipText(getTooltipInternal());
        setImageDescriptor(getImageDescriptor());
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        switch (flagToSet) {
        case IMessagesEditorChangeListener.SHOW_ONLY_MISSING:
            return BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_MISSING_TRANSLATION);
        case IMessagesEditorChangeListener.SHOW_ONLY_MISSING_AND_UNUSED:
            return BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_UNUSED_AND_MISSING_TRANSLATIONS);
        case IMessagesEditorChangeListener.SHOW_ONLY_UNUSED:
            return BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_UNUSED_TRANSLATION);
        case IMessagesEditorChangeListener.SHOW_ALL:
        default:
            return BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_KEY);
        }
    }

    public String getTextInternal() {
        switch (flagToSet) {
        case IMessagesEditorChangeListener.SHOW_ONLY_MISSING:
            return "Show only missing translations";
        case IMessagesEditorChangeListener.SHOW_ONLY_MISSING_AND_UNUSED:
            return "Show only missing or unused translations";
        case IMessagesEditorChangeListener.SHOW_ONLY_UNUSED:
            return "Show only unused translations";
        case IMessagesEditorChangeListener.SHOW_ALL:
        default:
            return "Show all";
        }
    }

    private String getTooltipInternal() {
        return getTextInternal();
    }
}
