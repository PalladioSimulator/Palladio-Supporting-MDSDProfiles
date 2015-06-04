package org.palladiosimulator.mdsdprofiles.api;

import java.awt.print.Book;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;
import org.modelversioning.emfprofileapplication.ProfileApplication;
import org.modelversioning.emfprofileapplication.ProfileImport;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.palladiosimulator.commons.emfutils.EMFLoadHelper;
import org.palladiosimulator.mdsdprofiles.notifier.MDSDProfilesNotifier;

/**
 * API to apply, update, query, and unapply stereotypes for an EObject.
 * 
 * @author Sebastian Lehrig, Steffen Becker
 */
public class StereotypeAPI {

    /**
     * This cross referencer finds, for a given stereotypable element, its stereotype applications.
     *
     * @see Book "Eclipse Modeling Framework: A Developer's Guide" chapter
     *      "13.5.3 Using Cross Referencers"
     *
     * @author Sebastian Lehrig
     */
    private static final class StereotypeApplicationCrossReferencer extends EcoreUtil.UsageCrossReferencer {

        private static final long serialVersionUID = -5714219655560791971L;
        private final EObject referencedObject;

        private StereotypeApplicationCrossReferencer(final Resource resource, final EObject referencedObject) {
            super(resource);

            this.referencedObject = referencedObject;
        }

        @Override
        protected boolean crossReference(final EObject eObject, final EReference eReference,
                final EObject crossReferencedObject) {
            return this.referencedObject == crossReferencedObject
                    && eReference == EMFProfileApplicationPackage.eINSTANCE.getStereotypeApplication_AppliedTo();
        }

        @Override
        protected boolean containment(final EObject eObject) {
            return (eObject.eClass() == EMFProfileApplicationPackage.eINSTANCE.getProfileApplication())
                    || (EMFProfileApplicationPackage.eINSTANCE.getStereotypeApplication().isSuperTypeOf(eObject
                            .eClass()));
        }

        @Override
        public Collection<Setting> findUsage(final EObject eObject) {
            return super.findUsage(eObject);
        }

        public EList<StereotypeApplication> findStereotypeApplications() {
            final EList<StereotypeApplication> stereotypeApplications = new BasicEList<StereotypeApplication>();

            for (final Setting setting : this.findUsage(this.referencedObject)) {
                stereotypeApplications.add((StereotypeApplication) setting.getEObject());
            }

            return stereotypeApplications;
        }
    }

    public static void applyStereotype(final EObject stereotypedElement, final Stereotype stereotype) {
        if (!ProfileAPI.hasProfileApplication(stereotypedElement.eResource())) {
            throw new RuntimeException("ApplyStereotype failed: No profile application found!");
        }
        if (!isStereotypeApplicable(stereotypedElement, stereotype)) {
            throw new RuntimeException("ApplyStereotype failed: \"" + stereotype.getName()
                    + "\" is not applicable to \"" + stereotypedElement.getClass().getName() + "\"!");
        }
        if (isStereotypeApplied(stereotypedElement, stereotype)) {
            throw new RuntimeException("ApplyStereotype failed: \"" + stereotype.getName() + "\" already applied to \""
                    + stereotypedElement.getClass().getName() + "\"!");
        }

        final ProfileApplication profileApplication = getProfileApplication(stereotypedElement);

        final StereotypeApplication newStereotypeApplication = (StereotypeApplication) stereotype.getEPackage()
                .getEFactoryInstance().create(stereotype);

        newStereotypeApplication.setAppliedTo(stereotypedElement);
        newStereotypeApplication.setExtension(stereotype.getApplicableExtensions(stereotypedElement).get(0));

        profileApplication.getStereotypeApplications().add(newStereotypeApplication);

        stereotypedElement.eNotify(new MDSDProfilesNotifier(stereotypedElement, MDSDProfilesNotifier.APPLY_STEREOTYPE,
                stereotype));
    }

    public static void applyStereotype(final EObject stereotypedElement, final String stereotypeName) {
        if (getApplicableStereotypes(stereotypedElement, stereotypeName).size() != 1) {
            throw new RuntimeException("ApplyStereotype based on name failed: name \"" + stereotypeName
                    + "\" not (uniquely) found!");
        }

        applyStereotype(stereotypedElement, getApplicableStereotypes(stereotypedElement, stereotypeName).get(0));
    }

    public static boolean updateStereotypeApplications(final EObject stereotypedElement,
            final EList<Stereotype> stereotypesToBeApplied) {
        final List<Stereotype> appliedStereotypes = getAppliedStereotypes(stereotypedElement);
        final List<Stereotype> unchanged = new LinkedList<Stereotype>();
        final List<Stereotype> additions = new LinkedList<Stereotype>();

        for (final Stereotype stereotype : stereotypesToBeApplied) {
            if (appliedStereotypes.contains(stereotype)) {
                unchanged.add(stereotype);
            } else {
                additions.add(stereotype);
            }
        }

        boolean changed = false;
        // removed stereotypes
        for (final Stereotype stereotype : appliedStereotypes) {
            if (!unchanged.contains(stereotype)) {
                unapplyStereotype(stereotypedElement, stereotype);
                changed = true;
            }
        }

        // added stereotypes
        for (final Stereotype stereotype : additions) {
            applyStereotype(stereotypedElement, stereotype);
            changed = true;
        }

        return changed;
    }

    public static boolean isStereotypeApplicable(final EObject stereotypedElement, final Stereotype stereotype) {
        if (isStereotypeApplied(stereotypedElement, stereotype)) {
            return false;
        }

        for (final ProfileImport profileImport : getProfileImports(stereotypedElement)) {
            for (final Stereotype applicableStereotype : profileImport.getProfile().getApplicableStereotypes(
                    stereotypedElement.eClass())) {
                if (EMFLoadHelper.getResourceURI(applicableStereotype).equals(EMFLoadHelper.getResourceURI(stereotype))) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isStereotypeApplicable(final EObject stereotypedElement, final String stereotypeName) {
        if (getApplicableStereotypes(stereotypedElement, stereotypeName).size() != 1) {
            throw new RuntimeException("ApplyStereotype based on name failed: name \"" + stereotypeName
                    + "\" not (uniquely) found!");
        }

        return isStereotypeApplicable(stereotypedElement, getApplicableStereotypes(stereotypedElement, stereotypeName)
                .get(0));
    }

    public static boolean isStereotypeApplied(final EObject stereotypedElement, final Stereotype stereotype) {

        // FXIME not nice
        try {
            getStereotypeApplication(stereotypedElement, stereotype);
        } catch (final RuntimeException e) {
            return false;
        }
        return true;
    }

    public static boolean isStereotypeApplied(final EObject stereotypedElement, final String stereotype) {
        return !getStereotypeApplications(stereotypedElement, stereotype).isEmpty();
    }

    public static boolean hasStereotypeApplications(final EObject stereotypedElement) {
        return !new StereotypeApplicationCrossReferencer(stereotypedElement.eResource(), stereotypedElement)
                .findStereotypeApplications().isEmpty();
    }

    public static EList<Stereotype> getApplicableStereotypes(final EObject stereotypedElement) {
        final EList<Stereotype> applicableStereotypes = new BasicEList<Stereotype>();

        for (final ProfileImport profileImport : getProfileImports(stereotypedElement)) {
            applicableStereotypes.addAll(profileImport.getProfile().getApplicableStereotypes(
                    stereotypedElement.eClass()));
        }

        return applicableStereotypes;
    }

    public static EList<Stereotype> getApplicableStereotypes(final EObject stereotypedElement, final Profile profile) {
        final EList<Stereotype> filteredStereotypes = new BasicEList<Stereotype>();

        for (final Stereotype stereotype : getApplicableStereotypes(stereotypedElement)) {
            if (stereotype.getProfile().getNsURI().equals(profile.getNsURI())) {
                filteredStereotypes.add(stereotype);
            }
        }

        return filteredStereotypes;
    }

    public static EList<Stereotype> getApplicableStereotypes(final EObject stereotypedElement,
            final String stereotypeName) {
        final EList<Stereotype> filteredStereotypes = new BasicEList<Stereotype>();

        for (final Stereotype stereotype : getApplicableStereotypes(stereotypedElement)) {
            if (stereotype.getName().equals(stereotypeName)) {
                filteredStereotypes.add(stereotype);
            }
        }

        return filteredStereotypes;
    }

    public static EList<StereotypeApplication> getStereotypeApplications(final EObject stereotypedElement) {
        return new StereotypeApplicationCrossReferencer(stereotypedElement.eResource(), stereotypedElement)
                .findStereotypeApplications();
    }

    public static EList<StereotypeApplication> getStereotypeApplications(final EObject stereotypedElement,
            final Profile profile) {
        final EList<StereotypeApplication> filteredStereotypeApplications = new BasicEList<StereotypeApplication>();

        for (final StereotypeApplication stereotypeApplication : getStereotypeApplications(stereotypedElement)) {
            if (stereotypeApplication.getExtension().getSource().getProfile().getNsURI().equals(profile.getNsURI())) {
                filteredStereotypeApplications.add(stereotypeApplication);
            }
        }

        return filteredStereotypeApplications;
    }

    public static EList<StereotypeApplication> getStereotypeApplications(final EObject stereotypedElement,
            final String stereotype) {
        final EList<StereotypeApplication> filteredStereotypeApplications = new BasicEList<StereotypeApplication>();

        for (final StereotypeApplication stereotypeApplication : getStereotypeApplications(stereotypedElement)) {
            if (stereotypeApplication.getExtension().getSource().getName().equals(stereotype)) {
                filteredStereotypeApplications.add(stereotypeApplication);
            }
        }

        return filteredStereotypeApplications;
    }

    public static StereotypeApplication getStereotypeApplication(final EObject stereotypedElement,
            final Stereotype stereotype) {
        for (final StereotypeApplication stereotypeApplication : getStereotypeApplications(stereotypedElement)) {
            if (EMFLoadHelper.getResourceURI(stereotypeApplication.getExtension().getSource()).equals(
                    EMFLoadHelper.getResourceURI(stereotype))) {
                return stereotypeApplication;
            }
        }

        throw new RuntimeException("GetStereotypeApplication failed: Stereotype \"" + stereotype.getName()
                + "\" has not been applied to \"" + stereotypedElement.getClass().getName() + "\"!");
    }

    public static EList<Stereotype> getAppliedStereotypes(final EObject stereotypedElement) {
        final EList<Stereotype> appliedStereotypes = new BasicEList<Stereotype>();

        for (final StereotypeApplication stereotypeApplication : getStereotypeApplications(stereotypedElement)) {
            appliedStereotypes.add(stereotypeApplication.getExtension().getSource());
        }

        return appliedStereotypes;
    }

    public static void unapplyStereotype(final EObject stereotypedElement, final Stereotype stereotype) {
        if (!isStereotypeApplied(stereotypedElement, stereotype)) {
            throw new RuntimeException("UnapplyStereotype failed: No application found for Stereotype \""
                    + stereotype.getName() + "\"!");
        }

        final StereotypeApplication stereotypeApplication = getStereotypeApplication(stereotypedElement, stereotype);
        stereotypeApplication.getProfileApplication().getStereotypeApplications().remove(stereotypeApplication);

        stereotypedElement.eNotify(new MDSDProfilesNotifier(stereotypedElement,
                MDSDProfilesNotifier.UNAPPLY_STEREOTYPE, stereotype));
    }

    public static void unapplyStereotype(final EObject stereotypedElement, final String stereotypeName) {
        final List<StereotypeApplication> stereotypeApplications = getStereotypeApplications(stereotypedElement,
                stereotypeName);
        if (stereotypeApplications.size() != 1) {
            throw new RuntimeException("UnapplyStereotype failed: Stereotype identification by name found "
                    + stereotypeApplications.size() + " fitting stereotypes!");
        }

        unapplyStereotype(stereotypedElement, stereotypeApplications.get(0).getExtension().getSource());
    }

    private static ProfileApplication getProfileApplication(final EObject stereotypedElement) {
        return ProfileAPI.getProfileApplication(stereotypedElement.eResource());
    }

    private static EList<ProfileImport> getProfileImports(final EObject stereotypedElement) {
        return getProfileApplication(stereotypedElement).getImportedProfiles();
    }
}
