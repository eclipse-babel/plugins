package org.eclipse.babel.editor.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class BabelSharedImages {

	private static ImageRegistry imageRegistry;

	static {
		ImageRegistry localRegistry = getImageRegistry();

		localRegistry.put(IBabelSharedImages.IMAGE_RESOURCE_BUNDLE, createImageDescriptor(IBabelSharedImages.IMAGE_RESOURCE_BUNDLE));
		localRegistry.put(IBabelSharedImages.IMAGE_PROPERTIES_FILE, createImageDescriptor(IBabelSharedImages.IMAGE_PROPERTIES_FILE));
		localRegistry.put(IBabelSharedImages.IMAGE_NEW_PROPERTIES_FILE, createImageDescriptor(IBabelSharedImages.IMAGE_NEW_PROPERTIES_FILE));
		localRegistry.put(IBabelSharedImages.IMAGE_LAYOUT_HIERARCHICAL, createImageDescriptor(IBabelSharedImages.IMAGE_LAYOUT_HIERARCHICAL));
		localRegistry.put(IBabelSharedImages.IMAGE_LAYOUT_FLAT, createImageDescriptor(IBabelSharedImages.IMAGE_LAYOUT_FLAT));

		localRegistry.put(IBabelSharedImages.IMAGE_ADD, createImageDescriptor(IBabelSharedImages.IMAGE_ADD));
		localRegistry.put(IBabelSharedImages.IMAGE_RENAME, createImageDescriptor(IBabelSharedImages.IMAGE_RENAME));

		localRegistry.put(IBabelSharedImages.IMAGE_REFACTORING, createImageDescriptor(IBabelSharedImages.IMAGE_REFACTORING));
		localRegistry.put(IBabelSharedImages.IMAGE_VIEW_LEFT, createImageDescriptor(IBabelSharedImages.IMAGE_VIEW_LEFT));
		
		localRegistry.put(IBabelSharedImages.IMAGE_EXPAND_ALL, createImageDescriptor(IBabelSharedImages.IMAGE_EXPAND_ALL));
		localRegistry.put(IBabelSharedImages.IMAGE_COLLAPSE_ALL, createImageDescriptor(IBabelSharedImages.IMAGE_COLLAPSE_ALL));
		
		localRegistry.put(IBabelSharedImages.IMAGE_KEY, createImageDescriptor(IBabelSharedImages.IMAGE_KEY));
		localRegistry.put(IBabelSharedImages.IMAGE_EMPTY, createImageDescriptor(IBabelSharedImages.IMAGE_EMPTY));
		localRegistry.put(IBabelSharedImages.IMAGE_DUPLICATE, createImageDescriptor(IBabelSharedImages.IMAGE_DUPLICATE));

		localRegistry.put(IBabelSharedImages.IMAGE_BASE_UNUSED_TRANSLATION, createImageDescriptor(IBabelSharedImages.IMAGE_BASE_UNUSED_TRANSLATION));

		localRegistry.put(IBabelSharedImages.IMAGE_ERROR, createImageDescriptor(IBabelSharedImages.IMAGE_ERROR));
		localRegistry.put(IBabelSharedImages.IMAGE_WARNING, createImageDescriptor(IBabelSharedImages.IMAGE_WARNING));

		localRegistry.put(IBabelSharedImages.IMAGE_WARNED_TRANSLATION, createWarnedTranslationImageDescriptor());
		localRegistry.put(IBabelSharedImages.IMAGE_MISSING_TRANSLATION, createMissingTranslationImageDescriptor());
		localRegistry.put(IBabelSharedImages.IMAGE_UNUSED_TRANSLATION, creteUnusedTranslationsImageDescriptor());
		localRegistry.put(IBabelSharedImages.IMAGE_UNUSED_AND_MISSING_TRANSLATIONS, createMissingAndUnusedTranslationsImageDescriptor());

		localRegistry.put(IBabelSharedImages.IMAGE_MINUS, createImageDescriptor(IBabelSharedImages.IMAGE_MINUS));
		localRegistry.put(IBabelSharedImages.IMAGE_PLUS, createImageDescriptor(IBabelSharedImages.IMAGE_PLUS));

		localRegistry.put(IBabelSharedImages.IMAGE_SIMILAR, createImageDescriptor(IBabelSharedImages.IMAGE_SIMILAR));
	}

	private BabelSharedImages()
	{

	}

	private static ImageRegistry getImageRegistry()
	{
		if (BabelSharedImages.imageRegistry == null) {
			Display display = Display.getCurrent();
			if (display == null) {
				display = Display.getDefault();
			}
			BabelSharedImages.imageRegistry = new ImageRegistry(display);
		}
		return BabelSharedImages.imageRegistry;
	}
	
	/**
	 * Returns the image managed under the given key in this registry.
	 *
	 * @param key the image's key
	 * @return the image managed under the given key
	 */
	public static Image get(String key) {
		Image image = getImageRegistry().get(key);
		if ( image == null ) {
			new Exception().printStackTrace();
		}

		return image;
	}

	/**
	 * Returns the image descriptor for the given key in this registry. Might be called in a non-UI thread.
	 *
	 * @param key the image's key
	 * @return the image descriptor for the given key
	 */
	public static ImageDescriptor getDescriptor(String key) {
		ImageDescriptor descriptor = getImageRegistry().getDescriptor(key);
		if ( descriptor == null ) {
			new Exception().printStackTrace();
		}
		return descriptor;
	}


	public static void putDescriptor(String key, ImageDescriptor imageDescriptor) {
		getImageRegistry().put(key, imageDescriptor);
	}
	
	/**
	 * Creates an ImageDescritor for the passed name by searching the icons folder for the image.
	 * @param name
	 * @return
	 */
	public static ImageDescriptor createImageDescriptor(String name) {

		String iconPath = "icons/"; //$NON-NLS-1$
		try {
			URL installURL = MessagesEditorPlugin.getDefault().getBundle().getEntry("/"); //$NON-NLS-1$
			URL url = new URL(installURL, iconPath + name);
			return ImageDescriptor.createFromURL(url);

		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}

	}

    /**
     * @return ImageDescriptor for the icon which indicates a key that has missing
     *         translations
     */
	public static ImageDescriptor createMissingTranslationImageDescriptor() {
		ImageDescriptor imageDescriptor = BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_KEY);
		ImageDescriptor missing = BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_ERROR);
		return new DecorationOverlayIcon(imageDescriptor, missing, IDecoration.BOTTOM_RIGHT);
	}

    /**
     * @return ImageDescriptor for the icon which indicates a key that is unused
     */
    public static ImageDescriptor creteUnusedTranslationsImageDescriptor() {
        ImageDescriptor imageDescriptor = BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_BASE_UNUSED_TRANSLATION);
        ImageDescriptor warning = BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_WARNING);
        return new DecorationOverlayIcon(imageDescriptor, warning, IDecoration.BOTTOM_RIGHT);
    }

    /**
     * @return ImageDescriptor for the icon which indicates a key that has missing
     *         translations and is unused
     */
    public static ImageDescriptor createMissingAndUnusedTranslationsImageDescriptor() {
        ImageDescriptor imageDescriptor = BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_BASE_UNUSED_TRANSLATION);
        ImageDescriptor missing = BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_ERROR);
        return new DecorationOverlayIcon(imageDescriptor, missing, IDecoration.BOTTOM_RIGHT);
    }

    /**
     * @return ImageDescriptor for the icon which indicates a key that has duplicate
     *         entries
     */
    public static ImageDescriptor createaDuplicateEntryAndUnusedImageDescriptor() {
		ImageDescriptor imageDescriptor = BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_KEY);
		ImageDescriptor warning = BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_WARNING);
        return new DecorationOverlayIcon(imageDescriptor, warning, IDecoration.BOTTOM_RIGHT);
    }

    /**
     * @return ImageDescriptor for the icon which indicates a key that has duplicate
     *         entries and is unused
     */
    public static ImageDescriptor createDuplicateEntryAndUnusedTranslationsImageDescriptor() {
        ImageDescriptor imageDescriptor = BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_BASE_UNUSED_TRANSLATION);
        ImageDescriptor duplicate = BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_DUPLICATE);
        return new DecorationOverlayIcon(imageDescriptor, duplicate, IDecoration.BOTTOM_RIGHT);
    }

    /**
     * @return ImageDescriptor for a general translation warning
     */
    private static ImageDescriptor createWarnedTranslationImageDescriptor() {
        ImageDescriptor imageDescriptor = BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_KEY);
        ImageDescriptor warning = BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_WARNING);
        return new DecorationOverlayIcon(imageDescriptor, warning, IDecoration.BOTTOM_RIGHT);
	}

}
