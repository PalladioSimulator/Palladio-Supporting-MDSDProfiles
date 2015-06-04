package org.palladiosimulator.mdsdprofiles.ui.commands;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.modelversioning.emfprofile.Profile;
import org.palladiosimulator.mdsdprofiles.api.ProfileAPI;

public class UpdateProfileElementsCommand extends ChangeCommand {

    private final Resource profileApplicationStore;
    private final EList<Profile> updatedProfileElements;

    private UpdateProfileElementsCommand(final Resource profileApplicationStore,
            final EList<Profile> updatedProfileElements) {
        super(profileApplicationStore);

        this.profileApplicationStore = profileApplicationStore;
        this.updatedProfileElements = updatedProfileElements;
    }

    public static UpdateProfileElementsCommand create(final Resource profileApplicationStore,
            final EList<Profile> updatedProfileElements) {
        return new UpdateProfileElementsCommand(profileApplicationStore, updatedProfileElements);
    }

    @Override
    protected void doExecute() {
        ProfileAPI.updateProfileApplications(this.profileApplicationStore, this.updatedProfileElements);
    }

    @Override
    public String getLabel() {
        return "Apply/Unapply Profile";
    }

    @Override
    public String getDescription() {
        return "Configure profile applications";
    }
}
