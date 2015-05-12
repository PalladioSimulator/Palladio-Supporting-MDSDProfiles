/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.util.observer;

/**
 * FIXME Why do you not use the observer pattern from palladio commons? see:
 * org.palladiosimulator.commons.designpatterns.AbstractObservable [Lehrig]
 * 
 * @author Matthias Eisenmann
 * @param <T>
 */
public interface IObserver<T> {

    /**
     * Update method to be called when this observer shall be notified for the given observable
     * 
     * @param observable
     *            the observable
     */
    public void update(T observable);

}
