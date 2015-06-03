package org.palladiosimulator.mdsdprofiles.ui.commands;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.modelversioning.emfprofile.Stereotype;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;

public class UpdateStereotypeElementsCommand extends ChangeCommand {

    private final StereotypableElement stereotypableElement;
    private final EList<Stereotype> updatedStereotypeElements;

    private UpdateStereotypeElementsCommand(final Notifier notifier, final StereotypableElement stereotypableElement,
            final EList<Stereotype> updatedStereotypeableElements) {
        super(notifier);

        this.stereotypableElement = stereotypableElement;
        this.updatedStereotypeElements = updatedStereotypeableElements;
    }

    public static UpdateStereotypeElementsCommand create(final StereotypableElement stereotypableElement,
            final EList<Stereotype> updatedStereotypableElements) {
        return new UpdateStereotypeElementsCommand(stereotypableElement.eResource(), stereotypableElement,
                updatedStereotypableElements);
    }

    @Override
    protected void doExecute() {
        this.stereotypableElement.updateStereotypeApplications(this.updatedStereotypeElements);
    }

    @Override
    public String getLabel() {
        return "Apply/Unapply Stereotypes";
    }

    @Override
    public String getDescription() {
        return "Configure stereotypes applications";
    }

    @Override
    public Collection<?> getAffectedObjects() {
        return Collections.singleton(this.stereotypableElement);
    }

}
