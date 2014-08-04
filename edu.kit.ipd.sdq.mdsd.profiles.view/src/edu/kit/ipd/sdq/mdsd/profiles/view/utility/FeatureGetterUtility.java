package edu.kit.ipd.sdq.mdsd.profiles.view.utility;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

/**
 * Utility to deliver the list of EAttributes, which will be used for data binding between the model
 * and UI.
 * 
 * @author emretaspolat
 * 
 */
public class FeatureGetterUtility {

    private static final Logger LOGGER = Logger.getLogger(FeatureGetterUtility.class);

    // hide the default constructor
    private FeatureGetterUtility() {
    };

    /**
     * Returns the list of EAttributes for the given Stereotype.
     * 
     * @param stereotype
     * @return List<EAttribute> EAttributes of Stereotype
     */
    public static List<EAttribute> getFeatureListOfStereotype(final Stereotype stereotype) {
        LOGGER.info("EAttributes of " + stereotype + ": " + stereotype.getEAllAttributes());
        return stereotype.getEAllAttributes();
    }

    /**
     * Returns the list of EAttributes for the given Stereotype Application.
     * 
     * @param stereotypeApplication
     * @return List<EAttribute> EAttributes of Stereotype
     */
    public static List<EAttribute> getFeatureListOfStereotypeApplication(
            final StereotypeApplication stereotypeApplication) {
        if (stereotypeApplication == null) {
            return new ArrayList<EAttribute>();
        } else {
            LOGGER.info("EAttributes of " + stereotypeApplication + ": "
                    + stereotypeApplication.eClass().getEAllAttributes());
            return stereotypeApplication.eClass().getEAllAttributes();
        }
    }

    /**
     * Returns the list of EAttributes for the given Stereotype Application by getting first to the
     * applied Stereotype.
     * 
     * @param stereotypeApplication
     * @return List<EAttribute> EAttributes of Stereotype of Stereotype Application
     */
    public static EList<EAttribute> getFeatureListOfSTFromSA(final StereotypeApplication stereotypeApplication) {
        return stereotypeApplication.getStereotype().eClass().getEAllAttributes();
    }

    // public static EList<EStructuralFeature> testMethod() {
    // EObject stereotypeApplication = EMFProfileApplicationPackage.Literals.STEREOTYPE_APPLICATION;
    // EList<EStructuralFeature> eAllStructuralFeatures =
    // stereotypeApplication.eClass().getEAllStructuralFeatures();
    // return eAllStructuralFeatures;
    // }

    /**
     * TODO
     * 
     * @return
     */
    public static EStructuralFeature getFeatureForStereotypeApplication() {

        final EObject stereotypeApplication = EMFProfileApplicationPackage.Literals.STEREOTYPE_APPLICATION;
        final EList<EStructuralFeature> eAllStructuralFeatures = stereotypeApplication.eClass()
                .getEAllStructuralFeatures();

        if (!eAllStructuralFeatures.isEmpty()) {
            try {
                for (final EStructuralFeature feature : eAllStructuralFeatures) {
                    if (feature instanceof EAttribute) {
                        return feature;
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // public static EStructuralFeature getFeatureForStereotype() {
    //
    // EObject stereotype = EMFProfilePackage.Literals.STEREOTYPE;
    // EList<EStructuralFeature> eAllStructuralFeatures =
    // stereotype.eClass().getEAllStructuralFeatures();
    //
    // if (!eAllStructuralFeatures.isEmpty()) {
    // try {
    // for (EStructuralFeature feature : eAllStructuralFeatures) {
    // if (feature instanceof EAttribute) {
    // return feature;
    // }
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // return null;
    // }
    //
    // public static EStructuralFeature getFeatureByStereotypeSelection(Stereotype stereotype) {
    //
    // Stereotype st = stereotype;
    // EList<EStructuralFeature> eAllStructuralFeatures = st.eClass().getEAllStructuralFeatures();
    //
    // if (!eAllStructuralFeatures.isEmpty()) {
    // try {
    // for (EStructuralFeature feature : eAllStructuralFeatures) {
    // if (feature instanceof EAttribute) {
    // return feature;
    // }
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // return null;
    // }
    //
    // public static EStructuralFeature
    // getFeatureByStereotypeApplicationSelection(StereotypeApplication stereotypeApplication) {
    //
    // StereotypeApplication sa = stereotypeApplication;
    // EList<EStructuralFeature> eAllStructuralFeatures = sa.eClass().getEAllStructuralFeatures();
    //
    // if (!eAllStructuralFeatures.isEmpty()) {
    // try {
    // for (EStructuralFeature feature : eAllStructuralFeatures) {
    // if (feature instanceof EAttribute) {
    // return feature;
    // }
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // return null;
    // }
    //
    // public static EStructuralFeature
    // getFeatureOverStereotypeApplicationtoStereotype(StereotypeApplication stereotypeApplication)
    // {
    //
    // StereotypeApplication sa = stereotypeApplication;
    // EList<EStructuralFeature> eAllStructuralFeatures =
    // sa.getStereotype().eClass().getEAllStructuralFeatures();
    //
    // if (!eAllStructuralFeatures.isEmpty()) {
    // try {
    // for (EStructuralFeature feature : eAllStructuralFeatures) {
    // if (feature instanceof EAttribute) {
    // return feature;
    // }
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // return null;
    // }

}
