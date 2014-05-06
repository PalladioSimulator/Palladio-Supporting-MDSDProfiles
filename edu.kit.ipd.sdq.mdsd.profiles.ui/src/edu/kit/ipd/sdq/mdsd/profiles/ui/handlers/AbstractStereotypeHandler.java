package edu.kit.ipd.sdq.mdsd.profiles.ui.handlers;

import org.apache.log4j.Logger;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.Stereotype;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
import edu.kit.ipd.sdq.mdsd.profiles.util.helper.Helper;

/**
 * @author Max Kramer
 * 
 */
public abstract class AbstractStereotypeHandler extends AbstractHandler {

    private static final Logger logger = Logger.getLogger(ApplyStereotypeHandler.class.getName());

    protected abstract String getStereotypeParameterID();

    protected abstract String getProfileParameterID();

    protected abstract String getActionName();

    protected abstract Command getCommand(EStereotypableObject eStereotypableObject, Stereotype stereotype);

    protected abstract void handle(EStereotypableObject eStereotypableObject, Stereotype stereotype);

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        if (logger.isDebugEnabled()) {
            logger.debug("method=execute | event=" + event.toString());
        }

        // TODO: maybe use static variables from ApplicableStereotypesSubmenu
        // here?
        final String stereotypeParameterID = getStereotypeParameterID();
        final String profileParameterID = getProfileParameterID();

        final String selectedStereotype = event.getParameter(stereotypeParameterID);
        final String selectedProfile = event.getParameter(profileParameterID);

        if (logger.isDebugEnabled()) {
            logger.debug("selected stereotype '" + selectedStereotype + "' from profile '" + selectedProfile + "' for "
                    + getActionName());
        }

        final ISelection selection = HandlerUtil.getActiveMenuSelection(event);

        if (selection == null || !(selection instanceof IStructuredSelection)) {
            return null;
        }

        final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

        final Object firstElement = structuredSelection.getFirstElement();

        if (firstElement != null && firstElement instanceof EStereotypableObject) {

            final EStereotypableObject eStereotypableObject = (EStereotypableObject) firstElement;

            final Profile profile = Helper.getProfile(selectedProfile);

            if (profile != null) {
                final Stereotype applicableStereotype = profile.getStereotype(selectedStereotype);

                if (applicableStereotype != null) {

                    final IEditorPart editorPart = HandlerUtil.getActiveEditor(event);

                    if (editorPart != null && editorPart instanceof IEditingDomainProvider) {
                        Command stereotypeCommand = getCommand(eStereotypableObject, applicableStereotype);

                        ((IEditingDomainProvider) editorPart).getEditingDomain().getCommandStack()
                                .execute(stereotypeCommand);
                    }
                }
            }

            return null;
        }

        // TODO: check if this is obsolete for graphical editors
        final Shell shell = HandlerUtil.getActiveShell(event);

        if (firstElement != null && firstElement instanceof EditPart) {
            EditPart editPart = (EditPart) firstElement;
            Object model = editPart.getModel();

            if (model != null && model instanceof Node) {
                Node node = (Node) model;
                EObject selectedObject = node.getElement();

                if (selectedObject != null && selectedObject instanceof EStereotypableObject) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("selected object is instance of EStereotypableObject | class="
                                + selectedObject.getClass());
                    }

                    final EStereotypableObject eStereotypableObject = (EStereotypableObject) selectedObject;
                    final Stereotype applicableStereotype = eStereotypableObject
                            .getApplicableStereotype(selectedStereotype);

                    handle(eStereotypableObject, applicableStereotype);

                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("selected object is not instance of EStereotypableObject | class="
                                + selectedObject.getClass());
                    }
                }

            } else {
                MessageDialog.openInformation(shell, "Info", "model from edit part is not an instance of Node!");
            }

        } else {
            MessageDialog.openInformation(shell, "Info", "Please select an edit part");
        }
        return null;
    }
}
