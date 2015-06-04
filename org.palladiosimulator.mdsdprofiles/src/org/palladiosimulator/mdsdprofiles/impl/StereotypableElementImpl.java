/**
 */
package org.palladiosimulator.mdsdprofiles.impl;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.palladiosimulator.mdsdprofiles.MdsdprofilesPackage;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Stereotypable Element</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public abstract class StereotypableElementImpl extends MinimalEObjectImpl.Container implements StereotypableElement {

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
     * @generated NOT
     */
    @Override
    public void applyStereotype(final Stereotype stereotype) {
        StereotypeAPI.applyStereotype(this, stereotype);
    }

    /**
     * <!-- begin-user-doc -->Applies the {@link Stereotype} as identified by its name.<!--
     * end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void applyStereotype(final String stereotypeName) {
        StereotypeAPI.applyStereotype(this, stereotypeName);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean updateStereotypeApplications(final EList<Stereotype> stereotypesToBeApplied) {
        return StereotypeAPI.updateStereotypeApplications(this, stereotypesToBeApplied);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isStereotypeApplicable(final Stereotype stereotype) {
        return StereotypeAPI.isStereotypeApplicable(this, stereotype);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isStereotypeApplicable(final String stereotypeName) {
        return StereotypeAPI.isStereotypeApplicable(this, stereotypeName);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isStereotypeApplied(final Stereotype stereotype) {
        return StereotypeAPI.isStereotypeApplied(this, stereotype);
    }

    /**
     * <!-- begin-user-doc --> Checks whether this element has a {@link Stereotype} with the given
     * name applied. <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean isStereotypeApplied(final String stereotype) {
        return StereotypeAPI.isStereotypeApplied(this, stereotype);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public boolean hasStereotypeApplications() {
        return StereotypeAPI.hasStereotypeApplications(this);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Stereotype> getApplicableStereotypes() {
        return StereotypeAPI.getApplicableStereotypes(this);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Stereotype> getApplicableStereotypes(final Profile profile) {
        return StereotypeAPI.getApplicableStereotypes(this, profile);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Stereotype> getApplicableStereotypes(final String stereotypeName) {
        return StereotypeAPI.getApplicableStereotypes(this, stereotypeName);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<StereotypeApplication> getStereotypeApplications() {
        return StereotypeAPI.getStereotypeApplications(this);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<StereotypeApplication> getStereotypeApplications(final Profile profile) {
        return StereotypeAPI.getStereotypeApplications(this, profile);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<StereotypeApplication> getStereotypeApplications(final String stereotype) {
        return StereotypeAPI.getStereotypeApplications(this, stereotype);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public StereotypeApplication getStereotypeApplication(final Stereotype stereotype) {
        return StereotypeAPI.getStereotypeApplication(this, stereotype);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public EList<Stereotype> getAppliedStereotypes() {
        return StereotypeAPI.getAppliedStereotypes(this);
    }

    /**
     * <!-- begin-user-doc -->Removes all applications of the given {@link Stereotype}.<!--
     * end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void unapplyStereotype(final Stereotype stereotype) {
        StereotypeAPI.unapplyStereotype(this, stereotype);
    }

    /**
     * <!-- begin-user-doc -->Removes all applications of the {@link Stereotype} as identified by
     * its name.<!-- end-user-doc -->
     *
     * @generated NOT
     */
    @Override
    public void unapplyStereotype(final String stereotypeName) {
        StereotypeAPI.unapplyStereotype(this, stereotypeName);
    }

} // StereotypableElementImpl
