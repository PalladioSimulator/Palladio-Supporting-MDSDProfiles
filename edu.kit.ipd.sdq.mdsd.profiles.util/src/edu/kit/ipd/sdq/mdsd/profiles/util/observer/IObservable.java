/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.util.observer;

/**
 * @author Matthias Eisenmann
 * 
 */
public interface IObservable<T> {

    public boolean addObserver(IObserver<T> observer);

    public void removeObserver(IObserver<T> observer);

    public void notifyObservers(T arg);

}
