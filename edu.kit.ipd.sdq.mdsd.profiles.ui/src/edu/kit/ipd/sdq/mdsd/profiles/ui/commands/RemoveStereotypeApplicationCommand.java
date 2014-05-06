package edu.kit.ipd.sdq.mdsd.profiles.ui.commands;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;

public class RemoveStereotypeApplicationCommand extends AbstractStereotypeCommand {

    private StereotypeApplication stereotypeApplication;

    public RemoveStereotypeApplicationCommand(EStereotypableObject eStereotypableObject, Stereotype stereotype,
            StereotypeApplication stereotypeApplication) {
        super("Remove Stereotype Application", eStereotypableObject, stereotype);
        this.stereotypeApplication = stereotypeApplication;
    }

    @Override
    public void execute() {
        this.eStereotypableObject.removeStereotypeApplication(this.stereotypeApplication);
    }

    @Override
    public void undo() {
        // undo removeStereoapplication by using the values of the removed application
        StereotypeApplication oldStereotypeApplication = this.stereotypeApplication;
        StereotypeApplication newStereotypeApplication = this.eStereotypableObject.applyStereotype(stereotype);
        Stereotype currentStereotype = oldStereotypeApplication.getStereotype();
        EList<EStructuralFeature> structuralFeatures = currentStereotype.getEAllStructuralFeatures();
        for (EStructuralFeature structuralFeature : structuralFeatures) {
            if (!"appliedTo".equals(structuralFeature.getName())
                    && !"profileApplication".equals(structuralFeature.getName())
                    && !"extension".equals(structuralFeature.getName())) {
                Object oldValue = oldStereotypeApplication.eGet(structuralFeature);
                newStereotypeApplication.eSet(structuralFeature, oldValue);
            }
        }
        this.stereotypeApplication = newStereotypeApplication;
    }

}
