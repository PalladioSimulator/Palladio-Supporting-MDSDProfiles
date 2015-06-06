package org.palladiosimulator.mdsdprofiles.provider;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderDecorator;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;
import org.modelversioning.emfprofileapplication.ProfileApplication;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.mdsdprofiles.notifier.MDSDProfilesNotifier;

/**
 * Customizes the item provider of stereotyped elements, e.g., to show a custom stereotype string
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

        final EObject stereotypedElement = (EObject) object;
        final StringBuilder stringBuilder = new StringBuilder();

        for (final Stereotype stereotype : StereotypeAPI.getApplicableStereotypes(stereotypedElement)) {
            stringBuilder.append("«").append(stereotype.getName()).append("» ");
        }

        return stringBuilder.append(super.getText(object)).toString();
    }

    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(final Object object) {
        if (notStereotyped(object)) {
            return super.getPropertyDescriptors(object);
        }

        final EObject stereotypedElement = (EObject) object;
        final List<IItemPropertyDescriptor> propertyDescriptors = new LinkedList<IItemPropertyDescriptor>(
                super.getPropertyDescriptors(stereotypedElement));
        addTaggedValuesPropertyDescriptors(propertyDescriptors, stereotypedElement);

        return propertyDescriptors;
    }

    private void addTaggedValuesPropertyDescriptors(final List<IItemPropertyDescriptor> propertyDescriptors,
            final EObject stereotypedElement) {
        for (final StereotypeApplication stereotypeApplication : StereotypeAPI
                .getStereotypeApplications(stereotypedElement)) {
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
    public IItemPropertyDescriptor getPropertyDescriptor(final Object object, final Object propertyId) {
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
        return ((Adapter)getDecoratedItemProvider()).getTarget();
    }

    @Override
    public void setTarget(final Notifier newTarget) {
        ((Adapter)getDecoratedItemProvider()).setTarget(newTarget);
    }

    // Triggered by items for which this item provider provides labels, etc. to inform about updates
    @Override
    public void notifyChanged(final Notification notification) {
        if (isChangeOfStereotype(notification)) {
            informViewerAboutLabelUpdate(notification);
        } else {
            super.notifyChanged(notification);
        }
    }

    /**
     * @param object Object to check for stereotypes
     * @return true if the object is an EObject with stereotypes
     */
    private boolean notStereotyped(final Object object) {
        return !(object instanceof EObject) || !StereotypeAPI.hasStereotypeApplications((EObject) object);
    }

    /**
     * The viewer has to update the items label as due to stereotype changes the label needs to be
     * updated too
     * @param notification Change notification describing a stereotype change
     */
    private void informViewerAboutLabelUpdate(final Notification notification) {
        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
    }

    /**
     * @param notification The notification to inspect
     * @return true if notification was result of a stereotype modification
     */
    private boolean isChangeOfStereotype(final Notification notification) {
        return notification.getEventType() == MDSDProfilesNotifier.APPLY_STEREOTYPE
                || notification.getEventType() == MDSDProfilesNotifier.UNAPPLY_STEREOTYPE;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Collection<?> getChildren(final Object object) {
        final Collection<?> result = super.getChildren(object);
        final Collection newResult = new LinkedList();
        for (final Object o : result) {
            if (!(o instanceof ProfileApplication)) {
                newResult.add(o);
            }
        }
        return newResult;
    }
}
