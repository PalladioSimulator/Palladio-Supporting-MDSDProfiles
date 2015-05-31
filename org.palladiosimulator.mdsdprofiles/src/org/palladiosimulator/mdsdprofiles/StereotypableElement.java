/**
 */
package org.palladiosimulator.mdsdprofiles;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EModelElement;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Stereotypable Element</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.palladiosimulator.mdsdprofiles.StereotypableElement#getProfileableElement <em>
 * Profileable Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.palladiosimulator.mdsdprofiles.MdsdprofilesPackage#getStereotypableElement()
 * @model abstract="true"
 * @generated
 */
public interface StereotypableElement extends EModelElement {
    /**
     * Returns the value of the '<em><b>Profileable Element</b></em>' reference. <!-- begin-user-doc
     * -->
     * <p>
     * If the meaning of the '<em>Profileable Element</em>' reference isn't clear, there really
     * should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Profileable Element</em>' reference.
     * @see org.palladiosimulator.mdsdprofiles.MdsdprofilesPackage#getStereotypableElement_ProfileableElement()
     * @model transient="true" changeable="false" volatile="true" derived="true"
     * @generated
     */
    ProfileableElement getProfileableElement();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model stereotypeRequired="true"
     * @generated
     */
    void applyStereotype(Stereotype stereotype);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model stereotypeNameRequired="true"
     * @generated
     */
    void applyStereotype(String stereotypeName);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model stereotypesToBeAppliedMany="true" stereotypesToBeAppliedOrdered="false"
     * @generated
     */
    boolean updateStereotypeApplications(EList<Stereotype> stereotypesToBeApplied);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" stereotypeRequired="true"
     * @generated
     */
    boolean isStereotypeApplicable(Stereotype stereotype);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" stereotypeNameRequired="true"
     * @generated
     */
    boolean isStereotypeApplicable(String stereotypeName);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" stereotypeRequired="true"
     * @generated
     */
    boolean isStereotypeApplied(Stereotype stereotype);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" stereotypeNameRequired="true"
     * @generated
     */
    boolean isStereotypeApplied(String stereotypeName);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true"
     * @generated
     */
    boolean hasStereotypeApplications();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model kind="operation" ordered="false"
     * @generated
     */
    EList<Stereotype> getApplicableStereotypes();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" profileRequired="true"
     * @generated
     */
    EList<Stereotype> getApplicableStereotypes(Profile profile);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" stereotypeNameRequired="true"
     * @generated
     */
    EList<Stereotype> getApplicableStereotypes(String stereotypeName);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model kind="operation" ordered="false"
     * @generated
     */
    EList<StereotypeApplication> getStereotypeApplications();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" profileRequired="true"
     * @generated
     */
    EList<StereotypeApplication> getStereotypeApplications(Profile profile);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model ordered="false" stereotypeNameRequired="true"
     * @generated
     */
    EList<StereotypeApplication> getStereotypeApplications(String stereotypeName);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true" ordered="false" stereotypeRequired="true"
     * @generated
     */
    StereotypeApplication getStereotypeApplication(Stereotype stereotype);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model kind="operation" ordered="false"
     * @generated
     */
    EList<Stereotype> getAppliedStereotypes();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model stereotypeRequired="true"
     * @generated
     */
    void unapplyStereotype(Stereotype stereotype);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model stereotypeNameRequired="true"
     * @generated
     */
    void unapplyStereotype(String stereotypeName);

} // StereotypableElement
