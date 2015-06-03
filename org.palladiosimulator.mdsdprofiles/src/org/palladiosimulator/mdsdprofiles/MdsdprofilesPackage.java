/**
 */
package org.palladiosimulator.mdsdprofiles;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta
 * objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 *
 * @see org.palladiosimulator.mdsdprofiles.MdsdprofilesFactory
 * @model kind="package"
 * @generated
 */
public interface MdsdprofilesPackage extends EPackage {

    /**
     * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    String eNAME = "mdsdprofiles";

    /**
     * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    String eNS_URI = "http://palladiosimulator.org/MDSDProfiles/1.0";

    /**
     * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    String eNS_PREFIX = "org.palladiosimulator";

    /**
     * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    MdsdprofilesPackage eINSTANCE = org.palladiosimulator.mdsdprofiles.impl.MdsdprofilesPackageImpl.init();

    /**
     * The meta object id for the '
     * {@link org.palladiosimulator.mdsdprofiles.impl.ProfileableElementImpl
     * <em>Profileable Element</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.palladiosimulator.mdsdprofiles.impl.ProfileableElementImpl
     * @see org.palladiosimulator.mdsdprofiles.impl.MdsdprofilesPackageImpl#getProfileableElement()
     * @generated
     */
    int PROFILEABLE_ELEMENT = 0;

    /**
     * The meta object id for the '
     * {@link org.palladiosimulator.mdsdprofiles.impl.StereotypableElementImpl
     * <em>Stereotypable Element</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.palladiosimulator.mdsdprofiles.impl.StereotypableElementImpl
     * @see org.palladiosimulator.mdsdprofiles.impl.MdsdprofilesPackageImpl#getStereotypableElement()
     * @generated
     */
    int STEREOTYPABLE_ELEMENT = 1;

    /**
     * The feature id for the '<em><b>Profileable Element</b></em>' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int STEREOTYPABLE_ELEMENT__PROFILEABLE_ELEMENT = 0;

    /**
     * The number of structural features of the '<em>Stereotypable Element</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int STEREOTYPABLE_ELEMENT_FEATURE_COUNT = 1;

    /**
     * The feature id for the '<em><b>Profileable Element</b></em>' reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROFILEABLE_ELEMENT__PROFILEABLE_ELEMENT = STEREOTYPABLE_ELEMENT__PROFILEABLE_ELEMENT;

    /**
     * The number of structural features of the '<em>Profileable Element</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROFILEABLE_ELEMENT_FEATURE_COUNT = STEREOTYPABLE_ELEMENT_FEATURE_COUNT + 0;

    /**
     * Returns the meta object for class '
     * {@link org.palladiosimulator.mdsdprofiles.ProfileableElement <em>Profileable Element</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Profileable Element</em>'.
     * @see org.palladiosimulator.mdsdprofiles.ProfileableElement
     * @generated
     */
    EClass getProfileableElement();

    /**
     * Returns the meta object for class '
     * {@link org.palladiosimulator.mdsdprofiles.StereotypableElement
     * <em>Stereotypable Element</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Stereotypable Element</em>'.
     * @see org.palladiosimulator.mdsdprofiles.StereotypableElement
     * @generated
     */
    EClass getStereotypableElement();

    /**
     * Returns the meta object for the reference '
     * {@link org.palladiosimulator.mdsdprofiles.StereotypableElement#getProfileableElement
     * <em>Profileable Element</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the reference '<em>Profileable Element</em>'.
     * @see org.palladiosimulator.mdsdprofiles.StereotypableElement#getProfileableElement()
     * @see #getStereotypableElement()
     * @generated
     */
    EReference getStereotypableElement_ProfileableElement();

    /**
     * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the factory that creates the instances of the model.
     * @generated
     */
    MdsdprofilesFactory getMdsdprofilesFactory();

    /**
     * <!-- begin-user-doc --> Defines literals for the meta objects that represent
     * <ul>
     * <li>each class,</li>
     * <li>each feature of each class,</li>
     * <li>each enum,</li>
     * <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     *
     * @generated
     */
    interface Literals {

        /**
         * The meta object literal for the '
         * {@link org.palladiosimulator.mdsdprofiles.impl.ProfileableElementImpl
         * <em>Profileable Element</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         *
         * @see org.palladiosimulator.mdsdprofiles.impl.ProfileableElementImpl
         * @see org.palladiosimulator.mdsdprofiles.impl.MdsdprofilesPackageImpl#getProfileableElement()
         * @generated
         */
        EClass PROFILEABLE_ELEMENT = eINSTANCE.getProfileableElement();

        /**
         * The meta object literal for the '
         * {@link org.palladiosimulator.mdsdprofiles.impl.StereotypableElementImpl
         * <em>Stereotypable Element</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
         *
         * @see org.palladiosimulator.mdsdprofiles.impl.StereotypableElementImpl
         * @see org.palladiosimulator.mdsdprofiles.impl.MdsdprofilesPackageImpl#getStereotypableElement()
         * @generated
         */
        EClass STEREOTYPABLE_ELEMENT = eINSTANCE.getStereotypableElement();

        /**
         * The meta object literal for the '<em><b>Profileable Element</b></em>' reference feature.
         * <!-- begin-user-doc --> <!-- end-user-doc -->
         *
         * @generated
         */
        EReference STEREOTYPABLE_ELEMENT__PROFILEABLE_ELEMENT = eINSTANCE.getStereotypableElement_ProfileableElement();

    }

} // MdsdprofilesPackage
