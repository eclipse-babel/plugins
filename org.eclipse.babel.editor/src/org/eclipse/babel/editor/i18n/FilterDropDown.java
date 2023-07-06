package org.eclipse.babel.editor.i18n;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.babel.editor.IMessagesEditorChangeListener;
import org.eclipse.babel.editor.actions.FilterKeysAction;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.util.BabelSharedImages;
import org.eclipse.babel.editor.util.IBabelSharedImages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolItem;

public class FilterDropDown extends Action {

    private Menu fMenu;
    private List<Action> actions = new ArrayList<>();
    private AbstractMessagesEditor editor;
    private TreeViewer treeViewer;

    public FilterDropDown(AbstractMessagesEditor editor, TreeViewer treeViewer) {
        super("Set filter", IAction.AS_PUSH_BUTTON);
        this.editor = editor;
        this.treeViewer = treeViewer;
        setImageDescriptor(BabelSharedImages.getDescriptor(IBabelSharedImages.IMAGE_VIEW_MENU));
        actions = new ArrayList<>();
        createActions();
    }

    private void createActions() {
    	actions.add(new FilterKeysAction(this.editor, IMessagesEditorChangeListener.SHOW_ALL));
    	actions.add(new FilterKeysAction(this.editor, IMessagesEditorChangeListener.SHOW_ONLY_MISSING));
    	actions.add(new FilterKeysAction(this.editor, IMessagesEditorChangeListener.SHOW_ONLY_MISSING_AND_UNUSED));
    	actions.add(new FilterKeysAction(this.editor, IMessagesEditorChangeListener.SHOW_ONLY_UNUSED));
    }

    @Override
    public void runWithEvent(Event event) {
    	ToolItem toolItem = (ToolItem) event.widget;

    	Menu localMenu = getMenu(toolItem.getParent().getShell());

		// Show the menu
		Rectangle ib = toolItem.getBounds();
		Point displayAt = toolItem.getParent().toDisplay(ib.x, ib.y + ib.height);
		localMenu.setLocation(displayAt);
		localMenu.setVisible(true);
    }

    public void dispose() {
        if (fMenu != null && !fMenu.isDisposed()) {
            fMenu.dispose();
        }
    }

    public Menu getMenu(Control parent) {
        if (fMenu != null && !fMenu.isDisposed()) {
            fMenu.dispose();
        }

        fMenu = new Menu(parent);
        for (IAction action : actions) {
            addActionToMenu(fMenu, action);
        }
        return fMenu;
    }

    protected void addActionToMenu(Menu parent, IAction action) {
        ActionContributionItem item = new ActionContributionItem(action);
        item.fill(parent, -1);
    }

	
}