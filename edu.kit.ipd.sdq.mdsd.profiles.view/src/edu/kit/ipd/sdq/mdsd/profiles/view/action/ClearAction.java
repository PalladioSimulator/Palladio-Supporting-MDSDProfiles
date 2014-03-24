package edu.kit.ipd.sdq.mdsd.profiles.view.action;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.kit.ipd.sdq.mdsd.profiles.registry.ProfileApplicationFileRegistry;
import edu.kit.ipd.sdq.mdsd.profiles.view.viewer.ProfilePropertiesView;

/**
 * Clears the entire PCM Profiles View, if activated.
 * 
 * @author emretaspolat
 *
 */

public class ClearAction extends Action {

	private static Logger logger = Logger.getLogger(RemoveStereotypeAction.class);		
	private TreeViewer treeViewer;
	private TableViewer tableViewer;


	public ClearAction(TreeViewer viewer, TableViewer table) {
		super("[Clear View]", IAction.AS_PUSH_BUTTON);
		this.treeViewer = viewer;
		this.tableViewer = table;
	}
	
	public void run() {
		ProfileApplicationFileRegistry.INSTANCE.clear();
		treeViewer.refresh();
		tableViewer.refresh();
		logger.info("View cleared.");
	}
}
