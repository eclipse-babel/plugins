/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 *    Alexej Strelzow - updateKey
 *    Samir Soyer     - passing Locale to NullableText
 ******************************************************************************/
package org.eclipse.babel.editor.i18n;

import java.util.Locale;

import org.eclipse.babel.core.message.IMessage;
import org.eclipse.babel.core.message.IMessagesBundle;
import org.eclipse.babel.core.message.IMessagesBundleGroup;
import org.eclipse.babel.core.message.internal.Message;
import org.eclipse.babel.core.util.BabelUtils;
import org.eclipse.babel.editor.IMessagesEditorChangeListener;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.internal.MessagesEditorChangeAdapter;
import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.babel.editor.widgets.NullableText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * Tree for displaying and navigating through resource bundle keys.
 * 
 * @author Pascal Essiembre
 */
public abstract class AbstractI18NEntry extends Composite {

    protected final AbstractMessagesEditor editor;
    private final String bundleGroupId;
    private final String projectName;
    protected final Locale locale;

    private boolean expanded = true;
    protected NullableText textBox;
//    private CBanner banner;
    protected String focusGainedText;

    public static final String INSTANCE_CLASS = "org.eclipse.babel.editor.i18n.I18NEntry";

    private IMessagesEditorChangeListener msgEditorUpdateKey = new MessagesEditorChangeAdapter() {
        public void selectedKeyChanged(String oldKey, String newKey) {
            updateKey(newKey);
        }
    };
	private FormToolkit toolkit;
	private Section section;

    /**
     * Constructor.
     * 
     * @param parent
     *            parent composite
     * @param keyTree
     *            key tree
     */
    public AbstractI18NEntry(Composite parent,
            final AbstractMessagesEditor editor, final Locale locale) {
        super(parent, SWT.NONE);
        this.setLayout(new FillLayout());
        
        this.toolkit = new FormToolkit(this.getDisplay());
        this.section = this.toolkit.createSection(this, Section.EXPANDED | Section.TWISTIE | Section.TITLE_BAR);

        this.editor = editor;
        this.locale = locale;
        this.bundleGroupId = editor.getBundleGroup().getResourceBundleId();
        this.projectName = editor.getBundleGroup().getProjectName();

        String localeName = UIUtils.getDisplayName(this.getLocale());
        String title = localeName + (!isEditable() ? " (" + MessagesEditorPlugin.getString("editor.readOnly") + ")" : "");

        Control bannerLeft = new FormEntryLeftBanner(this.section, this);// createBannerLeft(banner);

        this.section.setText(title);
        this.section.setTextClient(bannerLeft);
        createTextbox();
        this.section.setClient(this.textBox);
		this.section.setLayoutData( new GridData(GridData.FILL_BOTH));
    }

    public AbstractMessagesEditor getResourceBundleEditor() {
        return editor;
    }

    public Locale getLocale() {
        return locale;
    }

    public boolean isEditable() {
        IMessagesBundleGroup messagesBundleGroup = editor.getBundleGroup();
        IMessagesBundle bundle = messagesBundleGroup.getMessagesBundle(locale);
        return ((TextEditor) bundle.getResource().getSource()).isEditable();
    }

    public String getResourceLocationLabel() {
        IMessagesBundleGroup messagesBundleGroup = editor.getBundleGroup();
        IMessagesBundle bundle = messagesBundleGroup.getMessagesBundle(locale);
        return bundle.getResource().getResourceLocationLabel();
    }

    // /*default*/ Text getTextBox() {
    // return textBox;
    // }

    /**
     * @param editor
     * @param locale
     */
    private void createTextbox() {
        textBox = new NullableText(this.section, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL
                | SWT.V_SCROLL | SWT.BORDER, locale);
        textBox.setEnabled(false);
        textBox.setOrientation(UIUtils.getOrientation(locale));

        textBox.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent event) {
                focusGainedText = textBox.getText();
            }

            public void focusLost(FocusEvent event) {
                updateModel();
            }
        });
        // -- Setup read-only textbox --
        // that is the case if the corresponding editor is itself read-only.
        // it happens when the corresponding resource is defined inside the
        // target-platform for example
        textBox.setEditable(isEditable());

        // --- Handle tab key ---
        // TODO add a preference property listener and add/remove this listener
        textBox.addTraverseListener(new TraverseListener() {
            public void keyTraversed(TraverseEvent event) {
                // if (!MsgEditorPreferences.getFieldTabInserts()
                // && event.character == SWT.TAB) {
                // event.doit = true;
                // }
            }
        });

		// Handle dirtyness
		textBox.addKeyListener(getKeyListener());
		textBox.getTextBox().addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (textBox.isDirty()) {
					updateModel();
					textBox.setDirty(false);
				}
			}
		});

		textBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        editor.addChangeListener(msgEditorUpdateKey);
    }

    abstract void updateKey(String key);

    abstract KeyListener getKeyListener();

    protected void updateModel() {
        if (editor.getSelectedKey() != null) {
            if (!BabelUtils.equals(focusGainedText, textBox.getText())) {
                // IMessagesBundleGroup messagesBundleGroup =
                // RBManager.getInstance(projectName).getMessagesBundleGroup(bundleGroupId);
                IMessagesBundleGroup messagesBundleGroup = editor
                        .getBundleGroup();
                String key = editor.getSelectedKey();
                IMessage entry = messagesBundleGroup.getMessage(key, locale);
                if (entry == null) {
                    entry = new Message(key, locale);
                    IMessagesBundle messagesBundle = messagesBundleGroup
                            .getMessagesBundle(locale);
                    if (messagesBundle != null) {
                        messagesBundle.addMessage(entry);
                    }
                }
                entry.setText(textBox.getText());
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textBox.setEnabled(enabled);
    }

    @Override
    public void dispose() {
        editor.removeChangeListener(msgEditorUpdateKey);
        super.dispose();
    }

}
