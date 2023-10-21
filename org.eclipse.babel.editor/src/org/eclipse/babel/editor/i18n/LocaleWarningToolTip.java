package org.eclipse.babel.editor.i18n;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

public abstract class LocaleWarningToolTip extends ToolTip {

		private String headerText = "";
		private Label titleTextLabel;
		private Label titleImageLabel;		// TODO : Implement image support

		protected LocaleWarningToolTip(Control control) {
			super(control);
		}

		public String getText()
		{
			return this.headerText;
		}

		public void setText(String text) {
			this.headerText = text;
			if ( this.titleTextLabel != null && !this.titleTextLabel.isDisposed()) {
				this.titleTextLabel.setText(this.headerText);
			}
		}

		@Override
		protected Composite createToolTipContentArea(Event event, Composite parent) {
			Composite comp = new Composite(parent, SWT.NONE);

			GridLayout toolTipLayout = new GridLayout(1, false);
			toolTipLayout.marginBottom = 0;
			toolTipLayout.marginTop = 0;
			toolTipLayout.marginHeight = 0;
			toolTipLayout.marginWidth = 0;
			toolTipLayout.marginLeft = 0;
			toolTipLayout.marginRight = 0;
			toolTipLayout.verticalSpacing = 1;
			comp.setLayout(toolTipLayout);

			Composite headerCompsite = new Composite(comp, SWT.NONE);
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, false);
			data.widthHint = 200;
			headerCompsite.setLayoutData(data);

			GridLayout headerLayout = new GridLayout(2, false);
			headerLayout.marginBottom = 2;
			headerLayout.marginTop = 2;
			headerLayout.marginHeight = 0;
			headerLayout.marginWidth = 0;
			headerLayout.marginLeft = 5;
			headerLayout.marginRight = 2;

			headerCompsite.setLayout(headerLayout);
			
			this.titleImageLabel = new Label(headerCompsite, SWT.NONE);
			this.titleImageLabel.setImage(event.display.getSystemImage(SWT.ICON_INFORMATION));
			
			this.titleTextLabel = new Label(headerCompsite, SWT.NONE);
			this.titleTextLabel.setText(headerText);
			this.titleTextLabel.setFont(JFaceResources.getBannerFont());
			this.titleTextLabel.setLayoutData(new GridData(GridData.FILL_BOTH));

			createContentArea(comp).setLayoutData(new GridData(GridData.FILL_BOTH));

			return comp;
		}

		abstract Control createContentArea(Composite parent);
	}
