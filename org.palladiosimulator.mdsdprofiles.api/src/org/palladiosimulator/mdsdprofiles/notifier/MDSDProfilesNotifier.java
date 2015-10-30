package org.palladiosimulator.mdsdprofiles.notifier;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.NotificationImpl;

public class MDSDProfilesNotifier extends NotificationImpl {

    public static final int APPLY_PROFILE = 1000;

    public static final int UNAPPLY_PROFILE = 1001;

    public static final int APPLY_STEREOTYPE = 1002;

    public static final int UNAPPLY_STEREOTYPE = 1003;

    public static final int SET_TAGGED_VALUE = 1004;

    public static final class TaggedValueTuple {

        private final String stereotypeName;
        private final String taggedValueName;
        private final Object value;

        public TaggedValueTuple(final String stereotypeName, final String taggedValueName, final Object value) {
            this.stereotypeName = stereotypeName;
            this.taggedValueName = taggedValueName;
            this.value = value;
        }

        public String getStereotypeName() {
            return this.stereotypeName;
        }

        public String getTaggedValueName() {
            return this.taggedValueName;
        }

        public Object getValue() {
            return this.value;
        }
    }

    private final Notifier changedElement;

    public MDSDProfilesNotifier(final Notifier changedElement, final int eventType, final Object value) {
        super(eventType, null, value);

        if (eventType == SET_TAGGED_VALUE && !(value instanceof TaggedValueTuple)) {
            throw new RuntimeException("SET_TAGGED_VALUE events have to pass a TaggedValueTuple!");
        }

        this.changedElement = changedElement;
    }

    @Override
    public Object getNotifier() {
        return this.changedElement;
    }

}
