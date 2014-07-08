package edu.kit.ipd.sdq.mdsd.profiles.ui.menu;

import java.util.Comparator;

import org.modelversioning.emfprofile.Stereotype;

/**
 * A helper for lexicographical comparison of stereotypes based on their names.
 * 
 * @author Max Kramer
 * 
 */
public class ComparatorHelper {
    /**
     * Compares two stereotypes lexicographically based on their names.
     * 
     * @param stereotype1
     *            a stereotype
     * @param stereotype2
     *            a stereotype
     * @return the result of {@link java.lang.String#compareTo(String)}
     */
    public static int compare(final Stereotype stereotype1,
            final Stereotype stereotype2) {
        String attr1 = stereotype1.getName();
        String attr2 = stereotype2.getName();
        return compare(attr1, attr2);
    }

    /**
     * Null-safe comparison of strings based on
     * {@link java.lang.String#compareTo(String)}
     * 
     * @param attr1
     *            a string
     * @param attr2
     *            another string
     * @return the result of {@link java.lang.String#compareTo(String)}
     */
    public static int compare(final String attr1, final String attr2) {
        if (attr1 != null) {
            return attr1.compareTo(attr2);
        } else if (attr2 != null) {
            return attr2.compareTo(attr1);
        } else {
            // attr1 == null && attr2 == null
            return 0;
        }
    }

    /**
     * A Comparator that compares two stereotypes lexicographically based on
     * their names.
     * 
     * @return the comparator
     */
    public static Comparator<Stereotype> getStereotypeComparator() {
        return new Comparator<Stereotype>() {
            @Override
            public int compare(final Stereotype stereotype1,
                    final Stereotype stereotype2) {
                return ComparatorHelper.compare(stereotype1, stereotype2);
            }
        };
    }
}
