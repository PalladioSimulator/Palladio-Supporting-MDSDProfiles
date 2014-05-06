package edu.kit.ipd.sdq.mdsd.profiles.ui.menu;

import java.util.Comparator;

import org.modelversioning.emfprofile.Stereotype;

/**
 * @author Max Kramer
 * 
 */

public class ComparatorHelper {
    public static int compare(Stereotype confElem1, Stereotype confElem2) {
        String attr1 = confElem1.getName();
        String attr2 = confElem2.getName();
        return compare(attr1, attr2);
    }

    public static int compare(String attr1, String attr2) {
        if (attr1 != null) {
            return attr1.compareTo(attr2);
        } else if (attr2 != null) {
            return attr2.compareTo(attr1);
        } else {
            // attr1 == null && attr2 == null
            return 0;
        }
    }

    public static Comparator<Stereotype> getStereotypeComparator() {
        return new Comparator<Stereotype>() {
            @Override
            public int compare(Stereotype stereotype1, Stereotype stereotype2) {
                return ComparatorHelper.compare(stereotype1, stereotype2);
            }
        };
    }
}
