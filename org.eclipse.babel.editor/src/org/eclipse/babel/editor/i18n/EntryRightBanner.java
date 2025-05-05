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
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.babel.core.message.checks.IMessageCheckResult;
import org.eclipse.babel.editor.IMessagesEditorChangeListener;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.internal.MessagesEditorChangeAdapter;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
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

    private Label warningIcon;

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

    	LocaleWarningToolTip myTooltipLabel = new LocaleWarningToolTip(warningIcon) {

			@Override
			protected Composite createContentArea(Composite parent) {
				Composite comp = new Composite( parent, SWT.NONE);
				Object data = warningIcon.getData("markers");
				comp.setLayout(new RowLayout(SWT.VERTICAL));
				if (data instanceof Collection<?> collection) {
					Collection<IMessageCheckResult> markers = (Collection<IMessageCheckResult>) collection;
					for ( IMessageCheckResult marker : markers) {
						Label markerLabel = new Label(comp, SWT.NONE);
						markerLabel.setText(marker.getText());
					}
				}
				return comp;
			}
		};
		myTooltipLabel.setShift(new Point(-5, -5));
		myTooltipLabel.setHideOnMouseDown(false);
		myTooltipLabel.activate();

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
					if (isDisposed()) {
						return;
					}

					String key = editor.getSelectedKey();
					Collection<IMessageCheckResult> checks = editor.getMarkers().getFailedChecks(key, locale);

					warningIcon.setVisible(checks != null && checks.size() > 0);
					warningIcon.setData("markers", checks);
				}
			});
		}
	}

    @Override
    public void dispose() {
        editor.removeChangeListener(msgEditorChangeListener);
        editor.getMarkers().deleteObserver(observer);
        super.dispose();
    }
}
