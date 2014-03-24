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
 * 
 */
public class ProfileProjectBuilder extends IncrementalProjectBuilder implements IObservable<IFile> {

    private static final Logger logger = Logger.getLogger(ProfileProjectBuilder.class.getName());

    public static final String BUILDER_ID = "edu.kit.ipd.sdq.mdsd.profiles.builder.profileprojectbuilder";

    public static final String PROFILE_APPLICATION_FILE_EXTENSION = "pa.xmi";

    private final List<IObserver<IFile>> observers = new ArrayList<IObserver<IFile>>();

    // FIXME: find a better solution for accessing profile project builder in
    // ProfileApplicationFileRegistry
    private static final Collection<ProfileProjectBuilder> builders = new ArrayList<ProfileProjectBuilder>();

    public ProfileProjectBuilder() {
        builders.add(this);
    }

    public static Collection<ProfileProjectBuilder> getAllProfileProjectBuilders() {
        return builders;
    }

    class ProfileProjectResourceDeltaVisitor implements IResourceDeltaVisitor {

        /**
         * {@inheritDoc}
         * 
         * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
         */
        public boolean visit(IResourceDelta delta) throws CoreException {
            IResource resource = delta.getResource();
            switch (delta.getKind()) {
            case IResourceDelta.ADDED: {
                // handle added resource
                if (isProfileApplicationFile(resource)) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("project=" + ProfileProjectBuilder.this.getProject().getName()
                                + " | method=ProfileProjectResourceDeltaVisitor.visit | kind=added | resource="
                                + resource.getFullPath());
                    }
                    notifyObservers((IFile) resource);
                }
                break;
            }
            case IResourceDelta.REMOVED: {
                // handle removed resource
                if (logger.isDebugEnabled()) {
                    logger.debug("project=" + ProfileProjectBuilder.this.getProject().getName()
                            + " | method=ProfileProjectResourceDeltaVisitor.visit | kind=removed | resource="
                            + resource.getFullPath());
                }
                break;
            }
            case IResourceDelta.CHANGED: {
                // handle changed resource
                if (logger.isDebugEnabled()) {
                    logger.debug("project=" + ProfileProjectBuilder.this.getProject().getName()
                            + " | method=ProfileProjectResourceDeltaVisitor.visit | kind=changed | resource="
                            + resource.getFullPath());
                }
                break;
            }
            }
            // return true to continue visiting children.
            return true;
        }
    }

    class ProfileProjectResourceVisitor implements IResourceVisitor {
        public boolean visit(IResource resource) {
            if (logger.isDebugEnabled()) {
                logger.debug("project=" + ProfileProjectBuilder.this.getProject().getName()
                        + " | method=ProfileProjectResourceVisitor.visit | resource=" + resource.getFullPath());
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
    protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor) throws CoreException {

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

    private static void configureLogger() {

        PatternLayout layout = new PatternLayout("%d{HH:mm:ss,SSS} [%t] %-5p %c - %m%n");
        ConsoleAppender appender = new ConsoleAppender(layout);
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure(appender);

        // TODO: use logger configuration file
        // PropertyConfigurator.configureAndWatch("log4j.properties", 60 * 1000);
    }

    /**
     * {@inheritDoc}
     * 
     * <br/>
     * <br/>
     * 
     * At the builder's startup all profile application files which are contained in the project for
     * which the builder is configured for are requested.
     */
    @Override
    protected void startupOnInitialize() {
        super.startupOnInitialize();

        // add builder init logic here
        configureLogger();

        if (logger.isDebugEnabled()) {
            logger.debug("project=" + this.getProject().getName() + " | method=startupOnInitialize");
        }

        Collection<IFile> files = findProfileApplicationFiles(this.getProject());

        for (IFile file : files) {
            notifyObservers(file);
        }
    }

    protected void clean(IProgressMonitor monitor) {
        // add builder clean logic here
        if (logger.isDebugEnabled()) {
            logger.debug("project=" + this.getProject().getName() + " | method=clean");
        }
    }

    protected void fullBuild(final IProgressMonitor monitor) throws CoreException {
        if (logger.isDebugEnabled()) {
            logger.debug("project=" + this.getProject().getName() + " | method=fullBuild");
        }

        try {
            getProject().accept(new ProfileProjectResourceVisitor());
        } catch (CoreException e) {
            logger.error("project=" + this.getProject().getName() + " | full build failed", e);
        }
    }

    protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
        if (logger.isDebugEnabled()) {
            logger.debug("project=" + this.getProject().getName() + " | method=incrementalBuild");
        }
        // the visitor does the work.
        delta.accept(new ProfileProjectResourceDeltaVisitor());
    }

    public static Collection<IFile> findProfileApplicationFiles(final IProject project) {
        final Collection<IFile> files = new ArrayList<IFile>();

        if (project != null) {

            // visitor for finding all files contained in the project
            final IResourceVisitor resourceVisitor = new IResourceVisitor() {
                public boolean visit(final IResource resource) throws CoreException {
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
     * Determines whether the specified resource is a profile application file. It's assumed that
     * all file resources with file extension "pa.xmi" are profile application files. The file
     * content is not considered.
     * 
     * @param resource
     *            The resource in question.
     * @return True if resource is a file with extension "pa.xmi", otherwise false.
     */
    public static boolean isProfileApplicationFile(final IResource resource) {
        return (resource instanceof IFile && resource.getName().endsWith(PROFILE_APPLICATION_FILE_EXTENSION));
    }

    @Override
    public boolean addObserver(final IObserver<IFile> observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);

            Collection<IFile> files = findProfileApplicationFiles(this.getProject());

            for (IFile file : files) {
                notifyObservers(file);
            }

            return true;
        }

        return false;
    }

    @Override
    public void removeObserver(final IObserver<IFile> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(final IFile file) {
        for (final IObserver<IFile> observer : observers) {
            observer.update(file);
        }

    }
}