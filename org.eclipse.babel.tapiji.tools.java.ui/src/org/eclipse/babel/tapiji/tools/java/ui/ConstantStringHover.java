/*******************************************************************************
 * Copyright (c) 2012 Martin Reiterer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Martin Reiterer - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.tapiji.tools.java.ui;

import org.eclipse.babel.tapiji.tools.core.ui.ResourceBundleManager;
import org.eclipse.babel.tapiji.tools.java.ui.util.ASTutilsUI;
import org.eclipse.babel.tapiji.tools.java.visitor.ResourceAuditVisitor;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.text.java.hover.IJavaEditorTextHover;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.ui.IEditorPart;

public class ConstantStringHover implements IJavaEditorTextHover {

    IEditorPart editor = null;
    ResourceAuditVisitor csf = null;
    ResourceBundleManager manager = null;

    @Override
    public void setEditor(IEditorPart editor) {
        this.editor = editor;
        initConstantStringAuditor();
    }

    protected void initConstantStringAuditor() {
        // parse editor content and extract resource-bundle access strings

        // get the type of the currently loaded resource
        ITypeRoot typeRoot = JavaUI.getEditorInputTypeRoot(editor
                .getEditorInput());

        if (typeRoot == null) {
            return;
        }

        CompilationUnit cu = ASTutilsUI.getAstRoot(typeRoot);

        // There are a lot of things that can be null here. Better test
        if (cu == null || cu.getJavaElement() == null || cu.getJavaElement().getResource() == null || cu.getJavaElement().getResource().getProject() == null ) {
            return;
        }

        this.manager = ResourceBundleManager.getManager(cu.getJavaElement()
                .getResource().getProject());

        // determine the element at the position of the cursor
        this.csf = new ResourceAuditVisitor(null, this.manager.getProject().getName());
        cu.accept(this.csf);
    }

    @Override
    public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
        initConstantStringAuditor();
        if (hoverRegion == null) {
            return null;
        }

        // get region for string literals
        hoverRegion = getHoverRegion(textViewer, hoverRegion.getOffset());

        if (hoverRegion == null) {
            return null;
        }

        if (this.csf == null || this.manager == null) {
        	return null;
        }

        String bundleName = this.csf.getBundleReference(hoverRegion);
        String key = this.csf.getKeyAt(hoverRegion);

        String hoverText = this.manager.getKeyHoverString(bundleName, key);
        if (hoverText == null || hoverText.equals("")) {
            return null;
        } else {
            return hoverText;
        }
    }

    @Override
    public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
        if (this.editor == null || this.csf == null) {
            return null;
        }

        // Retrieve the property key at this position. Otherwise, null is
        // returned.
        return this.csf.getKeyAt(Long.valueOf(offset));
    }

}
