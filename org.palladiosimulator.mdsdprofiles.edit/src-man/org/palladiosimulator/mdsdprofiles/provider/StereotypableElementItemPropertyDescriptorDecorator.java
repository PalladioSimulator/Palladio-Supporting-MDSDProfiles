package org.palladiosimulator.mdsdprofiles.provider;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptorDecorator;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

public class StereotypableElementItemPropertyDescriptorDecorator extends ItemPropertyDescriptorDecorator {

    protected final StereotypeApplication stereotypeApplication;

    public StereotypableElementItemPropertyDescriptorDecorator(final Object object,
            IItemPropertyDescriptor itemPropertyDescriptor) {
        super(object, itemPropertyDescriptor);

        this.stereotypeApplication = (StereotypeApplication) object;
    }

    @Override
    public String getDisplayName(Object thisObject) {
        return stereotypeApplication.getStereotype().getName() + "::" + super.getDisplayName(thisObject);
    }

    @Override
    public void setPropertyValue(Object thisObject, Object value) {
        // FIXME Executing this command should not alter the selection [Lehrig]
        final EditingDomain editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(thisObject);
        final Command command = SetCommand.create(editingDomain, stereotypeApplication,
                super.getFeature(stereotypeApplication), value);
        editingDomain.getCommandStack().execute(command);
        // stereotypeApplication.eSet((EStructuralFeature) super.getFeature(stereotypeApplication),
        // value);
        // this.itemPropertyDescriptor.setPropertyValue(stereotypeApplication, value);
    }

    @Override
    public Object getPropertyValue(Object thisObject) {
        return this.itemPropertyDescriptor.getPropertyValue(stereotypeApplication);
    }

}
