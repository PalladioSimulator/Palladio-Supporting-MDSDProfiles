package edu.kit.ipd.sdq.mdsd.profiles.ui.commands;

import org.eclipse.emf.common.command.AbstractCommand;
import org.modelversioning.emfprofile.Stereotype;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;

public abstract class AbstractStereotypeCommand extends AbstractCommand {

	protected EStereotypableObject eStereotypableObject;
	protected Stereotype stereotype;

	public AbstractStereotypeCommand(final String label, final EStereotypableObject eStereotypableObject, final Stereotype stereotype) {
		super(label);
        this.eStereotypableObject = eStereotypableObject;
        this.stereotype = stereotype;
        this.isExecutable = true;
        this.isPrepared = true;
	}

	public AbstractStereotypeCommand(String label, String description) {
		super(label, description);
	}

	@Override
	public void redo() {
	    execute();
	}

}