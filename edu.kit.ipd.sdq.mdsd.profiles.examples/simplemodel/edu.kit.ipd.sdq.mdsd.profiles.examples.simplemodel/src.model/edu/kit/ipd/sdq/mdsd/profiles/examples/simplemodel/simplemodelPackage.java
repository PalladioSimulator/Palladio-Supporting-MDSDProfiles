/**
 */
package edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.simplemodelFactory
 * @model kind="package"
 * @generated
 */
public interface simplemodelPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "simplemodel";

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://simplemodel/1.0";

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "simplemodel";

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    simplemodelPackage eINSTANCE = edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.simplemodelPackageImpl.init();

    /**
     * The meta object id for the '{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.SuperImpl <em>Super</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.SuperImpl
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.simplemodelPackageImpl#getSuper()
     * @generated
     */
    int SUPER = 2;

    /**
     * The number of structural features of the '<em>Super</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int SUPER_FEATURE_COUNT = 0;

    /**
     * The meta object id for the '{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.AImpl <em>A</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.AImpl
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.simplemodelPackageImpl#getA()
     * @generated
     */
    int A = 0;

    /**
     * The feature id for the '<em><b>Bs</b></em>' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int A__BS = SUPER_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int A__NAME = SUPER_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>A</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int A_FEATURE_COUNT = SUPER_FEATURE_COUNT + 2;

    /**
     * The meta object id for the '{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.BImpl <em>B</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.BImpl
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.simplemodelPackageImpl#getB()
     * @generated
     */
    int B = 1;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int B__NAME = SUPER_FEATURE_COUNT + 0;

    /**
     * The number of structural features of the '<em>B</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int B_FEATURE_COUNT = SUPER_FEATURE_COUNT + 1;


    /**
     * Returns the meta object for class '{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.A <em>A</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>A</em>'.
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.A
     * @generated
     */
    EClass getA();

    /**
     * Returns the meta object for the containment reference list '{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.A#getBs <em>Bs</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the containment reference list '<em>Bs</em>'.
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.A#getBs()
     * @see #getA()
     * @generated
     */
    EReference getA_Bs();

    /**
     * Returns the meta object for the attribute '{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.A#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.A#getName()
     * @see #getA()
     * @generated
     */
    EAttribute getA_Name();

    /**
     * Returns the meta object for class '{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.B <em>B</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>B</em>'.
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.B
     * @generated
     */
    EClass getB();

    /**
     * Returns the meta object for the attribute '{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.B#getName <em>Name</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.B#getName()
     * @see #getB()
     * @generated
     */
    EAttribute getB_Name();

    /**
     * Returns the meta object for class '{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.Super <em>Super</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Super</em>'.
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.Super
     * @generated
     */
    EClass getSuper();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    simplemodelFactory getsimplemodelFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.AImpl <em>A</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.AImpl
         * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.simplemodelPackageImpl#getA()
         * @generated
         */
        EClass A = eINSTANCE.getA();

        /**
         * The meta object literal for the '<em><b>Bs</b></em>' containment reference list feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EReference A__BS = eINSTANCE.getA_Bs();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute A__NAME = eINSTANCE.getA_Name();

        /**
         * The meta object literal for the '{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.BImpl <em>B</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.BImpl
         * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.simplemodelPackageImpl#getB()
         * @generated
         */
        EClass B = eINSTANCE.getB();

        /**
         * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute B__NAME = eINSTANCE.getB_Name();

        /**
         * The meta object literal for the '{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.SuperImpl <em>Super</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.SuperImpl
         * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl.simplemodelPackageImpl#getSuper()
         * @generated
         */
        EClass SUPER = eINSTANCE.getSuper();

    }

} //simplemodelPackage
