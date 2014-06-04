/**
 */
package edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.tests;

import edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.A;
import edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.simplemodelFactory;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>A</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class ATest extends SuperTest {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static void main(String[] args) {
        TestRunner.run(ATest.class);
    }

    /**
     * Constructs a new A test case with the given name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ATest(String name) {
        super(name);
    }

    /**
     * Returns the fixture for this A test case.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected A getFixture() {
        return (A)fixture;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see junit.framework.TestCase#setUp()
     * @generated
     */
    @Override
    protected void setUp() throws Exception {
        setFixture(simplemodelFactory.eINSTANCE.createA());
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see junit.framework.TestCase#tearDown()
     * @generated
     */
    @Override
    protected void tearDown() throws Exception {
        setFixture(null);
    }

} //ATest
