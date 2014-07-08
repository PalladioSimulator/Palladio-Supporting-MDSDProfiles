package edu.kit.ipd.sdq.mdsd.profiles.ui.commands;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;

/**
 * A command for the removal of a single stereotype application.
 * 
 * @author Max Kramer
 * 
 */
public class RemoveStereotypeApplicationCommand extends
        AbstractStereotypeCommand {

    private StereotypeApplication stereotypeApplication;

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
    public RemoveStereotypeApplicationCommand(
            final EStereotypableObject eStereotypableObject,
            final Stereotype stereotype,
            final StereotypeApplication stereotypeApplication) {
        super("Remove Stereotype Application", eStereotypableObject, stereotype);
        this.stereotypeApplication = stereotypeApplication;
    }

    @Override
    public void execute() {
        this.eStereotypableObject
                .removeStereotypeApplication(this.stereotypeApplication);
    }

    @Override
    public void undo() {
        // undo removeStereoapplication by using the values of the removed
        // application
        StereotypeApplication oldStereotypeApplication =
                this.stereotypeApplication;
        StereotypeApplication newStereotypeApplication =
                this.eStereotypableObject.applyStereotype(stereotype);
        Stereotype currentStereotype = oldStereotypeApplication.getStereotype();
        EList<EStructuralFeature> structuralFeatures =
                currentStereotype.getEAllStructuralFeatures();
        for (EStructuralFeature structuralFeature : structuralFeatures) {
            if (!"appliedTo".equals(structuralFeature.getName())
                    && !"profileApplication"
                            .equals(structuralFeature.getName())
                    && !"extension".equals(structuralFeature.getName())) {
                Object oldValue =
                        oldStereotypeApplication.eGet(structuralFeature);
                newStereotypeApplication.eSet(structuralFeature, oldValue);
            }
        }
        this.stereotypeApplication = newStereotypeApplication;
    }

}
