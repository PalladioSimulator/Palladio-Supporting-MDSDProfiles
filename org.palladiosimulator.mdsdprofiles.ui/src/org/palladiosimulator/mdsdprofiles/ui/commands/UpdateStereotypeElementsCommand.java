package org.palladiosimulator.mdsdprofiles.ui.commands;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.ChangeCommand;
import org.modelversioning.emfprofile.Stereotype;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;

public class UpdateStereotypeElementsCommand extends ChangeCommand {

    private final EObject stereotypedElement;
    private final EList<Stereotype> updatedStereotypeElements;

    private UpdateStereotypeElementsCommand(final Notifier notifier, final EObject stereotypedElement,
            final EList<Stereotype> updatedStereotypeableElements) {
        super(notifier);

        this.stereotypedElement = stereotypedElement;
        this.updatedStereotypeElements = updatedStereotypeableElements;
    }

    public static UpdateStereotypeElementsCommand create(final EObject stereotypableElement,
            final EList<Stereotype> updatedStereotypableElements) {
        return new UpdateStereotypeElementsCommand(stereotypableElement.eResource(), stereotypableElement,
                updatedStereotypableElements);
    }

    @Override
    protected void doExecute() {
        StereotypeAPI.updateStereotypeApplications(this.stereotypedElement, this.updatedStereotypeElements);
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
        return Collections.singleton(this.stereotypedElement);
    }

}
