package edu.kit.ipd.sdq.mdsd.profiles.view.action;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
import edu.kit.ipd.sdq.mdsd.profiles.ui.commands.RemoveStereotypeApplicationCommand;

/**
 * Removes the in TreeViewer selected StereotypeApplication.
 * 
 * @author emretaspolat
 * 
 */
public class RemoveStereotypeAction extends Action {

    private static Logger logger = Logger.getLogger(RemoveStereotypeAction.class);
    private final TreeViewer treeViewer;
    private final TableViewer tableViewer;

    public RemoveStereotypeAction(final TreeViewer viewer, final TableViewer table) {
        super("[Remove Stereotype]", IAction.AS_PUSH_BUTTON);
        this.treeViewer = viewer;
        this.tableViewer = table;
    }

    @Override
    public void run() {
        final IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .getActiveEditor();
        final StereotypeApplication sa = (StereotypeApplication) ((IStructuredSelection) this.treeViewer.getSelection())
                .getFirstElement();
        logger.info("To be removed stereotype application is: " + sa.toString());
        if (sa != null) {
            final EStereotypableObject temp = (EStereotypableObject) sa.getAppliedTo();
            logger.info("Temp is: " + temp);
            final Stereotype stereotype = sa.getStereotype();
            logger.info("Stereotype is: " + stereotype);
            final Command removeStereotypeCommand = new RemoveStereotypeApplicationCommand(temp, stereotype, sa);
            ((IEditingDomainProvider) editorPart).getEditingDomain().getCommandStack().execute(removeStereotypeCommand);
            logger.info("Following stereotype is removed " + sa.toString() + " from " + temp.toString());
            this.treeViewer.refresh();
            this.tableViewer.setItemCount(0); // Workaround for tableViewer.refresh();
        }
    }

}
