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

import java.util.Locale;

import org.eclipse.babel.editor.i18n.actions.FoldingAction;
import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.babel.editor.util.LocaleImageUtil;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.babel.editor.widgets.ActionButton;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * Tree for displaying and navigating through resource bundle keys.
 * 
 * @author Pascal Essiembre
 */
public class FormEntryLeftBanner extends Composite {

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            parent composite
	 * @param keyTree
	 *            key tree
	 */
	public FormEntryLeftBanner(Composite parent, final AbstractI18NEntry i18NEntry) {
		super(parent, SWT.NONE);

		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		this.setLayout(layout);
		// TODO have "show country flags" in preferences.
		// TODO have text aligned bottom next to flag icon.
		Label imgLabel = new Label(this, SWT.NONE);
		Image countryIcon = LocaleImageUtil.getCountryIcon(i18NEntry.getLocale());
		if (countryIcon != null) {
			imgLabel.setImage(countryIcon);
		}

		ToolBarManager compositeToolbar = new ToolBarManager();

		final IAction foldAction = new FoldingAction(i18NEntry);
		compositeToolbar.add(foldAction);
		ToolBar toolbar = compositeToolbar.createControl(this);

		// Button commentButton = new Button(compos, SWT.TOGGLE);
		// commentButton.setImage(UIUtils.getImage("comment.gif"));

//		Link localeLabel = new Link(this, SWT.NONE);
//		localeLabel.setFont(UIUtils.createFont(localeLabel, SWT.BOLD));
//
//		boolean isEditable = i18NEntry.isEditable();
//		localeLabel.setText("<a>" + UIUtils.getDisplayName(i18NEntry.getLocale()) + "</a>"
//				+ (!isEditable ? " (" + MessagesEditorPlugin.getString("editor.readOnly") + ")" : ""));
//
//		localeLabel
//				.setToolTipText(MessagesEditorPlugin.getString("editor.i18nentry.resourcelocation", i18NEntry.getResourceLocationLabel())); //$NON-NLS-1$
//
//		localeLabel.addSelectionListener(new SelectionAdapter() {
//			public void widgetSelected(SelectionEvent e) {
//				i18NEntry.getResourceBundleEditor().setActivePage(i18NEntry.getLocale());
//			}
//		});


	}
}
