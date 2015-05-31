/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.ui.handlers;

import org.eclipse.emf.common.command.Command;
import org.modelversioning.emfprofile.Stereotype;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;

import edu.kit.ipd.sdq.mdsd.profiles.ui.ProfilesUIConstants;
import edu.kit.ipd.sdq.mdsd.profiles.ui.commands.ApplyStereotypeCommand;

/**
 * @author Matthias Eisenmann, Max Kramer
 * 
 */
public class ApplyStereotypeHandler extends AbstractStereotypeHandler {
    @Override
    protected String getStereotypeParameterID() {
        return ProfilesUIConstants.APPLY_STEREO_PARAM_ID;
    }

    @Override
    protected String getProfileParameterID() {
        return ProfilesUIConstants.APPLY_PROFILE_PARAM_ID;
    }

    @Override
    protected String getActionName() {
        return ProfilesUIConstants.APPLY_ACTION_NAME;
    }

    @Override
    protected Command getCommand(StereotypableElement eStereotypableObject, Stereotype stereotype) {
        return new ApplyStereotypeCommand(eStereotypableObject, stereotype);
    }

    @Override
    protected void handle(StereotypableElement eStereotypableObject, Stereotype stereotype) {
        if (stereotype != null) {
            eStereotypableObject.applyStereotype(stereotype);
        }
    }
}
