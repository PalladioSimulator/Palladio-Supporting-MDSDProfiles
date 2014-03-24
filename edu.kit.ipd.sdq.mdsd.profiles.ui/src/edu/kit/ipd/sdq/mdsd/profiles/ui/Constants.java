package edu.kit.ipd.sdq.mdsd.profiles.ui;

public class Constants {
	public static String APPLY_STEREO_PARAM_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.applyStereotypeCommand.parameter.stereotype";
	public static String APPLY_PROFILE_PARAM_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.applyStereotypeCommand.parameter.profile";
	public static String APPLY_ACTION_NAME = "application";
	
	public static String UNAPPLY_STEREO_PARAM_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.unapplyStereotypeCommand.parameter.stereotype";
	public static String UNAPPLY_PROFILE_PARAM_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.unapplyStereotypeCommand.parameter.profile";
	public static String UNAPPLY_ACTION_NAME = "removal";
	
	public static final String MENU_LOCATION = "popup:org.eclipse.ui.popup.any?after=additions";
	public static final String PROFILE_LIST_MENU_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.menu.profilelistmenu";
	public static final String DYNAMIC_LIST_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.menu.dynamicprofilelistmenu";
	/**
	 * This command id is also used in plugin.xml.
	 */
	public static final String APPLY_COMMAND_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.applyStereotypeCommand";
	public static final String UNAPPLY_COMMAND_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.unapplyStereotypeCommand";
	/**
	 * This stereotype command parameter id is also used in plugin.xml.
	 */
	public static final String APPLY_STEREOTYPE_PARAMETER_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.applyStereotypeCommand.parameter.stereotype";
	public static final String UNAPPLY_STEREOTYPE_PARAMETER_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.unapplyStereotypeCommand.parameter.stereotype";
	/**
	 * This profile command parameter id is also used in plugin.xml.
	 */
	public static final String APPLY_PROFILE_PARAMETER_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.applyStereotypeCommand.parameter.profile";
	public static final String UNAPPLY_PROFILE_PARAMETER_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.unapplyStereotypeCommand.parameter.profile";
	public static final String CUSTOMCOMMANDLABEL_EXT_PT_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.customcommandlabel";
	public static final String STEREOTYPE_NAME_ATTRIBUTE = "stereotype_name";
	public static final String APPLY_LABEL_ATTR = "apply_label";
	public static final String UNAPPLY_LABEL_ATTR = "unapply_label";
	public static String getDefaultUnapplyLabel(String currentStereotypeName) {
		return "Remove all applications of "
				+ currentStereotypeName + " stereotype";
	}
	public static String getDefaultApplyLabel(String currentStereotypeName) {
		return "Apply " + currentStereotypeName
				+ " stereotype";
	}
}
