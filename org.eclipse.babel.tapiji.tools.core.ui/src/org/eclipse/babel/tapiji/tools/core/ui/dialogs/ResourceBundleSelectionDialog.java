/*******************************************************************************
 * Copyright (c) 2012 Martin Reiterer, Matthias Lettmayer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Reiterer - initial API and implementation
 *     Matthias Lettmayer - adapt setInput, so only existing RB get displayed (fixed issue 40)
 ******************************************************************************/
package org.eclipse.babel.tapiji.tools.core.ui.dialogs;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.babel.core.message.manager.RBManager;
import org.eclipse.babel.tapiji.tools.core.ui.utils.ImageUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListDialog;

public class ResourceBundleSelectionDialog extends ListDialog {

    private IProject project;
    private SelectionMode selectionMode;

    public ResourceBundleSelectionDialog(Shell parent, IProject project, SelectionMode selectionMode) {
        super(parent);
        this.project = project;
        this.selectionMode = selectionMode;

        initDialog();
    }

    protected void initDialog() {
        this.setAddCancelButton(true);
        if (this.selectionMode == SelectionMode.SINGLE ) {
            this.setMessage("Select one of the following Resource-Bundle to open:");
        } else {
            this.setMessage("Select the Resouce-Bundle(s) to open:");
        }

        this.setTitle("Resource-Bundle Selector");
        this.setContentProvider(ArrayContentProvider.getInstance());
        this.setLabelProvider(new RBLabelProvider());
        this.setBlockOnOpen(true);

        if (project != null) {
            this.setInput(RBManager.getInstance(project).getMessagesBundleGroupNames());
        } else {
            this.setInput(RBManager.getAllMessagesBundleGroupNames());
        }
    }

    @Override
	protected int getTableStyle() {
		return (this.selectionMode == SelectionMode.MULTI ? SWT.MULTI : SWT.SINGLE) | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER;
	}

    /**
     * Returns the selected resource bundle.
     * In the case of a MULTI selection this method will return the first selection.
     * If no resource bundle is selected this method will return null.
     * 
     * @return
     */
    public String getSelectedBundleId() {
        Object[] selection = this.getResult();
        if (selection != null && selection.length > 0) {
            return (String) selection[0];
        }
        return null;
    }

    /**
     * Returns the selected resource bundles.
     * If no resource bundle is selected this method will return an empty array.
     * 
     * @return
     */
    public String[] getSelectedBundleIds() {
        Object[] selection = this.getResult();
        return Stream.of(selection) // Create stream from array
        		.filter(String.class::isInstance) // only strings should pass!
        		.map(String.class::cast) // cast to a string then
        		.collect(Collectors.toSet()) // collect everything in a Set of strings
        		.toArray(String[]::new); // convert the Set to a String[]
    }

    static class RBLabelProvider extends LabelProvider {
        @Override
        public Image getImage(Object element) {
            return ImageUtils.getImage(ImageUtils.IMAGE_RESOURCE_BUNDLE);
        }
    }

    public enum SelectionMode {
    	SINGLE, MULTI;
    }

}
