//package edu.kit.ipd.sdq.mdsd.profiles.view.utility;
//
//import java.util.ArrayList;
//
//import org.apache.log4j.Logger;
//import org.eclipse.emf.common.util.EList;
//import org.eclipse.emf.databinding.edit.EMFEditProperties;
//import org.eclipse.emf.databinding.edit.IEMFEditListProperty;
//import org.eclipse.emf.ecore.EAttribute;
//import org.eclipse.emf.ecore.EObject;
//import org.eclipse.emf.ecore.EReference;
//import org.eclipse.emf.ecore.EStructuralFeature;
//import org.eclipse.emf.ecore.impl.EAttributeImpl;
//import org.eclipse.emf.edit.domain.EditingDomain;
//import org.modelversioning.emfprofile.EMFProfilePackage;
//import org.modelversioning.emfprofile.Stereotype;
//import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;
//import org.modelversioning.emfprofileapplication.StereotypeApplication;
//
//import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
//
///**
// * 
// * @author emretaspolat
// *
// */
//public class EStructuralFeatureGetter {
//	
//	private static Logger logger = Logger.getLogger(EStructuralFeatureGetter.class);
//	
//	//null for the time being
//	private static EditingDomain editingDomain;
//	
//	/**
//	 * The method should distinct the dynamically created properties of a Stereotype Application from the ones which belongs to it statically.
//	 * 
//	 * @return IEMFEditListProperty[]
//	 * 				Variable-length of parameters taken from the corresponding ArrayList<>
//	 */
//	public static IEMFEditListProperty[] setInputPropertiesOfStereotypeApplications() {
//		
//		ArrayList<IEMFEditListProperty> props = new ArrayList<IEMFEditListProperty>();
//		
//		EObject stereotypeApplication = EMFProfileApplicationPackage.Literals.STEREOTYPE_APPLICATION;
//		EObject stereotype = EMFProfilePackage.Literals.STEREOTYPE;
//		
//		String eStructuralFeatureName_appliedTo = "appliedTo";
//		String eStructuralFeatureName_extension = "extension";
//		String eStructuralFeatureName_profileApplication = "profileApplication";
//				
//		EList<EStructuralFeature> eAllStructuralFeatures = stereotypeApplication.eClass().getEAllStructuralFeatures();
////		EList<EStructuralFeature> eAllStructuralFeatures = stereotype.eClass().getEAllStructuralFeatures();
//
//		if (!eAllStructuralFeatures.isEmpty()) {
//			try {
//				for (EStructuralFeature feature : eAllStructuralFeatures) {
//					if (feature instanceof EStructuralFeature) {
//						if (!feature.equals(stereotypeApplication.eClass().getEStructuralFeature(eStructuralFeatureName_appliedTo)) &&
//							!feature.equals(stereotypeApplication.eClass().getEStructuralFeature(eStructuralFeatureName_extension)) &&
//							!feature.equals(stereotypeApplication.eClass().getEStructuralFeature(eStructuralFeatureName_profileApplication))) {
//							props.add(EMFEditProperties.list(editingDomain, feature));
//						}
//					}
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		IEMFEditListProperty[] features = props.toArray(new IEMFEditListProperty[props.size()]);
//
//		return features;
//	}
//
// }
