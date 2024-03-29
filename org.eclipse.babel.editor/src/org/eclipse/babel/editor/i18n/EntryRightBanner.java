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

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.babel.core.message.checks.IMessageCheck;
import org.eclipse.babel.core.message.checks.IMessageCheckResult;
import org.eclipse.babel.core.message.checks.internal.DuplicateValueCheck;
import org.eclipse.babel.core.message.checks.internal.DuplicateValueMessageCheckResult;
import org.eclipse.babel.core.message.checks.internal.MissingValueCheck;
import org.eclipse.babel.core.message.checks.internal.MissingValueCheckResult;
import org.eclipse.babel.editor.IMessagesEditorChangeListener;
import org.eclipse.babel.editor.i18n.actions.ShowDuplicateAction;
import org.eclipse.babel.editor.i18n.actions.ShowMissingAction;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.internal.MessagesEditorChangeAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Tree for displaying and navigating through resource bundle keys.
 * 
 * @author Pascal Essiembre
 */
public class EntryRightBanner extends Composite {

    private Label colon;
    private Label warningIcon;
    private final Map actionByMarkerIds = new HashMap();
    private final ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
    private final AbstractMessagesEditor editor;
    private final AbstractI18NEntry i18nEntry;
    private final Locale locale;
    private final Observer observer = new Observer() {
        public void update(Observable o, Object arg) {
            updateMarkers();
        }
    };
    private final IMessagesEditorChangeListener msgEditorChangeListener = new MessagesEditorChangeAdapter() {
        public void selectedKeyChanged(String oldKey, String newKey) {
            updateMarkers();
        }
    };

    /**
     * Constructor.
     * 
     * @param parent
     *            parent composite
     * @param keyTree
     *            key tree
     */
    public EntryRightBanner(Composite parent, final AbstractI18NEntry i18nEntry) {
        super(parent, SWT.NONE);
        this.i18nEntry = i18nEntry;
        this.locale = i18nEntry.getLocale();
        this.editor = i18nEntry.getResourceBundleEditor();

        RowLayout layout = new RowLayout();
        setLayout(layout);
        layout.marginBottom = 0;
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.marginTop = 0;

        warningIcon = new Label(this, SWT.NONE);
        warningIcon.setImage(PlatformUI.getWorkbench().getSharedImages()
                .getImage(ISharedImages.IMG_OBJS_WARN_TSK));
        warningIcon.setVisible(false);
        warningIcon.setToolTipText("This locale has warnings.");

        colon = new Label(this, SWT.NONE);
        colon.setText(":");
        colon.setVisible(false);

        toolBarMgr.createControl(this);
        toolBarMgr.update(true);

        editor.addChangeListener(msgEditorChangeListener);
        editor.getMarkers().addObserver(observer);
    }

    /**
     * @param warningIcon
     * @param colon
     */
    private void updateMarkers() {
        Display display = toolBarMgr.getControl().getDisplay();
        // [RAP] only update markers, which belong to this UIThread
        if (display.equals(Display.getCurrent()) && !isDisposed()) {
            display.asyncExec(new Runnable() {
            public void run() {
                    if (isDisposed())
                        return;
                // if (!PlatformUI.getWorkbench().getDisplay().isDisposed()
                // && !editor.getMarkerManager().isDisposed()) {
                boolean isMarked = false;
                toolBarMgr.removeAll();
                actionByMarkerIds.clear();
                String key = editor.getSelectedKey();
                Collection<IMessageCheckResult> checks = editor.getMarkers()
                        .getFailedChecks(key, locale);
                if (checks != null) {
                    for (IMessageCheckResult check : checks) {
                        Action action = getCheckAction(key, check);
                        if (action != null) {
                            toolBarMgr.add(action);
                            toolBarMgr.update(true);
                            getParent().layout(true, true);
                            isMarked = true;
                        }
                    }
                }
                toolBarMgr.update(true);
                getParent().layout(true, true);

                warningIcon.setVisible(isMarked);
                colon.setVisible(isMarked);
            }
            // }
        });
        }

    }

    private Action getCheckAction(String key, IMessageCheckResult check) {
        if (check instanceof MissingValueCheckResult checkResult) {
            return new ShowMissingAction(key, locale);
        } else if (check instanceof DuplicateValueMessageCheckResult checkResult) {
            return new ShowDuplicateAction(
            		checkResult.getDuplicateKeys(), key,
                    locale);
        }
        return null;
    }

    @Override
    public void dispose() {
        editor.removeChangeListener(msgEditorChangeListener);
        editor.getMarkers().deleteObserver(observer);
        super.dispose();
    }
}
