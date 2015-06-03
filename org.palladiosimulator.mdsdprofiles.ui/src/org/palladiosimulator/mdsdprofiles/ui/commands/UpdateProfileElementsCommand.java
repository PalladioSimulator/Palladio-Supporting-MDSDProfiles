package org.palladiosimulator.mdsdprofiles.ui.commands;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.modelversioning.emfprofile.Profile;
import org.palladiosimulator.mdsdprofiles.ProfileableElement;

public class UpdateProfileElementsCommand extends ChangeCommand {

    private final ProfileableElement profileableElement;
    private final EList<Profile> updatedProfileElements;

    private UpdateProfileElementsCommand(final Notifier notifier, final ProfileableElement profileableElement,
            final EList<Profile> updatedProfileElements) {
        super(notifier);

        this.profileableElement = profileableElement;
        this.updatedProfileElements = updatedProfileElements;
    }

    public static UpdateProfileElementsCommand create(final ProfileableElement profileableElement,
            final EList<Profile> updatedProfileElements) {
        return new UpdateProfileElementsCommand(profileableElement.eResource(), profileableElement,
                updatedProfileElements);
    }

    @Override
    protected void doExecute() {
        this.profileableElement.updateProfileApplications(this.updatedProfileElements);
    }

    @Override
    public String getLabel() {
        return "Apply/Unapply Profile";
    }

    @Override
    public String getDescription() {
        return "Configure profile applications";
    }

    @Override
    public Collection<?> getAffectedObjects() {
        return Collections.singleton(this.profileableElement);
    }

}
