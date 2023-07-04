package org.eclipse.babel.editor.tree.actions;

import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.plugin.MessagesEditorPlugin;
import org.eclipse.babel.editor.util.ClipboardUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class PasteKeyAction extends AbstractTreeAction {

	public PasteKeyAction(AbstractMessagesEditor editor, TreeViewer treeViewer) {
		super(editor, treeViewer);

        setText(MessagesEditorPlugin.getString("key.paste")); //$NON-NLS-1$

		ImageDescriptor pasteImageDescriptor = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE);
		ImageDescriptor disabledPasteImageDescriptor = PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED);

		setImageDescriptor(pasteImageDescriptor);
		setDisabledImageDescriptor(disabledPasteImageDescriptor);
	}

	@Override
	public void runWithEvent(Event event) {
		MessagesBundleGroup bundleGroup = getBundleGroup();
        String clipboardContent = ClipboardUtil.getFromClipboard(event.display);
        if(clipboardContent != null) {
        	ClipboardUtil.deserializeKeys(bundleGroup, clipboardContent);
        }
	}
	
	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		/* We need a display to access the clipboard */
		Display display = null;
		if(this.treeViewer != null && this.treeViewer.getControl().isDisposed() == false) {
			display = this.treeViewer.getControl().getDisplay();
		}
	
		boolean enableAction = false;
		if(display != null) {
	        String clipboardContent = ClipboardUtil.getFromClipboard(display);
	        /* If there are valid clipboard content, the action should be enabled */
	        enableAction = clipboardContent != null && !clipboardContent.isEmpty();
		}

        this.setEnabled(enableAction);
	}
}
