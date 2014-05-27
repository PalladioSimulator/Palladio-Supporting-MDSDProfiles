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

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
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
import edu.kit.ipd.sdq.mdsd.profiles.util.helper.Helper;

/**
 * @author Matthias Eisenmann, Max Kramer
 * 
 */
public class ProfileApplicationFileRegistryImpl implements ProfileApplicationFileRegistry {

    private static final Logger logger = Logger.getLogger(ProfileApplicationFileRegistryImpl.class.getName());

    public static final ProfileApplicationFileRegistry INSTANCE = new ProfileApplicationFileRegistryImpl();

    /**
     * Contains all considered profile application files.
     */
    private final Collection<IFile> profileApplicationFiles = new ArrayList<IFile>();

    /**
     * Mapping of model resource to profile and corresponding decorator.
     */
    private final Map<String, Map<Profile, ProfileApplicationDecorator>> modelToDecoratorMap = new HashMap<String, Map<Profile, ProfileApplicationDecorator>>();

    /**
     * Keeps track of whether a decorator of a profile application file has already been created.
     */
    private final Map<IFile, ProfileApplicationDecorator> fileToDecoratorMap = new HashMap<IFile, ProfileApplicationDecorator>();

    /**
     * Hide default constructor
     */
    private ProfileApplicationFileRegistryImpl() {

        // TODO: remove logger configuration
        PatternLayout layout = new PatternLayout("%d{HH:mm:ss,SSS} [%t] %-5p %c - %m%n");
        ConsoleAppender appender = new ConsoleAppender(layout);
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure(appender);

        if (logger.isDebugEnabled()) {
            logger.debug("creating profile application file registry ...");
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

        if (logger.isDebugEnabled()) {
            logger.debug("... created profile application file registry");
        }
    }

    @Override
    public Collection<ProfileApplicationDecorator> getAllExistingProfileApplicationDecorators(
            final EStereotypableObject eStereotypableObject) {
        String modelURI = eStereotypableObject.eResource().getURI().toString();

        initializeProfileToDecoratorMap(modelURI);

        setUpProfileApplicationDecoratorsForExistingFiles(eStereotypableObject);

        return modelToDecoratorMap.get(modelURI).values();
    }

    @Override
    public ProfileApplicationDecorator getExistingProfileApplicationDecorator(
            final EStereotypableObject eStereotypableObject, final Profile profile) {

        if (eStereotypableObject == null || eStereotypableObject.eResource() == null) {
            logger.error("EStereotypableObject or its resource is null");
            // TODO check whether it is better to throw an Exception when the method is used wrongly
            return null;
        }

        // use profile from profile registry resource set
        final Profile profileFromRegistryResourceSet = Helper.getProfile(profile.getName());

        addRegistryAsObserverToAllProfileProjectBuilders();

        // first try to find the decorators in the map

        String modelURI = eStereotypableObject.eResource().getURI().toString();
        Map<Profile, ProfileApplicationDecorator> profileToDecoratorMap = modelToDecoratorMap.get(modelURI);

        if (profileToDecoratorMap != null && profileFromRegistryResourceSet != null
                && profileToDecoratorMap.containsKey(profileFromRegistryResourceSet)) {

            return profileToDecoratorMap.get(profileFromRegistryResourceSet);
        }

        initializeProfileToDecoratorMap(modelURI);

        setUpProfileApplicationDecoratorsForExistingFiles(eStereotypableObject);

        if (logger.isDebugEnabled()) {
            if (!modelToDecoratorMap.get(eStereotypableObject.eResource().getURI().toString()).containsKey(profileFromRegistryResourceSet)) {
                logger.debug("no decorator for profile '" + profileFromRegistryResourceSet.getName() + "'");
            }
        }

        return modelToDecoratorMap.get(eStereotypableObject.eResource().getURI().toString()).get(profileFromRegistryResourceSet);
    }

    @Override
    public ProfileApplicationDecorator getOrCreateProfileApplicationDecorator(
            final EStereotypableObject eStereotypableObject, final Profile profile) {

        // use profile from profile registry resource set
        final Profile profileFromRegistryResourceSet = Helper.getProfile(profile.getName());

        ProfileApplicationDecorator existingPAD = getExistingProfileApplicationDecorator(eStereotypableObject,
                profileFromRegistryResourceSet);

        if (existingPAD == null) {

            // If a new profile application file is created, it must be ensured that the profile
            // project nature is set to the project containing the file.
            // The nature is needed so that the registry is notified about existing profile
            // application files in the project.
            final IProject project = findProject(eStereotypableObject);

            if (!Helper.hasProfileProjectNature(project, ProfileProjectNature.NATURE_ID)) {
                Helper.addNature(project, ProfileProjectNature.NATURE_ID);
                if (logger.isDebugEnabled()) {
                    logger.debug("profile project nature added to project " + project.getName());
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("profile project nature exists for project " + project.getName());
                }
            }

            final IFile newProfileApplicationFile = createProfileApplicationFile(eStereotypableObject,
                    profileFromRegistryResourceSet.getName());

            logger.debug("created profile application file '" + newProfileApplicationFile.getFullPath() + "'");

            existingPAD = setUpProfileApplicationDecoratorForNewFile(eStereotypableObject,
                    profileFromRegistryResourceSet, newProfileApplicationFile);
        }

        return existingPAD;
    }

    private void addProfileApplicationFile(final IFile profileApplicationFile) {
        if (!profileApplicationFiles.contains(profileApplicationFile)) {
            profileApplicationFiles.add(profileApplicationFile);
            logger.debug("method=addProfileApplicationFile | profileApplicationFile="
                    + profileApplicationFile.getFullPath());
        } else {
            logger.debug("file '" + profileApplicationFile.getFullPath() + "' already registered");
        }
    }

    private ProfileApplicationDecorator setUpProfileApplicationDecoratorForExistingFile(
            final IFile profileApplicationFile, final EStereotypableObject eStereotypableObject) {

        ProfileApplicationDecorator profileApplicationDecorator = null;

        try {
            profileApplicationDecorator = new ProfileApplicationDecoratorImpl(profileApplicationFile,
                    eStereotypableObject.eResource().getResourceSet());
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("the file '" + profileApplicationFile.getFullPath()
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
                logger.error("expected exactly one profile application for decorator '"
                        + profileApplicationDecorator.getName() + "', actually "
                        + profileApplicationDecorator.getProfileApplications().size() + " profile applications found");
            }

            final ProfileApplication profileApplication = profileApplicationDecorator.getProfileApplications().get(0);

            if (profileApplication.getImportedProfiles().size() != 1) {
                logger.error("expected exacactly one imported profile for decorator '"
                        + profileApplicationDecorator.getName() + "', actually "
                        + profileApplication.getImportedProfiles().size() + " imported profiles found");
            }

            final Profile profile = ProfileImportResolver.resolve(profileApplication.getImportedProfiles().get(0),
                    Helper.getProfileRegistryResourceSet());

            if (!profileApplicationDecorator.getStereotypeApplications(eStereotypableObject).isEmpty()) {

                fileToDecoratorMap.put(profileApplicationFile, profileApplicationDecorator);

                String modelURI = eStereotypableObject.eResource().getURI().toString();

                modelToDecoratorMap.get(modelURI).put(profile, profileApplicationDecorator);

                logger.debug("created decorator for existing file '" + profileApplicationFile.getFullPath() + "'");

            } else {

                logger.debug("discarded created decorator '" + profileApplicationDecorator.getName()
                        + "': no stereotype application(s) for EStereotypableObject '" + eStereotypableObject + "'");

                profileApplicationDecorator = null;

                final Iterator<Resource> iterator = eStereotypableObject.eResource().getResourceSet().getResources()
                        .iterator();

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
            logger.error("failed to create profile application decorator for file '"
                    + profileApplicationFile.getFullPath() + "'");
        }

        return profileApplicationDecorator;
    }

    private void setUpProfileApplicationDecoratorsForExistingFiles(final EStereotypableObject eStereotypableObject) {
        addRegistryAsObserverToAllProfileProjectBuilders();

        for (final IFile profileApplicationFile : this.profileApplicationFiles) {
            if (!fileToDecoratorMap.containsKey(profileApplicationFile)) {
                setUpProfileApplicationDecoratorForExistingFile(profileApplicationFile, eStereotypableObject);
            }
        }
    }

    private ProfileApplicationDecorator setUpProfileApplicationDecoratorForNewFile(
            final EStereotypableObject eStereotypableObject, final Profile profile,
            final IFile newProfileApplicationFile) {

        // add only one profile to the collection of profiles so that the decorator is
        // responsible for one model/profile combination
        final Collection<Profile> profiles = new ArrayList<Profile>();
        profiles.add(profile);

        ProfileApplicationDecorator profileApplicationDecorator = null;

        try {
            profileApplicationDecorator = new ProfileApplicationDecoratorImpl(newProfileApplicationFile, profiles,
                    eStereotypableObject.eResource().getResourceSet());
        } catch (CoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        if (profileApplicationDecorator != null) {
            logger.debug("created decorator for new file '" + newProfileApplicationFile.getFullPath() + "'");

            fileToDecoratorMap.put(newProfileApplicationFile, profileApplicationDecorator);

            String modelURI = eStereotypableObject.eResource().getURI().toString();

            modelToDecoratorMap.get(modelURI).put(profile, profileApplicationDecorator);

        } else {
            logger.error("unable to create decorator for new file '" + newProfileApplicationFile.getFullPath() + "'");

            try {
                logger.debug("deleting profile application file '" + newProfileApplicationFile.getFullPath() + "' ...");
                newProfileApplicationFile.delete(true, null);
            } catch (CoreException e) {
                logger.error("failed to delete profile application file '" + newProfileApplicationFile.getFullPath()
                        + "'");
            }
        }

        return profileApplicationDecorator;
    }

    /**
     * Initializes the profile-to-decorator map for the specified resource.
     * 
     * @param modelURI
     *            The String representation of the URI of the model resource for which to initialize
     *            the map.
     */
    private void initializeProfileToDecoratorMap(final String modelURI) {

        Map<Profile, ProfileApplicationDecorator> profileToDecoratorMap = modelToDecoratorMap.get(modelURI);

        if (profileToDecoratorMap == null) {
            profileToDecoratorMap = new HashMap<Profile, ProfileApplicationDecorator>();
            modelToDecoratorMap.put(modelURI, profileToDecoratorMap);
        }
    }

    private void addRegistryAsObserverToAllProfileProjectBuilders() {

        final Collection<ProfileProjectBuilder> profileProjectBuilders = ProfileProjectBuilder
                .getAllProfileProjectBuilders();

        if (logger.isDebugEnabled()) {
            logger.debug(profileProjectBuilders.size() + " active profile project builder(s)");
        }

        for (final ProfileProjectBuilder builder : profileProjectBuilders) {
            if (builder.addObserver(this)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("registry attached to builder of project '" + builder.getProject().getName() + "'");
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
            logger.error("determining project by EStereotypableObject's resource only possible with platform resource URI");
            return null;
        }

        final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

        final IPath path = Helper.createPath(uri);

        final IFile file = root.getFile(path);

        return file.getProject();
    }

    private IFile createProfileApplicationFile(final EStereotypableObject eStereotypableObject, final String profileName) {

        final String fileName = createProfileApplicationFileName(eStereotypableObject, profileName);

        final URI profileApplicationFileUri = createProfileApplicationFileURI(eStereotypableObject, fileName);

        final IPath path = Helper.createPath(profileApplicationFileUri);

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
     * @return
     */
    private String createProfileApplicationFileName(final EStereotypableObject eStereotypableObject,
            final String profileName) {
        URI modelURI = eStereotypableObject.eResource().getURI();

        modelURI = modelURI.trimFileExtension();

        final String modelName = modelURI.lastSegment();

        logger.debug("modelName=" + modelName + " | profileName=" + profileName);

        return new String(modelName + "." + profileName + ".pa.xmi");
    }

    private URI createProfileApplicationFileURI(final EStereotypableObject eStereotypableObject, final String fileName) {
        // create IPath for profile application file by determining the EMF URI of the resource of
        // specified EStereotypableObject
        URI modelURI = eStereotypableObject.eResource().getURI();

        return URI.createPlatformResourceURI(modelURI.trimSegments(1).toPlatformString(false) + "/" + fileName, true);
    }

    @Override
    public void update(IFile file) {
        addProfileApplicationFile(file);
    }

    /**
     * {@inheritDoc}
     * 
     * It detaches the profile application file registry from all builders.
     */
    @Override
    public void clear() {
        fileToDecoratorMap.clear();
        modelToDecoratorMap.clear();
        profileApplicationFiles.clear();

        final Collection<ProfileProjectBuilder> profileProjectBuilders = ProfileProjectBuilder
                .getAllProfileProjectBuilders();

        for (ProfileProjectBuilder builder : profileProjectBuilders) {
            builder.removeObserver(this);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("cleared profile application file registry");
        }
    }
}
