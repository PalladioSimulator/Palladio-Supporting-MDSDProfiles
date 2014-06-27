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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.After;
import org.junit.AfterClass;
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
 * @author Matthias Eisenmann
 * 
 */
public class EStereotypableObjectImplTest {

    /**
     * The object under test.
     */
    private EStereotypableObject testee;

    private ResourceSet resourceSet;
    private Resource resource;
    private IProject project;
    private final String MODEL_FILE_NAME = "model.xmi";
    private final String PROJECT_NAME = "DummyProject";

    private final String ENTITY_BEAN_QUALIFIED_NAME = "EntityBean";
    private final String SESSION_BEAN_QUALIFIED_NAME = "SessionBean";
    private final String OTHER_BEAN_QUALIFIED_NAME = "OtherBean";

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        // TODO: remove to test suite if more test classes exist

        // configure logger
        PatternLayout layout = new PatternLayout("%d{HH:mm:ss,SSS} [%t] %-5p %c - %m%n");
        ConsoleAppender appender = new ConsoleAppender(layout);
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure(appender);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {

        // Create a resource set to hold the resources.
        resourceSet = new ResourceSetImpl();

        // Register the appropriate resource factory to handle all file
        // extensions.
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
                .put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());

        // Register the package to ensure it is available during loading.
        resourceSet.getPackageRegistry().put(simplemodelPackage.eNS_URI, simplemodelPackage.eINSTANCE);

        // create a dummy project that is used as container for xmi files

        IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        project = root.getProject(PROJECT_NAME);

        if (!project.exists()) {
            project.create(null);
        }

        if (!project.isOpen()) {
            project.open(null);
        }

        resource = resourceSet.createResource(URI.createPlatformResourceURI(project.getFullPath() + "/"
                + MODEL_FILE_NAME, true));

        testee = simplemodelFactory.eINSTANCE.createA();
        resource.getContents().add((EObject) testee);
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
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
    // @Test
    public void testEStereotypableObjectImpl() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#setProfileApplicationDecorator(org.modelversioning.emfprofile.application.registry.ProfileApplicationDecorator)}
     * .
     */
    // @Test
    public void testSetProfileApplicationDecorator() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getStereotypeApplications()}
     * .
     */
    @Test
    public void testGetStereotypeApplications() {

        // retrieve stereotypes for application
        Stereotype entityBeanStereotype = testee.getApplicableStereotype(ENTITY_BEAN_QUALIFIED_NAME);
        Stereotype sessionBeanStereotype = testee.getApplicableStereotype(SESSION_BEAN_QUALIFIED_NAME);

        // check that there are no stereotype applications at the beginning
        EList<StereotypeApplication> stereotypeApplications = testee.getStereotypeApplications();
        assertEquals(0, stereotypeApplications.size());

        // first stereotype application
        StereotypeApplication entityBeanApplication = testee.applyStereotype(entityBeanStereotype);
        stereotypeApplications = testee.getStereotypeApplications();
        assertEquals(1, stereotypeApplications.size());
        assertSame(entityBeanApplication, stereotypeApplications.get(0));

        // second stereotype application
        StereotypeApplication sessionBeanApplication = testee.applyStereotype(sessionBeanStereotype);
        stereotypeApplications = testee.getStereotypeApplications();
        assertEquals(2, stereotypeApplications.size());
        assertSame(sessionBeanApplication, stereotypeApplications.get(1));
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getStereotypeApplications(org.modelversioning.emfprofile.Stereotype)}
     * .
     */
    @Test
    public void testGetStereotypeApplicationsStereotype() {

        // retrieve stereotypes for application
        Stereotype entityBeanStereotype = testee.getApplicableStereotype(ENTITY_BEAN_QUALIFIED_NAME);
        Stereotype sessionBeanStereotype = testee.getApplicableStereotype(SESSION_BEAN_QUALIFIED_NAME);

        // check that there are no stereotype applications at the beginning
        EList<StereotypeApplication> entityBeanApplications = testee.getStereotypeApplications(entityBeanStereotype);
        assertTrue(entityBeanApplications.isEmpty());

        EList<StereotypeApplication> sessionBeanApplications = testee.getStereotypeApplications(sessionBeanStereotype);
        assertTrue(sessionBeanApplications.isEmpty());

        // first stereotype application
        testee.applyStereotype(entityBeanStereotype);

        // get the stereotype again, this time from the resource set of the model to which it is
        // applied (before it was the stereotype of the resource set of the profile registry)
        entityBeanStereotype = testee.getApplicableStereotype(ENTITY_BEAN_QUALIFIED_NAME);

        entityBeanApplications = testee.getStereotypeApplications(entityBeanStereotype);
        assertEquals(1, entityBeanApplications.size());

        sessionBeanApplications = testee.getStereotypeApplications(sessionBeanStereotype);
        assertTrue(sessionBeanApplications.isEmpty());

        // second stereotype application
        testee.applyStereotype(sessionBeanStereotype);

        // get the stereotype again, this time from the resource set of the model to which it is
        // applied (before it was the stereotype of the resource set of the profile registry)
        sessionBeanStereotype = testee.getApplicableStereotype(SESSION_BEAN_QUALIFIED_NAME);

        entityBeanApplications = testee.getStereotypeApplications(entityBeanStereotype);
        assertEquals(1, entityBeanApplications.size());

        sessionBeanApplications = testee.getStereotypeApplications(sessionBeanStereotype);
        assertEquals(1, sessionBeanApplications.size());

        assertNotSame(entityBeanApplications.get(0), sessionBeanApplications.get(0));

        assertSame(entityBeanStereotype, entityBeanApplications.get(0).getStereotype());
        assertSame(sessionBeanStereotype, sessionBeanApplications.get(0).getStereotype());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getStereotypeApplications(org.modelversioning.emfprofile.Stereotype)}
     * .
     * 
     * It checks that a NullPointerException doesn't occur if null is passed as parameter.
     */
    @Test
    public void testGetStereotypeApplicationsStereotypeNull() {

        Stereotype stereotype = null;

        EList<StereotypeApplication> stereotypeApplications = testee.getStereotypeApplications(stereotype);

        assertTrue(stereotypeApplications.isEmpty());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getStereotypeApplications(java.lang.String)}
     * .
     */
    @Test
    public void testGetStereotypeApplicationsString() {

        // retrieve stereotypes for application
        Stereotype entityBeanStereotype = testee.getApplicableStereotype(ENTITY_BEAN_QUALIFIED_NAME);
        Stereotype sessionBeanStereotype = testee.getApplicableStereotype(SESSION_BEAN_QUALIFIED_NAME);

        // check that there are no stereotype applications at the beginning
        EList<StereotypeApplication> entityBeanApplications = testee
                .getStereotypeApplications(ENTITY_BEAN_QUALIFIED_NAME);
        assertTrue(entityBeanApplications.isEmpty());

        EList<StereotypeApplication> sessionBeanApplications = testee
                .getStereotypeApplications(SESSION_BEAN_QUALIFIED_NAME);
        assertTrue(sessionBeanApplications.isEmpty());

        // first stereotype application
        testee.applyStereotype(entityBeanStereotype);

        entityBeanApplications = testee.getStereotypeApplications(ENTITY_BEAN_QUALIFIED_NAME);
        assertEquals(1, entityBeanApplications.size());

        sessionBeanApplications = testee.getStereotypeApplications(SESSION_BEAN_QUALIFIED_NAME);
        assertTrue(sessionBeanApplications.isEmpty());

        // second stereotype application
        testee.applyStereotype(sessionBeanStereotype);

        entityBeanApplications = testee.getStereotypeApplications(ENTITY_BEAN_QUALIFIED_NAME);
        assertEquals(1, entityBeanApplications.size());

        sessionBeanApplications = testee.getStereotypeApplications(SESSION_BEAN_QUALIFIED_NAME);
        assertEquals(1, sessionBeanApplications.size());

        assertNotSame(entityBeanApplications.get(0), sessionBeanApplications.get(0));

        // get the stereotypes again, this time from the resource set of the model to which it is
        // applied (before it was the stereotype of the resource set of the profile registry)
        entityBeanStereotype = testee.getApplicableStereotype(ENTITY_BEAN_QUALIFIED_NAME);
        sessionBeanStereotype = testee.getApplicableStereotype(SESSION_BEAN_QUALIFIED_NAME);

        assertSame(entityBeanStereotype, entityBeanApplications.get(0).getStereotype());
        assertSame(sessionBeanStereotype, sessionBeanApplications.get(0).getStereotype());

        assertEquals(ENTITY_BEAN_QUALIFIED_NAME, entityBeanApplications.get(0).getStereotype().getName());
        assertEquals(SESSION_BEAN_QUALIFIED_NAME, sessionBeanApplications.get(0).getStereotype().getName());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getStereotypeApplications(java.lang.String)}
     * .
     * 
     * It checks that a NullPointerException doesn't occur if null is passed as parameter.
     */
    @Test
    public void testGetStereotypeApplicationsStringNull() {

        String qualifiedName = null;

        EList<StereotypeApplication> stereotypeApplications = testee.getStereotypeApplications(qualifiedName);

        assertTrue(stereotypeApplications.isEmpty());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getAppliedStereotypes()}
     * .
     */
    // @Test
    public void testGetAppliedStereotypes() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getAppliedStereotype(java.lang.String)}
     * .
     */
    // @Test
    public void testGetAppliedStereotype() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#isStereotypeApplicable(org.modelversioning.emfprofile.Stereotype)}
     * .
     */
    // @Test
    public void testIsStereotypeApplicable() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#isStereotypeApplied(org.modelversioning.emfprofile.Stereotype)}
     * .
     */
    // @Test
    public void testIsStereotypeApplied() {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#applyStereotype(org.modelversioning.emfprofile.Stereotype)}
     * .
     */
    @Test
    public void testApplyStereotypeOnce() {

        // retrieve a stereotype for application
        Stereotype stereotype = testee.getApplicableStereotype(ENTITY_BEAN_QUALIFIED_NAME);

        StereotypeApplication stereotypeApplication = testee.applyStereotype(stereotype);

        // get the stereotype again, this time from the resource set of the model to which it is
        // applied (before it was the stereotype of the resource set of the profile registry)
        stereotype = testee.getApplicableStereotype(ENTITY_BEAN_QUALIFIED_NAME);

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
    public void testApplyStereotypeTwice() {

        // retrieve a stereotype for application
        Stereotype stereotype = testee.getApplicableStereotype(ENTITY_BEAN_QUALIFIED_NAME);

        StereotypeApplication firstStereotypeApplication = testee.applyStereotype(stereotype);
        assertNotNull(firstStereotypeApplication);

        StereotypeApplication secondStereotypeApplication = testee.applyStereotype(stereotype);
        assertNotNull(secondStereotypeApplication);

        assertNotSame(firstStereotypeApplication, secondStereotypeApplication);
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#applyStereotype(org.modelversioning.emfprofile.Stereotype)}
     * .
     * 
     * It checks that a NullPointerException doesn't occur if null is passed as parameter.
     */
    @Test
    public void testApplyStereotypeNull() {

        StereotypeApplication stereotypeApplication = testee.applyStereotype(null);

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
    public void testRemoveStereotypeApplicationOnce() {

        // retrieve stereotypes for application
        Stereotype entityBeanStereotype = testee.getApplicableStereotype(ENTITY_BEAN_QUALIFIED_NAME);

        StereotypeApplication entityBeanApplication = testee.applyStereotype(entityBeanStereotype);
        assertEquals(1, testee.getStereotypeApplications().size());

        testee.removeStereotypeApplication(entityBeanApplication);
        assertTrue(testee.getStereotypeApplications().isEmpty());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#removeStereotypeApplication(org.modelversioning.emfprofileapplication.StereotypeApplications)}
     * .
     * 
     * It checks the behavior in case that a stereotype application which has already been removed
     * is tried to be removed again.
     */
    @Test
    public void testRemoveStereotypeApplicationTwice() {

        // retrieve stereotypes for application
        Stereotype entityBeanStereotype = testee.getApplicableStereotype(ENTITY_BEAN_QUALIFIED_NAME);

        StereotypeApplication entityBeanApplication = testee.applyStereotype(entityBeanStereotype);
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
     * It checks that the correct stereotype application is removed if two stereotypes of the same
     * type are applied.
     */
    @Test
    public void testRemoveStereotypeApplicationOneOutOfTwo() {

        // retrieve stereotypes for application
        Stereotype entityBeanStereotype = testee.getApplicableStereotype(ENTITY_BEAN_QUALIFIED_NAME);

        StereotypeApplication firstStereotypeApplication = testee.applyStereotype(entityBeanStereotype);
        StereotypeApplication secondStereotypeApplication = testee.applyStereotype(entityBeanStereotype);

        testee.removeStereotypeApplication(secondStereotypeApplication);

        assertEquals(1, testee.getStereotypeApplications().size());
        assertSame(firstStereotypeApplication, testee.getStereotypeApplications().get(0));
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#removeStereotypeApplication(org.modelversioning.emfprofileapplication.StereotypeApplications)}
     * .
     * 
     * It checks that a NullPointerException doesn't occur if null is passed as parameter.
     */
    @Test
    public void testRemoveStereotypeApplicationNull() {

        testee.removeStereotypeApplication(null);

        assertTrue(testee.getStereotypeApplications().isEmpty());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#removeStereotypeApplication(org.modelversioning.emfprofileapplication.StereotypeApplications)}
     * .
     * 
     * It checks the behavior in case that someone tries to remove a stereotype application which
     * belongs to another object.
     */
    @Test
    public void testRemoveStereotypeApplicationFromOtherObject() {

        // set up a second EStereotypableObject
        EStereotypableObject otherTestee = simplemodelFactory.eINSTANCE.createA();
        resource.getContents().add((EObject) otherTestee);

        // retrieve stereotypes for application
        Stereotype entityBeanStereotype = testee.getApplicableStereotype(ENTITY_BEAN_QUALIFIED_NAME);

        StereotypeApplication testeeStereotypeApplication = testee.applyStereotype(entityBeanStereotype);
        StereotypeApplication otherTesteeStereotypeApplication = otherTestee.applyStereotype(entityBeanStereotype);

        // try to remove otherTestee's stereotype application from testee and vice versa
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
    public void testGetApplicableStereotypes() {

        EList<Stereotype> applicableStereotypes = testee.getApplicableStereotypes();

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
    public void testGetApplicableStereotypeExisting() {

        Stereotype applicableStereotype = testee.getApplicableStereotype(SESSION_BEAN_QUALIFIED_NAME);

        assertNotNull(applicableStereotype);
        assertEquals(SESSION_BEAN_QUALIFIED_NAME, applicableStereotype.getName());
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getApplicableStereotype(java.lang.String)}
     * .
     * 
     * It checks that an unavailable stereotype is not found (returns null).
     */
    @Test
    public void testGetApplicableStereotypeNotExisting() {

        final String QUALIFIED_NAME = "UnavailableStereotype";

        Stereotype applicableStereotype = testee.getApplicableStereotype(QUALIFIED_NAME);

        assertNull(applicableStereotype);
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getApplicableStereotype(java.lang.String)}
     * .
     * 
     * It checks that a NullPointerException doesn't occur if null is passed as parameter.
     */
    @Test
    public void testGetApplicableStereotypeNull() {

        Stereotype stereotype = testee.getApplicableStereotype(null);

        assertNull(stereotype);
    }

    /**
     * Test method for
     * {@link edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl.EStereotypableObjectImpl#getStereotypeApplications()}
     * .
     * 
     * It checks that the references of EStereotypableObject and applied-to in StereotypeApplication
     * is correct after loading persisted model instances file and profile application file.
     */
    @Test
    public void testValidatePersistedReferencesOfEStereotypableObjectsAndProfileApplication() {

        final String RESOURCES_PROJECT_NAME = "EJBTestResources";
        final String MODEL_INSTANCES_FILE_PATH = "/modelinstances/AContainsTwoBs.xmi";

        Resource modelInstancesResource = getModelInstancesResource(RESOURCES_PROJECT_NAME, MODEL_INSTANCES_FILE_PATH);

        // implicitly a look-up in existing profile application file takes place when requesting
        // stereotype applications

        final B b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        assertEquals("b1", b1.getName());
        assertEquals(1, b1.getStereotypeApplications().size());

        final B b2 = (B) modelInstancesResource.getEObject("//@bs.1");
        assertEquals("b2", b2.getName());
        assertEquals(2, b2.getStereotypeApplications().size());
    }

    /**
     * It checks that an already persisted tagged value is loaded correctly and a change to this
     * tagged value is persisted correctly. This is done by reloading the tagged value.
     */
    @Test
    public void testCheckTaggedValuePersistence() {
        final String RESOURCES_PROJECT_NAME = "EJBTestResources";
        final String MODEL_INSTANCES_FILE_PATH = "/modelinstances/AContainsTwoBs_TaggedValuePersistenceTest.xmi";

        Resource modelInstancesResource = getModelInstancesResource(RESOURCES_PROJECT_NAME, MODEL_INSTANCES_FILE_PATH);

        // setup for checking the precondition
        B b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        assertEquals("b1", b1.getName());

        EList<StereotypeApplication> stereotypeApplications = b1.getStereotypeApplications(SESSION_BEAN_QUALIFIED_NAME);
        assertEquals(1, stereotypeApplications.size());

        StereotypeApplication stereotypeApplication = stereotypeApplications.get(0);

        Object stateful = getValueOfEStructuralFeature(stereotypeApplication, "stateful");
        assertEquals("revert profile application file", false, stateful);

        // set and persist the new value
        setValueOfEStructuralFeature(stereotypeApplication, "stateful", true);
        b1.saveContainingProfileApplication();

        // cleanup
        modelInstancesResource.unload();
        resourceSet.getResources().remove(modelInstancesResource);
        ProfileApplicationFileRegistry.INSTANCE.clear();

        // check the reloaded attribute value
        modelInstancesResource = getModelInstancesResource(RESOURCES_PROJECT_NAME, MODEL_INSTANCES_FILE_PATH);

        b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        stereotypeApplications = b1.getStereotypeApplications(SESSION_BEAN_QUALIFIED_NAME);
        assertEquals(1, stereotypeApplications.size());

        stereotypeApplication = stereotypeApplications.get(0);
        
        stateful = getValueOfEStructuralFeature(stereotypeApplication, "stateful");
        assertEquals(true, stateful);
        
        // set and persist the old value
        setValueOfEStructuralFeature(stereotypeApplication, "stateful", false);
        b1.saveContainingProfileApplication();
        stateful = getValueOfEStructuralFeature(stereotypeApplication, "stateful");
        assertEquals(false, stateful);
    }

    /**
     * It checks that an already persisted StereotypeApplication reference is loaded correctly and a
     * change to this reference is persisted correctly. This is done by reloading the reference.
     */
    @Test
    public void testCheckStereotypeReferencePersistence() {
        final String RESOURCES_PROJECT_NAME = "EJBTestResources";
        final String MODEL_INSTANCES_FILE_PATH = "/modelinstances/AContainsTwoBs_ReferencePersistenceTest.xmi";

        Resource modelInstancesResource = getModelInstancesResource(RESOURCES_PROJECT_NAME, MODEL_INSTANCES_FILE_PATH);

        // setup for checking the precondition
        B b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        assertEquals("b1", b1.getName());

        EList<StereotypeApplication> stereotypeApplicationsOfB1 = b1
                .getStereotypeApplications(OTHER_BEAN_QUALIFIED_NAME);
        assertEquals(1, stereotypeApplicationsOfB1.size());

        StereotypeApplication stereotypeApplicationOfB1 = stereotypeApplicationsOfB1.get(0);

        Object myReferencedStereotype = getValueOfEStructuralFeature(stereotypeApplicationOfB1,
                "myReferencedStereotype");
        assertNotNull(myReferencedStereotype);
        assertEquals(true, myReferencedStereotype instanceof StereotypeApplication);

        StereotypeApplication referencedStereotypeApplication = (StereotypeApplication) myReferencedStereotype;
        assertEquals("referencedStereotype1", getValueOfEStructuralFeature(referencedStereotypeApplication, "name"));

        // retrieve the reference to be set as new value
        // both ReferencedStereotypes are applied to root object A
        A a1 = (A) modelInstancesResource.getEObject("/");
        assertEquals("a1", a1.getName());

        EList<StereotypeApplication> stereotypeApplicationsOfA1 = a1.getStereotypeApplications("ReferencedStereotype");
        assertEquals(2, stereotypeApplicationsOfA1.size());

        StereotypeApplication newReferencedStereotypeApplication = stereotypeApplicationsOfA1.get(1);
        assertEquals("referencedStereotype2", getValueOfEStructuralFeature(newReferencedStereotypeApplication, "name"));

        // set and persist the new value
        setValueOfEStructuralFeature(stereotypeApplicationOfB1, "myReferencedStereotype",
                newReferencedStereotypeApplication);
        a1.saveContainingProfileApplication();

        // cleanup
        modelInstancesResource.unload();
        resourceSet.getResources().remove(modelInstancesResource);
        ProfileApplicationFileRegistry.INSTANCE.clear();

        // check the reloaded attribute value
        modelInstancesResource = getModelInstancesResource(RESOURCES_PROJECT_NAME, MODEL_INSTANCES_FILE_PATH);

        b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        assertEquals("b1", b1.getName());

        stereotypeApplicationsOfB1 = b1.getStereotypeApplications(OTHER_BEAN_QUALIFIED_NAME);
        assertEquals(1, stereotypeApplicationsOfB1.size());

        stereotypeApplicationOfB1 = stereotypeApplicationsOfB1.get(0);

        myReferencedStereotype = getValueOfEStructuralFeature(stereotypeApplicationOfB1, "myReferencedStereotype");
        assertNotNull(myReferencedStereotype);
        assertEquals(true, myReferencedStereotype instanceof StereotypeApplication);

        referencedStereotypeApplication = (StereotypeApplication) myReferencedStereotype;
        assertEquals("referencedStereotype2", getValueOfEStructuralFeature(referencedStereotypeApplication, "name"));
        
        // reset and persist the old value
        referencedStereotypeApplication = stereotypeApplicationsOfA1.get(0);
        setValueOfEStructuralFeature(stereotypeApplicationOfB1, "myReferencedStereotype",
        		referencedStereotypeApplication);
        a1.saveContainingProfileApplication();
        assertEquals("referencedStereotype1", getValueOfEStructuralFeature(referencedStereotypeApplication, "name"));
    }

    /**
     * It checks that the number of loaded resources in a resource set after retrieving the
     * applicable stereotypes of an EStereotypableObject is the same after retrieving the applicable
     * stereotypes of another EStereotypableObject (from the same model).
     */
    @Test
    public void testNumberOfLoadedResourcesInResourceSet() {
        final String RESOURCES_PROJECT_NAME = "EJBTestResources";
        final String MODEL_INSTANCES_FILE_PATH = "/modelinstances/AContainsTwoBs.xmi";

        Resource modelInstancesResource = getModelInstancesResource(RESOURCES_PROJECT_NAME, MODEL_INSTANCES_FILE_PATH);

        final B b1 = (B) modelInstancesResource.getEObject("//@bs.0");
        b1.getApplicableStereotypes();

        final int numberOfResources = resourceSet.getResources().size();

        final B b2 = (B) modelInstancesResource.getEObject("//@bs.1");
        b2.getApplicableStereotypes();

        assertEquals(numberOfResources, resourceSet.getResources().size());
    }

    private Resource getModelInstancesResource(final String projectName, final String modelInstancesFilePath) {
        final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

        final IProject resourcesProject = root.getProject(projectName);

        if (!resourcesProject.exists()) {
            fail("project '" + projectName + "' does not exist");
        }

        final IFile modelInstancesFile = resourcesProject.getFile(modelInstancesFilePath);

        if (!modelInstancesFile.exists()) {
            fail("file '" + modelInstancesFilePath + "' does not exist");
        }

        final URI uri = URI.createPlatformResourceURI(modelInstancesFile.getFullPath().toString(), true);

        Resource modelInstancesResource = null;

        try {
            modelInstancesResource = resourceSet.getResource(uri, true);
        } catch (RuntimeException exception) {
            exception.printStackTrace();
        }

        return modelInstancesResource;
    }

    /**
     * Retrieves the value of the specified StereotypeApplication's EStructuralFeature given by the
     * name of the feature.
     * 
     * @param stereotypeApplication
     *            The StereotypeApplication containing the EStructuralFeature.
     * @param name
     *            The name of the EStructuralFeature.
     * @return The value of the EStructuralFeature.
     */
    private static Object getValueOfEStructuralFeature(final StereotypeApplication stereotypeApplication,
            final String name) {
        assertNotNull(stereotypeApplication);
        assertNotNull(name);

        Stereotype stereotype = stereotypeApplication.getStereotype();

        EStructuralFeature taggedValue = stereotype.getTaggedValue(name);

        return stereotypeApplication.eGet(taggedValue);
    }

    /**
     * Sets the value of the specified StereotypeApplication's EStructuralFeature given by the name
     * of the feature.
     * 
     * @param stereotypeApplication
     *            The StereotypeApplication containing the EStructuralFeature.
     * @param name
     *            The name of the EStructuralFeature.
     * @param newValue
     *            The new value to be set.
     */
    private static void setValueOfEStructuralFeature(final StereotypeApplication stereotypeApplication,
            final String name, final Object newValue) {
        assertNotNull(stereotypeApplication);
        assertNotNull(name);

        Stereotype stereotype = stereotypeApplication.getStereotype();

        EStructuralFeature taggedValue = stereotype.getTaggedValue(name);

        stereotypeApplication.eSet(taggedValue, newValue);
    }
}
