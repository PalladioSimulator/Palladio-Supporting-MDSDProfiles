package org.palladiosimulator.mdsdprofiles.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.DecoratorAdapterFactory;
import org.eclipse.emf.edit.provider.IItemProviderDecorator;
import org.eclipse.emf.edit.provider.INotifyChangedListener;

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

    // Method gets call only for every new item _type_ and not every element as we assume the ItemProviders
    // which we decorate to be stateless! In stateless case we should ignore the target parameter here!
    @Override
    protected IItemProviderDecorator createItemProviderDecorator(final Object target, final Object type) {
        final StereotypableElementItemProviderDecorator stereotypableElementItemProviderDecorator =
                new StereotypableElementItemProviderDecorator(this);

        // Listen to changes in the decorated item provider and forward them to our listeners
        // creating a listener chain
        stereotypableElementItemProviderDecorator.addListener(
                new INotifyChangedListener() {

                    @Override
                    public void notifyChanged(final Notification notification) {
                        // Propagate change event from the adapted item provider...
                        fireNotifyChanged(notification);
                    }
                });
        return stereotypableElementItemProviderDecorator;
    }

    // When delivering adapters from this factory, make sure they are registered as listeners on
    // their model elements to receive apply and unapply notifications
    @Override
    public Object adapt(final Object target, final Object type) {
        final Object result = super.adapt(target, type);

        if (result instanceof StereotypableElementItemProviderDecorator && target instanceof Notifier) {
            final StereotypableElementItemProviderDecorator decorator = (StereotypableElementItemProviderDecorator) result;
            final Notifier notifier = (Notifier) target;
            if (!notifier.eAdapters().contains(decorator)) {
                notifier.eAdapters().add(decorator);
            }
        }

        return result;
    }
}
