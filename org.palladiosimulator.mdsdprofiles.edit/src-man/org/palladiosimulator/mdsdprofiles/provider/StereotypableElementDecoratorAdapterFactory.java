package org.palladiosimulator.mdsdprofiles.provider;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.DecoratorAdapterFactory;
import org.eclipse.emf.edit.provider.IItemProviderDecorator;

public class StereotypableElementDecoratorAdapterFactory extends DecoratorAdapterFactory {

    public StereotypableElementDecoratorAdapterFactory(final AdapterFactory decoratedAdapterFactory) {
        super(decoratedAdapterFactory);
    }

    @Override
    protected IItemProviderDecorator createItemProviderDecorator(final Object target, final Object type) {
        final StereotypableElementItemProviderDecorator stereotypableElementItemProviderDecorator = new StereotypableElementItemProviderDecorator(
                this);

        if (((Class<?>) type).isInstance(stereotypableElementItemProviderDecorator)) {
            // stereotypableElementItemProviderDecorator.addListener(new INotifyChangedListener() {
            //
            // @Override
            // public void notifyChanged(final Notification notification) {
            // fireNotifyChanged(notification);
            // }
            //
            // });
            return stereotypableElementItemProviderDecorator;
        }
        return null;
    }
}
