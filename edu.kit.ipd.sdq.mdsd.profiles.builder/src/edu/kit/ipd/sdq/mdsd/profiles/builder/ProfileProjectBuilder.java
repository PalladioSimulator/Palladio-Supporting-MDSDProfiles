/**
 *
 */
package edu.kit.ipd.sdq.mdsd.profiles.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import edu.kit.ipd.sdq.mdsd.profiles.util.observer.IObservable;
import edu.kit.ipd.sdq.mdsd.profiles.util.observer.IObserver;

/**
 * @author Matthias Eisenmann
 */
public class ProfileProjectBuilder extends IncrementalProjectBuilder implements
        IObservable<IFile> {

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = Logger
            .getLogger(ProfileProjectBuilder.class.getName());

    /**
     * The identifier of this builder.
     */
    public static final String BUILDER_ID = "edu.kit.ipd.sdq.mdsd.profiles"
            + ".builder.profileprojectbuilder";
    /**
     * The file extension for all profile application files.
     */
    public static final String PROFILE_APPLICATION_FILE_EXTENSION = "pa.xmi";

    /**
     * A list of alls observers for profile application files.
     */
    private final List<IObserver<IFile>> observers =
            new ArrayList<IObserver<IFile>>();

    // FIXME: find a better solution for accessing profile project builder in
    // ProfileApplicationFileRegistry
    /**
     * All builders.
     */
    private static final Collection<ProfileProjectBuilder> BUILDERS =
            new ArrayList<ProfileProjectBuilder>();

    /**
     * Constructor that adds the new builder to the static list of all builders.
     */
    public ProfileProjectBuilder() {
        BUILDERS.add(this);
    }

    /**
     * Returns all profile builders.
     * 
     * @return all builders
     */
    public static Collection<ProfileProjectBuilder>
            getAllProfileProjectBuilders() {
        return BUILDERS;
    }

    /**
     * A delta-based visitor that notifies observers in case of an addition of a
     * profile application file.
     * 
     * @author Matthias Eisenmann
     * 
     */
    class ProfileProjectResourceDeltaVisitor implements IResourceDeltaVisitor {

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.core.resources.IResourceDeltaVisitor
         *      #visit(org.eclipse.core.resources.IResourceDelta)
         */
        @Override
        public boolean visit(final IResourceDelta delta) throws CoreException {
            IResource resource = delta.getResource();
            switch (delta.getKind()) {
            case IResourceDelta.ADDED:
                // handle added resource
                if (isProfileApplicationFile(resource)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("project="
                                + ProfileProjectBuilder.this.getProject()
                                        .getName()
                                + " | method=ProfileProjectResourceDeltaVisitor"
                                + ".visit | kind=added | resource="
                                + resource.getFullPath());
                    }
                    notifyObservers((IFile) resource);
                }
                break;
            case IResourceDelta.REMOVED:
                // handle removed resource
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("project="
                            + ProfileProjectBuilder.this.getProject().getName()
                            + " | method=ProfileProjectResourceDeltaVisitor"
                            + ".visit | kind=removed | resource="
                            + resource.getFullPath());
                }
                break;
            case IResourceDelta.CHANGED:
                // handle changed resource
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("project="
                            + ProfileProjectBuilder.this.getProject().getName()
                            + " | method=ProfileProjectResourceDeltaVisitor"
                            + ".visit | kind=changed | resource="
                            + resource.getFullPath());
                }
                break;
            default:
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("project="
                            + ProfileProjectBuilder.this.getProject().getName()
                            + " | method=ProfileProjectResourceDeltaVisitor"
                            + ".visit | kind=UNKNOWN! | resource="
                            + resource.getFullPath());
                }
                break;
            }
            // return true to continue visiting children.
            return true;
        }
    }

    /**
     * A visitor for a given resource that notifies observers of the resource is
     * a profile application file. profile application file
     * 
     * @author Matthias Eisenmann
     * 
     */
    class ProfileProjectResourceVisitor implements IResourceVisitor {
        @Override
        public boolean visit(final IResource resource) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("project="
                        + ProfileProjectBuilder.this.getProject().getName()
                        + " | method=ProfileProjectResourceVisitor"
                        + ".visit | resource=" + resource.getFullPath());
            }

            if (isProfileApplicationFile(resource)) {
                notifyObservers((IFile) resource);
            }
            // return true to continue visiting children.
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final IProject[] build(final int kind,
            final Map<String, String> args, final IProgressMonitor monitor)
            throws CoreException {

        if (kind == FULL_BUILD) {
            fullBuild(monitor);
        } else {
            IResourceDelta delta = getDelta(getProject());
            if (delta == null) {
                fullBuild(monitor);
            } else {
                incrementalBuild(delta, monitor);
            }
        }
        return null;
    }

    /**
     * Configures the logger for this builder class.
     */
    private static void configureLogger() {

        PatternLayout layout =
                new PatternLayout("%d{HH:mm:ss,SSS} [%t] %-5p %c - %m%n");
        ConsoleAppender appender = new ConsoleAppender(layout);
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure(appender);

        // TODO: use logger configuration file
        // PropertyConfigurator.configureAndWatch("log4j.properties", 60 *
        // 1000);
    }

    /**
     * {@inheritDoc} <br/>
     * <br/>
     * 
     * At the builder's startup all profile application files which are
     * contained in the project for which the builder is configured for are
     * requested.
     */
    @Override
    protected final void startupOnInitialize() {
        super.startupOnInitialize();

        // add builder init logic here
        configureLogger();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("project=" + this.getProject().getName()
                    + " | method=startupOnInitialize");
        }

        Collection<IFile> files =
                findProfileApplicationFiles(this.getProject());

        for (IFile file : files) {
            notifyObservers(file);
        }
    }

    @Override
    protected final void clean(final IProgressMonitor monitor) {
        // add builder clean logic here
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("project=" + this.getProject().getName()
                    + " | method=clean");
        }
    }

    /**
     * Performs a full build.
     * 
     * @param monitor
     *            the progress monitor to be used
     * @throws CoreException
     */
    protected final void fullBuild(final IProgressMonitor monitor) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("project=" + this.getProject().getName()
                    + " | method=fullBuild");
        }

        try {
            getProject().accept(new ProfileProjectResourceVisitor());
        } catch (CoreException e) {
            LOGGER.error("project=" + this.getProject().getName()
                    + " | full build failed", e);
        }
    }

    /**
     * Performs an incremental build using the given delta.
     * 
     * @param delta
     *            the delta to be used
     * @param monitor
     *            the progress monitor to be used
     * @throws CoreException
     *             if the visitor fails on the delta
     */
    protected final void incrementalBuild(final IResourceDelta delta,
            final IProgressMonitor monitor) throws CoreException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("project=" + this.getProject().getName()
                    + " | method=incrementalBuild");
        }
        // the visitor does the work.
        delta.accept(new ProfileProjectResourceDeltaVisitor());
    }

    /**
     * Finds all profile application files in the given project.
     * 
     * @param project
     *            the project to be searched
     * @return a collection of all profile application files
     */
    public static Collection<IFile> findProfileApplicationFiles(
            final IProject project) {
        final Collection<IFile> files = new ArrayList<IFile>();

        if (project != null) {

            // visitor for finding all files contained in the project
            final IResourceVisitor resourceVisitor = new IResourceVisitor() {
                @Override
                public boolean visit(final IResource resource)
                        throws CoreException {
                    if (isProfileApplicationFile(resource)) {
                        files.add((IFile) resource);
                    }
                    return true;
                }
            };

            try {
                project.accept(resourceVisitor);
            } catch (CoreException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return files;
    }

    /**
     * Determines whether the specified resource is a profile application file.
     * It's assumed that all file resources with file extension "pa.xmi" are
     * profile application files. The file content is not considered.
     * 
     * @param resource
     *            The resource in question.
     * @return True if resource is a file with extension "pa.xmi", otherwise
     *         false.
     */
    public static boolean isProfileApplicationFile(final IResource resource) {
        return (resource instanceof IFile && resource.getName().endsWith(
                PROFILE_APPLICATION_FILE_EXTENSION));
    }

    @Override
    public final boolean addObserver(final IObserver<IFile> observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);

            Collection<IFile> files =
                    findProfileApplicationFiles(this.getProject());

            for (IFile file : files) {
                notifyObservers(file);
            }

            return true;
        }

        return false;
    }

    @Override
    public final void removeObserver(final IObserver<IFile> observer) {
        observers.remove(observer);
    }

    @Override
    public final void notifyObservers(final IFile file) {
        for (final IObserver<IFile> observer : observers) {
            observer.update(file);
        }

    }
}
