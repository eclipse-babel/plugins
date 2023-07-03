package org.eclipse.babel.editor.util;

import java.util.Locale;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class LocaleImageUtil {
	
	private LocaleImageUtil() {
		/* Utility class */
	}

    /**
     * Loads country icon based on locale country.
     * 
     * @param countryLocale
     *            the locale on which to grab the country
     * @return an image, or <code>null</code> if no match could be made
     */
    public static Image getCountryIcon(Locale countryLocale) {
        Image image = null;
        String countryCode = null;
        if (countryLocale != null && countryLocale.getCountry() != null) {
            countryCode = countryLocale.getCountry().toLowerCase();
        }

        if (countryCode != null && countryCode.length() > 0) {

            String imageName = "countries/" + //$NON-NLS-1$
                    countryCode.toLowerCase() + ".gif"; //$NON-NLS-1$

            ImageDescriptor descriptor = BabelSharedImages.getDescriptor(countryCode);
            if ( descriptor == null ) {
            	descriptor = BabelSharedImages.createImageDescriptor(imageName);
            	BabelSharedImages.putDescriptor(countryCode, descriptor);
            }

            image = BabelSharedImages.get(countryCode);
        }
        if (image == null) {
        	image = BabelSharedImages.get("countries/blank.gif"); //$NON-NLS-1$
        }
        return image;
    }
}
