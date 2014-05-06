/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.kit.ipd.sdq.mdsd.profiles.nature.ProfileProjectNature;

/**
 * @author Matthias Eisenmann
 * 
 */
public class ToggleNatureHandler extends AbstractHandler {

    private QualifiedName path = new QualifiedName("html", "path");

    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell shell = HandlerUtil.getActiveShell(event);
        ISelection selection = HandlerUtil.getActiveMenuSelection(event);
        IStructuredSelection structuredSelection = (IStructuredSelection) selection;

        Object firstElement = structuredSelection.getFirstElement();
        if (firstElement instanceof IProject) {
            // createOutput(shell, firstElement);
            IProject project = (IProject) firstElement;
            System.out.println(path);
            toggleNature(project);

        } else {
            MessageDialog.openInformation(shell, "Info", "Please select a project folder");
        }
        return null;
    }

    /**
     * Toggles sample nature on a project
     * 
     * @param project
     *            to have sample nature added or removed
     */
    private void toggleNature(final IProject project) {
        try {
            IProjectDescription description = project.getDescription();
            String[] natures = description.getNatureIds();

            System.out.println("DBG: ToggleNatureAction.toggleNature");
            System.out.println("DBG: nature ids: ");

            for (int i = 0; i < natures.length; ++i) {
                if (ProfileProjectNature.NATURE_ID.equals(natures[i])) {
                    // Remove the nature
                    System.out.println("DBG: --- " + natures[i]);
                    String[] newNatures = new String[natures.length - 1];
                    System.arraycopy(natures, 0, newNatures, 0, i);
                    System.arraycopy(natures, i + 1, newNatures, i, natures.length - i - 1);
                    description.setNatureIds(newNatures);
                    project.setDescription(description, null);
                    return;
                }
            }

            // Add the nature
            String[] newNatures = new String[natures.length + 1];
            System.arraycopy(natures, 0, newNatures, 0, natures.length);
            newNatures[natures.length] = ProfileProjectNature.NATURE_ID;
            description.setNatureIds(newNatures);
            project.setDescription(description, null);
        } catch (CoreException e) {
        }
    }
}
