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
package org.eclipse.babel.editor.i18n.actions;

import java.util.Locale;

import org.eclipse.babel.editor.util.BabelSharedImages;
import org.eclipse.babel.editor.util.IBabelSharedImages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

/**
 * @author Pascal Essiembre
 * 
 */
public class ShowMissingAction extends Action {

    /**
     * 
     */
    public ShowMissingAction(String key, Locale locale) {
        super();
        setText("Key missing a value.");
        setImageDescriptor(BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_EMPTY));
        setToolTipText("TODO put something here"); // TODO put tooltip
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        MessageDialog.openInformation(Display.getDefault().getActiveShell(),
                "Missing value", "Key missing a value");
    }

}
