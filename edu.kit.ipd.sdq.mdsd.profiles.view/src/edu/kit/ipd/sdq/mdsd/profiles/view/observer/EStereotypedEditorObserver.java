package edu.kit.ipd.sdq.mdsd.profiles.view.observer;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.modelversioning.emfprofile.application.registry.ProfileApplicationDecorator;
import org.modelversioning.emfprofile.application.registry.ui.observer.ActiveEditorObserver;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
import edu.kit.ipd.sdq.mdsd.profiles.registry.ProfileApplicationFileRegistry;

/**
 * Delegated observer which observes the active editors and selection for regarding decorators.
 * 
 * @author emretaspolat
 * 
 */
public class EStereotypedEditorObserver {

    private static Logger logger = Logger.getLogger(EStereotypedEditorObserver.class);

    protected ActiveEditorObserver INSTANCE;

    protected EStereotypedEditorObserver() {
        this.INSTANCE = getActiveEditorObserver();
    }

    public static ActiveEditorObserver getActiveEditorObserver() {
        return ActiveEditorObserver.INSTANCE;
    }

    private Collection<ProfileApplicationDecorator> decorators;

    /**
     * Delegated method, instead of overridden, to find Decorators of Profile Applications from own
     * implemented ProfileApplicationRegistry.
     * 
     * @param eStereotypableObject
     * @return
     */
    public Collection<ProfileApplicationDecorator> findProfileApplicationDecorators(
            final EStereotypableObject eStereotypableObject) {

        if (eStereotypableObject.getAppliedStereotypes().isEmpty()) {
            // do nothing
        } else {
            this.decorators = ProfileApplicationFileRegistry.INSTANCE
                    .getAllExistingProfileApplicationDecorators(eStereotypableObject);
        }

        if (this.decorators == null || this.decorators.isEmpty()) {
            logger.error("Delegated observer couldn't find an decorators for the selection.");
            ProfileApplicationFileRegistry.INSTANCE.clear();
            final Collection<ProfileApplicationDecorator> newDecorators = ProfileApplicationFileRegistry.INSTANCE
                    .getAllExistingProfileApplicationDecorators(eStereotypableObject);
            return newDecorators;
        } else {
            return this.decorators;
        }
    }
    

}
