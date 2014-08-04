package edu.kit.ipd.sdq.mdsd.profiles.view.observer;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.BasicEList;
import org.modelversioning.emfprofile.application.registry.ProfileApplicationDecorator;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;

/**
 * Calls the delegated observer and returns the stereotyped objects from decorators.
 * 
 * @author emretaspolat
 * 
 */
public class EProfileApplicationLoader {

    private static final Logger LOGGER = Logger.getLogger(EProfileApplicationLoader.class);

    private final Collection<EStereotypableObject> eStereotypableObjects = new BasicEList<>();
    private Collection<ProfileApplicationDecorator> profileApplicationDecorators;

    private final EStereotypedEditorObserver observer = new EStereotypedEditorObserver();

    /**
     * Calls EStereotypedEditorObserver to pull the EStereotypableObject decorators from
     * ProfileApplicationFileRegistry.
     * 
     * @param eStereotypableObject
     * @return
     */
    public Collection<EStereotypableObject> performObservation(EStereotypableObject eStereotypableObject) {

        getProfileApplicationDecorator(eStereotypableObject);

        if (this.profileApplicationDecorators.iterator().hasNext()) {
            for (final StereotypeApplication stereotypeApplication : this.profileApplicationDecorators.iterator()
                    .next().getStereotypeApplications()) {
                this.eStereotypableObjects.add((EStereotypableObject) stereotypeApplication.getAppliedTo());
            }
        }
        LOGGER.info("EStereotyped Objects: " + this.eStereotypableObjects.toString());
        return this.eStereotypableObjects;
    }

    /**
     * @param eStereotypableObject
     */
    public Collection<ProfileApplicationDecorator> getProfileApplicationDecorator(
            EStereotypableObject eStereotypableObject) {
        try {
            this.profileApplicationDecorators = this.observer.findProfileApplicationDecorators(eStereotypableObject);
            LOGGER.info("Decorator(s): " + this.profileApplicationDecorators);
        } catch (final Exception e) {
            LOGGER.error("Chosen Object was not stereotyped. Please select another one.");
            e.printStackTrace();
            throw new NullPointerException();
        }
        return profileApplicationDecorators;
    }
}
