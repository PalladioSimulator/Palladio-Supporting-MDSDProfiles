/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.registry.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.application.registry.ProfileApplicationDecorator;
import org.modelversioning.emfprofile.application.registry.internal.ProfileApplicationDecoratorImpl;
import org.modelversioning.emfprofileapplication.ProfileApplication;
import org.modelversioning.emfprofileapplication.util.ProfileImportResolver;

import edu.kit.ipd.sdq.mdsd.profiles.builder.ProfileProjectBuilder;
import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
import edu.kit.ipd.sdq.mdsd.profiles.nature.ProfileProjectNature;
import edu.kit.ipd.sdq.mdsd.profiles.registry.ProfileApplicationFileRegistry;
import edu.kit.ipd.sdq.mdsd.profiles.util.helper.ProfileHelper;

/**
 * @author Matthias Eisenmann, Max Kramer
 * 
 */
public final class ProfileApplicationFileRegistryImpl implements ProfileApplicationFileRegistry {

    private static final Logger LOGGER = Logger.getLogger(ProfileApplicationFileRegistryImpl.class.getName());

    public static final ProfileApplicationFileRegistry INSTANCE = new ProfileApplicationFileRegistryImpl();

    /**
     * Contains all considered profile application files.
     */
    private final Collection<IFile> profileApplicationFiles = new ArrayList<IFile>();

    /**
     * Mapping of model resource to profile and corresponding decorator.
     */
    private final Map<Resource, Map<Profile, ProfileApplicationDecorator>> modelToDecoratorMap = new HashMap<Resource, Map<Profile, ProfileApplicationDecorator>>();

    /**
     * Keeps track of whether a decorator of a profile application file has already been created.
     */
    private final Map<Resource, Map<IFile, ProfileApplicationDecorator>> resource2file2DecoratorMap = new HashMap<Resource, Map<IFile, ProfileApplicationDecorator>>();

    /**
     * Hide default constructor
     */
    private ProfileApplicationFileRegistryImpl() {

        // TODO: remove logger configuration
        /*
         * FIXME (from Lehrig) I commented-out this global (!!!) reset of the logger configuration.
         * It actually destroyed every PCM-based workflow; especially simulation durations increased
         * heavily since everything was logged. Please provide a logger configuration that is
         * consistent with other projects.
         */
        // PatternLayout layout =
        // new PatternLayout("%d{HH:mm:ss,SSS} [%t] %-5p %c - %m%n");
        // ConsoleAppender appender = new ConsoleAppender(layout);
        // BasicConfigurator.resetConfiguration();
        // BasicConfigurator.configure(appender);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("creating profile application file registry ...");
        }

        final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
        final IProject[] projects = root.getProjects();

        for (final IProject project : projects) {
            try { // TODO: consider closed projects?
                if (project.isOpen() && project.isNatureEnabled(ProfileProjectNature.NATURE_ID)) {
                    Collection<IFile> files = ProfileProjectBuilder.findProfileApplicationFiles(project);

                    for (final IFile file : files) {
                        addProfileApplicationFile(file);
                    }
                }
            } catch (CoreException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        addRegistryAsObserverToAllProfileProjectBuilders();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("... created profile application file registry");
        }
    }

    @Override
    public Collection<ProfileApplicationDecorator> getAllExistingProfileApplicationDecorators(
            final EStereotypableObject eStereotypableObject) {

        initializeProfileToDecoratorMap(eStereotypableObject.eResource());

        setUpProfileApplicationDecoratorsForExistingFiles(eStereotypableObject);

        return modelToDecoratorMap.get(eStereotypableObject.eResource()).values();
    }

    @Override
    public ProfileApplicationDecorator getExistingProfileApplicationDecorator(
            final EStereotypableObject eStereotypableObject, final Profile profile) {

        if (eStereotypableObject == null || eStereotypableObject.eResource() == null) {
            LOGGER.error("EStereotypableObject or its resource is null");
            // TODO check whether it is better to throw an Exception when the
            // method is used wrongly
            return null;
        }

        // use profile from profile registry resource set
        final Profile profileFromRegistryResourceSet = ProfileHelper.getProfile(profile.getName());

        addRegistryAsObserverToAllProfileProjectBuilders();

        // first try to find the decorators in the map
        Map<Profile, ProfileApplicationDecorator> profileToDecoratorMap = modelToDecoratorMap.get(eStereotypableObject
                .eResource());

        if (profileToDecoratorMap != null && profileFromRegistryResourceSet != null
                && profileToDecoratorMap.containsKey(profileFromRegistryResourceSet)) {

            return profileToDecoratorMap.get(profileFromRegistryResourceSet);
        }

        initializeProfileToDecoratorMap(eStereotypableObject.eResource());

        setUpProfileApplicationDecoratorsForExistingFiles(eStereotypableObject);

        if (LOGGER.isDebugEnabled()) {
            if (!modelToDecoratorMap.get(eStereotypableObject.eResource()).containsKey(profileFromRegistryResourceSet)) {
                LOGGER.debug("no decorator for profile '" + profileFromRegistryResourceSet.getName()
                        + "' and resource '" + eStereotypableObject.eResource() + "'");
            }
        }

        return modelToDecoratorMap.get(eStereotypableObject.eResource()).get(profileFromRegistryResourceSet);
    }

    @Override
    public ProfileApplicationDecorator getOrCreateProfileApplicationDecorator(
            final EStereotypableObject eStereotypableObject, final Profile profile) {

        // use profile from profile registry resource set
        final Profile profileFromRegistryResourceSet = ProfileHelper.getProfile(profile.getName());

        ProfileApplicationDecorator existingPAD = getExistingProfileApplicationDecorator(eStereotypableObject,
                profileFromRegistryResourceSet);

        if (existingPAD == null) {

            // If a new profile application file is created, it must be ensured
            // that the profile
            // project nature is set to the project containing the file.
            // The nature is needed so that the registry is notified about
            // existing profile
            // application files in the project.
            final IProject project = findProject(eStereotypableObject);

            if (!ProfileHelper.hasProfileProjectNature(project, ProfileProjectNature.NATURE_ID)) {
                ProfileHelper.addNature(project, ProfileProjectNature.NATURE_ID);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("profile project nature added to project " + project.getName());
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("profile project nature exists for project " + project.getName());
                }
            }

            final IFile newProfileApplicationFile = createProfileApplicationFile(eStereotypableObject,
                    profileFromRegistryResourceSet.getName());

            LOGGER.debug("created profile application file '" + newProfileApplicationFile.getFullPath() + "'");

            existingPAD = setUpProfileApplicationDecoratorForNewFile(eStereotypableObject,
                    profileFromRegistryResourceSet, newProfileApplicationFile);
        }

        return existingPAD;
    }

    /**
     * Registers the given profile application file.
     * 
     * @param profileApplicationFile
     *            the pa file
     */
    private void addProfileApplicationFile(final IFile profileApplicationFile) {
        if (!profileApplicationFiles.contains(profileApplicationFile)) {
            profileApplicationFiles.add(profileApplicationFile);
            LOGGER.debug("method=addProfileApplicationFile | profileApplicationFile="
                    + profileApplicationFile.getFullPath());
        } else {
            LOGGER.debug("file '" + profileApplicationFile.getFullPath() + "' already registered");
        }
    }

    /**
     * Sets up the profile application decorator for the given profile application file.
     * 
     * @param profileApplicationFile
     *            the pa file
     * @param eStereotypableObject
     *            the decorated object
     * @return the decorator
     */
    private ProfileApplicationDecorator setUpProfileApplicationDecoratorForExistingFile(
            final IFile profileApplicationFile, final EStereotypableObject eStereotypableObject) {

        ProfileApplicationDecorator profileApplicationDecorator = null;

        Resource eResource = eStereotypableObject.eResource();
        try {
            profileApplicationDecorator = new ProfileApplicationDecoratorImpl(profileApplicationFile,
                    eResource.getResourceSet());
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            LOGGER.error("the file '" + profileApplicationFile.getFullPath()
                    + "' does not contain any profile applications");

            try {
                profileApplicationFile.delete(true, null);
            } catch (CoreException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        if (profileApplicationDecorator != null) {

            /*
             * Determination of the profile:
             * 
             * In EMF Profiles it's possible that a profile application has multiple imported
             * profiles. But according to our policy we have exactly one imported profile per
             * profile application file. So we can take the first profile (which is expected to be
             * the only one).
             * 
             * Use the profile contained in the profile registry's resource set. Otherwise
             * mismatches can occur when profile is used as key in the map (different profile
             * instances in different resource sets), since the profile diagram is reloaded into the
             * model's resource set by EMF Profiles (develop branch).
             */

            if (profileApplicationDecorator.getProfileApplications().size() != 1) {
                LOGGER.error("expected exactly one profile application for decorator '"
                        + profileApplicationDecorator.getName() + "', actually "
                        + profileApplicationDecorator.getProfileApplications().size() + " profile applications found");
            }

            final ProfileApplication profileApplication = profileApplicationDecorator.getProfileApplications().get(0);

            if (profileApplication.getImportedProfiles().size() != 1) {
                LOGGER.error("expected exacactly one imported profile for decorator '"
                        + profileApplicationDecorator.getName() + "', actually "
                        + profileApplication.getImportedProfiles().size() + " imported profiles found");
            }

            final Profile profile = ProfileImportResolver.resolve(profileApplication.getImportedProfiles().get(0),
                    ProfileHelper.getProfileRegistryResourceSet());

            if (!profileApplicationDecorator.getStereotypeApplications(eStereotypableObject).isEmpty()) {

                resource2file2DecoratorMap.get(eResource).put(profileApplicationFile, profileApplicationDecorator);

                modelToDecoratorMap.get(eResource).put(profile, profileApplicationDecorator);

                LOGGER.debug("created decorator for existing file '" + profileApplicationFile.getFullPath() + "'");

            } else {
                LOGGER.debug("discarded created decorator '" + profileApplicationDecorator.getName()
                        + "': no stereotype application(s) for EStereotypableObject '" + eStereotypableObject + "'");

                profileApplicationDecorator = null;

                final Iterator<Resource> iterator = eResource.getResourceSet().getResources().iterator();

                while (iterator.hasNext()) {
                    final Resource resource = iterator.next();
                    URI uri = resource.getURI();
                    if (uri.isPlatformResource()) {
                        String platformString = uri.toPlatformString(false);
                        if (platformString != null && platformString.endsWith(profileApplicationFile.getName())) {
                            iterator.remove();
                        }
                    }

                }
            }
        } else {
            LOGGER.error("failed to create profile application decorator for file '"
                    + profileApplicationFile.getFullPath() + "'");
        }

        return profileApplicationDecorator;
    }

    /**
     * Sets up the profile application decorators for the given object.
     * 
     * @param eStereotypableObject
     *            the decorated object
     */
    private void setUpProfileApplicationDecoratorsForExistingFiles(final EStereotypableObject eStereotypableObject) {
        addRegistryAsObserverToAllProfileProjectBuilders();

        for (final IFile profileApplicationFile : this.profileApplicationFiles) {
            Map<IFile, ProfileApplicationDecorator> file2DecoratorMap = resource2file2DecoratorMap
                    .get(eStereotypableObject.eResource());
            if (!file2DecoratorMap.containsKey(profileApplicationFile)) {
                setUpProfileApplicationDecoratorForExistingFile(profileApplicationFile, eStereotypableObject);
            }
        }
    }

    /**
     * Sets up the profile application decorator for the given decorated object, profile and new
     * profile application file.
     * 
     * @param eStereotypableObject
     *            the decorated object
     * @param profile
     *            the profile
     * @param newProfileApplicationFile
     *            the new pa file
     * @return the decorator
     */
    private ProfileApplicationDecorator setUpProfileApplicationDecoratorForNewFile(
            final EStereotypableObject eStereotypableObject, final Profile profile,
            final IFile newProfileApplicationFile) {

        // add only one profile to the collection of profiles so that the
        // decorator is
        // responsible for one model/profile combination
        final Collection<Profile> profiles = new ArrayList<Profile>();
        profiles.add(profile);

        ProfileApplicationDecorator profileApplicationDecorator = null;

        Resource eResource = eStereotypableObject.eResource();
        try {
            profileApplicationDecorator = new ProfileApplicationDecoratorImpl(newProfileApplicationFile, profiles,
                    eResource.getResourceSet());
        } catch (CoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (profileApplicationDecorator != null) {
            LOGGER.debug("created decorator for new file '" + newProfileApplicationFile.getFullPath() + "'");

            resource2file2DecoratorMap.get(eResource).put(newProfileApplicationFile, profileApplicationDecorator);

            modelToDecoratorMap.get(eResource).put(profile, profileApplicationDecorator);

        } else {
            LOGGER.error("unable to create decorator for new file '" + newProfileApplicationFile.getFullPath() + "'");

            try {
                LOGGER.debug("deleting profile application file '" + newProfileApplicationFile.getFullPath() + "' ...");
                newProfileApplicationFile.delete(true, null);
            } catch (CoreException e) {
                LOGGER.error("failed to delete profile application file '" + newProfileApplicationFile.getFullPath()
                        + "'");
            }
        }

        return profileApplicationDecorator;
    }

    /**
     * Initializes the profile-to-decorator map for the specified resource.
     * 
     * @param resource
     *            The resource for which to initialize the map.
     */
    private void initializeProfileToDecoratorMap(final Resource resource) {

        Map<Profile, ProfileApplicationDecorator> profileToDecoratorMap = modelToDecoratorMap.get(resource);

        if (profileToDecoratorMap == null) {
            profileToDecoratorMap = new HashMap<Profile, ProfileApplicationDecorator>();
            modelToDecoratorMap.put(resource, profileToDecoratorMap);
        }

        Map<IFile, ProfileApplicationDecorator> file2DecoratorMap = resource2file2DecoratorMap.get(resource);
        if (file2DecoratorMap == null) {
            file2DecoratorMap = new HashMap<IFile, ProfileApplicationDecorator>();
            resource2file2DecoratorMap.put(resource, file2DecoratorMap);
        }
    }

    /**
     * Adds this registry to all profile project builders.
     */
    private void addRegistryAsObserverToAllProfileProjectBuilders() {

        final Collection<ProfileProjectBuilder> profileProjectBuilders = ProfileProjectBuilder
                .getAllProfileProjectBuilders();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(profileProjectBuilders.size() + " active profile project builder(s)");
        }

        for (final ProfileProjectBuilder builder : profileProjectBuilders) {
            if (builder.addObserver(this)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("registry attached to builder of project '" + builder.getProject().getName() + "'");
                }
            }
        }
    }

    /**
     * Determines the project in which the specified EStereotypableObject is located.
     * 
     * @param eStereotypableObject
     *            The {@link EStereotypableObject} for which to find the project.
     * @return The project that contains the specified {@link EStereotypableObject}.
     */
    private IProject findProject(final EStereotypableObject eStereotypableObject) {

        final URI uri = eStereotypableObject.eResource().getURI();

        if (!uri.isPlatformResource()) {
            LOGGER.error("determining project by EStereotypableObject's resource only possible with platform resource URI");
            return null;
        }

        final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

        final IPath path = ProfileHelper.createPath(uri);

        final IFile file = root.getFile(path);

        return file.getProject();
    }

    /**
     * Creates a profile application file for the given object and profile.
     * 
     * @param eStereotypableObject
     *            the decorated object
     * @param profileName
     *            the profile
     * @return the pa file
     */
    private IFile createProfileApplicationFile(final EStereotypableObject eStereotypableObject, final String profileName) {

        final String fileName = createProfileApplicationFileName(eStereotypableObject, profileName);

        final URI profileApplicationFileUri = createProfileApplicationFileURI(eStereotypableObject, fileName);

        final IPath path = ProfileHelper.createPath(profileApplicationFileUri);

        final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

        return root.getFile(path);
    }

    /**
     * Creates the name of profile application file in which stereotype applications for the
     * specified object shall be saved.
     * 
     * The file name consists of the source model name, the name of the profile that contains the
     * stereotype to be applied, and the characteristic extension 'pa.xmi'.
     * 
     * Pattern: &lt;model&gt;.&lt;profile&gt;.pa.xmi
     * 
     * @param eStereotypableObject
     *            the selected object
     * @param profileName
     *            the name of the profile to be applied
     * @return the file name
     */
    private String createProfileApplicationFileName(final EStereotypableObject eStereotypableObject,
            final String profileName) {
        URI modelURI = eStereotypableObject.eResource().getURI();

        modelURI = modelURI.trimFileExtension();

        final String modelName = modelURI.lastSegment();

        LOGGER.debug("modelName=" + modelName + " | profileName=" + profileName);

        return new String(modelName + "." + profileName + ".pa.xmi");
    }

    /**
     * Creates the URI for the profile application file for the given object and file name.
     * 
     * @param eStereotypableObject
     *            the decorated object
     * @param fileName
     *            the pa file name
     * @return the URI
     */
    private URI createProfileApplicationFileURI(final EStereotypableObject eStereotypableObject, final String fileName) {
        // create IPath for profile application file by determining the EMF URI
        // of the resource of
        // specified EStereotypableObject
        URI modelURI = eStereotypableObject.eResource().getURI();

        return URI.createPlatformResourceURI(modelURI.trimSegments(1).toPlatformString(false) + "/" + fileName, true);
    }

    @Override
    public void update(final IFile file) {
        if (file.exists()) {
            addProfileApplicationFile(file);
        } else {
            if (profileApplicationFiles.contains(file)) {
                profileApplicationFiles.remove(file);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * It detaches the profile application file registry from all builders.
     */
    @Override
    public void clear() {
        resource2file2DecoratorMap.clear();
        modelToDecoratorMap.clear();
        profileApplicationFiles.clear();

        final Collection<ProfileProjectBuilder> profileProjectBuilders = ProfileProjectBuilder
                .getAllProfileProjectBuilders();

        for (ProfileProjectBuilder builder : profileProjectBuilders) {
            builder.removeObserver(this);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("cleared profile application file registry");
        }
    }
}
