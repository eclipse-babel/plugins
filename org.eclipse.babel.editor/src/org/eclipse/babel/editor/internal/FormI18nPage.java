package org.eclipse.babel.editor.internal;

import org.eclipse.babel.editor.i18n.SideNavComposite;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class FormI18nPage extends Composite {
	private AbstractMessagesEditor messageEditor;

	private FormToolkit toolkit;
	private Form form;
	private SashForm sashForm;

	public FormI18nPage(Composite parent, AbstractMessagesEditor messageEditor) {
		super(parent, SWT.NONE);

		this.messageEditor = messageEditor;
		
		this.toolkit = new FormToolkit(this.getDisplay());
		this.setLayout(new FillLayout());

		this.form = this.toolkit.createForm(this);
		toolkit.decorateFormHeading(this.form);
		this.form.setText("Messages Editor");
		Composite head = this.form.getHead();
		IToolBarManager toolBarManager = this.form.getToolBarManager();
		
		
		GridLayout layout = new GridLayout();
		this.form.getBody().setLayout(layout);
		Hyperlink link = toolkit.createHyperlink(this.form.getBody(), "Click here.", SWT.WRAP);
		link.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				System.out.println("Link activated!");
			}
		});

        this.sashForm = new SashForm(this.form.getBody(), SWT.SMOOTH);
        SideNavComposite sideNavComposite = new SideNavComposite(this.sashForm, this.messageEditor);
        this.sashForm.setWeights(new int[] {25});
	}
}
