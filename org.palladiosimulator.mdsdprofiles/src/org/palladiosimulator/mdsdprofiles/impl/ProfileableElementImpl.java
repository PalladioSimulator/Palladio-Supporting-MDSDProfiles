/**
 */
package org.palladiosimulator.mdsdprofiles.impl;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.registry.IProfileRegistry;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationFactory;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;
import org.modelversioning.emfprofileapplication.ProfileApplication;
import org.modelversioning.emfprofileapplication.ProfileImport;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.modelversioning.emfprofileapplication.util.ProfileImportResolver;
import org.palladiosimulator.commons.emfutils.EMFLoadHelper;
import org.palladiosimulator.mdsdprofiles.MdsdprofilesPackage;
import org.palladiosimulator.mdsdprofiles.ProfileableElement;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;
import org.palladiosimulator.mdsdprofiles.notifier.MDSDProfilesNotifier;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Profileable Element</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @author Sebastian Lehrig
 * 
 * @generated
 */
public class ProfileableElementImpl extends StereotypableElementImpl implements ProfileableElement {

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
        final ProfileApplication profileApplication = this.ensureProfileApplicationExists();

        for (final ProfileImport profileImport : profileApplication.getImportedProfiles()) {
            if (this.isImportedProfile(profile, profileImport)) {
                throw new RuntimeException("ApplyProfile failed: Profile \"" + profile.getName() + "\" ["
                        + profile.getNsURI() + "] already applied");
            }
        }

        final ProfileImport newProfileImport = EMFProfileApplicationFactory.eINSTANCE.createProfileImport();
        newProfileImport.setProfile(profile);
        newProfileImport.setNsURI(profile.getNsURI());
        newProfileImport.setLocation(profile.eResource().getURI().toString());
        profileApplication.getImportedProfiles().add(newProfileImport);

        this.eNotify(new MDSDProfilesNotifier(this, MDSDProfilesNotifier.APPLY_PROFILE, profile));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean updateProfileApplications(final EList<Profile> profilesToBeApplied) {
        final List<Profile> appliedProfiles = this.getAppliedProfiles();
        final List<Profile> unchanged = new LinkedList<Profile>();
        final List<Profile> additions = new LinkedList<Profile>();

        for (final Profile profile : profilesToBeApplied) {
            if (appliedProfiles.contains(profile)) {
                unchanged.add(profile);
            } else {
                additions.add(profile);
            }
        }

        boolean changed = false;
        // removed profiles
        for (final Profile profile : appliedProfiles) {
            if (!unchanged.contains(profile)) {
                this.unapplyProfile(profile);
                changed = true;
            }
        }

        // added profiles
        for (final Profile profile : additions) {
            this.applyProfile(profile);
            changed = true;
        }

        return changed;
    }

    private boolean isImportedProfile(final Profile profile, final ProfileImport profileImport) {
        return profileImport.getNsURI().equals(profile.getNsURI());
    }

    private ProfileApplication ensureProfileApplicationExists() {
        final ProfileApplication profileApplication = queryProfileApplication();

        if (profileApplication != null) {
            return profileApplication;
        }

        final ProfileApplication newProfileApplication = EMFProfileApplicationFactory.eINSTANCE
                .createProfileApplication();
        this.getProfileApplicationResource().getContents().add(newProfileApplication);

        return newProfileApplication;
    }

    private Resource getProfileApplicationResource() {
        return this.eResource();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean hasProfileApplication() {
        return queryProfileApplication() != null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public ProfileApplication getProfileApplication() {
        final ProfileApplication profileApplication = queryProfileApplication();

        if (profileApplication == null) {
            throw new RuntimeException("GetProfileApplication failed: Element of type \"" + this.getClass().getName()
                    + "\" has no profile application!");
        }

        for (final ProfileImport profileImport : profileApplication.getImportedProfiles()) {
            if (profileImport.getProfile() == null) {
                ProfileImportResolver.resolve(profileImport);
            }
        }

        return profileApplication;
    }

    private ProfileApplication queryProfileApplication() {
        ProfileApplication profileApplication = null;

        for (final EObject eObject : this.eResource().getContents()) {
            if (eObject.eClass() == EMFProfileApplicationPackage.eINSTANCE.getProfileApplication()) {
                profileApplication = (ProfileApplication) eObject;
                break;
            }
        }
        return profileApplication;
    }

    private ProfileApplication getProfileApplicationForURI(final String profileApplicationURI) {
        final URI objectURI = URI.createURI(profileApplicationURI);

        if (objectURI.isRelative()) {
            return (ProfileApplication) this.eResource().getEObject(objectURI.fragment());
        } else {
            return (ProfileApplication) EMFLoadHelper.loadAndResolveEObject(this.getProfileApplicationResource()
                    .getResourceSet(), objectURI);
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Profile> getApplicableProfiles() {
        final EList<Profile> applicableProfiles = new BasicEList<Profile>();
        applicableProfiles.addAll(IProfileRegistry.eINSTANCE.getRegisteredProfiles());
        return applicableProfiles;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Profile> getAppliedProfiles() {
        final EList<Profile> appliedProfiles = new BasicEList<Profile>();
        if (this.hasProfileApplication()) {
            for (final ProfileImport profileImport : this.getProfileApplication().getImportedProfiles()) {
                appliedProfiles.add(profileImport.getProfile());
            }
        }
        return appliedProfiles;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void unapplyProfile(final Profile profile) {
        final EList<ProfileImport> profileImports = this.getProfileApplication().getImportedProfiles();

        ProfileImport profileImport = null;
        for (final ProfileImport profImp : profileImports) {
            if (this.isImportedProfile(profile, profImp)) {
                this.unapplyAllStereotypeApplications(profile);
                profileImport = profImp;
                break;
            }
        }

        if (profileImport == null) {
            throw new RuntimeException("UnapplyProfile failed: Profile \"" + profile.getName() + "\" ["
                    + profile.getNsURI() + "] not found");
        }

        profileImports.remove(profileImport);

        if (profileImports.size() == 0) {
            this.removeProfileApplication();
        }

        this.eNotify(new MDSDProfilesNotifier(this, MDSDProfilesNotifier.UNAPPLY_PROFILE, profile));
    }

    /**
     * Unapplies all stereotype applications for a given profile.
     *
     * @param profile
     *            The profile to be unapplied.
     */
    private void unapplyAllStereotypeApplications(final Profile profile) {
        final List<StereotypeApplication> stereotypeApplications = new LinkedList<StereotypeApplication>();
        stereotypeApplications.addAll(this.getProfileApplication().getStereotypeApplications());

        for (final StereotypeApplication stereotypeApplication : stereotypeApplications) {
            if (stereotypeApplication.getExtension().getSource().getProfile().getNsURI().equals(profile.getNsURI())) {
                final StereotypableElement stereotypableElement = (StereotypableElement) stereotypeApplication
                        .getAppliedTo();
                stereotypableElement.unapplyStereotype(stereotypeApplication.getExtension().getSource());
            }
        }
    }

    private void removeProfileApplication() {
        this.getProfileApplicationResource().getContents().remove(this.getProfileApplication());
    }
} // ProfileableElementImpl
