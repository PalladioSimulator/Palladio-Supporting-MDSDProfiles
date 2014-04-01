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

    private static Logger logger = Logger.getLogger(EProfileApplicationLoader.class);

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
    @SuppressWarnings("static-access")
    public Collection<EStereotypableObject> performObservation(final EStereotypableObject eStereotypableObject) {

        try {
            this.profileApplicationDecorators = this.observer.findProfileApplicationDecorators(eStereotypableObject);
            logger.info("Decorator(s): " + this.profileApplicationDecorators);
        } catch (final Exception e) {
            logger.error("Chosen Object was not stereotyped. Please select another one.");
            e.printStackTrace();
            throw new NullPointerException();
        }

        // if (profileApplicationDecorators.isEmpty()) {
        // profileApplicationDecorators =
        // EStereotypedEditorObserver.findProfileApplicationDecorators(eStereotypableObject);
        // logger.error("ProfileApplicationDecorators is empty.");
        // } else {
        // profileApplicationDecorators =
        // ProfileApplicationFileRegistry.INSTANCE.getAllExistingProfileApplicationDecorators(eStereotypableObject);
        // logger.info("Decorators taken from ProfileApplicationFileRegistry.");
        // }

        // observer.getActiveEditorObserver().refreshViewer();

        if (this.profileApplicationDecorators.iterator().hasNext()) {
            for (final StereotypeApplication stereotypeApplication : this.profileApplicationDecorators.iterator()
                    .next().getStereotypeApplications()) {
                this.eStereotypableObjects.add((EStereotypableObject) stereotypeApplication.getAppliedTo());
            }
        }
        logger.info("EStereotyped Objects: " + this.eStereotypableObjects.toString());
        return this.eStereotypableObjects;
    }
}
