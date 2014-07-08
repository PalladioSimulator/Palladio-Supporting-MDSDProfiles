/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.impl;

import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.gmf.runtime.emf.core.util.EMFCoreUtil;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofile.application.registry.ProfileApplicationDecorator;
import org.modelversioning.emfprofile.registry.IProfileRegistry;
import org.modelversioning.emfprofileapplication.ProfileApplication;
import org.modelversioning.emfprofileapplication.StereotypeApplicability;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
import edu.kit.ipd.sdq.mdsd.profiles.registry.ProfileApplicationFileRegistry;

/**
 * @author Matthias Eisenmann
 */
public class EStereotypableObjectImpl extends EObjectImpl implements
        EStereotypableObject {

    private static final Logger LOGGER = Logger
            .getLogger(EStereotypableObjectImpl.class.getName());

    /**
     * Default constructor
     */
    protected EStereotypableObjectImpl() {
        super();
    }

    /**
     * Returns all profile application decorators that are registered for this
     * object.
     * 
     * @return the decorators
     */
    private Collection<ProfileApplicationDecorator>
            getProfileApplicationDecorators() {

        Collection<ProfileApplicationDecorator> profileApplicationDecorators =
                ProfileApplicationFileRegistry.INSTANCE
                        .getAllExistingProfileApplicationDecorators(this);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("returning " + profileApplicationDecorators.size()
                    + " decorator(s)");
            for (final ProfileApplicationDecorator pad : profileApplicationDecorators) {
                LOGGER.debug(pad.getName());
            }
        }

        return profileApplicationDecorators;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EList<StereotypeApplication> getStereotypeApplications() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("method=getStereotypeApplications | eStereotypableObject="
                    + this);
        }

        EList<StereotypeApplication> stereotypeApplications =
                new BasicEList<StereotypeApplication>();

        for (final ProfileApplicationDecorator profileApplicationDecorator : getProfileApplicationDecorators()) {
            stereotypeApplications.addAll(profileApplicationDecorator
                    .getStereotypeApplications(this));
        }

        return stereotypeApplications;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EList<StereotypeApplication> getStereotypeApplications(
            final Stereotype stereotype) {
        final EList<StereotypeApplication> stereotypeApplications =
                new BasicEList<StereotypeApplication>();

        for (StereotypeApplication stereotypeApplication : getStereotypeApplications()) {
            Stereotype appliedStereotype =
                    stereotypeApplication.getStereotype();
            boolean sameStereotypes =
                    appliedStereotype != null
                            && appliedStereotype.equals(stereotype);
            boolean equalStereotypes = false;
            if (!sameStereotypes) {
                String appliedNsURI =
                        appliedStereotype.getEPackage().getNsURI();
                String appliedQualifiedName =
                        EMFCoreUtil.getQualifiedName(appliedStereotype, true);
                String appliedFullyQualifiedName =
                        appliedNsURI + appliedQualifiedName;
                String nsURI = stereotype.getEPackage().getNsURI();
                String qualifiedName =
                        EMFCoreUtil.getQualifiedName(stereotype, true);
                String fullyQualifiedName = nsURI + qualifiedName;
                equalStereotypes =
                        appliedFullyQualifiedName != null
                                && appliedFullyQualifiedName
                                        .equals(fullyQualifiedName);
            }
            if (sameStereotypes || equalStereotypes) {
                stereotypeApplications.add(stereotypeApplication);
            }
        }
        return stereotypeApplications;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EList<StereotypeApplication> getStereotypeApplications(
            final String qualifiedName) {
        final EList<StereotypeApplication> stereotypeApplications =
                new BasicEList<StereotypeApplication>();

        for (StereotypeApplication stereotypeApplication : getStereotypeApplications()) {
            if (stereotypeApplication.getStereotype().getName()
                    .equals(qualifiedName)) {
                stereotypeApplications.add(stereotypeApplication);
            }
        }

        return stereotypeApplications;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EList<Stereotype> getAppliedStereotypes() {

        EList<Stereotype> appliedStereotypes = new BasicEList<Stereotype>();

        for (StereotypeApplication stereotypeApplication : getStereotypeApplications()) {
            appliedStereotypes.add(stereotypeApplication.getStereotype());
        }

        return appliedStereotypes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stereotype getAppliedStereotype(final String qualifiedName) {
        for (Stereotype stereotype : getAppliedStereotypes()) {
            if (stereotype.getName().equals(qualifiedName)) {
                return stereotype;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStereotypeApplicable(final Stereotype stereotype) {
        return getApplicableStereotypes().contains(stereotype);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStereotypeApplied(final Stereotype stereotype) {
        return getAppliedStereotypes().contains(stereotype);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StereotypeApplication applyStereotype(final Stereotype stereotype) {

        if (LOGGER.isDebugEnabled()) {
            if (stereotype != null) {
                LOGGER.debug("method=applyStereotype | stereotype="
                        + stereotype.getName() + " | profile="
                        + stereotype.getProfile().getName()
                        + " | eStereotypableObject=" + this);
            } else {
                LOGGER.debug("method=applyStereotype | stereotype=null | eStereotypableObject="
                        + this);
            }
        }

        if (stereotype == null) {
            return null;
        }

        final ProfileApplicationDecorator profileApplicationDecorator =
                ProfileApplicationFileRegistry.INSTANCE
                        .getOrCreateProfileApplicationDecorator(this,
                                stereotype.getProfile());

        final Collection<? extends StereotypeApplicability> stereotypeApplicabilities =
                profileApplicationDecorator.getApplicableStereotypes(this);

        for (final StereotypeApplicability stereotypeApplicability : stereotypeApplicabilities) {

            // make sure that it is the correct stereotype of the containing
            // profile
            if (stereotypeApplicability.getStereotype().getName()
                    .equals(stereotype.getName())
                    && stereotypeApplicability.getStereotype().getProfile()
                            .getName()
                            .equals(stereotype.getProfile().getName())) {

                final StereotypeApplication stereotypeApplication =
                        profileApplicationDecorator.applyStereotype(
                                stereotypeApplicability, this);
                return stereotypeApplication;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeStereotypeApplication(
            final StereotypeApplication stereotypeApplication) {
        // FIXME use new getProfileApplicationDecorator(Profile) instead of
        // iterating over all
        // decorators
        if (stereotypeApplication == null) {
            return;
        }

        for (final ProfileApplicationDecorator profileApplicationDecorator : getProfileApplicationDecorators()) {

            if (profileApplicationDecorator.getStereotypeApplications(this)
                    .contains(stereotypeApplication)) {

                if (stereotypeApplication.getAppliedTo() == this) {
                    profileApplicationDecorator
                            .removeEObject(stereotypeApplication);
                    return;
                } else {
                    LOGGER.error("prevented removal of stereotype application '"
                            + stereotypeApplication
                            + "' from object '"
                            + this
                            + "': applied-to-reference to different object");
                }
            }
        }

        return;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EList<Stereotype> getApplicableStereotypes() {
        final EList<Stereotype> applicableStereotypes =
                new BasicEList<Stereotype>();

        for (final Profile profile : IProfileRegistry.INSTANCE
                .getRegisteredProfiles()) {

            applicableStereotypes.addAll(getApplicableStereotypes(profile));
        }

        return applicableStereotypes;
    }

    @Override
    public EList<Stereotype> getApplicableStereotypes(final Profile profile) {
        // Check applicability of stereotypes by calling existing decorators.
        // This ensures that
        // upper and lower bounds of stereotype extensions are checked by EMF
        // Profile internally.
        final EList<Stereotype> applicableStereotypes =
                new BasicEList<Stereotype>();
        final ProfileApplicationDecorator profileApplicationDecorator =
                ProfileApplicationFileRegistry.INSTANCE
                        .getExistingProfileApplicationDecorator(this, profile);

        if (profileApplicationDecorator != null) {
            for (final StereotypeApplicability stereotypeApplicability : profileApplicationDecorator
                    .getApplicableStereotypes(this)) {
                applicableStereotypes.add(stereotypeApplicability
                        .getStereotype());
            }
        } else {

            applicableStereotypes.addAll(profile.getApplicableStereotypes(this
                    .eClass()));
        }
        return applicableStereotypes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stereotype getApplicableStereotype(final String qualifiedName) {
        for (Stereotype stereotype : getApplicableStereotypes()) {
            if (stereotype.getName().equals(qualifiedName)) {
                return stereotype;
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EList<ProfileApplication> getAllProfileApplications() {
        final EList<ProfileApplication> profileApplications =
                new BasicEList<ProfileApplication>();

        for (final ProfileApplicationDecorator profileApplicationDecorator : getProfileApplicationDecorators()) {
            profileApplications.addAll(profileApplicationDecorator
                    .getProfileApplications());
        }

        return profileApplications;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EList<ProfileApplication> getProfileApplications() {
        final EList<ProfileApplication> profileApplications =
                new BasicEList<ProfileApplication>();

        for (final StereotypeApplication stereotypeApplication : getStereotypeApplications()) {
            if (!profileApplications.contains(stereotypeApplication
                    .getProfileApplication())) {
                profileApplications.add(stereotypeApplication
                        .getProfileApplication());
            }
        }

        return profileApplications;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveContainingProfileApplication() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("saving containing profile application of EStereotypableObject '"
                    + this + "'");
        }

        for (final ProfileApplicationDecorator profileApplicationDecorator : getProfileApplicationDecorators()) {
            if (profileApplicationDecorator.isDirty()
                    && !profileApplicationDecorator.getStereotypeApplications(
                            this).isEmpty()) {
                try {
                    profileApplicationDecorator.save();
                } catch (IOException e) {
                    LOGGER.error("writing profile application file failed", e);
                } catch (CoreException e) {
                    LOGGER.error("writing profile application file failed", e);
                }
            }
        }
    }

    @Override
    public EList<StereotypeApplication> removeAllStereotypeApplications(
            final Stereotype stereotype) {
        EList<StereotypeApplication> stereotypeApplications =
                getStereotypeApplications(stereotype);
        for (StereotypeApplication stereotypeApplication : stereotypeApplications) {
            removeStereotypeApplication(stereotypeApplication);
        }
        return stereotypeApplications;
    }
}
