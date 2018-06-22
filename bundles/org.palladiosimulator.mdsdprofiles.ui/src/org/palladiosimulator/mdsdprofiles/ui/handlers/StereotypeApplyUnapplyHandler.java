package org.palladiosimulator.mdsdprofiles.ui.handlers;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.modelversioning.emfprofile.Stereotype;
import org.palladiosimulator.mdsdprofiles.api.ProfileAPI;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.mdsdprofiles.ui.Activator;
import org.palladiosimulator.mdsdprofiles.ui.commands.UpdateStereotypeElementsCommand;

/**
 * Handles apply and unapply operations of stereotypes to stereotypable elements via an
 * FeatureEditorDialog.
 * 
 * @author Sebastian Lehrig
 */
public class StereotypeApplyUnapplyHandler extends AbstractApplyUnapplyHandler {

    @Override
    protected void applyUnapplyStateChanged(final ExecutionEvent event) throws ExecutionException {
        final EObject stereotypedElement = getTargetElement(event);

        if (!ProfileAPI.hasProfileApplication(stereotypedElement.eResource())) {
            final Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, "No MDSD Profile applied", null);
            ErrorDialog.openError(HandlerUtil.getActiveShellChecked(event), "MDSD Profiles: Error",
                    "To apply stereotypes, at least one MDSD Profile has to be applied first to the root node", status);
            return;
        }

        final List<Stereotype> choiceOfValues = StereotypeAPI.getApplicableStereotypes(stereotypedElement);
        final List<Stereotype> currentValues = new LinkedList<Stereotype>();
        for (final Stereotype applicableStereotype : choiceOfValues) {
            if (StereotypeAPI.isStereotypeApplied(stereotypedElement, applicableStereotype)) {
                currentValues.add(applicableStereotype);
            }
        }

        final EList<Stereotype> updatedStereotypes = getUpdatedProfileElementsFromDialog(event, stereotypedElement,
                currentValues, choiceOfValues, "Select Profile to be applied");

        if (updatedStereotypes != null) {
            final Command command = UpdateStereotypeElementsCommand.create(stereotypedElement, updatedStereotypes);
            getEditingDomainFor(stereotypedElement).getCommandStack().execute(command);
        }
    }
}
