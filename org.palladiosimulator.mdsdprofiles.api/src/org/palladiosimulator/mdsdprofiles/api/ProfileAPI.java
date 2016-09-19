package org.palladiosimulator.mdsdprofiles.api;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
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
import org.palladiosimulator.mdsdprofiles.notifier.MDSDProfilesNotifier;

/**
 * API to apply, update, query, and unapply profiles in a resource.
 * 
 * @author Sebastian Lehrig, Steffen Becker
 */
public final class ProfileAPI {

    private ProfileAPI() {
    }

    public static void applyProfile(final Resource profileApplicationStore, final Profile profile) {
        final ProfileApplication profileApplication = ensureProfileApplicationExists(profileApplicationStore);

        for (final ProfileImport profileImport : profileApplication.getImportedProfiles()) {
            if (isImportedProfile(profile, profileImport)) {
                throw new RuntimeException("ApplyProfile failed: Profile \"" + profile.getName() + "\" ["
                        + profile.getNsURI() + "] already applied");
            }
        }

        final ProfileImport newProfileImport = EMFProfileApplicationFactory.eINSTANCE.createProfileImport();
        newProfileImport.setProfile(profile);
        newProfileImport.setNsURI(profile.getNsURI());
        newProfileImport.setLocation(profile.eResource().getURI().toString());
        profileApplication.getImportedProfiles().add(newProfileImport);

        profileApplicationStore.eNotify(
                new MDSDProfilesNotifier(profileApplicationStore, MDSDProfilesNotifier.APPLY_PROFILE, profile));
    }

    public static void applyProfile(final Resource profileApplicationStore, final String profileName) {
        final List<Profile> foundProfiles = new LinkedList<>();

        for (final Profile profile : getApplicableProfiles()) {
            if (profile.getName().equals(profileName)) {
                foundProfiles.add(profile);
            }
        }

        if (foundProfiles.size() == 0) {
            throw new RuntimeException(
                    "ApplyProfile based on name failed: Name \"" + profileName + "\" not found in profile registry!");
        }
        if (foundProfiles.size() > 1) {
            throw new RuntimeException("ApplyProfile based on name failed: Name \"" + profileName
                    + "\" is not unique in profile registry!");
        }

        ProfileAPI.applyProfile(profileApplicationStore, foundProfiles.get(0));
    }

    public static boolean updateProfileApplications(final Resource profileApplicationStore,
            final EList<Profile> profilesToBeApplied) {
        final List<Profile> appliedProfiles = getAppliedProfiles(profileApplicationStore);
        final List<Profile> unchanged = new LinkedList<>();
        final List<Profile> additions = new LinkedList<>();

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
                unapplyProfile(profileApplicationStore, profile);
                changed = true;
            }
        }

        // added profiles
        for (final Profile profile : additions) {
            applyProfile(profileApplicationStore, profile);
            changed = true;
        }

        return changed;
    }

    public static boolean isProfileApplied(final Resource profileApplicationStore, final Profile profile) {
        if (!hasProfileApplication(profileApplicationStore)) {
            return false;
        }

        for (final Profile appliedProfile : getAppliedProfiles(profileApplicationStore)) {
            if (profile.getName().equals(appliedProfile.getName())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isProfileApplied(final Resource profileApplicationStore, final String profileName) {
        if (!hasProfileApplication(profileApplicationStore)) {
            return false;
        }

        final List<Profile> foundProfiles = new LinkedList<>();

        for (final Profile profile : getAppliedProfiles(profileApplicationStore)) {
            if (profile.getName().equals(profileName)) {
                foundProfiles.add(profile);
            }
        }

        if (foundProfiles.size() == 0) {
            return false;
        }

        if (foundProfiles.size() == 1) {
            return true;
        }

        throw new RuntimeException(
                "ApplyProfile based on name failed: Name \"" + profileName + "\" is not unique in profile registry!");
    }

    public static boolean hasProfileApplication(final Resource profileApplicationStore) {
        return queryProfileApplication(profileApplicationStore) != null;
    }

    public static ProfileApplication getProfileApplication(final Resource profileApplicationStore) {
        final ProfileApplication profileApplication = queryProfileApplication(profileApplicationStore);

        if (profileApplication == null) {
            throw new RuntimeException("GetProfileApplication failed: Resource with URI \""
                    + profileApplicationStore.getURI() + "\" has no profile application!");
        }

        for (final ProfileImport profileImport : profileApplication.getImportedProfiles()) {
            if (profileImport.getProfile() == null) {
                ProfileImportResolver.resolve(profileImport);
            }
        }

        return profileApplication;
    }

    public static EList<Profile> getApplicableProfiles() {
        final EList<Profile> applicableProfiles = new BasicEList<>();

        applicableProfiles.addAll(IProfileRegistry.eINSTANCE.getRegisteredProfiles());

        return applicableProfiles;
    }

    public static EList<Profile> getAppliedProfiles(final Resource profileApplicationStore) {
        final EList<Profile> appliedProfiles = new BasicEList<>();

        if (hasProfileApplication(profileApplicationStore)) {
            for (final ProfileImport profileImport : getProfileApplication(profileApplicationStore)
                    .getImportedProfiles()) {
                appliedProfiles.add(profileImport.getProfile());
            }
        }

        return appliedProfiles;
    }

    public static void unapplyProfile(final Resource profileApplicationStore, final Profile profile) {
        final EList<ProfileImport> profileImports = getProfileApplication(profileApplicationStore)
                .getImportedProfiles();

        ProfileImport profileImport = null;
        for (final ProfileImport profImp : profileImports) {
            if (isImportedProfile(profile, profImp)) {
                unapplyAllStereotypeApplications(profileApplicationStore, profile);
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
            removeProfileApplication(profileApplicationStore);
        }

        profileApplicationStore.eNotify(
                new MDSDProfilesNotifier(profileApplicationStore, MDSDProfilesNotifier.UNAPPLY_PROFILE, profile));
    }

    private static ProfileApplication ensureProfileApplicationExists(final Resource profileApplicationStore) {
        final ProfileApplication profileApplication = queryProfileApplication(profileApplicationStore);

        if (profileApplication != null) {
            return profileApplication;
        }

        final ProfileApplication newProfileApplication = EMFProfileApplicationFactory.eINSTANCE
                .createProfileApplication();
        profileApplicationStore.getContents().add(newProfileApplication);

        return newProfileApplication;
    }

    private static ProfileApplication queryProfileApplication(final Resource profileApplicationStore) {
        ProfileApplication profileApplication = null;

        for (final EObject eObject : profileApplicationStore.getContents()) {
            if (eObject.eClass() == EMFProfileApplicationPackage.eINSTANCE.getProfileApplication()) {
                profileApplication = (ProfileApplication) eObject;
                break;
            }
        }

        return profileApplication;
    }

    private static boolean isImportedProfile(final Profile profile, final ProfileImport profileImport) {
        return profileImport.getNsURI().equals(profile.getNsURI());
    }

    /**
     * Unapplies all stereotype applications for a given profile.
     *
     * @param profile
     *            The profile to be unapplied.
     */
    private static void unapplyAllStereotypeApplications(final Resource profileApplicationStore,
            final Profile profile) {
        final List<StereotypeApplication> stereotypeApplications = new LinkedList<>();
        stereotypeApplications.addAll(getProfileApplication(profileApplicationStore).getStereotypeApplications());

        for (final StereotypeApplication stereotypeApplication : stereotypeApplications) {
            if (stereotypeApplication.getStereotype().getProfile().getNsURI().equals(profile.getNsURI())) {
                StereotypeAPI.unapplyStereotype(stereotypeApplication.getAppliedTo(),
                        stereotypeApplication.getStereotype());
            }
        }
    }

    private static void removeProfileApplication(final Resource profileApplicationStore) {
        profileApplicationStore.getContents().remove(getProfileApplication(profileApplicationStore));
    }

}
