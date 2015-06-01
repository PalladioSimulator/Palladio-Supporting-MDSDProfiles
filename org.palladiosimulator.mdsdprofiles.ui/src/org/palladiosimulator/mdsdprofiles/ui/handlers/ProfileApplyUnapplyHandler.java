package org.palladiosimulator.mdsdprofiles.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.palladiosimulator.mdsdprofiles.ProfileableElement;

/**
 * Handles apply and unapply operations of profiles to profilable elements via an
 * FeatureEditorDialog.
 * 
 * @author Sebastian Lehrig
 */
public class ProfileApplyUnapplyHandler extends AbstractApplyUnapplyHandler {

    private static final String SELECT_PROFILE_TO_BE_APPLIED = "Select Profile to be applied";

    @Override
    protected boolean applyUnapplyStateChanged(final ExecutionEvent event) throws ExecutionException {
        final ProfileableElement profileableElement = getTargetElement(event);

        // FIXME
        // final ProfileableElement profileableElement = getTargetElement(event);
        // getEditingDomainFor(profileableElement).createCommand(commandClass, commandParameter)
        return profileableElement.updateProfileApplications(getUpdatedProfileElementsFromDialog(event,
                profileableElement, profileableElement.getAppliedProfiles(),
                profileableElement.getApplicableProfiles(), SELECT_PROFILE_TO_BE_APPLIED));
    }

}
