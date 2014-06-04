/**
 */
package edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.tests;

import edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.B;
import edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.simplemodelFactory;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>B</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class BTest extends SuperTest {

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static void main(String[] args) {
        TestRunner.run(BTest.class);
    }

    /**
     * Constructs a new B test case with the given name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public BTest(String name) {
        super(name);
    }

    /**
     * Returns the fixture for this B test case.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected B getFixture() {
        return (B)fixture;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see junit.framework.TestCase#setUp()
     * @generated
     */
    @Override
    protected void setUp() throws Exception {
        setFixture(simplemodelFactory.eINSTANCE.createB());
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

} //BTest
