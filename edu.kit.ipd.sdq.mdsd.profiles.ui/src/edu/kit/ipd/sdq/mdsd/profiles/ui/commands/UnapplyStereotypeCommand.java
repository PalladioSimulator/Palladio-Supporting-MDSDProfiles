package edu.kit.ipd.sdq.mdsd.profiles.ui.commands;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;

/**
 * @author Max Kramer
 * 
 */
public class UnapplyStereotypeCommand extends AbstractStereotypeCommand {

    // is set after execution was successful
    private EList<RemoveStereotypeApplicationCommand> removeSACommands;

    /**
     * 
     * @param eStereotypableObject
     *            The EStereotypableObject from which all stereotype applications of the given
     *            stereotype shall be removew at execution.
     * @param stereotype
     *            The stereotype to be applied.
     */
    public UnapplyStereotypeCommand(final StereotypableElement eStereotypableObject, final Stereotype stereotype) {
        super("Unapply Stereotype", eStereotypableObject, stereotype);
        this.removeSACommands = new BasicEList<RemoveStereotypeApplicationCommand>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.emf.common.command.Command#execute()
     */
    @Override
    public void execute() {
        EList<StereotypeApplication> stereotypeApplications = this.eStereotypableObject.getStereotypeApplications();
        for (StereotypeApplication stereotypeApplication : stereotypeApplications) {
            RemoveStereotypeApplicationCommand removeSACommand = new RemoveStereotypeApplicationCommand(
                    this.eStereotypableObject, this.stereotype);
            removeSACommand.execute();
            this.removeSACommands.add(removeSACommand);
        }
    }

    @Override
    public void undo() {
        for (RemoveStereotypeApplicationCommand removeSACommand : this.removeSACommands) {
            removeSACommand.undo();
        }
    }

}
