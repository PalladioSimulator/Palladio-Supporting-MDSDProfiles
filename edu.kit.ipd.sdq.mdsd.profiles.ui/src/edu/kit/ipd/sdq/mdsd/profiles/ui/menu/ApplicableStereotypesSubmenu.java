/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.ui.menu;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.menus.IWorkbenchContribution;
import org.eclipse.ui.services.IServiceLocator;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.Stereotype;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
import edu.kit.ipd.sdq.mdsd.profiles.ui.ProfilesUIConstants;

/**
 * @author Matthias Eisenmann
 * 
 */

public class ApplicableStereotypesSubmenu extends CompoundContributionItem implements IWorkbenchContribution {

    private static final Logger LOGGER = Logger.getLogger(ApplicableStereotypesSubmenu.class.getName());

    static {
        /*
         * FIXME (from Lehrig) I commented-out this global (!!!) reset of the logger configuration.
         * It actually destroyed every PCM-based workflow; especially simulation durations increased
         * heavily since everything was logged. Please provide a logger configuration that is
         * consistent with other projects.
         */
        // TODO: remove logger configuration
        // PatternLayout layout =
        // new PatternLayout("%d{HH:mm:ss,SSS} [%t] %-5p %c - %m%n");
        // ConsoleAppender appender = new ConsoleAppender(layout);
        // BasicConfigurator.resetConfiguration();
        // BasicConfigurator.configure(appender);
    }

    /**
     * This command id is also used in plugin.xml.
     */
    private static final String COMMAND_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.applyStereotypeCommand";

    /**
     * This stereotype command parameter id is also used in plugin.xml.
     */
    private static final String STEREOTYPE_COMMAND_PARAMETER_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.applyStereotypeCommand.parameter.stereotype";

    /**
     * This profile command parameter id is also used in plugin.xml.
     */
    private static final String PROFILE_COMMAND_PARAMETER_ID = "edu.kit.ipd.sdq.mdsd.profiles.ui.handlers.applyStereotypeCommand.parameter.profile";

    private IServiceLocator serviceLocator;

    /**
      * 
      */
    public ApplicableStereotypesSubmenu() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Creates an applicable stereotypes submenu with the given (optional) id.
     * 
     * @param id
     *            the contribution item identifier, or <code>null</code>
     */
    public ApplicableStereotypesSubmenu(final String id) {
        super(id);
        // TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.actions.CompoundContributionItem#getContributionItems()
     */
    @Override
    protected IContributionItem[] getContributionItems() {

        final EStereotypableObject eStereotypableObject = ProfilesUIConstants
                .getEStereotypableObjectFromCurrentSelection();

        if (eStereotypableObject == null) {
            return new IContributionItem[] {};
        }

        final EList<Stereotype> applicableStereotypes = eStereotypableObject.getApplicableStereotypes();

        LOGGER.debug("applicableStereotypes " + applicableStereotypes);

        IContributionItem[] items = new IContributionItem[applicableStereotypes.size()];

        for (int i = 0; i < items.length; i++) {
            Stereotype applicableStereotype = applicableStereotypes.get(i);

            final Profile profile = applicableStereotype.getProfile();

            items[i] = createContributionItem(applicableStereotype.getName(), profile.getName());
        }

        return items;
    }

    @Override
    public void initialize(final IServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    /**
     * Creates a single contribution item labeled with the combination of the specified qualified
     * name of the stereotype and the profile containing the stereotype.
     * 
     * @param stereotypeName
     *            The stereotype's qualified name to be used as label.
     * @param profileName
     *            The name of the profile which contains the stereotype specified by its qualified
     *            name.
     * @return The created contribution item.
     */
    private IContributionItem createContributionItem(final String stereotypeName, final String profileName) {

        final CommandContributionItemParameter contributionParameter = new CommandContributionItemParameter(
                serviceLocator, null, COMMAND_ID, CommandContributionItem.STYLE_PUSH);
        contributionParameter.label = stereotypeName + " (from " + profileName + ")";
        contributionParameter.visibleEnabled = true;

        // use a parameter to identify the selected stereotype in the handler
        final Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put(STEREOTYPE_COMMAND_PARAMETER_ID, stereotypeName);
        parameterMap.put(PROFILE_COMMAND_PARAMETER_ID, profileName);
        contributionParameter.parameters = parameterMap;

        return new CommandContributionItem(contributionParameter);
    }
}
