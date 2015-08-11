package org.palladiosimulator.mdsdprofiles.api;

import java.awt.print.Book;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
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
 * @author Sebastian Lehrig, Steffen Becker, Max Schettler
 */
public class StereotypeAPI {

    private final static List<Integer> EXLUDED_PARAMETER_FEATURE_IDS = Arrays.asList(
            EMFProfileApplicationPackage.STEREOTYPE_APPLICATION__APPLIED_TO,
            EMFProfileApplicationPackage.STEREOTYPE_APPLICATION__PROFILE_APPLICATION,
            EMFProfileApplicationPackage.STEREOTYPE_APPLICATION__EXTENSION);
    /**
     * This cross referencer finds, for a given stereotypable element, its stereotype applications.
     *
     * @see Book "Eclipse Modeling Framework: A Developer's Guide" chapter
     *      "13.5.3 Using Cross Referencers"
     *
     * @author Sebastian Lehrig
     */
    private static final class StereotypeApplicationCrossReferencer extends EcoreUtil.UsageCrossReferencer {

        private static final EClass STEREOTYPE_APPLICATION_ECLASS = EMFProfileApplicationPackage.eINSTANCE.getStereotypeApplication();
        private static final EClass PROFILE_APPLICATION_ECLASS = EMFProfileApplicationPackage.eINSTANCE.getProfileApplication();
        private static final EReference STEREOTYPE_APPLICATION_APPLIED_TO_EREFERENCE = EMFProfileApplicationPackage.eINSTANCE.getStereotypeApplication_AppliedTo();
        private static final long serialVersionUID = -5714219655560791971L;
        private final EObject referencedObject;

        private StereotypeApplicationCrossReferencer(final EObject referencedObject) {
            super(referencedObject.eResource());

            this.referencedObject = referencedObject;
        }

        @Override
        protected boolean crossReference(final EObject eObject, final EReference eReference,
                final EObject crossReferencedObject) {
            return this.referencedObject == crossReferencedObject
                    && eReference == STEREOTYPE_APPLICATION_APPLIED_TO_EREFERENCE;
        }

        @Override
        protected boolean containment(final EObject eObject) {
            return (eObject.eClass() == PROFILE_APPLICATION_ECLASS)
                    || (STEREOTYPE_APPLICATION_ECLASS.isSuperTypeOf(eObject.eClass()));
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
        final EList<Stereotype> applicableStereotypes = getApplicableStereotypes(stereotypedElement, stereotypeName);
        if (applicableStereotypes.size() != 1) {
            throw new RuntimeException("ApplyStereotype based on name failed: name \"" + stereotypeName
                    + "\" not uniquely found (" + applicableStereotypes.size() + " times)!");
        }

        applyStereotype(stereotypedElement, applicableStereotypes.get(0));
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

    /**
     * Sets the specified tagged value on the {@link Stereotype}.
     *
     * @param stereotypedElement
     *            the entity on which the stereotype is applied.
     * @param newValue
     *            the value to be set
     * @param stereotypeName
     *            the stereotype`s name
     * @param taggedValueName
     *            the tagged value`s name
     */
    public static void setTaggedValue(final EObject stereotypedElement, final int newValue,
            final String stereotypeName, final String taggedValueName) {
        final List<StereotypeApplication> stereotypeApplications = getStereotypeApplications(stereotypedElement,
                stereotypeName);
        final StereotypeApplication stereotypeApplication = stereotypeApplications.get(0);
        final EStructuralFeature taggedValue = stereotypeApplication.getStereotype().getTaggedValue(taggedValueName);
        stereotypeApplication.eSet(taggedValue, newValue);
    }
    
    /**
     * Sets the specified String tagged value on the {@link Stereotype}.
     *
     * @param stereotypedElement
     *            the entity on which the stereotype is applied.
     * @param newValue
     *            the String value to be set
     * @param stereotypeName
     *            the stereotype`s name
     * @param taggedValueName
     *            the tagged value`s name
     */
    public static void setStringTaggedValue(final EObject stereotypedElement, final String newValue,
            final String stereotypeName, final String taggedValueName) {
        final List<StereotypeApplication> stereotypeApplications = getStereotypeApplications(stereotypedElement,
                stereotypeName);
        final StereotypeApplication stereotypeApplication = stereotypeApplications.get(0);
        final EStructuralFeature taggedValue = stereotypeApplication.getStereotype().getTaggedValue(taggedValueName);
        stereotypeApplication.eSet(taggedValue, newValue);
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

        // FIXME that code is not nice ;)
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
        return !new StereotypeApplicationCrossReferencer(stereotypedElement)
        .findStereotypeApplications().isEmpty();
    }

    /**
     * Checks whether any {@link EObject} in the given set has a stereotype with the given name
     * applied.
     *
     * @param setOfElements
     *            the set of EObjects
     * @param stereotypeName
     *            the stereotype name
     * @return <code>true</code> if there is at least one stereotype application of the given
     *         stereotype to an element of the given set; <code>false</code> otherwise.
     */
    public static boolean hasAppliedStereotype(final Set<? extends EObject> setOfElements, final String stereotypeName) {
        for (final EObject eobject : setOfElements) {
            if (StereotypeAPI.isStereotypeApplied(eobject, stereotypeName)) {
                return true;
            }
        }

        return false;
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
        return new StereotypeApplicationCrossReferencer(stereotypedElement)
        .findStereotypeApplications();
    }

    public static EList<StereotypeApplication> getStereotypeApplications(final EObject stereotypedElement,
            final Profile profile) {
        final EList<StereotypeApplication> filteredStereotypeApplications = new BasicEList<StereotypeApplication>();

        for (final StereotypeApplication stereotypeApplication : getStereotypeApplications(stereotypedElement)) {
            if (stereotypeApplication.getStereotype().getProfile().getNsURI().equals(profile.getNsURI())) {
                filteredStereotypeApplications.add(stereotypeApplication);
            }
        }

        return filteredStereotypeApplications;
    }

    public static EList<StereotypeApplication> getStereotypeApplications(final EObject stereotypedElement,
            final String stereotype) {
        final EList<StereotypeApplication> filteredStereotypeApplications = new BasicEList<StereotypeApplication>();

        for (final StereotypeApplication stereotypeApplication : getStereotypeApplications(stereotypedElement)) {
            if (stereotypeApplication.getStereotype().getName().equals(stereotype)) {
                filteredStereotypeApplications.add(stereotypeApplication);
            }
        }

        return filteredStereotypeApplications;
    }

    public static StereotypeApplication getStereotypeApplication(final EObject stereotypedElement,
            final Stereotype stereotype) {
        for (final StereotypeApplication stereotypeApplication : getStereotypeApplications(stereotypedElement)) {
            if (EMFLoadHelper.getResourceURI(stereotypeApplication.getStereotype()).equals(
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
            appliedStereotypes.add(stereotypeApplication.getStereotype());
        }

        return appliedStereotypes;
    }

    /**
     * Returns the tagged value of the specified {@link Stereotype}.
     *
     * @param pcmEntity
     *            the entity on which the stereotype is applied
     * @param taggedValueName
     *            the tagged value`s name
     * @param stereotypeName
     *            the stereotype`s name
     * @return the value
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <DATA_TYPE> DATA_TYPE getTaggedValue(final EObject stereotypedElement, final String taggedValueName,
            final String stereotypeName) {
        final EList<StereotypeApplication> pcmEntityStereotypeApplications = getStereotypeApplications(
                stereotypedElement, stereotypeName);
        final StereotypeApplication stereotypeApplication = pcmEntityStereotypeApplications.get(0);

        final Stereotype stereotype = stereotypeApplication.getStereotype();

        final EStructuralFeature taggedValue = stereotype.getTaggedValue(taggedValueName);

        return (DATA_TYPE) stereotypeApplication.eGet(taggedValue);

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

        unapplyStereotype(stereotypedElement, stereotypeApplications.get(0).getStereotype());
    }

	/**
	 * Returns the {@link EStructuralFeature}s that define the {@link Stereotype}`s
	 * parameters.
	 * 
	 * @param stereotype
	 *            the {@link Stereotype}
	 * @return the parameters` features
	 */
	public static Collection<EStructuralFeature> getParameters(
			final Stereotype stereotype) {
		final Collection<EStructuralFeature> parameterFeatures = new LinkedList<>();

		for (EStructuralFeature eStructuralFeature : stereotype.getEAllStructuralFeatures()) {
			if (!EXLUDED_PARAMETER_FEATURE_IDS.contains(eStructuralFeature.getFeatureID())) {
				parameterFeatures.add(eStructuralFeature);
			}

		}
		return parameterFeatures;
	}

	/**
	 * Returns the {@link EStructuralFeature} that defines the given parameter
	 * (identified by its name).
	 * 
	 * @param stereotype
	 *            the {@link Stereotype}
	 * @param parameterName
	 *            the parameter`s name
	 * @return the parameter`s {@link EStructuralFeature}
	 */
	public static EStructuralFeature getParameter(final Stereotype stereotype,
			final String parameterName) {
		for (EStructuralFeature parameterFeature : getParameters(stereotype)) {
			if (parameterFeature.getName().equals(parameterName)) {
				return parameterFeature;
			}
		}

		throw new RuntimeException("The parameter with name \"" + parameterName
				+ "\" does not exist on the Stereotype \"" + stereotype + "\"");
	}

    private static ProfileApplication getProfileApplication(final EObject stereotypedElement) {
        return ProfileAPI.getProfileApplication(stereotypedElement.eResource());
    }

    private static EList<ProfileImport> getProfileImports(final EObject stereotypedElement) {
        return getProfileApplication(stereotypedElement).getImportedProfiles();
    }
}
