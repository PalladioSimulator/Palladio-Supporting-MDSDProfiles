package edu.kit.ipd.sdq.mdsd.profiles.view.utility;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
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
    public static List<EAttribute> getAttributeListOfStereotype(final Stereotype stereotype) {
        LOGGER.info("EAttributes of " + stereotype + ": " + stereotype.getEAllAttributes());
        return stereotype.getEAllAttributes();
    }

    /**
     * Returns the list of EAttributes for the given Stereotype Application.
     * 
     * @param stereotypeApplication
     * @return List<EAttribute> EAttributes of Stereotype
     */
    public static List<EAttribute> getAttributeListOfStereotypeApplication(
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
     * Returns the list of EReferences for the given Stereotype Application.
     * 
     * @param stereotypeApplication
     * @return List<EReference> EReferences of Stereotype
     */
    public static List<EReference> getReferenceListOfStereotypeApplication(
            final StereotypeApplication stereotypeApplication) {
        if (stereotypeApplication == null) {
            return new ArrayList<EReference>();
        } else {
            LOGGER.info("EReferences of " + stereotypeApplication + ": "
                    + stereotypeApplication.eClass().getEAllReferences());
            return stereotypeApplication.eClass().getEAllReferences();
        }
    }
    
    /**
     * Returns the list of Containment References for the given Stereotype Application.
     * 
     * @param stereotypeApplication
     * @return List<EReference> EReferences of Stereotype
     */
    public static List<EReference> getContainmentListOfStereotypeApplication(
            final StereotypeApplication stereotypeApplication) {
        if (stereotypeApplication == null) {
            return new ArrayList<EReference>();
        } else {
            LOGGER.info("EContainments of " + stereotypeApplication + ": "
                    + stereotypeApplication.eClass().getEAllContainments());
            return stereotypeApplication.eClass().getEAllContainments();
        }
    }
    
    /**
     * Returns the list of EStructuralFeatures that were defined in the Stereotype of the given Stereotype Application. 
     * Excludes all generic features that are defined for all stereotypes.
     * 
     * @param stereotypeApplication
     * @return List<EStructuralFeature> EStructuralFeatures of Stereotype
     */
    public static List<EStructuralFeature> getTaggedStructuralFeatures(
            final StereotypeApplication stereotypeApplication) {
    	List<EStructuralFeature> listStructuralFeatures = new ArrayList<EStructuralFeature>();
        if (stereotypeApplication == null) {
            return new ArrayList<EStructuralFeature>();
        } else {
            LOGGER.info("EStructuralFeatures of " + stereotypeApplication + ": "
                    + stereotypeApplication.eClass().getEAllStructuralFeatures());
            listStructuralFeatures.addAll(stereotypeApplication.eClass().getEAllStructuralFeatures());
            listStructuralFeatures.remove(stereotypeApplication.eClass().getEStructuralFeature("appliedTo"));
            listStructuralFeatures.remove(stereotypeApplication.eClass().getEStructuralFeature("extension"));
            listStructuralFeatures.remove(stereotypeApplication.eClass().getEStructuralFeature("profileApplication"));
            return listStructuralFeatures;
        }
    }

    /**
     * FIXME Emre: this utility method is never used
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

}
