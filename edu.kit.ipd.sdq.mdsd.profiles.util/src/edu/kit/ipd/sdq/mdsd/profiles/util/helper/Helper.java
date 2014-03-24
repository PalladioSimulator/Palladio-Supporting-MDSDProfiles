/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.util.helper;

import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.registry.IProfileRegistry;

/**
 * @author Matthias Eisenmann
 * 
 */
public class Helper {

    private Helper() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Retrieves the profile with the specified profile name.
     * 
     * @param profileName
     *            The name of profile to be retrieved.
     * @return The profile with the specified name, or null if no profile was found.
     */
    public static Profile getProfile(final String profileName) {
        Collection<Profile> profiles = IProfileRegistry.INSTANCE.getRegisteredProfiles();

        for (Profile profile : profiles) {
            if (profile.getName().equals(profileName)) {
                return profile;
            }
        }

        return null;
    }

    /**
     * Retrieves the resource set of the first registered profile. All registered profiles should
     * have the same resource set.
     * 
     * @return The resource set of the first registered profile, or null if no profile is
     *         registered.
     */
    public static ResourceSet getProfileRegistryResourceSet() {
        Collection<Profile> profiles = IProfileRegistry.INSTANCE.getRegisteredProfiles();

        if (profiles.iterator().hasNext()) {
            return profiles.iterator().next().eResource().getResourceSet();
        }

        return null;
    }

    /**
     * Creates the path from the specified URI.
     * 
     * @param uri
     *            The URI for which to create the path.
     * @return The path.
     */
    public static IPath createPath(final URI uri) {

        if (uri.isPlatform()) {
            return new Path(uri.toPlatformString(true)).removeTrailingSeparator();
        } else if (uri.isFile()) {
            return new Path(uri.toFileString()).removeTrailingSeparator();
        } else {
            return new Path(uri.toString()).removeTrailingSeparator();
        }
    }

    /**
     * Determines whether the specified project has the nature specified by the ID.
     * 
     * @param project
     *            The project in question.
     * @param natureId
     *            The nature ID in question.
     * @return True if the specified project has the specified nature, otherwise false.
     */
    public static boolean hasProfileProjectNature(final IProject project, final String natureId) {

        boolean isNatureEnabled = false;
        try {
            isNatureEnabled = project.isNatureEnabled(natureId);
        } catch (CoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return isNatureEnabled;
    }

    /**
     * Adds the nature specified by the ID to the specified project.
     * 
     * @param project
     *            The project to which the nature shall be added.
     * @param natureId
     *            The nature to be added.
     */
    public static void addNature(final IProject project, final String natureId) {

        IProjectDescription description = null;

        try {
            description = project.getDescription();
        } catch (CoreException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String[] natures = description.getNatureIds();
        String[] newNatures = new String[natures.length + 1];
        System.arraycopy(natures, 0, newNatures, 0, natures.length);
        newNatures[natures.length] = natureId;
        description.setNatureIds(newNatures);

        try {
            project.setDescription(description, null);
        } catch (CoreException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}