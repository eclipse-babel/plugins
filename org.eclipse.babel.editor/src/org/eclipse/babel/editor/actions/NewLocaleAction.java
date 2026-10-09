/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 *    Matthias Lettmayer - implemented action and created dialog
 ******************************************************************************/
package org.eclipse.babel.editor.actions;

import java.util.Locale;

import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.util.BabelSharedImages;
import org.eclipse.babel.editor.util.IBabelSharedImages;
import org.eclipse.babel.editor.widgets.LocaleSelector;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Pascal Essiembre
 * 
 */
public class NewLocaleAction extends Action {

    private AbstractMessagesEditor editor;

    /**
     * @param editor2 
     * 
     */
    public NewLocaleAction(AbstractMessagesEditor editor) {
        super("New &Locale...");
        this.editor = editor;
        setToolTipText("Add a new locale to the resource bundle.");
        setImageDescriptor(BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_NEW_PROPERTIES_FILE));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        // created choose locale dialog
        Dialog localeDialog = new Dialog(editor.getSite().getShell()) {
            LocaleSelector selector;

            @Override
            protected void configureShell(Shell newShell) {
                super.configureShell(newShell);
                newShell.setText("Add new local");
            }

            @Override
            protected Control createDialogArea(Composite parent) {
                Composite comp = (Composite) super.createDialogArea(parent);
                selector = new LocaleSelector(comp);
                return comp;
            }

            @Override
            protected void okPressed() {
                // add local to bundleGroup
                MessagesBundleGroup bundleGroup = editor.getBundleGroup();
                Locale newLocal = selector.getSelectedLocale();

                // exists local already?
                boolean existsLocal = false;
                Locale[] locales = bundleGroup.getLocales();
                for (Locale locale : locales) {
                    if (locale == null) {
                        if (newLocal == null) {
                            existsLocal = true;
                            break;
                        }
                    } else if (locale.equals(newLocal)) {
                        existsLocal = true;
                        break;
                    }
                }

                if (!existsLocal)
                    bundleGroup.addMessagesBundle(newLocal);

                super.okPressed();
            }
        };
        // open dialog
        localeDialog.open();
    }
}
