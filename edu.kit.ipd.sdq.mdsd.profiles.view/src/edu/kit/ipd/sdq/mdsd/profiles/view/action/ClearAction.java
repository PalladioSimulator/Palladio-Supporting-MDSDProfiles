package edu.kit.ipd.sdq.mdsd.profiles.view.action;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;

import edu.kit.ipd.sdq.mdsd.profiles.registry.ProfileApplicationFileRegistry;

/**
 * Clears the entire PCM Profiles View, if activated.
 * 
 * @author emretaspolat
 * 
 */

public class ClearAction extends Action {

    private static Logger logger = Logger.getLogger(RemoveStereotypeAction.class);
    private final TreeViewer treeViewer;
    private final TableViewer tableViewer;

    public ClearAction(final TreeViewer viewer, final TableViewer table) {
        super("[Clear View]", IAction.AS_PUSH_BUTTON);
        this.treeViewer = viewer;
        this.tableViewer = table;
    }

    @Override
    public void run() {
        ProfileApplicationFileRegistry.INSTANCE.clear();
        this.treeViewer.refresh();
        this.tableViewer.refresh();
        logger.info("View cleared.");
    }
}
