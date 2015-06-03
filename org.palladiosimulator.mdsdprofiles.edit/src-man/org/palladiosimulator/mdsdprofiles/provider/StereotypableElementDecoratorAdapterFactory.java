package org.palladiosimulator.mdsdprofiles.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.DecoratorAdapterFactory;
import org.eclipse.emf.edit.provider.IItemProviderDecorator;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.ViewerNotification;
import org.palladiosimulator.mdsdprofiles.notifier.MDSDProfilesNotifier;

/**
 * An adapter factory for stereotypable elements. Takes care of firing notifications about
 * stereotype apply and unapply operations, e.g., needed for label updates within viewers.
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

                    if (notification.getEventType() == MDSDProfilesNotifier.APPLY_STEREOTYPE
                            || notification.getEventType() == MDSDProfilesNotifier.UNAPPLY_STEREOTYPE) {
                        fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                    }

                }
            });
            return stereotypableElementItemProviderDecorator;
        }
        return null;
    }
}
