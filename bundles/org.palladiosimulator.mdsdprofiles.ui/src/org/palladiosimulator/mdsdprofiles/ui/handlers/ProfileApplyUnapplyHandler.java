package org.palladiosimulator.mdsdprofiles.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.modelversioning.emfprofile.Profile;
import org.palladiosimulator.mdsdprofiles.api.ProfileAPI;
import org.palladiosimulator.mdsdprofiles.ui.commands.UpdateProfileElementsCommand;

/**
 * Handles apply and unapply operations of profiles to profilable elements via an
 * FeatureEditorDialog.
 * 
 * @author Sebastian Lehrig
 */
public class ProfileApplyUnapplyHandler extends AbstractApplyUnapplyHandler {

    private static final String SELECT_PROFILE_TO_BE_APPLIED = "Select Profile to be applied";

    @Override
    protected void applyUnapplyStateChanged(final ExecutionEvent event) throws ExecutionException {
        final EObject eObject = getTargetElement(event);
        final EList<Profile> updatedProfiles = getUpdatedProfileElementsFromDialog(event, eObject,
                ProfileAPI.getAppliedProfiles(eObject.eResource()),
                ProfileAPI.getApplicableProfiles(), SELECT_PROFILE_TO_BE_APPLIED);

        if (updatedProfiles != null) {
            final Command command = UpdateProfileElementsCommand.create(eObject.eResource(), updatedProfiles);
            getEditingDomainFor(eObject).getCommandStack().execute(command);
        }
    }
}
