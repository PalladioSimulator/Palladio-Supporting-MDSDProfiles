package org.palladiosimulator.mdsdprofiles.notifier;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.NotificationImpl;

public class MDSDProfilesNotifier extends NotificationImpl {

    public static final int APPLY_PROFILE = 1000;

    public static final int UNAPPLY_PROFILE = 1001;

    public static final int APPLY_STEREOTYPE = 1002;

    public static final int UNAPPLY_STEREOTYPE = 1003;

    private final Notifier changedElement;

    public MDSDProfilesNotifier(final Notifier changedElement, final int eventType, final Object value) {
        super(eventType, null, value);

        this.changedElement = changedElement;
    }

    @Override
    public Object getNotifier() {
        return this.changedElement;
    }

}
