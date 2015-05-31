package edu.kit.ipd.sdq.mdsd.profiles.view.action;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * Clears the entire PCM Profiles View, if activated.
 * 
 * @author emretaspolat
 * 
 */

public class ClearAction extends Action {

    private static final Logger LOGGER = Logger.getLogger(RemoveStereotypeAction.class);
    private final TreeViewer treeViewer;
    private final TableViewer tableViewer;

    public ClearAction(final TreeViewer viewer, final TableViewer table) {
        super("[Clear View]", IAction.AS_PUSH_BUTTON);
        this.treeViewer = viewer;
        this.tableViewer = table;
    }

    @Override
    public void run() {
        this.treeViewer.refresh();
        this.tableViewer.refresh();
        LOGGER.info("View cleared.");
    }
}
