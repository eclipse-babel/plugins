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
package org.eclipse.babel.editor.util;

import java.awt.ComponentOrientation;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.eclipse.babel.editor.compat.SwtRapCompatibilitySWT;
import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.babel.editor.preferences.MsgEditorPreferences;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.text.StringMatcher;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Utility methods related to application UI.
 * 
 * @author Pascal Essiembre
 */
public final class UIUtils {

    public static final String PDE_NATURE = "org.eclipse.pde.PluginNature"; //$NON-NLS-1$
    public static final String JDT_JAVA_NATURE = "org.eclipse.jdt.core.javanature"; //$NON-NLS-1$

    /**
     * Sort the Locales alphabetically. Make sure the root Locale is first.
     * 
     * @param locales
     */
    public static final void sortLocales(Locale[] locales) {
        List<Locale> localesList = new ArrayList<Locale>(Arrays.asList(locales));
        Comparator<Locale> comp = new Comparator<Locale>() {
            public int compare(Locale l1, Locale l2) {
                if (Locale.ROOT.equals(l1)) {
                    return -1;
                }
                if (Locale.ROOT.equals(l2)) {
                    return 1;
                }
                String name1 = ""; //$NON-NLS-1$
                String name2 = ""; //$NON-NLS-1$
                if (l1 != null) {
                    name1 = l1.getDisplayName();
                }
                if (l2 != null) {
                    name2 = l2.getDisplayName();
                }
                return name1.compareTo(name2);
            }
        };
        Collections.sort(localesList, comp);
        for (int i = 0; i < locales.length; i++) {
            locales[i] = localesList.get(i);
        }
    }

    /**
     * @param locale
     * @return true if the locale is selected by the local-filter defined in the
     *         rpeferences
     * @see MsgEditorPreferences#getFilterLocalesStringMatcher()
     */
    public static boolean isDisplayed(Locale locale) {
        if (Locale.ROOT.equals(locale) || locale == null) {
            return true;
        }
        StringMatcher[] patterns = MsgEditorPreferences.getInstance()
                .getFilterLocalesStringMatchers();
        if (patterns == null || patterns.length == 0) {
            return true;
        }
        String locStr = locale.toString();
        for (int i = 0; i < patterns.length; i++) {
            if (patterns[i].match(locStr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reads the filter of locales in the preferences and apply it to filter the
     * passed locales.
     * 
     * @param locales
     * @return The new collection of locales; removed the ones not selected by
     *         the preferences.
     */
    public static Locale[] filterLocales(Locale[] locales) {
        StringMatcher[] patterns = MsgEditorPreferences.getInstance()
                .getFilterLocalesStringMatchers();
        Set<Locale> already = new HashSet<Locale>();
        // first look for the root locale:
        ArrayList<Locale> result = new ArrayList<Locale>();
        for (int j = 0; j < locales.length; j++) {
            Locale loc = locales[j];
            if (Locale.ROOT.equals(loc) || loc == null) {
                already.add(loc);
                result.add(loc);
                break;
            }
        }
        // now go through each pattern until already indexed locales found all
        // locales
        // or we run out of locales.
        for (int pi = 0; pi < patterns.length; pi++) {
            StringMatcher pattern = patterns[pi];
            for (int j = 0; j < locales.length; j++) {
                Locale loc = locales[j];
                if (!already.contains(loc)) {
                    if (pattern.match(loc.toString())) {
                        already.add(loc);
                        result.add(loc);
                        if (already.size() == locales.length) {
                            for (int k = 0; k < locales.length; k++) {
                                locales[k] = (Locale) result.get(k);
                            }
                            return locales;
                        }
                    }
                }
            }
        }
        Locale[] filtered = new Locale[result.size()];
        for (int k = 0; k < filtered.length; k++) {
            filtered[k] = result.get(k);
        }
        return filtered;
    }

    /**
     * Constructor.
     */
    private UIUtils() {
        super();
    }

    /**
     * Gets the approximate width required to display a given number of
     * characters in a control.
     * 
     * @param control
     *            the control on which to get width
     * @param numOfChars
     *            the number of chars
     * @return width
     */
    public static int getWidthInChars(Control control, int numOfChars) {
        GC gc = new GC(control);
        Point extent = gc.textExtent("W");//$NON-NLS-1$
        gc.dispose();
        return numOfChars * extent.x;
    }

    /**
     * Gets the approximate height required to display a given number of
     * characters in a control, assuming, they were laid out vertically.
     * 
     * @param control
     *            the control on which to get height
     * @param numOfChars
     *            the number of chars
     * @return height
     */
    public static int getHeightInChars(Control control, int numOfChars) {
        GC gc = new GC(control);
        Point extent = gc.textExtent("W");//$NON-NLS-1$
        gc.dispose();
        return numOfChars * extent.y;
    }

    /**
     * Shows an error dialog based on the supplied arguments.
     * 
     * @param shell
     *            the shell
     * @param exception
     *            the core exception
     * @param msgKey
     *            key to the plugin message text
     */
    public static void showErrorDialog(Shell shell, CoreException exception,
            String msgKey) {
        exception.printStackTrace();
        ErrorDialog.openError(shell, MessagesEditorPlugin.getString(msgKey),
                exception.getLocalizedMessage(), exception.getStatus());
    }

    /**
     * Shows an error dialog based on the supplied arguments.
     * 
     * @param shell
     *            the shell
     * @param exception
     *            the core exception
     * @param msgKey
     *            key to the plugin message text
     */
    public static void showErrorDialog(Shell shell, Exception exception,
            String msgKey) {
        exception.printStackTrace();
        IStatus status = new Status(IStatus.ERROR,
                MessagesEditorPlugin.PLUGIN_ID, 0,
                MessagesEditorPlugin.getString(msgKey) + " " //$NON-NLS-1$
                        + MessagesEditorPlugin.getString("error.seeLogs"), //$NON-NLS-1$
                exception);
        ErrorDialog.openError(shell, MessagesEditorPlugin.getString(msgKey),
                exception.getLocalizedMessage(), status);
    }

    /**
     * Gets a locale, null-safe, display name.
     * 
     * @param locale
     *            locale to get display name
     * @return display name
     */
    public static String getDisplayName(Locale locale) {
        if (locale == null || Locale.ROOT.equals(locale)) {
            return MessagesEditorPlugin
                    .getString("editor.i18nentry.rootlocale.label"); //$NON-NLS-1$
        }
        return locale.getDisplayName();
    }

    /**
     * Gets the orientation suited for a given locale.
     * 
     * @param locale
     *            the locale
     * @return <code>SWT.RIGHT_TO_LEFT</code> or <code>SWT.LEFT_TO_RIGHT</code>
     */
    public static int getOrientation(Locale locale) {
        if (locale != null) {
            ComponentOrientation orientation = ComponentOrientation
                    .getOrientation(locale);
            if (orientation == ComponentOrientation.RIGHT_TO_LEFT) {
                return SwtRapCompatibilitySWT.RIGHT_TO_LEFT;
            }
        }
        return SWT.LEFT_TO_RIGHT;
    }

    /**
     * Parses manually the project descriptor looking for a nature.
     * <p>
     * Calling IProject.getNature(naturedId) throws exception if the Nature is
     * not defined in the currently executed platform. For example if looking
     * for a pde nature inside an eclipse-platform.
     * </p>
     * <p>
     * This method returns the result without that constraint.
     * </p>
     * 
     * @param proj
     *            The project to examine
     * @param nature
     *            The nature to look for.
     * @return true if the nature is defined in that project.
     */
    public static boolean hasNature(IProject proj, String nature) {
        IFile projDescr = proj.getFile(".project"); //$NON-NLS-1$
        if (!projDescr.exists()) {
            return false;// a corrupted project
        }
        // <classpathentry kind="src" path="src"/>
        InputStream in = null;
        try {
            projDescr.refreshLocal(IResource.DEPTH_ZERO, null);
            in = projDescr.getContents();
            // supposedly in utf-8. should not really matter for us
            Reader r = new InputStreamReader(in, "UTF-8");
            LineNumberReader lnr = new LineNumberReader(r);
            String line = lnr.readLine();
            while (line != null) {
                if (line.trim().equals("<nature>" + nature + "</nature>")) {
                    lnr.close();
                    r.close();
                    return true;
                }
                line = lnr.readLine();
            }
            lnr.close();
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                }
        }
        return false;
    }


    
}
