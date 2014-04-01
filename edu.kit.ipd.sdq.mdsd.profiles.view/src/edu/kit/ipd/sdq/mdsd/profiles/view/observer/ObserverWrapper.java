package edu.kit.ipd.sdq.mdsd.profiles.view.observer;

import org.modelversioning.emfprofile.application.registry.ui.observer.ActiveEditorObserver;

/**
 * Wraps two observers from EMF- and PCM-Profiles.
 * 
 * TODO: Implement the Wrapper completely
 * 
 * @author emretaspolat
 * 
 */
public class ObserverWrapper {

    private EStereotypedEditorObserver eStereotypedEditorObserver;
    private ActiveEditorObserver activeEditorObserver;

    public ObserverWrapper(final EStereotypedEditorObserver eStereotypedEditorObserver) {
        this.eStereotypedEditorObserver = eStereotypedEditorObserver;
    }

    public ObserverWrapper(final ActiveEditorObserver activeEditorObserver) {
        this.activeEditorObserver = activeEditorObserver;
    }

}
