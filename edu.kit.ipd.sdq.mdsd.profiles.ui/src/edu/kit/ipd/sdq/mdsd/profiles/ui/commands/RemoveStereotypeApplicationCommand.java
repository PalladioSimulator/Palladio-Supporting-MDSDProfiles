package edu.kit.ipd.sdq.mdsd.profiles.ui.commands;

import org.modelversioning.emfprofile.Stereotype;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;

/**
 * A command for the removal of a single stereotype application.
 * 
 * @author Max Kramer
 * 
 */
public class RemoveStereotypeApplicationCommand extends AbstractStereotypeCommand {
    /**
     * Default constructor for the command.
     * 
     * @param eStereotypableObject
     *            stereotypable object of the command
     * @param stereotype
     *            the stereotype of the command
     * @param stereotypeApplication
     *            the stereotype application to be removed
     */
    public RemoveStereotypeApplicationCommand(final StereotypableElement eStereotypableObject,
            final Stereotype stereotype) {
        super("Remove Stereotype Application", eStereotypableObject, stereotype);
    }

    @Override
    public void execute() {
        this.eStereotypableObject.unapplyStereotype(this.stereotype);
    }

    @Override
    public void undo() {
        this.eStereotypableObject.applyStereotype(stereotype);
    }

}
