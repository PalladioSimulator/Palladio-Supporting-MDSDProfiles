/**
 */
package org.palladiosimulator.mdsdprofiles.impl;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.registry.IProfileRegistry;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationFactory;
import org.modelversioning.emfprofileapplication.ProfileApplication;
import org.modelversioning.emfprofileapplication.ProfileImport;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.modelversioning.emfprofileapplication.util.ProfileImportResolver;
import org.palladiosimulator.commons.emfutils.EMFLoadHelper;
import org.palladiosimulator.mdsdprofiles.MdsdprofilesPackage;
import org.palladiosimulator.mdsdprofiles.ProfileableElement;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;

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

    private static final String PROFILE_APPLICATION_URI = "profileApplicationURI";
    private static final String HTTP_PALLADIOSIMULATOR_ORG_MDSD_PROFILES_PROFILE_APPLICATION_1_0 = "http://palladiosimulator.org/MDSDProfiles/ProfileApplication/1.0";

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
        final EAnnotation profileApplicationAnnotation = this.ensureProfileAnnotationExists();

        if (profileApplicationAnnotation.getDetails().get(PROFILE_APPLICATION_URI) == null) {
            final ProfileApplication newProfileApplication = EMFProfileApplicationFactory.eINSTANCE
                    .createProfileApplication();

            this.getProfileApplicationResource().getContents().add(newProfileApplication);

            // FIXME use relative URI instead of getResourceURI(...) result [Lehrig]
            profileApplicationAnnotation.getDetails().put(PROFILE_APPLICATION_URI,
                    EMFLoadHelper.getResourceURI(newProfileApplication));
        }

        return this.getProfileApplication();
    }

    private Resource getProfileApplicationResource() {
        return this.eResource();
    }

    private EAnnotation ensureProfileAnnotationExists() {
        if (!this.hasProfileApplication()) {
            final EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
            eAnnotation.setSource(HTTP_PALLADIOSIMULATOR_ORG_MDSD_PROFILES_PROFILE_APPLICATION_1_0);
            this.getEAnnotations().add(eAnnotation);
        }
        return this.getProfileApplicationAnnotation();
    }

    private EAnnotation getProfileApplicationAnnotation() {
        return this.getEAnnotation(HTTP_PALLADIOSIMULATOR_ORG_MDSD_PROFILES_PROFILE_APPLICATION_1_0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean hasProfileApplication() {
        return this.getProfileApplicationAnnotation() != null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public ProfileApplication getProfileApplication() {
        if (!this.hasProfileApplication()) {
            throw new RuntimeException("GetProfileApplication failed: Element of type \"" + this.getClass().getName()
                    + "\" has no profile application!");
        }

        final EAnnotation profileApplicationAnnotation = this.getProfileApplicationAnnotation();
        final String profileApplicationURI = profileApplicationAnnotation.getDetails().get(PROFILE_APPLICATION_URI);
        final ProfileApplication profileApplication = (ProfileApplication) EMFLoadHelper.loadAndResolveEObject(this
                .getProfileApplicationResource().getResourceSet(), profileApplicationURI);

        for (final ProfileImport profileImport : profileApplication.getImportedProfiles()) {
            if (profileImport.getProfile() == null) {
                ProfileImportResolver.resolve(profileImport);
            }
        }

        return profileApplication;
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
        this.getEAnnotations().remove(this.getProfileApplicationAnnotation());
    }
} // ProfileableElementImpl
