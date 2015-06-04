package org.palladiosimulator.mdsdprofiles.provider;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandWrapper;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptorDecorator;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

/**
 * Customizes the properties of stereotypable elements, e.g., to add entries for tagged values to
 * the property sheet.
 * 
 * @author Sebastian Lehrig, Steffen Becker
 */
public class StereotypableElementItemPropertyDescriptorDecorator extends ItemPropertyDescriptorDecorator {

    protected final StereotypeApplication stereotypeApplication;

    public StereotypableElementItemPropertyDescriptorDecorator(final Object object,
            final IItemPropertyDescriptor itemPropertyDescriptor) {
        super(object, itemPropertyDescriptor);

        this.stereotypeApplication = (StereotypeApplication) object;
    }

    /**
     * Prefix tags with the stereotype name plus "::".
     */
    @Override
    public String getDisplayName(final Object thisObject) {
        return stereotypeApplication.getStereotype().getName() + "::" + super.getDisplayName(thisObject);
    }

    @Override
    public void setPropertyValue(final Object thisObject, final Object value) {
        final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(thisObject);
        final Command command = new CommandWrapper(SetCommand.create(editingDomain, stereotypeApplication,
                super.getFeature(stereotypeApplication), value)) {
            @Override
            public Collection<?> getAffectedObjects() {
                // Return this to select the original object instead of the stereotype application
                return Collections.singleton(thisObject);
            }
        };
        editingDomain.getCommandStack().execute(command);
    }

    @Override
    public Object getPropertyValue(final Object thisObject) {
        return this.itemPropertyDescriptor.getPropertyValue(stereotypeApplication);
    }

}
