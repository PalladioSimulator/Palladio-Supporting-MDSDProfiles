package edu.kit.ipd.sdq.mdsd.profiles.ui;

/**
 * A utility class containing all constant ids and labels for the ui
 * contribution.
 * 
 * @author Matthias Eisenmann
 * 
 */
public final class ProfilesUIConstants {
    /**
     * Utility classes should not have a public or default constructor.
     */
    private ProfilesUIConstants() {
        // empty
    }

    public static final String APPLY_STEREO_PARAM_ID =
            "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.applyStereotypeCommand.parameter.stereotype";
    public static final String APPLY_PROFILE_PARAM_ID =
            "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.applyStereotypeCommand.parameter.profile";
    public static final String APPLY_ACTION_NAME = "application";

    public static final String UNAPPLY_STEREO_PARAM_ID =
            "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.unapplyStereotypeCommand.parameter.stereotype";
    public static final String UNAPPLY_PROFILE_PARAM_ID =
            "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.unapplyStereotypeCommand.parameter.profile";
    public static final String UNAPPLY_ACTION_NAME = "removal";

    public static final String MENU_LOCATION =
            "popup:org.eclipse.ui.popup.any?after=additions";
    public static final String PROFILE_LIST_MENU_ID =
            "edu.kit.ipd.sdq.mdsd.profiles.ui.menu.profilelistmenu";
    public static final String DYNAMIC_LIST_ID =
            "edu.kit.ipd.sdq.mdsd.profiles.ui.menu.dynamicprofilelistmenu";
    /**
     * This command id is also used in plugin.xml.
     */
    public static final String APPLY_COMMAND_ID =
            "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.applyStereotypeCommand";
    public static final String UNAPPLY_COMMAND_ID =
            "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.unapplyStereotypeCommand";
    /**
     * This stereotype command parameter id is also used in plugin.xml.
     */
    public static final String APPLY_STEREOTYPE_PARAMETER_ID =
            "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.applyStereotypeCommand.parameter.stereotype";
    public static final String UNAPPLY_STEREOTYPE_PARAMETER_ID =
            "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.unapplyStereotypeCommand.parameter.stereotype";
    /**
     * This profile command parameter id is also used in plugin.xml.
     */
    public static final String APPLY_PROFILE_PARAMETER_ID =
            "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.applyStereotypeCommand.parameter.profile";
    public static final String UNAPPLY_PROFILE_PARAMETER_ID =
            "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.unapplyStereotypeCommand.parameter.profile";
    public static final String CUSTOMCOMMANDLABEL_EXT_PT_ID =
            "edu.kit.ipd.sdq.mdsd.profiles.ui.customcommandlabel";
    public static final String STEREOTYPE_NAME_ATTRIBUTE = "stereotype_name";
    public static final String APPLY_LABEL_ATTR = "apply_label";
    public static final String UNAPPLY_LABEL_ATTR = "unapply_label";

    /**
     * Return the default label for the "unapply stereotype" action for the
     * given stereotype name.
     * 
     * @param stereotypeName
     *            the name of the stereotype to be labeled
     * @return the label
     */
    public static String getDefaultUnapplyLabel(final String stereotypeName) {
        return "Remove all applications of " + stereotypeName + " stereotype";
    }

    /**
     * Return the default label for the "apply stereotype" action for the given
     * stereotype name.
     * 
     * @param stereotypeName
     *            the name of the stereotype to be labeled
     * @return the label
     */
    public static String getDefaultApplyLabel(final String stereotypeName) {
        return "Apply " + stereotypeName + " stereotype";
    }
}