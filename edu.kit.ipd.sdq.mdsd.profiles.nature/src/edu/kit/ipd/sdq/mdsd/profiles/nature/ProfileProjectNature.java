/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.nature;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import edu.kit.ipd.sdq.mdsd.profiles.builder.ProfileProjectBuilder;

/**
 * @author Matthias Eisenmann
 * 
 */
public class ProfileProjectNature implements IProjectNature {

    public static final String NATURE_ID = "edu.kit.ipd.sdq.mdsd.profiles.nature.profileprojectnature";

    /**
     * The project to which this project nature applies.
     */
    private IProject project;

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure() throws CoreException {
        // Add nature-specific information
        // for the project, such as adding a builder
        // to a project's build spec.

        IProjectDescription desc = project.getDescription();
        ICommand[] commands = desc.getBuildSpec();

        for (int i = 0; i < commands.length; ++i) {
            if (commands[i].getBuilderName().equals(ProfileProjectBuilder.BUILDER_ID)) {
                return;
            }
        }

        ICommand[] newCommands = new ICommand[commands.length + 1];
        System.arraycopy(commands, 0, newCommands, 0, commands.length);
        ICommand command = desc.newCommand();
        command.setBuilderName(ProfileProjectBuilder.BUILDER_ID);
        newCommands[newCommands.length - 1] = command;
        desc.setBuildSpec(newCommands);
        project.setDescription(desc, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deconfigure() throws CoreException {
        // Remove the nature-specific information here.
        System.out.println("DBG: ProfileProjectNature.deconfigure");
        IProjectDescription description = getProject().getDescription();
        ICommand[] commands = description.getBuildSpec();
        for (int i = 0; i < commands.length; ++i) {
            if (commands[i].getBuilderName().equals(ProfileProjectBuilder.BUILDER_ID)) {
                ICommand[] newCommands = new ICommand[commands.length - 1];
                System.arraycopy(commands, 0, newCommands, 0, i);
                System.arraycopy(commands, i + 1, newCommands, i, commands.length - i - 1);
                description.setBuildSpec(newCommands);
                project.setDescription(description, null);
                return;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IProject getProject() {
        return project;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setProject(IProject project) {
        this.project = project;

    }

}
