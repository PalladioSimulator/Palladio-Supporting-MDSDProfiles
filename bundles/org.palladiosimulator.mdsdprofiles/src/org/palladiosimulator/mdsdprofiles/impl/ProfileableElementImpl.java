/**
 */
package org.palladiosimulator.mdsdprofiles.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofileapplication.ProfileApplication;
import org.palladiosimulator.mdsdprofiles.MdsdprofilesPackage;
import org.palladiosimulator.mdsdprofiles.ProfileableElement;
import org.palladiosimulator.mdsdprofiles.api.ProfileAPI;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Profileable Element</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class ProfileableElementImpl extends StereotypableElementImpl implements ProfileableElement {

    private Resource getProfileApplicationStore() {
        return this.eResource();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ProfileableElementImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return MdsdprofilesPackage.Literals.PROFILEABLE_ELEMENT;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void applyProfile(final Profile profile) {
        ProfileAPI.applyProfile(this.getProfileApplicationStore(), profile);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean updateProfileApplications(final EList<Profile> profilesToBeApplied) {
        return ProfileAPI.updateProfileApplications(this.getProfileApplicationStore(), profilesToBeApplied);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean hasProfileApplication() {
        return ProfileAPI.hasProfileApplication(this.getProfileApplicationStore());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public ProfileApplication getProfileApplication() {
        return ProfileAPI.getProfileApplication(this.getProfileApplicationStore());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Profile> getApplicableProfiles() {
        return ProfileAPI.getApplicableProfiles();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Profile> getAppliedProfiles() {
        return ProfileAPI.getAppliedProfiles(this.getProfileApplicationStore());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void unapplyProfile(final Profile profile) {
        ProfileAPI.unapplyProfile(this.getProfileApplicationStore(), profile);
    }

} // ProfileableElementImpl
