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

import org.eclipse.babel.editor.i18n.I18NPage;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.internal.MessagesEditorChangeAdapter;
import org.eclipse.babel.editor.util.BabelSharedImages;
import org.eclipse.babel.editor.util.IBabelSharedImages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

/**
 * @author Pascal Essiembre
 * 
 */
public class KeyTreeVisibleAction extends Action {

    private AbstractMessagesEditor editor;

    /**
     * 
     */
    public KeyTreeVisibleAction(AbstractMessagesEditor editor) {
        super("Show/Hide Key Tree", IAction.AS_CHECK_BOX);
        // setText();
        this.editor = editor;
        setToolTipText("Show/hide the key tree");
        setImageDescriptor(BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_VIEW_LEFT));
		if (this.editor != null) {
			editor.addChangeListener(new MessagesEditorChangeAdapter() {
				public void keyTreeVisibleChanged(boolean visible) {
					setChecked(visible);
				}
			});

			boolean keyTreeVisible = true;
			I18NPage i18nPage = editor.getI18NPage();
			if (i18nPage != null) {
				keyTreeVisible = i18nPage.isKeyTreeVisible();
			}

			setChecked(keyTreeVisible);
        }
    }
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        this.editor.getI18NPage().setKeyTreeVisible(!this.editor.getI18NPage().isKeyTreeVisible());
    }

}
