/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.ui.handlers;

import org.eclipse.emf.common.command.Command;
import org.modelversioning.emfprofile.Stereotype;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
import edu.kit.ipd.sdq.mdsd.profiles.ui.ProfilesUIConstants;
import edu.kit.ipd.sdq.mdsd.profiles.ui.commands.UnapplyStereotypeCommand;

/**
 * @author Max Kramer
 * 
 */
public class UnapplyStereotypeHandler extends AbstractStereotypeHandler {
    @Override
    protected String getStereotypeParameterID() {
        return ProfilesUIConstants.UNAPPLY_STEREO_PARAM_ID;
    }

    @Override
    protected String getProfileParameterID() {
        return ProfilesUIConstants.UNAPPLY_PROFILE_PARAM_ID;
    }

    @Override
    protected String getActionName() {
        return ProfilesUIConstants.UNAPPLY_ACTION_NAME;
    }

    @Override
    protected Command getCommand(EStereotypableObject eStereotypableObject, Stereotype stereotype) {
        return new UnapplyStereotypeCommand(eStereotypableObject, stereotype);
    }

    @Override
    protected void handle(EStereotypableObject eStereotypableObject, Stereotype stereotype) {
        eStereotypableObject.removeAllStereotypeApplications(stereotype);
    }
}
