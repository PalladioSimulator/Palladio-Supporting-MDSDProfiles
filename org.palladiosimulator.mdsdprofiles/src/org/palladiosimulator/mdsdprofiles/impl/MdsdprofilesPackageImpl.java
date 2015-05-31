/**
 */
package org.palladiosimulator.mdsdprofiles.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.modelversioning.emfprofile.EMFProfilePackage;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;
import org.palladiosimulator.mdsdprofiles.MdsdprofilesFactory;
import org.palladiosimulator.mdsdprofiles.MdsdprofilesPackage;
import org.palladiosimulator.mdsdprofiles.ProfileableElement;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 *
 * @generated
 */
public class MdsdprofilesPackageImpl extends EPackageImpl implements MdsdprofilesPackage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass profileableElementEClass = null;
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass stereotypableElementEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI
     * value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init
     * init()}, which also performs initialization of the package, or returns the registered
     * package, if one already exists. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.palladiosimulator.mdsdprofiles.MdsdprofilesPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private MdsdprofilesPackageImpl() {
        super(eNS_URI, MdsdprofilesFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others
     * upon which it depends.
     *
     * <p>
     * This method is used to initialize {@link MdsdprofilesPackage#eINSTANCE} when that field is
     * accessed. Clients should not invoke it directly. Instead, they should simply access that
     * field to obtain the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #eNS_URI
     * @see #createPackageContents()
     * @see #initializePackageContents()
     * @generated
     */
    public static MdsdprofilesPackage init() {
        if (isInited) {
            return (MdsdprofilesPackage) EPackage.Registry.INSTANCE.getEPackage(MdsdprofilesPackage.eNS_URI);
        }

        // Obtain or create and register package
        final MdsdprofilesPackageImpl theMdsdprofilesPackage = (MdsdprofilesPackageImpl) (EPackage.Registry.INSTANCE
                .get(eNS_URI) instanceof MdsdprofilesPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI)
                : new MdsdprofilesPackageImpl());

        isInited = true;

        // Initialize simple dependencies
        EMFProfileApplicationPackage.eINSTANCE.eClass();

        // Create package meta-data objects
        theMdsdprofilesPackage.createPackageContents();

        // Initialize created meta-data
        theMdsdprofilesPackage.initializePackageContents();

        // Mark meta-data to indicate it can't be changed
        theMdsdprofilesPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(MdsdprofilesPackage.eNS_URI, theMdsdprofilesPackage);
        return theMdsdprofilesPackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getProfileableElement() {
        return this.profileableElementEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getStereotypableElement() {
        return this.stereotypableElementEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getStereotypableElement_ProfileableElement() {
        return (EReference) this.stereotypableElementEClass.getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public MdsdprofilesFactory getMdsdprofilesFactory() {
        return (MdsdprofilesFactory) this.getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private boolean isCreated = false;

    /**
     * Creates the meta-model objects for the package. This method is guarded to have no affect on
     * any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public void createPackageContents() {
        if (this.isCreated) {
            return;
        }
        this.isCreated = true;

        // Create classes and their features
        this.profileableElementEClass = this.createEClass(PROFILEABLE_ELEMENT);

        this.stereotypableElementEClass = this.createEClass(STEREOTYPABLE_ELEMENT);
        this.createEReference(this.stereotypableElementEClass, STEREOTYPABLE_ELEMENT__PROFILEABLE_ELEMENT);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private boolean isInitialized = false;

    /**
     * Complete the initialization of the package and its meta-model. This method is guarded to have
     * no affect on any invocation but its first. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public void initializePackageContents() {
        if (this.isInitialized) {
            return;
        }
        this.isInitialized = true;

        // Initialize package
        this.setName(eNAME);
        this.setNsPrefix(eNS_PREFIX);
        this.setNsURI(eNS_URI);

        // Obtain other dependent packages
        final EMFProfilePackage theEMFProfilePackage = (EMFProfilePackage) EPackage.Registry.INSTANCE
                .getEPackage(EMFProfilePackage.eNS_URI);
        final EMFProfileApplicationPackage theEMFProfileApplicationPackage = (EMFProfileApplicationPackage) EPackage.Registry.INSTANCE
                .getEPackage(EMFProfileApplicationPackage.eNS_URI);
        final EcorePackage theEcorePackage = (EcorePackage) EPackage.Registry.INSTANCE
                .getEPackage(EcorePackage.eNS_URI);

        // Create type parameters

        // Set bounds for type parameters

        // Add supertypes to classes
        this.profileableElementEClass.getESuperTypes().add(this.getStereotypableElement());
        this.stereotypableElementEClass.getESuperTypes().add(theEcorePackage.getEModelElement());

        // Initialize classes and features; add operations and parameters
        this.initEClass(this.profileableElementEClass, ProfileableElement.class, "ProfileableElement", !IS_ABSTRACT,
                !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

        EOperation op = this.addEOperation(this.profileableElementEClass, null, "applyProfile", 0, 1, IS_UNIQUE,
                IS_ORDERED);
        this.addEParameter(op, theEMFProfilePackage.getProfile(), "profile", 1, 1, IS_UNIQUE, IS_ORDERED);

        op = this.addEOperation(this.profileableElementEClass, this.ecorePackage.getEBoolean(),
                "updateProfileApplications", 0, 1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, theEMFProfilePackage.getProfile(), "profilesToBeApplied", 0, -1, IS_UNIQUE, !IS_ORDERED);

        this.addEOperation(this.profileableElementEClass, this.ecorePackage.getEBoolean(), "hasProfileApplication", 1,
                1, IS_UNIQUE, IS_ORDERED);

        this.addEOperation(this.profileableElementEClass, theEMFProfileApplicationPackage.getProfileApplication(),
                "getProfileApplication", 1, 1, IS_UNIQUE, IS_ORDERED);

        this.addEOperation(this.profileableElementEClass, theEMFProfilePackage.getProfile(), "getApplicableProfiles",
                0, -1, IS_UNIQUE, !IS_ORDERED);

        this.addEOperation(this.profileableElementEClass, theEMFProfilePackage.getProfile(), "getAppliedProfiles", 0,
                -1, IS_UNIQUE, !IS_ORDERED);

        op = this.addEOperation(this.profileableElementEClass, null, "unapplyProfile", 0, 1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, theEMFProfilePackage.getProfile(), "profile", 1, 1, IS_UNIQUE, IS_ORDERED);

        this.initEClass(this.stereotypableElementEClass, StereotypableElement.class, "StereotypableElement",
                IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
        this.initEReference(this.getStereotypableElement_ProfileableElement(), this.getProfileableElement(), null,
                "profileableElement", null, 0, 1, StereotypableElement.class, IS_TRANSIENT, IS_VOLATILE,
                !IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, IS_DERIVED, IS_ORDERED);

        op = this.addEOperation(this.stereotypableElementEClass, null, "applyStereotype", 0, 1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, theEMFProfilePackage.getStereotype(), "stereotype", 1, 1, IS_UNIQUE, IS_ORDERED);

        op = this.addEOperation(this.stereotypableElementEClass, null, "applyStereotype", 0, 1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEString(), "stereotypeName", 1, 1, IS_UNIQUE, IS_ORDERED);

        op = this.addEOperation(this.stereotypableElementEClass, this.ecorePackage.getEBoolean(),
                "updateStereotypeApplications", 0, 1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, theEMFProfilePackage.getStereotype(), "stereotypesToBeApplied", 0, -1, IS_UNIQUE,
                !IS_ORDERED);

        op = this.addEOperation(this.stereotypableElementEClass, this.ecorePackage.getEBoolean(),
                "isStereotypeApplicable", 1, 1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, theEMFProfilePackage.getStereotype(), "stereotype", 1, 1, IS_UNIQUE, IS_ORDERED);

        op = this.addEOperation(this.stereotypableElementEClass, this.ecorePackage.getEBoolean(),
                "isStereotypeApplicable", 1, 1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEString(), "stereotypeName", 1, 1, IS_UNIQUE, IS_ORDERED);

        op = this.addEOperation(this.stereotypableElementEClass, this.ecorePackage.getEBoolean(),
                "isStereotypeApplied", 1, 1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, theEMFProfilePackage.getStereotype(), "stereotype", 1, 1, IS_UNIQUE, IS_ORDERED);

        op = this.addEOperation(this.stereotypableElementEClass, this.ecorePackage.getEBoolean(),
                "isStereotypeApplied", 1, 1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEString(), "stereotypeName", 1, 1, IS_UNIQUE, IS_ORDERED);

        this.addEOperation(this.stereotypableElementEClass, this.ecorePackage.getEBoolean(),
                "hasStereotypeApplications", 1, 1, IS_UNIQUE, IS_ORDERED);

        this.addEOperation(this.stereotypableElementEClass, theEMFProfilePackage.getStereotype(),
                "getApplicableStereotypes", 0, -1, IS_UNIQUE, !IS_ORDERED);

        op = this.addEOperation(this.stereotypableElementEClass, theEMFProfilePackage.getStereotype(),
                "getApplicableStereotypes", 0, -1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, theEMFProfilePackage.getProfile(), "profile", 1, 1, IS_UNIQUE, IS_ORDERED);

        op = this.addEOperation(this.stereotypableElementEClass, theEMFProfilePackage.getStereotype(),
                "getApplicableStereotypes", 0, -1, IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEString(), "stereotypeName", 1, 1, IS_UNIQUE, IS_ORDERED);

        this.addEOperation(this.stereotypableElementEClass, theEMFProfileApplicationPackage.getStereotypeApplication(),
                "getStereotypeApplications", 0, -1, IS_UNIQUE, !IS_ORDERED);

        op = this.addEOperation(this.stereotypableElementEClass,
                theEMFProfileApplicationPackage.getStereotypeApplication(), "getStereotypeApplications", 0, -1,
                IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, theEMFProfilePackage.getProfile(), "profile", 1, 1, IS_UNIQUE, IS_ORDERED);

        op = this.addEOperation(this.stereotypableElementEClass,
                theEMFProfileApplicationPackage.getStereotypeApplication(), "getStereotypeApplications", 0, -1,
                IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEString(), "stereotypeName", 1, 1, IS_UNIQUE, IS_ORDERED);

        op = this.addEOperation(this.stereotypableElementEClass,
                theEMFProfileApplicationPackage.getStereotypeApplication(), "getStereotypeApplication", 1, 1,
                IS_UNIQUE, !IS_ORDERED);
        this.addEParameter(op, theEMFProfilePackage.getStereotype(), "stereotype", 1, 1, IS_UNIQUE, IS_ORDERED);

        this.addEOperation(this.stereotypableElementEClass, theEMFProfilePackage.getStereotype(),
                "getAppliedStereotypes", 0, -1, IS_UNIQUE, !IS_ORDERED);

        op = this
                .addEOperation(this.stereotypableElementEClass, null, "unapplyStereotype", 0, 1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, theEMFProfilePackage.getStereotype(), "stereotype", 1, 1, IS_UNIQUE, IS_ORDERED);

        op = this
                .addEOperation(this.stereotypableElementEClass, null, "unapplyStereotype", 0, 1, IS_UNIQUE, IS_ORDERED);
        this.addEParameter(op, this.ecorePackage.getEString(), "stereotypeName", 1, 1, IS_UNIQUE, IS_ORDERED);

        // Create resource
        this.createResource(eNS_URI);
    }

} // MdsdprofilesPackageImpl
