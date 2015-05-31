/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.ui.commands;

import org.modelversioning.emfprofile.Stereotype;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;

/**
 * @author Matthias Eisenmann, Max Kramer
 * 
 */
public class ApplyStereotypeCommand extends AbstractStereotypeCommand {

    /**
     * 
     * @param eStereotypableObject
     *            The EStereotypableObject to which the specified stereotype shall be applied at
     *            execution.
     * @param stereotype
     *            The stereotype to be applied.
     */
    public ApplyStereotypeCommand(final StereotypableElement eStereotypableObject, final Stereotype stereotype) {
        super("Apply Stereotype", eStereotypableObject, stereotype);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.common.command.Command#execute()
     */
    @Override
    public void execute() {
        this.eStereotypableObject.applyStereotype(this.stereotype);
    }

    @Override
    public void undo() {
        this.eStereotypableObject.unapplyStereotype(this.stereotype);
    }

}
