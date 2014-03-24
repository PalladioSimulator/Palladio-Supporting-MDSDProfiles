/**
 */
package edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl;

import edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.Super;
import edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.simplemodelFactory;
import edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.simplemodelPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class simplemodelPackageImpl extends EPackageImpl implements simplemodelPackage {
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass aEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass bEClass = null;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private EClass superEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
     * package URI value.
     * <p>Note: the correct way to create the package is via the static
     * factory method {@link #init init()}, which also performs
     * initialization of the package, or returns the registered package,
     * if one already exists.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.simplemodelPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private simplemodelPackageImpl() {
        super(eNS_URI, simplemodelFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
     * 
     * <p>This method is used to initialize {@link simplemodelPackage#eINSTANCE} when that field is accessed.
     * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static simplemodelPackage init() {
        if (isInited) return (simplemodelPackage)EPackage.Registry.INSTANCE.getEPackage(simplemodelPackage.eNS_URI);

        // Obtain or create and register package
        simplemodelPackageImpl thesimplemodelPackage = (simplemodelPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof simplemodelPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new simplemodelPackageImpl());

        isInited = true;

        // Create package meta-data objects
        thesimplemodelPackage.createPackageContents();

        // Initialize created meta-data
        thesimplemodelPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        thesimplemodelPackage.freeze();

  
        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(simplemodelPackage.eNS_URI, thesimplemodelPackage);
        return thesimplemodelPackage;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getA() {
        return aEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EReference getA_Bs() {
        return (EReference)aEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getA_Name() {
        return (EAttribute)aEClass.getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getB() {
        return bEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EAttribute getB_Name() {
        return (EAttribute)bEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EClass getSuper() {
        return superEClass;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public simplemodelFactory getsimplemodelFactory() {
        return (simplemodelFactory)getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package.  This method is
     * guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void createPackageContents() {
        if (isCreated) return;
        isCreated = true;

        // Create classes and their features
        aEClass = createEClass(A);
        createEReference(aEClass, A__BS);
        createEAttribute(aEClass, A__NAME);

        bEClass = createEClass(B);
        createEAttribute(bEClass, B__NAME);

        superEClass = createEClass(SUPER);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model.  This
     * method is guarded to have no affect on any invocation but its first.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void initializePackageContents() {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize package
        setName(eNAME);
        setNsPrefix(eNS_PREFIX);
        setNsURI(eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        aEClass.getESuperTypes().add(this.getSuper());
        bEClass.getESuperTypes().add(this.getSuper());

        // Initialize classes and features; add operations and parameters
        initEClass(aEClass, edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.A.class, "A", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEReference(getA_Bs(), this.getB(), null, "bs", null, 0, -1, edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.A.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
        initEAttribute(getA_Name(), ecorePackage.getEString(), "name", null, 1, 1, edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.A.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(bEClass, edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.B.class, "B", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        initEAttribute(getB_Name(), ecorePackage.getEString(), "name", null, 1, 1, edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.B.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

        initEClass(superEClass, Super.class, "Super", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        // Create resource
        createResource(eNS_URI);
    }

} //simplemodelPackageImpl
