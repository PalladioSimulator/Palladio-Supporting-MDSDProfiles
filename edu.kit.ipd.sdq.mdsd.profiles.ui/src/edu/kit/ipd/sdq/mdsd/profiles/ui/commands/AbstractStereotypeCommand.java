package edu.kit.ipd.sdq.mdsd.profiles.ui.commands;

import org.eclipse.emf.common.command.AbstractCommand;
import org.modelversioning.emfprofile.Stereotype;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;

/**
 * Abstract superclass for all stereotype commands.
 * 
 * @author Max Kramer
 * 
 */
public abstract class AbstractStereotypeCommand extends AbstractCommand {

    /**
     * The stereotypable object of the command.
     */
    protected StereotypableElement eStereotypableObject;
    /**
     * The stereotype of the command.
     */
    protected Stereotype stereotype;

    /**
     * Default constructor for labeled stereotype commands.
     * 
     * @param label
     *            the label of the command
     * @param eStereotypableObject
     *            stereotypable object of the command
     * @param stereotype
     *            the stereotype of the command
     */
    public AbstractStereotypeCommand(final String label, final StereotypableElement eStereotypableObject,
            final Stereotype stereotype) {
        super(label);
        this.eStereotypableObject = eStereotypableObject;
        this.stereotype = stereotype;
        this.isExecutable = true;
        this.isPrepared = true;
    }

    @Override
    public void redo() {
        execute();
    }

}
