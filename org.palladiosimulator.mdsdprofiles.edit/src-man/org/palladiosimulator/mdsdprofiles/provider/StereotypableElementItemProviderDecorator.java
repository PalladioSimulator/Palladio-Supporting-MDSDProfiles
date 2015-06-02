package org.palladiosimulator.mdsdprofiles.provider;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderDecorator;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;

/**
 * Customizes the item provider of stereotypable elements, e.g., to show a custom stereotype string
 * with guillemets.
 * 
 * @author Sebastian Lehrig, Steffen Becker
 */
public class StereotypableElementItemProviderDecorator extends ItemProviderDecorator implements
        IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider,
        IItemPropertySource, Adapter {

    protected final static List<Integer> EXLUDED_FEATURE_IDS = Arrays.asList(
            EMFProfileApplicationPackage.STEREOTYPE_APPLICATION__APPLIED_TO,
            EMFProfileApplicationPackage.STEREOTYPE_APPLICATION__PROFILE_APPLICATION,
            EMFProfileApplicationPackage.STEREOTYPE_APPLICATION__EXTENSION);

    public StereotypableElementItemProviderDecorator(final AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
     * An object is prefixed by all stereotype names; each surrounded by guillemets.
     */
    @Override
    public String getText(final Object object) {
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
    public List<IItemPropertyDescriptor> getPropertyDescriptors(final Object object) {
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

    private void addTaggedValuesPropertyDescriptors(final List<IItemPropertyDescriptor> propertyDescriptors,
            final StereotypableElement stereotypableElement) {
        for (final StereotypeApplication stereotypeApplication : stereotypableElement.getStereotypeApplications()) {
            final IItemPropertySource stereotypeApplicationPropertySource = (IItemPropertySource) this
                    .getAdapterFactory().adapt(stereotypeApplication, IItemPropertySource.class);

            final List<IItemPropertyDescriptor> stereotypePropertyDescriptors = stereotypeApplicationPropertySource
                    .getPropertyDescriptors(stereotypeApplication);

            for (final IItemPropertyDescriptor stereotypePropertyDescriptor : stereotypePropertyDescriptors) {
                final EStructuralFeature feature = (EStructuralFeature) stereotypePropertyDescriptor.getFeature(null);
                if (!EXLUDED_FEATURE_IDS.contains(feature.getFeatureID())) {
                    propertyDescriptors.add(new StereotypableElementItemPropertyDescriptorDecorator(
                            stereotypeApplication, stereotypePropertyDescriptor));
                }
            }
        }
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

    @Override
    public Notifier getTarget() {
        return ((Adapter) getDecoratedItemProvider()).getTarget();
    }

    @Override
    public void setTarget(Notifier newTarget) {
        ((Adapter) getDecoratedItemProvider()).setTarget(newTarget);
    }

}
