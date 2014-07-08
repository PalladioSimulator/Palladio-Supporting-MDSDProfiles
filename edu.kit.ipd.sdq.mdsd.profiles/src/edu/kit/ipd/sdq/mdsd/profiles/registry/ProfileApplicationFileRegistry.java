/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.registry;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.application.registry.ProfileApplicationDecorator;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
import edu.kit.ipd.sdq.mdsd.profiles.registry.impl.ProfileApplicationFileRegistryImpl;
import edu.kit.ipd.sdq.mdsd.profiles.util.observer.IObserver;

/**
 * @author Matthias Eisenmann
 * 
 */
public interface ProfileApplicationFileRegistry extends IObserver<IFile> {

    /**
     * Singleton instance of profile application file registry.
     */
    ProfileApplicationFileRegistry INSTANCE =
            ProfileApplicationFileRegistryImpl.INSTANCE;

    /**
     * Retrieves all decorators of relevant existing profile application files
     * for the specified eStereotypableObject.
     * 
     * Sets up decorators for existing profile application files but only
     * returns decorators for files that contain at least one stereotype
     * application for the specified eStereotypableObject.
     * 
     * @param eStereotypableObject
     *            the decorated object
     * @return The decorators of relevant profile application files, or empty
     *         collection if there's no profile application file that contains
     *         at least one stereotype application for the specified
     *         eStereotypableObject.
     */
    public Collection<ProfileApplicationDecorator>
            getAllExistingProfileApplicationDecorators(
                    EStereotypableObject eStereotypableObject);

    /**
     * Retrieves the decorator of a relevant existing profile application file
     * for the specified eStereotypableObject and profile.
     * 
     * A profile application file is relevant, if it contains at least one
     * stereotype from the specified profile applied to the specified
     * eStereotypableObject.
     * 
     * @param eStereotypableObject
     *            the decorated object
     * @param profile
     *            the profile for which decorators shall be returned
     * @return The decorator of a relevant existing profile application file, or
     *         null if there's no profile application file that contains at
     *         least one stereotype from the specified profile applied to the
     *         specified eStereotypableObject.
     */
    public ProfileApplicationDecorator getExistingProfileApplicationDecorator(
            EStereotypableObject eStereotypableObject, Profile profile);

    /**
     * Retrieves the decorator of a relevant existing profile application file
     * for the specified eStereotypableObject and profile or creates an new
     * profile application file if no relevant file was found.
     * 
     * A profile application file is relevant, if it contains at least one
     * stereotype from the specified profile applied to the specified
     * eStereotypableObject.
     * 
     * @param eStereotypabledObject
     *            the object that shall be decorated
     * @param profile
     *            the profile for which a decorator shall be returned
     * @return The decorator of a relevant existing profile application file, or
     *         null if creation of new profile application file failed.
     */
    public ProfileApplicationDecorator getOrCreateProfileApplicationDecorator(
            EStereotypableObject eStereotypabledObject, Profile profile);

    /**
     * Clears the state of profile application file registry.
     */
    public void clear();

}
