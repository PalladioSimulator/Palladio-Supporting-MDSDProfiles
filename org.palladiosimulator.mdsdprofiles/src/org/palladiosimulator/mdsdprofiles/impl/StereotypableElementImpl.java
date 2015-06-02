/**
 */
package org.palladiosimulator.mdsdprofiles.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EModelElementImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.ProfileApplication;
import org.modelversioning.emfprofileapplication.ProfileImport;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.palladiosimulator.commons.emfutils.EMFLoadHelper;
import org.palladiosimulator.mdsdprofiles.MdsdprofilesPackage;
import org.palladiosimulator.mdsdprofiles.ProfileableElement;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Stereotypable Element</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.palladiosimulator.mdsdprofiles.impl.StereotypableElementImpl#getProfileableElement
 * <em>Profileable Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class StereotypableElementImpl extends EModelElementImpl implements StereotypableElement {
    private static final String HTTP_PALLADIOSIMULATOR_ORG_MDSD_PROFILES_STEREOTYPE_APPLICATION_1_0 = "http://palladiosimulator.org/MDSDProfiles/StereotypeApplication/1.0";

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected StereotypableElementImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return MdsdprofilesPackage.Literals.STEREOTYPABLE_ELEMENT;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ProfileableElement getProfileableElement() {
        final ProfileableElement profileableElement = this.basicGetProfileableElement();
        return profileableElement != null && profileableElement.eIsProxy() ? (ProfileableElement) this
                .eResolveProxy((InternalEObject) profileableElement) : profileableElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    public ProfileableElement basicGetProfileableElement() {
        // Assumption: the profileable element is the first (root) element within the resource
        final EObject eObject = this.getProfileApplicationResource().getContents().get(0);

        if (!(eObject instanceof ProfileableElement)) {
            return null;
        }

        return (ProfileableElement) eObject;
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
    public void applyStereotype(final Stereotype stereotype) {
        final ProfileableElement profileableElement = this.getProfileableElement();
        if (profileableElement == null) {
            throw new RuntimeException("ApplyStereotype failed: No profile application found!");
        }
        if (!this.isStereotypeApplicable(stereotype)) {
            throw new RuntimeException("ApplyStereotype failed: \"" + stereotype.getName()
                    + "\" is not applicable to \"" + this.getClass().getName() + "\"!");
        }
        if (this.isStereotypeApplied(stereotype)) {
            throw new RuntimeException("ApplyStereotype failed: \"" + stereotype.getName() + "\" already applied to \""
                    + this.getClass().getName() + "\"!");
        }

        final StereotypeApplication newStereotypeApplication = (StereotypeApplication) stereotype.getEPackage()
                .getEFactoryInstance().create(stereotype);
        // final StereotypeApplication newStereotypeApplication =
        // EMFProfileApplicationFactory.eINSTANCE.createStereotypeApplication();
        newStereotypeApplication.setAppliedTo(this);
        // TODO is the next operation save? necessary? [Lehrig]
        newStereotypeApplication.setExtension(stereotype.getApplicableExtensions(this).get(0));

        final ProfileApplication profileApplication = profileableElement.getProfileApplication();
        profileApplication.getStereotypeApplications().add(newStereotypeApplication);

        final EAnnotation stereotypeApplicationAnnotation = this.ensureStereotypeAnnotationExists();
        // FIXME use relative URI instead of getResourceURI(...) result [Lehrig]
        stereotypeApplicationAnnotation.getDetails().put(EMFLoadHelper.getResourceURI(stereotype),
                EMFLoadHelper.getResourceURI(newStereotypeApplication));
    }

    private EAnnotation ensureStereotypeAnnotationExists() {
        if (!this.hasStereotypeApplications()) {
            final EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
            eAnnotation.setSource(HTTP_PALLADIOSIMULATOR_ORG_MDSD_PROFILES_STEREOTYPE_APPLICATION_1_0);
            this.getEAnnotations().add(eAnnotation);
        }
        return this.getStereotypeApplicationAnnotation();
    }

    /**
     * <!-- begin-user-doc -->Applies the {@link Stereotype} as identified by its name.<!--
     * end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void applyStereotype(final String stereotypeName) {
        if (this.getApplicableStereotypes(stereotypeName).size() != 1) {
            throw new RuntimeException("ApplyStereotype based on name failed: name \"" + stereotypeName
                    + "\" not (uniquely) found!");
        }

        this.applyStereotype(this.getApplicableStereotypes(stereotypeName).get(0));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean updateStereotypeApplications(final EList<Stereotype> stereotypesToBeApplied) {
        final List<Stereotype> appliedStereotypes = this.getAppliedStereotypes();
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
                this.unapplyStereotype(stereotype);
                changed = true;
            }
        }

        // added stereotypes
        for (final Stereotype stereotype : additions) {
            this.applyStereotype(stereotype);
            changed = true;
        }

        return changed;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isStereotypeApplicable(final Stereotype stereotype) {
        if (this.isStereotypeApplied(stereotype)) {
            return false;
        }

        for (final ProfileImport profileImport : this.getProfileImports()) {
            for (final Stereotype applicableStereotype : profileImport.getProfile().getApplicableStereotypes(
                    this.eClass())) {
                if (EMFLoadHelper.getResourceURI(applicableStereotype).equals(EMFLoadHelper.getResourceURI(stereotype))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isStereotypeApplicable(final String stereotypeName) {
        if (this.getApplicableStereotypes(stereotypeName).size() != 1) {
            throw new RuntimeException("ApplyStereotype based on name failed: name \"" + stereotypeName
                    + "\" not (uniquely) found!");
        }

        return this.isStereotypeApplicable(this.getApplicableStereotypes(stereotypeName).get(0));
    }

    private EList<ProfileImport> getProfileImports() {
        return this.getProfileableElement().getProfileApplication().getImportedProfiles();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isStereotypeApplied(final Stereotype stereotype) {
        try {
            this.getStereotypeApplication(stereotype);
        } catch (final RuntimeException e) {
            return false;
        }
        return true;
    }

    /**
     * <!-- begin-user-doc --> Checks whether this element has a {@link Stereotype} with the given
     * name applied. <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isStereotypeApplied(final String stereotype) {
        return !this.getStereotypeApplications(stereotype).isEmpty();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean hasStereotypeApplications() {
        return this.getStereotypeApplicationAnnotation() != null;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Stereotype> getApplicableStereotypes() {
        final EList<Stereotype> applicableStereotypes = new BasicEList<Stereotype>();
        for (final ProfileImport profileImport : this.getProfileImports()) {
            applicableStereotypes.addAll(profileImport.getProfile().getApplicableStereotypes(this.eClass()));
        }

        return applicableStereotypes;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Stereotype> getApplicableStereotypes(final Profile profile) {
        final EList<Stereotype> filteredStereotypes = new BasicEList<Stereotype>();

        for (final Stereotype stereotype : this.getApplicableStereotypes()) {
            if (stereotype.getProfile().getNsURI().equals(profile.getNsURI())) {
                filteredStereotypes.add(stereotype);
            }
        }

        return filteredStereotypes;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Stereotype> getApplicableStereotypes(final String stereotypeName) {
        final EList<Stereotype> filteredStereotypes = new BasicEList<Stereotype>();

        for (final Stereotype stereotype : this.getApplicableStereotypes()) {
            if (stereotype.getName().equals(stereotypeName)) {
                filteredStereotypes.add(stereotype);
            }
        }

        return filteredStereotypes;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<StereotypeApplication> getStereotypeApplications() {
        final EList<StereotypeApplication> stereotypeApplications = new BasicEList<StereotypeApplication>();

        if (!this.hasStereotypeApplications()) {
            return stereotypeApplications;
        }

        final EAnnotation stereotypeApplicationAnnotation = this.getStereotypeApplicationAnnotation();
        final Collection<String> stereotypeApplicationURIs = stereotypeApplicationAnnotation.getDetails().values();
        for (final String stereotypeApplicationURI : stereotypeApplicationURIs) {
            stereotypeApplications.add((StereotypeApplication) EMFLoadHelper.loadAndResolveEObject(this
                    .getProfileApplicationResource().getResourceSet(), stereotypeApplicationURI));
        }

        return stereotypeApplications;
    }

    private EAnnotation getStereotypeApplicationAnnotation() {
        return this.getEAnnotation(HTTP_PALLADIOSIMULATOR_ORG_MDSD_PROFILES_STEREOTYPE_APPLICATION_1_0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<StereotypeApplication> getStereotypeApplications(final Profile profile) {
        final EList<StereotypeApplication> filteredStereotypeApplications = new BasicEList<StereotypeApplication>();

        for (final StereotypeApplication stereotypeApplication : this.getStereotypeApplications()) {
            if (stereotypeApplication.getExtension().getSource().getProfile().getNsURI().equals(profile.getNsURI())) {
                filteredStereotypeApplications.add(stereotypeApplication);
            }
        }

        return filteredStereotypeApplications;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<StereotypeApplication> getStereotypeApplications(final String stereotype) {
        final EList<StereotypeApplication> filteredStereotypeApplications = new BasicEList<StereotypeApplication>();

        for (final StereotypeApplication stereotypeApplication : this.getStereotypeApplications()) {
            if (stereotypeApplication.getExtension().getSource().getName().equals(stereotype)) {
                filteredStereotypeApplications.add(stereotypeApplication);
            }
        }

        return filteredStereotypeApplications;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public StereotypeApplication getStereotypeApplication(final Stereotype stereotype) {
        for (final StereotypeApplication stereotypeApplication : this.getStereotypeApplications()) {
            if (EMFLoadHelper.getResourceURI(stereotypeApplication.getExtension().getSource()).equals(
                    EMFLoadHelper.getResourceURI(stereotype))) {
                return stereotypeApplication;
            }
        }

        throw new RuntimeException("GetStereotypeApplication failed: Stereotype \"" + stereotype.getName()
                + "\" has not been applied to \"" + this.getClass().getName() + "\"!");
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Stereotype> getAppliedStereotypes() {
        final EList<Stereotype> appliedStereotypes = new BasicEList<Stereotype>();
        if (this.hasStereotypeApplications()) {
            for (final StereotypeApplication stereotypeApplication : this.getStereotypeApplications()) {
                appliedStereotypes.add(stereotypeApplication.getExtension().getSource());
            }
        }
        return appliedStereotypes;
    }

    /**
     * <!-- begin-user-doc -->Removes all applications of the given {@link Stereotype}.<!--
     * end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void unapplyStereotype(final Stereotype stereotype) {
        if (!this.isStereotypeApplied(stereotype)) {
            throw new RuntimeException("UnapplyStereotype failed: No application found for Stereotype \""
                    + stereotype.getName() + "\"!");
        }

        final StereotypeApplication stereotypeApplication = this.getStereotypeApplication(stereotype);
        stereotypeApplication.getProfileApplication().getStereotypeApplications().remove(stereotypeApplication);

        this.getStereotypeApplicationAnnotation().getDetails().removeKey(EMFLoadHelper.getResourceURI(stereotype));

        if (this.getStereotypeApplicationAnnotation().getDetails().size() == 0) {
            this.getEAnnotations().remove(this.getStereotypeApplicationAnnotation());
        }
    }

    /**
     * <!-- begin-user-doc -->Removes all applications of the {@link Stereotype} as identified by
     * its name.<!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void unapplyStereotype(final String stereotypeName) {
        final List<StereotypeApplication> stereotypeApplications = this.getStereotypeApplications(stereotypeName);
        if (stereotypeApplications.size() != 1) {
            throw new RuntimeException("UnapplyStereotype failed: Stereotype identification by name found "
                    + stereotypeApplications.size() + " fitting stereotypes!");
        }

        this.unapplyStereotype(stereotypeApplications.get(0).getExtension().getSource());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
        switch (featureID) {
        case MdsdprofilesPackage.STEREOTYPABLE_ELEMENT__PROFILEABLE_ELEMENT:
            if (resolve) {
                return this.getProfileableElement();
            }
            return this.basicGetProfileableElement();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(final int featureID) {
        switch (featureID) {
        case MdsdprofilesPackage.STEREOTYPABLE_ELEMENT__PROFILEABLE_ELEMENT:
            return this.basicGetProfileableElement() != null;
        }
        return super.eIsSet(featureID);
    }

} // StereotypableElementImpl
