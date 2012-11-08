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
package org.eclipse.babel.tapiji.tools.core.ui.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.babel.tapiji.tools.core.Activator;
import org.eclipse.babel.tapiji.tools.core.Logger;
import org.eclipse.babel.tapiji.tools.core.ui.analyzer.RBAuditor;
import org.eclipse.babel.tapiji.tools.core.ui.extensions.I18nAuditor;
import org.eclipse.babel.tapiji.tools.core.ui.preferences.TapiJIPreferences;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.util.IPropertyChangeListener;

public class ExtensionManager {

    // list of registered extension plug-ins
    private static List<I18nAuditor> extensions = null;

    // change listener for builder property change events
    private static IPropertyChangeListener propertyChangeListener = null;

    // file-endings supported by the registered extension plug-ins
    private static Set<String> supportedFileEndings = new HashSet<String>();

    public static List<I18nAuditor> getRegisteredI18nAuditors() {
	if (extensions == null) {
	    extensions = new ArrayList<I18nAuditor>();

	    // init default auditors
	    extensions.add(new RBAuditor());

	    // lookup registered auditor extensions
	    IConfigurationElement[] config = Platform
		    .getExtensionRegistry()
		    .getConfigurationElementsFor(Activator.BUILDER_EXTENSION_ID);

	    try {
		for (IConfigurationElement e : config) {
		    addExtensionPlugIn((I18nAuditor) e
			    .createExecutableExtension("class"));
		}
	    } catch (CoreException ex) {
		Logger.logError(ex);
	    }
	}

	// init builder property change listener
	if (propertyChangeListener == null) {
	    propertyChangeListener = new BuilderPropertyChangeListener();
	    TapiJIPreferences.addPropertyChangeListener(propertyChangeListener);
	}

	return extensions;
    }

    public static Set<String> getSupportedFileEndings() {
	return supportedFileEndings;
    }

    private static void addExtensionPlugIn(I18nAuditor extension) {
	I18nAuditor a = extension;
	extensions.add(a);
	supportedFileEndings.addAll(Arrays.asList(a.getFileEndings()));
    }
}