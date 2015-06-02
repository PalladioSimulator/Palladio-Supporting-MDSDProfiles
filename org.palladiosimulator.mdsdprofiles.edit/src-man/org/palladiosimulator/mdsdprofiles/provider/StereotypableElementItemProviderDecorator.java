package org.palladiosimulator.mdsdprofiles.provider;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderDecorator;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;

public class StereotypableElementItemProviderDecorator extends ItemProviderDecorator implements
        IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider,
        IItemPropertySource, Adapter {

    public StereotypableElementItemProviderDecorator(final AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    @Override
    public String getText(Object object) {
        if (notStereotyped(object)) {
            return super.getText(object);
        }

        final StereotypableElement stereotypableElement = (StereotypableElement) object;
        final StringBuilder stringBuilder = new StringBuilder();

        for (final Stereotype stereotype : stereotypableElement.getAppliedStereotypes()) {
            stringBuilder.append("«").append(stereotype.getName()).append("» ");
        }

        return stringBuilder.append(super.getText(object)).toString();
    }

    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
        if (notStereotyped(object)) {
            return super.getPropertyDescriptors(object);
        }

        final StereotypableElement stereotypableElement = (StereotypableElement) object;
        final List<IItemPropertyDescriptor> propertyDescriptors = new LinkedList<IItemPropertyDescriptor>(
                super.getPropertyDescriptors(stereotypableElement));
        addTaggedValuesPropertyDescriptors(propertyDescriptors, stereotypableElement);

        return propertyDescriptors;
    }

    private boolean notStereotyped(Object object) {
        return !(object instanceof StereotypableElement)
                || !((StereotypableElement) object).hasStereotypeApplications();
    }

    @Override
    public IItemPropertyDescriptor getPropertyDescriptor(Object object, Object propertyId) {
        if (notStereotyped(object)) {
            return super.getPropertyDescriptor(object, propertyId);
        }

        for (final IItemPropertyDescriptor propertyDescriptor : getPropertyDescriptors(object)) {
            if (propertyDescriptor.getId(object).equals(propertyId)) {
                return propertyDescriptor;
            }
        }

        throw new RuntimeException("Problem finding property ID -- no clue why :(");
    }

    private void addTaggedValuesPropertyDescriptors(final List<IItemPropertyDescriptor> propertyDescriptors,
            final StereotypableElement stereotypableElement) {
        for (final StereotypeApplication stereotypeApplication : stereotypableElement.getStereotypeApplications()) {
            final IItemPropertySource stereotypeApplicationPropertySource = (IItemPropertySource) this
                    .getAdapterFactory().adapt(stereotypeApplication, IItemPropertySource.class);

            final List<IItemPropertyDescriptor> stereotypePropertyDescriptors = stereotypeApplicationPropertySource
                    .getPropertyDescriptors(stereotypeApplication);
            propertyDescriptors.addAll(stereotypePropertyDescriptors);
        }
    }

    @Override
    public Notifier getTarget() {
        return ((Adapter) getDecoratedItemProvider()).getTarget();
    }

    @Override
    public void setTarget(Notifier newTarget) {
        ((Adapter) getDecoratedItemProvider()).setTarget(newTarget);
    }

}
