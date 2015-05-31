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
import org.palladiosimulator.mdsdprofiles.StereotypableElement;

import edu.kit.ipd.sdq.mdsd.profiles.util.helper.ProfileHelper;

/**
 * @author Max Kramer
 * 
 */
public abstract class AbstractStereotypeHandler extends AbstractHandler {

    private static final Logger LOGGER = Logger.getLogger(ApplyStereotypeHandler.class.getName());

    /**
     * Returns the identifier for the stereotype command parameter as defined in the plugin.xml.
     * 
     * @return the identifier
     */
    protected abstract String getStereotypeParameterID();

    /**
     * Returns the identifier for the profile command parameter as defined in the plugin.xml.
     * 
     * @return the identifier
     */
    protected abstract String getProfileParameterID();

    /**
     * Returns the action name for debugging.
     * 
     * @return the name
     */
    protected abstract String getActionName();

    /**
     * Returns a command object for the concrete handler.
     * 
     * @param eStereotypableObject
     *            the target object on which the command should be executed
     * @param stereotype
     *            the stereotype for the command
     * @return the command object
     */
    protected abstract Command getCommand(StereotypableElement eStereotypableObject, Stereotype stereotype);

    /**
     * Performs the concrete command in the concrete handler.
     * 
     * @param eStereotypableObject
     *            the target object on which the command should be executed
     * @param stereotype
     *            the stereotype for the command
     */
    protected abstract void handle(StereotypableElement eStereotypableObject, Stereotype stereotype);

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method=execute | event=" + event.toString());
        }

        // TODO: maybe use static variables from ApplicableStereotypesSubmenu
        // here?
        final String stereotypeParameterID = getStereotypeParameterID();
        final String profileParameterID = getProfileParameterID();

        final String selectedStereotype = event.getParameter(stereotypeParameterID);
        final String selectedProfile = event.getParameter(profileParameterID);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("selected stereotype '" + selectedStereotype + "' from profile '" + selectedProfile + "' for "
                    + getActionName());
        }

        final ISelection selection = HandlerUtil.getActiveMenuSelection(event);

        if (selection == null || !(selection instanceof IStructuredSelection)) {
            return null;
        }

        final IStructuredSelection structuredSelection = (IStructuredSelection) selection;

        final Object firstElement = structuredSelection.getFirstElement();

        if (firstElement != null && firstElement instanceof StereotypableElement) {

            final StereotypableElement eStereotypableObject = (StereotypableElement) firstElement;

            final Profile profile = ProfileHelper.getProfile(selectedProfile);

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

                if (selectedObject != null && selectedObject instanceof StereotypableElement) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("selected object is instance of EStereotypableObject | class="
                                + selectedObject.getClass());
                    }

                    final StereotypableElement eStereotypableObject = (StereotypableElement) selectedObject;
                    Stereotype applicableStereotype = null;
                    for (final Stereotype appStereotype : eStereotypableObject.getApplicableStereotypes()) {
                        if (appStereotype.getName().equals(selectedStereotype)) {
                            applicableStereotype = appStereotype;
                            break;
                        }
                    }

                    handle(eStereotypableObject, applicableStereotype);

                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("selected object is not instance of EStereotypableObject | class="
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
