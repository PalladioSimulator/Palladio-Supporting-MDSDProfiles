package org.palladiosimulator.mdsdprofiles.ui.handlers;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.modelversioning.emfprofile.Stereotype;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;
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
        final StereotypableElement stereotypeableElement = getTargetElement(event);

        if (stereotypeableElement.getProfileableElement() == null
                || !stereotypeableElement.getProfileableElement().hasProfileApplication()) {
            final Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, 0, "No MDSD Profile applied", null);
            ErrorDialog.openError(HandlerUtil.getActiveShellChecked(event), "MDSD Profiles: Error",
                    "To apply stereotypes, at least one MDSD Profile has to be applied first to the root node", status);
            return;
        }

        final List<Stereotype> choiceOfValues = stereotypeableElement.getApplicableStereotypes();
        final List<Stereotype> currentValues = new LinkedList<Stereotype>();
        for (final Stereotype applicableStereotype : choiceOfValues) {
            if (stereotypeableElement.isStereotypeApplied(applicableStereotype)) {
                currentValues.add(applicableStereotype);
            }
        }

        final UpdateStereotypeElementsCommand command = UpdateStereotypeElementsCommand.create(
                stereotypeableElement,
                getUpdatedProfileElementsFromDialog(event, stereotypeableElement, currentValues, choiceOfValues,
                        "Select Profile to be applied"));

        getEditingDomainFor(stereotypeableElement).getCommandStack().execute(command);
    }
}
