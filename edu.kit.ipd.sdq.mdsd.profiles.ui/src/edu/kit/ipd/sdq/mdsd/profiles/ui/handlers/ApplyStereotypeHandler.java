/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.ui.handlers;

import org.eclipse.emf.common.command.Command;
import org.modelversioning.emfprofile.Stereotype;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
import edu.kit.ipd.sdq.mdsd.profiles.ui.Constants;
import edu.kit.ipd.sdq.mdsd.profiles.ui.commands.ApplyStereotypeCommand;

/**
 * @author Matthias Eisenmann, Max Kramer
 * 
 */
public class ApplyStereotypeHandler extends AbstractStereotypeHandler {
	@Override
	protected String getStereotypeParameterID() {
		return Constants.APPLY_STEREO_PARAM_ID;
	}

	@Override
	protected String getProfileParameterID() {
		return Constants.APPLY_PROFILE_PARAM_ID;
	}

	@Override
	protected String getActionName() {
		return Constants.APPLY_ACTION_NAME;
	}

	@Override
	protected Command getCommand(EStereotypableObject eStereotypableObject,
			Stereotype stereotype) {
		return new ApplyStereotypeCommand(eStereotypableObject, stereotype);
	}

	@Override
	protected void handle(EStereotypableObject eStereotypableObject,
			Stereotype stereotype) {
		if (stereotype != null) {
			eStereotypableObject.applyStereotype(stereotype);
		}
	}
}
