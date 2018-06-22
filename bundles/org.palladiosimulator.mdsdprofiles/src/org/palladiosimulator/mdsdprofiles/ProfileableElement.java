/**
 */
package org.palladiosimulator.mdsdprofiles;

import org.eclipse.emf.common.util.EList;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofileapplication.ProfileApplication;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Profileable Element</b></em>
 * '. <!-- end-user-doc -->
 *
 *
 * @see org.palladiosimulator.mdsdprofiles.MdsdprofilesPackage#getProfileableElement()
 * @model
 * @generated
 */
public interface ProfileableElement extends StereotypableElement {

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model profileRequired="true"
     * @generated
     */
    void applyProfile(Profile profile);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model profilesToBeAppliedMany="true" profilesToBeAppliedOrdered="false"
     * @generated
     */
    boolean updateProfileApplications(EList<Profile> profilesToBeApplied);

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model required="true"
     * @generated
     */
    boolean hasProfileApplication();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model kind="operation" required="true"
     * @generated
     */
    ProfileApplication getProfileApplication();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model kind="operation" ordered="false"
     * @generated
     */
    EList<Profile> getApplicableProfiles();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model kind="operation" ordered="false"
     * @generated
     */
    EList<Profile> getAppliedProfiles();

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @model profileRequired="true"
     * @generated
     */
    void unapplyProfile(Profile profile);

} // ProfileableElement
