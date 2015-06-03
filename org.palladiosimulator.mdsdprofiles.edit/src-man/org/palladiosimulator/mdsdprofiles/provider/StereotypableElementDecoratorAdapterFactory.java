package org.palladiosimulator.mdsdprofiles.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.DecoratorAdapterFactory;
import org.eclipse.emf.edit.provider.IItemProviderDecorator;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.modelversioning.emfprofileapplication.EMFProfileApplicationPackage;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

/**
 * An adapter factory for stereotypable elements.
 * 
 * @author Sebastian Lehrig, Steffen Becker
 */
public class StereotypableElementDecoratorAdapterFactory extends DecoratorAdapterFactory {

    public StereotypableElementDecoratorAdapterFactory(final AdapterFactory decoratedAdapterFactory) {
        super(decoratedAdapterFactory);
    }

    @Override
    protected IItemProviderDecorator createItemProviderDecorator(final Object target, final Object type) {
        final StereotypableElementItemProviderDecorator stereotypableElementItemProviderDecorator = new StereotypableElementItemProviderDecorator(
                this);

        if (((Class<?>) type).isInstance(stereotypableElementItemProviderDecorator)) {
            stereotypableElementItemProviderDecorator.addListener(new INotifyChangedListener() {

                @Override
                public void notifyChanged(final Notification notification) {
                    fireNotifyChanged(notification);

                    if (notification.getFeature() == EMFProfileApplicationPackage.eINSTANCE
                            .getProfileApplication_StereotypeApplications()) {
                        Object elementToUpdate = null;
                        if (notification.getEventType() == Notification.REMOVE) {
                            elementToUpdate = ((StereotypeApplication) notification.getOldValue()).getAppliedTo();
                        } else if (notification.getEventType() == Notification.ADD) {
                            elementToUpdate = ((StereotypeApplication) notification.getNewValue()).getAppliedTo();
                        }

                        if (elementToUpdate != null) {
                            // Request a label update of the Annotation's parent object
                            fireNotifyChanged(new ViewerNotification(notification, elementToUpdate, false, true));
                        }
                    }
                }
            });
            return stereotypableElementItemProviderDecorator;
        }
        return null;
    }
}
