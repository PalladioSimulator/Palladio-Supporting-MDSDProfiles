/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.view.utility;

import static org.junit.Assert.*;

import java.io.IOException;

import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

/**
 * @author emretaspolat
 *
 */
public class EFeatureSorterUtilityTest {

	private static Stereotype stereotype;
	private static StereotypeApplication stereotypeApplication;
	
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		URI uri = URI.createPlatformResourceURI("edu.kit.ipd.sdq.mdsd.profiles.view/test-resources/Library.ExtendedLibrary.pa.xmi", true);
		CommonPlugin.resolve(uri);
		Resource r = rs.createResource(uri);
		r.load(null);
		
		stereotype = (Stereotype) r.getContents().get(0);
		stereotypeApplication = (StereotypeApplication) r.getContents().get(0);
	}
	
	@Test
	public void testGetFeatureListOfStereotype() {
		assertNotNull("Stereotyp war null", stereotype);
		assertNotNull("Liste von EAttributes war null!", FeatureGetterUtility.getFeatureListOfStereotype(stereotype));
	}
	
	@Test
	public void testGetFeatureListOfStereotypeApplication() {
		assertNotNull("Stereotyp war null", stereotype);
		assertNotNull("Liste von EAttributes war null!", FeatureGetterUtility.getFeatureListOfStereotypeApplication(stereotypeApplication));
	}

}
