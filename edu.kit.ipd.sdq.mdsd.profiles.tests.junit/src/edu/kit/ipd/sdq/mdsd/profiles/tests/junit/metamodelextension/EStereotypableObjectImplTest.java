/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.tests.junit.metamodelextension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

import edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.A;
import edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.B;
import edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.simplemodelFactory;
import edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.simplemodelPackage;
import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
import edu.kit.ipd.sdq.mdsd.profiles.registry.ProfileApplicationFileRegistry;

/**
 * @author Matthias Eisenmann, Emre Taspolatoglu
 * 
 */
public class EStereotypableObjectImplTest {

    /**
     * The object under test.
     */
    private EStereotypableObject testee;

    /**
     *
     */
    private ResourceSet resourceSet;
    /**
     *
     */
    private Resource resource;
    /**
     *
     */
    private IProject project;
    /**
     *
     */
    private final String modelFileName = "model.xmi";
    /**
    *
    */
    private final String dummyProjectName = "DummyProject";
    /**
    *
    */
    private final String entityBeanQualifiedName = "EntityBean";
    /**
    *
    */
    private final String sessionBeanQualifiedName = "SessionBean";
    /**
    *
    */
    private final String otherBeanQualifiedName = "OtherBean";

    /**
     *
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        // TODO: remove to test suite if more test classes exist

        // configure logger
        PatternLayout layout =
                new PatternLayout("%d{HH:mm:ss,SSS} [%t] %-5p %c - %m%n");
        ConsoleAppender appender = new ConsoleAppender(layout);
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure(appender);
    }

    /**
     * @throws CoreException
     *             if project cannot be created or opened
     */
    @Before
    public final void setUp() throws CoreException {

        // Create a resource set to hold the resources.
        resourceSet = new ResourceSetImpl();

        // Register the appropriate resource factory to handle all file
        // extensions.
        resourceSet
                .getResourceFactoryRegistry()
                .getExtensionToFactoryMap()
                .put(Resource.Factory.Registry.DEFAULT_EXTENSION,
                        new XMIResourceFactoryImpl());

        // Register the package to ensure it is available during loading.
        resourceSet.getPackageRegistry().put(simplemodelPackage.eNS_URI,
                simplemodelPackage.eINSTANCE);

        // create a dummy project that is used as container for xmi files

        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        project = root.getProject(dummyProjectName);

        if (!project.exists()) {
            project.create(null);
        }

        if (!project.isOpen()) {
            project.open(null);
        }

        resource =
                resourceSet.createResource(URI.createPlatformResourceURI(
                        project.getFullPath() + "/" + modelFileName, true));

        testee = simplemodelFactory.eINSTANCE.createA();
        resource.getContents().add(testee);
    }

    /**
     * @throws CoreException
     *             if the project cannot be deleted
     */
    @After
    public final void tearDown() throws CoreException {
        ProfileApplicationFileRegistry.INSTANCE.clear();
        project.delete(IResource.ALWAYS_DELETE_PROJECT_CONTENT, null);
        resourceSet.getResources().remove(resource);
        resource = null;
        resourceSet = null;
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#EStereotypableObjectImpl()}
     * .
     */
    @Test
    public final void testEStereotypableObjectImpl() {
    	 Boolean testeeEStereotypeableObject;
    	 
    	 if (testee instanceof EStereotypableObject) {
    		 testeeEStereotypeableObject = true;
    	 } else {
    		 testeeEStereotypeableObject = false;
    	 }
    	 
    	 assertTrue(testeeEStereotypeableObject);
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#setProfileApplicationDecorator(org.modelversioning.emfprofile.application.registry.ProfileApplicationDecorator)}
     * .
     */
    // @Test
    public final void testSetProfileApplicationDecorator() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getStereotypeApplications()}
     * .
     */
    @Test
    public final void testGetStereotypeApplications() {

        // retrieve stereotypes for application
        Stereotype entityBeanStereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);
        Stereotype sessionBeanStereotype =
                testee.getApplicableStereotype(sessionBeanQualifiedName);

        // check that there are no stereotype applications at the beginning
        EList<StereotypeApplication> stereotypeApplications =
                testee.getStereotypeApplications();
        assertEquals(0, stereotypeApplications.size());

        // first stereotype application
        StereotypeApplication entityBeanApplication =
                testee.applyStereotype(entityBeanStereotype);
        stereotypeApplications = testee.getStereotypeApplications();
        assertEquals(1, stereotypeApplications.size());
        assertSame(entityBeanApplication, stereotypeApplications.get(0));

        // second stereotype application
        StereotypeApplication sessionBeanApplication =
                testee.applyStereotype(sessionBeanStereotype);
        stereotypeApplications = testee.getStereotypeApplications();
        assertEquals(2, stereotypeApplications.size());
        assertSame(sessionBeanApplication, stereotypeApplications.get(1));
    }
    
    @Test
    public void testGetStereotypeApplicationsChangedResource() {
    	// 1) apply stereotype
        Stereotype entityBeanStereotype = testee.getApplicableStereotype(entityBeanQualifiedName);
    	StereotypeApplication entityBeanApplication = testee.applyStereotype(entityBeanStereotype);
    	
    	// 2) new resource, put model "testee" in resource
    	resource = resourceSet.createResource(URI.createPlatformResourceURI(project.getFullPath() + "/" + modelFileName.replace(".xmi", "2.xmi"), true));
    	
    	// 3) check whether testee has a stereotype applied after unloading
    	resource.unload();
    	try {
			resource.load(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
        
//    	resource.getContents().remove(0);
    	assertEquals("Wrong number of stereotypes.", 1, testee.getStereotypeApplications().size());
    	assertEquals("Wrong applied object.", entityBeanApplication, testee.getStereotypeApplications().get(0));
    }
    
    @Test
    public void testGetStereotypeApplicationsAfterResourceSetCleaned() {
    	// 1) apply stereotype
        Stereotype entityBeanStereotype = testee.getApplicableStereotype(entityBeanQualifiedName);
    	StereotypeApplication entityBeanApplication = testee.applyStereotype(entityBeanStereotype);
    	
    	// 2) new resource, put model "testee" in resource
    	resource = resourceSet.createResource(URI.createPlatformResourceURI(project.getFullPath() + "/" + modelFileName.replace(".xmi", "2.xmi"), true));
    	
    	// 3) check whether testee has a stereotype applied after re-initializing the ResourceSet and Resource
        resourceSet.getResources().remove(resource);
        resource = null;
        resourceSet = null;
        
        resourceSet = new ResourceSetImpl();

        // Register the appropriate resource factory to handle all file extensions.
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
        // Register the package to ensure it is available during loading.
        resourceSet.getPackageRegistry().put(simplemodelPackage.eNS_URI, simplemodelPackage.eINSTANCE);
        
    	resource = resourceSet.createResource(URI.createPlatformResourceURI(project.getFullPath() + "/" + modelFileName.replace(".xmi", "2.xmi"), true));
        resource.getContents().add(testee);
        
    	assertEquals("Wrong number of stereotypes.", 1, testee.getStereotypeApplications().size());
    	assertEquals("Wrong applied object.", entityBeanApplication, testee.getStereotypeApplications().get(0));
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getStereotypeApplications(org.modelversioning.emfprofile.Stereotype)}
     * .
     */
    @Test
    public final void testGetStereotypeApplicationsStereotype() {

        // retrieve stereotypes for application
        Stereotype entityBeanStereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);
        Stereotype sessionBeanStereotype =
                testee.getApplicableStereotype(sessionBeanQualifiedName);

        // check that there are no stereotype applications at the beginning
        EList<StereotypeApplication> entityBeanApplications =
                testee.getStereotypeApplications(entityBeanStereotype);
        assertTrue(entityBeanApplications.isEmpty());

        EList<StereotypeApplication> sessionBeanApplications =
                testee.getStereotypeApplications(sessionBeanStereotype);
        assertTrue(sessionBeanApplications.isEmpty());

        // first stereotype application
        testee.applyStereotype(entityBeanStereotype);

        // get the stereotype again, this time from the resource set of the
        // model to which it is
        // applied (before it was the stereotype of the resource set of the
        // profile registry)
        entityBeanStereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);

        entityBeanApplications =
                testee.getStereotypeApplications(entityBeanStereotype);
        assertEquals(1, entityBeanApplications.size());

        sessionBeanApplications =
                testee.getStereotypeApplications(sessionBeanStereotype);
        assertTrue(sessionBeanApplications.isEmpty());

        // second stereotype application
        testee.applyStereotype(sessionBeanStereotype);

        // get the stereotype again, this time from the resource set of the
        // model to which it is
        // applied (before it was the stereotype of the resource set of the
        // profile registry)
        sessionBeanStereotype =
                testee.getApplicableStereotype(sessionBeanQualifiedName);

        entityBeanApplications =
                testee.getStereotypeApplications(entityBeanStereotype);
        assertEquals(1, entityBeanApplications.size());

        sessionBeanApplications =
                testee.getStereotypeApplications(sessionBeanStereotype);
        assertEquals(1, sessionBeanApplications.size());

        assertNotSame(entityBeanApplications.get(0),
                sessionBeanApplications.get(0));

        assertSame(entityBeanStereotype, entityBeanApplications.get(0)
                .getStereotype());
        assertSame(sessionBeanStereotype, sessionBeanApplications.get(0)
                .getStereotype());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getStereotypeApplications(org.modelversioning.emfprofile.Stereotype)}
     * .
     * 
     * It checks that a NullPointerException doesn't occur if null is passed as
     * parameter.
     */
    @Test
    public final void testGetStereotypeApplicationsStereotypeNull() {

        Stereotype stereotype = null;

        EList<StereotypeApplication> stereotypeApplications =
                testee.getStereotypeApplications(stereotype);

        assertTrue(stereotypeApplications.isEmpty());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getStereotypeApplications(java.lang.String)}
     * .
     */
    @Test
    public final void testGetStereotypeApplicationsString() {

        // retrieve stereotypes for application
        Stereotype entityBeanStereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);
        Stereotype sessionBeanStereotype =
                testee.getApplicableStereotype(sessionBeanQualifiedName);

        // check that there are no stereotype applications at the beginning
        EList<StereotypeApplication> entityBeanApplications =
                testee.getStereotypeApplications(entityBeanQualifiedName);
        assertTrue(entityBeanApplications.isEmpty());

        EList<StereotypeApplication> sessionBeanApplications =
                testee.getStereotypeApplications(sessionBeanQualifiedName);
        assertTrue(sessionBeanApplications.isEmpty());

        // first stereotype application
        testee.applyStereotype(entityBeanStereotype);

        entityBeanApplications =
                testee.getStereotypeApplications(entityBeanQualifiedName);
        assertEquals(1, entityBeanApplications.size());

        sessionBeanApplications =
                testee.getStereotypeApplications(sessionBeanQualifiedName);
        assertTrue(sessionBeanApplications.isEmpty());

        // second stereotype application
        testee.applyStereotype(sessionBeanStereotype);

        entityBeanApplications =
                testee.getStereotypeApplications(entityBeanQualifiedName);
        assertEquals(1, entityBeanApplications.size());

        sessionBeanApplications =
                testee.getStereotypeApplications(sessionBeanQualifiedName);
        assertEquals(1, sessionBeanApplications.size());

        assertNotSame(entityBeanApplications.get(0),
                sessionBeanApplications.get(0));

        // get the stereotypes again, this time from the resource set of the
        // model to which it is
        // applied (before it was the stereotype of the resource set of the
        // profile registry)
        entityBeanStereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);
        sessionBeanStereotype =
                testee.getApplicableStereotype(sessionBeanQualifiedName);

        assertSame(entityBeanStereotype, entityBeanApplications.get(0)
                .getStereotype());
        assertSame(sessionBeanStereotype, sessionBeanApplications.get(0)
                .getStereotype());

        assertEquals(entityBeanQualifiedName, entityBeanApplications.get(0)
                .getStereotype().getName());
        assertEquals(sessionBeanQualifiedName, sessionBeanApplications.get(0)
                .getStereotype().getName());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getStereotypeApplications(java.lang.String)}
     * .
     * 
     * It checks that a NullPointerException doesn't occur if null is passed as
     * parameter.
     */
    @Test
    public final void testGetStereotypeApplicationsStringNull() {

        String qualifiedName = null;

        EList<StereotypeApplication> stereotypeApplications =
                testee.getStereotypeApplications(qualifiedName);

        assertTrue(stereotypeApplications.isEmpty());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getAppliedStereotypes()}
     * .
     */
    @Test
    public final void testGetAppliedStereotypes() {
    	
        // retrieve stereotypes for application
        Stereotype entityBeanStereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);
        Stereotype sessionBeanStereotype =
                testee.getApplicableStereotype(sessionBeanQualifiedName);

        // check that there are no stereotype applications at the beginning
        EList<StereotypeApplication> stereotypeApplications =
                testee.getStereotypeApplications();
        assertEquals(0, stereotypeApplications.size());
        EList<Stereotype> appliedStereotypes =
                testee.getAppliedStereotypes();
        assertEquals(0, appliedStereotypes.size());
        
        // first stereotype application
        StereotypeApplication entityBeanApplication =
                testee.applyStereotype(entityBeanStereotype);
        stereotypeApplications = testee.getStereotypeApplications();
        assertEquals(1, stereotypeApplications.size());
        assertSame(entityBeanApplication, stereotypeApplications.get(0));

        // second stereotype application
        StereotypeApplication sessionBeanApplication =
                testee.applyStereotype(sessionBeanStereotype);
        stereotypeApplications = testee.getStereotypeApplications();
        assertEquals(2, stereotypeApplications.size());
        assertSame(sessionBeanApplication, stereotypeApplications.get(1));
        
        // now get the two applied stereotypes
        appliedStereotypes = testee.getAppliedStereotypes();
        assertEquals(2, appliedStereotypes.size());
        assertSame("First one wrong!", entityBeanApplication.getStereotype(), appliedStereotypes.get(0));
        assertSame("Second one wrong!", sessionBeanApplication.getStereotype(), appliedStereotypes.get(1));
        
    }
    
    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getAppliedStereotypes()}
     * .
     */
    @Test
    public final void testGetAppliedStereotypesChangedResource() {
    	    		
        // check that there are no stereotype applications at the beginning
        EList<StereotypeApplication> stereotypeApplications =
                testee.getStereotypeApplications();
        assertEquals(0, stereotypeApplications.size());
        EList<Stereotype> appliedStereotypes =
                testee.getAppliedStereotypes();
        assertEquals(0, appliedStereotypes.size());
        
       	// 1) apply stereotype
    	
        // retrieve stereotypes for application
        Stereotype entityBeanStereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);
        Stereotype sessionBeanStereotype =
                testee.getApplicableStereotype(sessionBeanQualifiedName);
        
        // first stereotype application
        StereotypeApplication entityBeanApplication =
                testee.applyStereotype(entityBeanStereotype);
        stereotypeApplications = testee.getStereotypeApplications();
        assertEquals(1, stereotypeApplications.size());
        assertSame(entityBeanApplication, stereotypeApplications.get(0));

        // second stereotype application
        StereotypeApplication sessionBeanApplication =
                testee.applyStereotype(sessionBeanStereotype);
        stereotypeApplications = testee.getStereotypeApplications();
        assertEquals(2, stereotypeApplications.size());
        assertSame(sessionBeanApplication, stereotypeApplications.get(1));
        
        // now get the two applied stereotypes
        appliedStereotypes = testee.getAppliedStereotypes();
        assertEquals(2, appliedStereotypes.size());
        assertSame("First one wrong!", entityBeanApplication.getStereotype(), appliedStereotypes.get(0));
        assertSame("Second one wrong!", sessionBeanApplication.getStereotype(), appliedStereotypes.get(1));
        
    	// 2) new resource, put model "testee" in resource
    	resource = resourceSet.createResource(URI.createPlatformResourceURI(project.getFullPath() + "/" + modelFileName.replace(".xmi", "2.xmi"), true));

    	// 3) check whether testee has a stereotype applied after unloading
    	resource.unload();
    	try {
			resource.load(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        appliedStereotypes = testee.getAppliedStereotypes();
        assertEquals(2, appliedStereotypes.size());
        assertSame("First one wrong!", entityBeanApplication.getStereotype(), appliedStereotypes.get(0));
        assertSame("Second one wrong!", sessionBeanApplication.getStereotype(), appliedStereotypes.get(1));
    
    }
    
    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getAppliedStereotypes()}
     * .
     */
    @Test
    public final void testGetAppliedStereotypesAfterResourceSetCleaned() {
    	    		
        // check that there are no stereotype applications at the beginning
        EList<StereotypeApplication> stereotypeApplications =
                testee.getStereotypeApplications();
        assertEquals(0, stereotypeApplications.size());
        EList<Stereotype> appliedStereotypes =
                testee.getAppliedStereotypes();
        assertEquals(0, appliedStereotypes.size());
        
       	// 1) apply stereotypes
    	
        // retrieve stereotypes for application
        Stereotype entityBeanStereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);
        Stereotype sessionBeanStereotype =
                testee.getApplicableStereotype(sessionBeanQualifiedName);
        
        // first stereotype application
        StereotypeApplication entityBeanApplication =
                testee.applyStereotype(entityBeanStereotype);
        stereotypeApplications = testee.getStereotypeApplications();
        assertEquals(1, stereotypeApplications.size());
        assertSame(entityBeanApplication, stereotypeApplications.get(0));

        // second stereotype application
        StereotypeApplication sessionBeanApplication =
                testee.applyStereotype(sessionBeanStereotype);
        stereotypeApplications = testee.getStereotypeApplications();
        assertEquals(2, stereotypeApplications.size());
        assertSame(sessionBeanApplication, stereotypeApplications.get(1));
        
        // now get the two applied stereotypes
        appliedStereotypes = testee.getAppliedStereotypes();
        assertEquals(2, appliedStereotypes.size());
        assertTrue("Testee in old resource has no stereotypes.", !(testee.getAppliedStereotypes().isEmpty()));
        assertSame("First one wrong!", entityBeanApplication.getStereotype(), appliedStereotypes.get(0));
        assertSame("Second one wrong!", sessionBeanApplication.getStereotype(), appliedStereotypes.get(1));
        
    	// 2) new resource, put model "testee" in resource
    	resource = resourceSet.createResource(URI.createPlatformResourceURI(project.getFullPath() + "/" + modelFileName.replace(".xmi", "2.xmi"), true));

    	// 3) check whether testee has a stereotype applied after unloading
        resourceSet.getResources().remove(resource);
        resource = null;
        resourceSet = null;
        
        EList<Stereotype> anotherAppliedStereotypes = testee.getAppliedStereotypes();
        assertEquals("Stereotypes are not the same.", appliedStereotypes, anotherAppliedStereotypes);

        resourceSet = new ResourceSetImpl();

        // Register the appropriate resource factory to handle all file extensions.
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
        // Register the package to ensure it is available during loading.
        resourceSet.getPackageRegistry().put(simplemodelPackage.eNS_URI, simplemodelPackage.eINSTANCE);
        
    	resource = resourceSet.createResource(URI.createPlatformResourceURI(project.getFullPath() + "/" + modelFileName.replace(".xmi", "2.xmi"), true));
        resource.getContents().add(testee);
        
        anotherAppliedStereotypes = testee.getAppliedStereotypes();
        assertNotNull("Testee null.", testee);

        assertTrue("Testee is stereotypable", testee instanceof EStereotypableObject);
        assertTrue("Testee in new resource has no stereotypes.", !(testee.getAppliedStereotypes().isEmpty()));
        assertEquals("Stereotypes are not the same, just now.", appliedStereotypes, anotherAppliedStereotypes);
        
        appliedStereotypes = testee.getAppliedStereotypes();
        assertEquals(2, appliedStereotypes.size());
        assertSame("First one wrong!", entityBeanApplication.getStereotype(), appliedStereotypes.get(0));
        assertSame("Second one wrong!", sessionBeanApplication.getStereotype(), appliedStereotypes.get(1));
    
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getAppliedStereotypes()}
     * .
     */
    @Test
    public final void testGetAppliedStereotypesAfterResourceCleaned() {
    	    		
        // check that there are no stereotype applications at the beginning
        EList<StereotypeApplication> stereotypeApplications =
                testee.getStereotypeApplications();
        assertEquals(0, stereotypeApplications.size());
        EList<Stereotype> appliedStereotypes =
                testee.getAppliedStereotypes();
        assertEquals(0, appliedStereotypes.size());
        
       	// 1) apply stereotypes
    	
        // retrieve stereotypes for application
        Stereotype entityBeanStereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);
        Stereotype sessionBeanStereotype =
                testee.getApplicableStereotype(sessionBeanQualifiedName);
        
        // first stereotype application
        StereotypeApplication entityBeanApplication =
                testee.applyStereotype(entityBeanStereotype);
        stereotypeApplications = testee.getStereotypeApplications();
        assertEquals(1, stereotypeApplications.size());
        assertSame(entityBeanApplication, stereotypeApplications.get(0));

        // second stereotype application
        StereotypeApplication sessionBeanApplication =
                testee.applyStereotype(sessionBeanStereotype);
        stereotypeApplications = testee.getStereotypeApplications();
        assertEquals(2, stereotypeApplications.size());
        assertSame(sessionBeanApplication, stereotypeApplications.get(1));
        
        // now get the two applied stereotypes
        appliedStereotypes = testee.getAppliedStereotypes();
        assertEquals(2, appliedStereotypes.size());
        assertTrue("Testee in old resource has no stereotypes.", !(testee.getAppliedStereotypes().isEmpty()));
        assertSame("First one wrong!", entityBeanApplication.getStereotype(), appliedStereotypes.get(0));
        assertSame("Second one wrong!", sessionBeanApplication.getStereotype(), appliedStereotypes.get(1));
        
    	// 2) new resource, put model "testee" in resource
    	resource = resourceSet.createResource(URI.createPlatformResourceURI(project.getFullPath() + "/" + modelFileName.replace(".xmi", "2.xmi"), true));

    	// 3) check whether testee has a stereotype applied after unloading
        resourceSet.getResources().remove(resource);
        resource = null;
//        resourceSet = null;
        
        EList<Stereotype> anotherAppliedStereotypes = testee.getAppliedStereotypes();
        assertEquals("Stereotypes are not the same.", appliedStereotypes, anotherAppliedStereotypes);

//        resourceSet = new ResourceSetImpl();

        // Register the appropriate resource factory to handle all file extensions.
//        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
        // Register the package to ensure it is available during loading.
//        resourceSet.getPackageRegistry().put(simplemodelPackage.eNS_URI, simplemodelPackage.eINSTANCE);
        
    	resource = resourceSet.createResource(URI.createPlatformResourceURI(project.getFullPath() + "/" + modelFileName.replace(".xmi", "2.xmi"), true));
        resource.getContents().add(testee);
        
        anotherAppliedStereotypes = testee.getAppliedStereotypes();
        assertNotNull("Testee null.", testee);

        assertTrue("Testee is stereotypable", testee instanceof EStereotypableObject);
        assertTrue("Testee in new resource has no stereotypes.", !(testee.getAppliedStereotypes().isEmpty()));
        assertEquals("Stereotypes are not the same, just now.", appliedStereotypes, anotherAppliedStereotypes);
        
        appliedStereotypes = testee.getAppliedStereotypes();
        assertEquals(2, appliedStereotypes.size());
        assertSame("First one wrong!", entityBeanApplication.getStereotype(), appliedStereotypes.get(0));
        assertSame("Second one wrong!", sessionBeanApplication.getStereotype(), appliedStereotypes.get(1));
    
    }
    
    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getAppliedStereotype(java.lang.String)}
     * .
     */
     @Test
    public final void testGetAppliedStereotype() {
         // retrieve stereotypes for application
         Stereotype entityBeanStereotype =
                 testee.getApplicableStereotype(entityBeanQualifiedName);

         // check that there are no stereotype applications at the beginning
         EList<StereotypeApplication> stereotypeApplications =
                 testee.getStereotypeApplications();
         assertEquals(0, stereotypeApplications.size());
         EList<Stereotype> appliedStereotypes =
                 testee.getAppliedStereotypes();
         assertEquals(0, appliedStereotypes.size());
         
         // one stereotype application
         StereotypeApplication entityBeanApplication =
                 testee.applyStereotype(entityBeanStereotype);
         stereotypeApplications = testee.getStereotypeApplications();
         assertEquals(1, stereotypeApplications.size());
         assertSame(entityBeanApplication, stereotypeApplications.get(0));
         
         // now get the applied stereotype
         appliedStereotypes = testee.getAppliedStereotypes();
         assertEquals(1, appliedStereotypes.size());
         assertSame("Wrong!", entityBeanApplication.getStereotype(), appliedStereotypes.get(0));
     }
     
    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#isStereotypeApplicable(org.modelversioning.emfprofile.Stereotype)}
     * .
     */
    // @Test
    public final void testIsStereotypeApplicable() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#isStereotypeApplied(org.modelversioning.emfprofile.Stereotype)}
     * .
     */
    // @Test
    public final void testIsStereotypeApplied() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#applyStereotype(org.modelversioning.emfprofile.Stereotype)}
     * .
     */
    @Test
    public final void testApplyStereotypeOnce() {

        // retrieve a stereotype for application
        Stereotype stereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);

        StereotypeApplication stereotypeApplication =
                testee.applyStereotype(stereotype);

        // get the stereotype again, this time from the resource set of the
        // model to which it is
        // applied (before it was the stereotype of the resource set of the
        // profile registry)
        stereotype = testee.getApplicableStereotype(entityBeanQualifiedName);

        assertNotNull(stereotypeApplication);

        assertSame(stereotype, stereotypeApplication.getStereotype());
        assertSame(testee, stereotypeApplication.getAppliedTo());
        // TODO: check stereotypeApplication.getExtension()
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#applyStereotype(org.modelversioning.emfprofile.Stereotype)}
     * .
     */
    @Test
    public final void testApplyStereotypeTwice() {

        // retrieve a stereotype for application
        Stereotype stereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);

        StereotypeApplication firstStereotypeApplication =
                testee.applyStereotype(stereotype);
        assertNotNull(firstStereotypeApplication);

        StereotypeApplication secondStereotypeApplication =
                testee.applyStereotype(stereotype);
        assertNotNull(secondStereotypeApplication);

        assertNotSame(firstStereotypeApplication, secondStereotypeApplication);
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#applyStereotype(org.modelversioning.emfprofile.Stereotype)}
     * .
     * 
     * It checks that a NullPointerException doesn't occur if null is passed as
     * parameter.
     */
    @Test
    public final void testApplyStereotypeNull() {

        StereotypeApplication stereotypeApplication =
                testee.applyStereotype(null);

        assertNull(stereotypeApplication);
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#removeStereotypeApplication(org.modelversioning.emfprofileapplication.StereotypeApplications)}
     * .
     * 
     * It checks that an applied stereotype is removed successfully.
     */
    @Test
    public final void testRemoveStereotypeApplicationOnce() {

        // retrieve stereotypes for application
        Stereotype entityBeanStereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);

        StereotypeApplication entityBeanApplication =
                testee.applyStereotype(entityBeanStereotype);
        assertEquals(1, testee.getStereotypeApplications().size());

        testee.removeStereotypeApplication(entityBeanApplication);
        assertTrue(testee.getStereotypeApplications().isEmpty());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#removeStereotypeApplication(org.modelversioning.emfprofileapplication.StereotypeApplications)}
     * .
     * 
     * It checks the behavior in case that a stereotype application which has
     * already been removed is tried to be removed again.
     */
    @Test
    public final void testRemoveStereotypeApplicationTwice() {

        // retrieve stereotypes for application
        Stereotype entityBeanStereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);

        StereotypeApplication entityBeanApplication =
                testee.applyStereotype(entityBeanStereotype);
        assertEquals(1, testee.getStereotypeApplications().size());

        testee.removeStereotypeApplication(entityBeanApplication);
        assertTrue(testee.getStereotypeApplications().isEmpty());

        testee.removeStereotypeApplication(entityBeanApplication);
        assertTrue(testee.getStereotypeApplications().isEmpty());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#removeStereotypeApplication(org.modelversioning.emfprofileapplication.StereotypeApplications)}
     * .
     * 
     * It checks that the correct stereotype application is removed if two
     * stereotypes of the same type are applied.
     */
    @Test
    public final void testRemoveStereotypeApplicationOneOutOfTwo() {

        // retrieve stereotypes for application
        Stereotype entityBeanStereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);

        StereotypeApplication firstStereotypeApplication =
                testee.applyStereotype(entityBeanStereotype);
        StereotypeApplication secondStereotypeApplication =
                testee.applyStereotype(entityBeanStereotype);

        testee.removeStereotypeApplication(secondStereotypeApplication);

        assertEquals(1, testee.getStereotypeApplications().size());
        assertSame(firstStereotypeApplication, testee
                .getStereotypeApplications().get(0));
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#removeStereotypeApplication(org.modelversioning.emfprofileapplication.StereotypeApplications)}
     * .
     * 
     * It checks that a NullPointerException doesn't occur if null is passed as
     * parameter.
     */
    @Test
    public final void testRemoveStereotypeApplicationNull() {

        testee.removeStereotypeApplication(null);

        assertTrue(testee.getStereotypeApplications().isEmpty());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#removeStereotypeApplication(org.modelversioning.emfprofileapplication.StereotypeApplications)}
     * .
     * 
     * It checks the behavior in case that someone tries to remove a stereotype
     * application which belongs to another object.
     */
    @Test
    public final void testRemoveStereotypeApplicationFromOtherObject() {

        // set up a second EStereotypableObject
        EStereotypableObject otherTestee =
                simplemodelFactory.eINSTANCE.createA();
        resource.getContents().add(otherTestee);

        // retrieve stereotypes for application
        Stereotype entityBeanStereotype =
                testee.getApplicableStereotype(entityBeanQualifiedName);

        StereotypeApplication testeeStereotypeApplication =
                testee.applyStereotype(entityBeanStereotype);
        StereotypeApplication otherTesteeStereotypeApplication =
                otherTestee.applyStereotype(entityBeanStereotype);

        // try to remove otherTestee's stereotype application from testee and
        // vice versa
        testee.removeStereotypeApplication(otherTesteeStereotypeApplication);
        assertEquals(1, testee.getStereotypeApplications().size());

        otherTestee.removeStereotypeApplication(testeeStereotypeApplication);
        assertEquals(1, otherTestee.getStereotypeApplications().size());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getApplicableStereotypes()}
     * .
     */
    @Test
    public final void testGetApplicableStereotypes() {

        EList<Stereotype> applicableStereotypes =
                testee.getApplicableStereotypes();

        assertTrue(!applicableStereotypes.isEmpty());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getApplicableStereotype(java.lang.String)}
     * .
     * 
     * It checks that an available stereotype is found.
     */
    @Test
    public final void testGetApplicableStereotypeExisting() {

        Stereotype applicableStereotype =
                testee.getApplicableStereotype(sessionBeanQualifiedName);

        assertNotNull(applicableStereotype);
        assertEquals(sessionBeanQualifiedName, applicableStereotype.getName());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getApplicableStereotype(java.lang.String)}
     * .
     * 
     * It checks that an unavailable stereotype is not found (returns null).
     */
    @Test
    public final void testGetApplicableStereotypeNotExisting() {

        final String qualifiedName = "UnavailableStereotype";

        Stereotype applicableStereotype =
                testee.getApplicableStereotype(qualifiedName);

        assertNull(applicableStereotype);
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getApplicableStereotype(java.lang.String)}
     * .
     * 
     * It checks that a NullPointerException doesn't occur if null is passed as
     * parameter.
     */
    @Test
    public final void testGetApplicableStereotypeNull() {

        Stereotype stereotype = testee.getApplicableStereotype(null);

        assertNull(stereotype);
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getStereotypeApplications()}
     * .
     * 
     * It checks that the references of EStereotypableObject and applied-to in
     * StereotypeApplication is correct after loading persisted model instances
     * file and profile application file.
     */
    @Test
    public final void
            testPersistedRefsOfEStereotypableObjectsAndProfileApplication() {

        final String resourcesProjectName = "EJBTestResources";
        final String modelInstancesFilePath =
                "/modelinstances/AContainsTwoBs.xmi";

        Resource modelInstancesResource =
                getModelInstancesResource(resourcesProjectName,
                        modelInstancesFilePath);

        // implicitly a look-up in existing profile application file takes place
        // when requesting
        // stereotype applications

        final B b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        assertEquals("b1", b1.getName());
        assertEquals(1, b1.getStereotypeApplications().size());

        final B b2 = (B) modelInstancesResource.getEObject("//@bs.1");
        assertEquals("b2", b2.getName());
        assertEquals(2, b2.getStereotypeApplications().size());
    }
    
    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getStereotypeApplications()}
     * .
     * 
     * It checks that the references of EStereotypableObject and applied-to in
     * StereotypeApplication is correct after loading persisted model instances
     * file and profile application file.
     */
    @Test
    public final void
            testPersistedRefsOfEStereotypableObjectsAndProfileApplicationAfterResourceSetChanged() {

        final String resourcesProjectName = "EJBTestResources";
        final String modelInstancesFilePath =
                "/modelinstances/AContainsTwoBs.xmi";

        Resource modelInstancesResource =
                getModelInstancesResource(resourcesProjectName,
                        modelInstancesFilePath);
        
       	// 1) apply stereotypes
    	
        // implicitly a look-up in existing profile application file takes place
        // when requesting
        // stereotype applications

        B b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        assertEquals("b1", b1.getName());
        assertEquals(1, b1.getStereotypeApplications().size());

        B b2 = (B) modelInstancesResource.getEObject("//@bs.1");
        assertEquals("b2", b2.getName());
        assertEquals(2, b2.getStereotypeApplications().size());
        
    	// 2) new resource, put model "testee" in resource
//        modelInstancesResource = resourceSet.createResource(URI.createPlatformResourceURI(project.getFullPath() + "/" + modelFileName.replace(".xmi", "2.xmi"), true));

    	// 3) check whether there are stereotypes applied after changing ResourceSet and Resource
        resourceSet.getResources().remove(resource);
        modelInstancesResource = null;
        resourceSet = null;

        resourceSet = new ResourceSetImpl();

        // Register the appropriate resource factory to handle all file extensions.
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
        // Register the package to ensure it is available during loading.
        resourceSet.getPackageRegistry().put(simplemodelPackage.eNS_URI, simplemodelPackage.eINSTANCE);
        
        modelInstancesResource = getModelInstancesResource(resourcesProjectName, modelInstancesFilePath);
        
        b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        assertEquals("b1", b1.getName());
        assertEquals(1, b1.getStereotypeApplications().size());

        b2 = (B) modelInstancesResource.getEObject("//@bs.1");
        assertEquals("b2", b2.getName());
        assertEquals(2, b2.getStereotypeApplications().size());
        
    }

    /**
     * It checks that an already persisted tagged value is loaded correctly and
     * a change to this tagged value is persisted correctly. This is done by
     * reloading the tagged value.
     */
    @Test
    public final void testCheckTaggedValuePersistence() {
        final String resourcesProjectName = "EJBTestResources";
        final String modelInstancesFilePath =
                "/modelinstances/AContainsTwoBs_TaggedValuePersistenceTest.xmi";

        Resource modelInstancesResource =
                getModelInstancesResource(resourcesProjectName,
                        modelInstancesFilePath);

        // setup for checking the precondition
        B b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        assertEquals("b1", b1.getName());

        EList<StereotypeApplication> stereotypeApplications =
                b1.getStereotypeApplications(sessionBeanQualifiedName);
        assertEquals(1, stereotypeApplications.size());

        StereotypeApplication stereotypeApplication =
                stereotypeApplications.get(0);

        Object stateful =
                getValueOfEStructuralFeature(stereotypeApplication, "stateful");
        assertEquals("revert profile application file", false, stateful);

        // set and persist the new value
        setValueOfEStructuralFeature(stereotypeApplication, "stateful", true);
        b1.saveContainingProfileApplication();

        // cleanup
        modelInstancesResource.unload();
        resourceSet.getResources().remove(modelInstancesResource);
        ProfileApplicationFileRegistry.INSTANCE.clear();

        // check the reloaded attribute value
        modelInstancesResource =
                getModelInstancesResource(resourcesProjectName,
                        modelInstancesFilePath);

        b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        stereotypeApplications =
                b1.getStereotypeApplications(sessionBeanQualifiedName);
        assertEquals(1, stereotypeApplications.size());

        stereotypeApplication = stereotypeApplications.get(0);

        stateful =
                getValueOfEStructuralFeature(stereotypeApplication, "stateful");
        assertEquals(true, stateful);

        // set and persist the old value
        setValueOfEStructuralFeature(stereotypeApplication, "stateful", false);
        b1.saveContainingProfileApplication();
        stateful =
                getValueOfEStructuralFeature(stereotypeApplication, "stateful");
        assertEquals(false, stateful);
    }

    /**
     * It checks that an already persisted StereotypeApplication reference is
     * loaded correctly and a change to this reference is persisted correctly.
     * This is done by reloading the reference.
     */
    @Test
    public final void testCheckStereotypeReferencePersistence() {
        final String resourcesProjectName = "EJBTestResources";
        final String modelInstanceFilePath =
                "/modelinstances/AContainsTwoBs_ReferencePersistenceTest.xmi";

        Resource modelInstancesResource =
                getModelInstancesResource(resourcesProjectName,
                        modelInstanceFilePath);

        // setup for checking the precondition
        B b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        assertEquals("b1", b1.getName());

        EList<StereotypeApplication> stereotypeApplicationsOfB1 =
                b1.getStereotypeApplications(otherBeanQualifiedName);
        assertEquals(1, stereotypeApplicationsOfB1.size());

        StereotypeApplication stereotypeApplicationOfB1 =
                stereotypeApplicationsOfB1.get(0);

        Object myReferencedStereotype =
                getValueOfEStructuralFeature(stereotypeApplicationOfB1,
                        "myReferencedStereotype");
        assertNotNull(myReferencedStereotype);
        assertEquals(true,
                myReferencedStereotype instanceof StereotypeApplication);

        StereotypeApplication referencedStereotypeApplication =
                (StereotypeApplication) myReferencedStereotype;
        assertEquals(
                "referencedStereotype1",
                getValueOfEStructuralFeature(referencedStereotypeApplication,
                        "name"));

        // retrieve the reference to be set as new value
        // both ReferencedStereotypes are applied to root object A
        A a1 = (A) modelInstancesResource.getEObject("/");
        assertEquals("a1", a1.getName());

        EList<StereotypeApplication> stereotypeApplicationsOfA1 =
                a1.getStereotypeApplications("ReferencedStereotype");
        assertEquals(2, stereotypeApplicationsOfA1.size());

        StereotypeApplication newReferencedStereotypeApplication =
                stereotypeApplicationsOfA1.get(1);
        assertEquals(
                "referencedStereotype2",
                getValueOfEStructuralFeature(
                        newReferencedStereotypeApplication, "name"));

        // set and persist the new value
        setValueOfEStructuralFeature(stereotypeApplicationOfB1,
                "myReferencedStereotype", newReferencedStereotypeApplication);
        a1.saveContainingProfileApplication();

        // cleanup
        modelInstancesResource.unload();
        resourceSet.getResources().remove(modelInstancesResource);
        ProfileApplicationFileRegistry.INSTANCE.clear();

        // check the reloaded attribute value
        modelInstancesResource =
                getModelInstancesResource(resourcesProjectName,
                        modelInstanceFilePath);

        b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        assertEquals("b1", b1.getName());

        stereotypeApplicationsOfB1 =
                b1.getStereotypeApplications(otherBeanQualifiedName);
        assertEquals(1, stereotypeApplicationsOfB1.size());

        stereotypeApplicationOfB1 = stereotypeApplicationsOfB1.get(0);

        myReferencedStereotype =
                getValueOfEStructuralFeature(stereotypeApplicationOfB1,
                        "myReferencedStereotype");
        assertNotNull(myReferencedStereotype);
        assertEquals(true,
                myReferencedStereotype instanceof StereotypeApplication);

        referencedStereotypeApplication =
                (StereotypeApplication) myReferencedStereotype;
        assertEquals(
                "referencedStereotype2",
                getValueOfEStructuralFeature(referencedStereotypeApplication,
                        "name"));

        // reset and persist the old value
        b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        stereotypeApplicationsOfB1 =
                b1.getStereotypeApplications(otherBeanQualifiedName);
        assertEquals(1, stereotypeApplicationsOfB1.size());
        stereotypeApplicationOfB1 = stereotypeApplicationsOfB1.get(0);
        myReferencedStereotype =
                getValueOfEStructuralFeature(stereotypeApplicationOfB1,
                        "myReferencedStereotype");
        assertNotNull(myReferencedStereotype);
        assertEquals(true,
                myReferencedStereotype instanceof StereotypeApplication);
        referencedStereotypeApplication =
                (StereotypeApplication) myReferencedStereotype;
        assertEquals(
                "referencedStereotype2",
                getValueOfEStructuralFeature(referencedStereotypeApplication,
                        "name"));

        referencedStereotypeApplication = stereotypeApplicationsOfA1.get(0);
        assertEquals(
                "referencedStereotype1",
                getValueOfEStructuralFeature(referencedStereotypeApplication,
                        "name"));
        setValueOfEStructuralFeature(stereotypeApplicationOfB1,
                "myReferencedStereotype", referencedStereotypeApplication);
        b1.saveContainingProfileApplication();
    }

    /**
     * It checks that the number of loaded resources in a resource set after
     * retrieving the applicable stereotypes of an EStereotypableObject is the
     * same after retrieving the applicable stereotypes of another
     * EStereotypableObject (from the same model).
     */
    @Test
    public final void testNumberOfLoadedResourcesInResourceSet() {
        final String resourcesProjectName = "EJBTestResources";
        final String modelInstancesFilePath =
                "/modelinstances/AContainsTwoBs.xmi";

        Resource modelInstancesResource =
                getModelInstancesResource(resourcesProjectName,
                        modelInstancesFilePath);

        final B b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        b1.getApplicableStereotypes();

        final int numberOfResources = resourceSet.getResources().size();

        final B b2 = (B) modelInstancesResource.getEObject("//@bs.1");
        b2.getApplicableStereotypes();

        assertEquals(numberOfResources, resourceSet.getResources().size());
    }

    /**
     * Returns the resource with the given path in the project with the given
     * name.
     * 
     * @param projectName
     *            the project name
     * @param modelInstancesFilePath
     *            the relative path to file in the project
     * @return the resource
     */
    private Resource getModelInstancesResource(final String projectName,
            final String modelInstancesFilePath) {
        final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

        final IProject resourcesProject = root.getProject(projectName);

        if (!resourcesProject.exists()) {
            fail("project '" + projectName + "' does not exist");
        }

        final IFile modelInstancesFile =
                resourcesProject.getFile(modelInstancesFilePath);

        if (!modelInstancesFile.exists()) {
            fail("file '" + modelInstancesFilePath + "' does not exist");
        }

        final URI uri =
                URI.createPlatformResourceURI(modelInstancesFile.getFullPath()
                        .toString(), true);

        Resource modelInstancesResource = null;

        try {
            modelInstancesResource = resourceSet.getResource(uri, true);
        } catch (RuntimeException exception) {
            exception.printStackTrace();
        }

        return modelInstancesResource;
    }

    /**
     * Retrieves the value of the specified StereotypeApplication's
     * EStructuralFeature given by the name of the feature.
     * 
     * @param stereotypeApplication
     *            The StereotypeApplication containing the EStructuralFeature.
     * @param name
     *            The name of the EStructuralFeature.
     * @return The value of the EStructuralFeature.
     */
    private static Object
            getValueOfEStructuralFeature(
                    final StereotypeApplication stereotypeApplication,
                    final String name) {
        assertNotNull(stereotypeApplication);
        assertNotNull(name);

        Stereotype stereotype = stereotypeApplication.getStereotype();

        EStructuralFeature taggedValue = stereotype.getTaggedValue(name);

        return stereotypeApplication.eGet(taggedValue);
    }

    /**
     * Sets the value of the specified StereotypeApplication's
     * EStructuralFeature given by the name of the feature.
     * 
     * @param stereotypeApplication
     *            The StereotypeApplication containing the EStructuralFeature.
     * @param name
     *            The name of the EStructuralFeature.
     * @param newValue
     *            The new value to be set.
     */
    private static void setValueOfEStructuralFeature(
            final StereotypeApplication stereotypeApplication,
            final String name, final Object newValue) {
        assertNotNull(stereotypeApplication);
        assertNotNull(name);

        Stereotype stereotype = stereotypeApplication.getStereotype();

        EStructuralFeature taggedValue = stereotype.getTaggedValue(name);

        stereotypeApplication.eSet(taggedValue, newValue);
    }
}
