/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.ui.commands;

import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;

/**
 * @author Matthias Eisenmann, Max Kramer
 * 
 */
public class ApplyStereotypeCommand extends AbstractStereotypeCommand {

    // is set after execution was successful
    private StereotypeApplication stereotypeApplication;

    /**
     * 
     * @param eStereotypableObject
     *            The EStereotypableObject to which the specified stereotype shall be applied at
     *            execution.
     * @param stereotype
     *            The stereotype to be applied.
     */
    public ApplyStereotypeCommand(final EStereotypableObject eStereotypableObject, final Stereotype stereotype) {
        super("Apply Stereotype", eStereotypableObject, stereotype);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.common.command.Command#execute()
     */
    @Override
    public void execute() {
        this.stereotypeApplication = this.eStereotypableObject.applyStereotype(this.stereotype);
    }

    @Override
    public void undo() {
        this.eStereotypableObject.removeStereotypeApplication(this.stereotypeApplication);
    }

}
