/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.util.observer;

/**
 * @author Matthias Eisenmann
 * @param <T>
 */
public interface IObservable<T> {

    /**
     * Register the given observer for this observable.
     * 
     * @param observer
     *            the observer to be registered
     * @return whether the observer was newly added (otherwise it was already
     *         registered)
     */
    public boolean addObserver(IObserver<T> observer);

    /**
     * De-registers the given observer for this observable.
     * 
     * @param observer
     *            the observer to be de-registered
     */
    public void removeObserver(IObserver<T> observer);

    /**
     * Notifies all observers that have been registered for this observable.
     * 
     * @param observed
     *            the observed object
     */
    public void notifyObservers(T observed);

}
