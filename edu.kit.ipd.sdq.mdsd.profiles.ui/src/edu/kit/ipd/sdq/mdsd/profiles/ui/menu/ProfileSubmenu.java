/**
 * 
 */
package edu.kit.ipd.sdq.mdsd.profiles.ui.menu;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
import org.eclipse.gmf.runtime.notation.Shape;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;
import org.eclipse.ui.services.IServiceLocator;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.Stereotype;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
import edu.kit.ipd.sdq.mdsd.profiles.ui.Constants;
import edu.kit.ipd.sdq.mdsd.profiles.util.datastructures.Pair;

/**
 * @author Matthias Eisenmann, Max Kramer
 * 
 */

public class ProfileSubmenu extends CompoundContributionItem {

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

    private Profile profile;

    /**
     * Creates a profile submenu for the given profile.
     * 
     * @param profile
     *            the profile
     */
    public ProfileSubmenu(final Profile profile) {
        this.profile = profile;
    }

    /**
     * Creates a profile submenu with the given (optional) id.
     * 
     * @param id
     *            the contribution item identifier, or <code>null</code>
     */
    public ProfileSubmenu(final String id) {
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

        ISelection selection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService()
                .getSelection();

        if (selection == null || !(selection instanceof IStructuredSelection)) {
            LOGGER.debug("selection is null or not instance of IStructuredSelection");
            return new IContributionItem[] {};
        }

        IStructuredSelection structuredSelection = (IStructuredSelection) selection;

        Object firstElement = structuredSelection.getFirstElement();

        if (firstElement == null || !(firstElement instanceof EStereotypableObject)) {
            if (firstElement instanceof Shape) {
                LOGGER.debug("vc " + ((Shape) firstElement).getVisibleChildren());
            }
            LOGGER.debug("firstElement ' " + firstElement + "' is null or not an instance of EStereotypableObject");
            return new IContributionItem[] {};
        }
        final EStereotypableObject eStereotypableObject = (EStereotypableObject) firstElement;
        return getContributionItemsForSelectedElement(eStereotypableObject);
    }

    /**
     * Returns an array of contribution items for the selected stereotypable eObject.
     * 
     * @param eStereotypableObject
     *            the selected stereotypableeObject
     * @return the array
     */
    private IContributionItem[] getContributionItemsForSelectedElement(final EStereotypableObject eStereotypableObject) {

        final EList<Stereotype> applicableStereotypes = eStereotypableObject.getApplicableStereotypes(this.profile);

        EList<Stereotype> appliedStereotypes = eStereotypableObject.getAppliedStereotypes();

        removeAppliedStereotypesOfOtherProfiles(appliedStereotypes);

        // a contribution for every applicable stereotype of the current profile
        // and each stereotype of the current profile that is already applied
        int contributionCount = applicableStereotypes.size() + appliedStereotypes.size();

        IConfigurationElement[] configElements = Platform.getExtensionRegistry().getConfigurationElementsFor(
                Constants.CUSTOMCOMMANDLABEL_EXT_PT_ID);

        Map<String, IConfigurationElement> stereotypeName2ConfigElemMap = mapStereotypeNames2ConfigElements(configElements);

        Map<Stereotype, Pair<Boolean, Boolean>> stereotype2ApplicableAppliedMap = mapStereotypes2CommandTypes(
                applicableStereotypes, appliedStereotypes, contributionCount);

        Set<Stereotype> commandableStereotypes = stereotype2ApplicableAppliedMap.keySet();
        List<Stereotype> lexicographicalStereotypeList = new LinkedList<Stereotype>(commandableStereotypes);
        Comparator<Stereotype> comparator = ComparatorHelper.getStereotypeComparator();
        Collections.sort(lexicographicalStereotypeList, comparator);

        return getContributionsForAppliedAndApplicableStereos(contributionCount, stereotypeName2ConfigElemMap,
                stereotype2ApplicableAppliedMap, commandableStereotypes);
    }

    /**
     * Removes all stereotypes that pertain to another profile from the given list of applied
     * stereotypes.
     * 
     * @param appliedStereotypes
     *            the list of applied stereotypes to be cleaned
     */
    private void removeAppliedStereotypesOfOtherProfiles(final EList<Stereotype> appliedStereotypes) {
        Iterator<Stereotype> appliedStereotypesIterator = appliedStereotypes.iterator();
        while (appliedStereotypesIterator.hasNext()) {
            Stereotype appliedStereotype = appliedStereotypesIterator.next();
            String appliedProfileNsURI = appliedStereotype.getProfile().getNsURI();
            String currentProfileNsURI = this.profile.getNsURI();
            if (appliedProfileNsURI == null || !appliedProfileNsURI.equals(currentProfileNsURI)) {
                appliedStereotypesIterator.remove();
            }
        }
    }

    /**
     * Creates a mapping from stereotype names to configuration elements.
     * 
     * @param configElements
     *            the configuration elements
     * @return the mapping
     */
    private Map<String, IConfigurationElement> mapStereotypeNames2ConfigElements(
            final IConfigurationElement[] configElements) {
        Map<String, IConfigurationElement> stereotypeName2ConfigElemMap = new HashMap<String, IConfigurationElement>(
                configElements.length);

        for (IConfigurationElement configElem : configElements) {
            String stereotypeName = configElem.getAttribute(Constants.STEREOTYPE_NAME_ATTRIBUTE);
            stereotypeName2ConfigElemMap.put(stereotypeName, configElem);
        }
        return stereotypeName2ConfigElemMap;
    }

    /**
     * Creates a mapping from stereotypes to a pair of booleans representing whether the stereotype
     * is a) applicable b) applied.
     * 
     * @param applicableStereotypes
     *            a list of applicable stereotypes
     * @param appliedStereotypes
     *            a list of applied stereotypes
     * @param contributionCount
     *            the
     * @return the mapping
     */
    private Map<Stereotype, Pair<Boolean, Boolean>> mapStereotypes2CommandTypes(
            final EList<Stereotype> applicableStereotypes, final EList<Stereotype> appliedStereotypes,
            final int contributionCount) {
        Map<Stereotype, Pair<Boolean, Boolean>> stereotype2ApplicableAppliedMap = new HashMap<Stereotype, Pair<Boolean, Boolean>>(
                contributionCount);

        for (Stereotype applicableStereotype : applicableStereotypes) {
            stereotype2ApplicableAppliedMap.put(applicableStereotype, new Pair<Boolean, Boolean>(true, false));
        }

        for (Stereotype appliedStereotype : appliedStereotypes) {
            Pair<Boolean, Boolean> applicableApplied = stereotype2ApplicableAppliedMap.get(appliedStereotype);
            boolean applicable = (applicableApplied != null);
            applicableApplied = new Pair<Boolean, Boolean>(applicable, true);
            stereotype2ApplicableAppliedMap.put(appliedStereotype, applicableApplied);
        }
        return stereotype2ApplicableAppliedMap;
    }

    /**
     * Returns an array of contribution items based on the given mappings
     * 
     * @param contributionCount
     *            the number of contributions
     * @param stereotypeName2ConfigElemMap
     *            the mapping from stereotype names to configuration elements
     * @param stereotype2ApplicableAppliedMap
     *            the mapping from stereotypes to applicable and applied flags
     * @param commandableStereotypes
     *            the set of stereotypes
     * @return the array of contribution items
     */
    private IContributionItem[] getContributionsForAppliedAndApplicableStereos(final int contributionCount,
            final Map<String, IConfigurationElement> stereotypeName2ConfigElemMap,
            final Map<Stereotype, Pair<Boolean, Boolean>> stereotype2ApplicableAppliedMap,
            final Set<Stereotype> commandableStereotypes) {
        IContributionItem[] contributionItems = new IContributionItem[contributionCount];
        int contributionIndex = 0;

        for (Stereotype currentStereotype : commandableStereotypes) {
            String currentStereotypeName = currentStereotype.getName();
            Pair<Boolean, Boolean> applicableApplied = stereotype2ApplicableAppliedMap.get(currentStereotype);
            boolean applicable = applicableApplied.getFirst();
            boolean applied = applicableApplied.getSecond();

            IConfigurationElement configElement = stereotypeName2ConfigElemMap.get(currentStereotypeName);
            String profileName = profile.getName();
            if (applicable) {
                IContributionItem contributionItem = getContributionItem(currentStereotypeName, configElement,
                        profileName, false);
                contributionItems[contributionIndex] = contributionItem;
                contributionIndex++;
            }
            if (applied) {
                IContributionItem contributionItem = getContributionItem(currentStereotypeName, configElement,
                        profileName, true);
                contributionItems[contributionIndex] = contributionItem;
                contributionIndex++;
            }
        }
        return contributionItems;
    }

    /**
     * Creates a single contribution item labeled with the combination of the specified qualified
     * name of the stereotype and the profile containing the stereotype.
     * 
     * @param stereotypeName
     *            The stereotype's qualified name to be used as label.
     * @param configElement
     *            the configuration element
     * @param profileName
     *            The name of the profile which contains the stereotype specified by its qualified
     *            name.
     * @param isUnapply
     *            whether it is an unapply stereotype contribution
     * @return The created contribution item.
     */
    private IContributionItem getContributionItem(final String stereotypeName,
            final IConfigurationElement configElement, final String profileName, final boolean isUnapply) {
        String label = isUnapply ? Constants.getDefaultUnapplyLabel(stereotypeName) : Constants
                .getDefaultApplyLabel(stereotypeName);
        if (configElement != null) {
            String labelAttribute = isUnapply ? Constants.UNAPPLY_LABEL_ATTR : Constants.APPLY_LABEL_ATTR;
            String customLabel = configElement.getAttribute(labelAttribute);
            if (customLabel != null && !customLabel.equals("")) {
                label = customLabel;
            }
        }
        IContributionItem contributionItem = createContributionItem(label, stereotypeName, profileName, isUnapply);
        return contributionItem;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    /**
     * Creates a single contribution item labeled with the combination of the specified qualified
     * name of the stereotype and the profile containing the stereotype.
     * 
     * @param label
     *            the label
     * @param stereotypeName
     *            The stereotype's qualified name to be used as label.
     * @param profileName
     *            The name of the profile which contains the stereotype specified by its qualified
     *            name.
     * @param isUnapply
     *            whether it is an unapply stereotype contribution
     * @return The created contribution item.
     */
    private IContributionItem createContributionItem(final String label, final String stereotypeName,
            final String profileName, final boolean isUnapply) {

        IServiceLocator serviceLocator = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        String commandID = isUnapply ? Constants.UNAPPLY_COMMAND_ID : Constants.APPLY_COMMAND_ID;
        final CommandContributionItemParameter contributionParameter = new CommandContributionItemParameter(
                serviceLocator, null, commandID, CommandContributionItem.STYLE_PUSH);
        contributionParameter.label = label;
        contributionParameter.visibleEnabled = true;

        // use a parameter to identify the selected stereotype in the handler
        final Map<String, String> parameterMap = new HashMap<String, String>();
        String stereoParamID = isUnapply ? Constants.UNAPPLY_STEREOTYPE_PARAMETER_ID
                : Constants.APPLY_STEREOTYPE_PARAMETER_ID;
        parameterMap.put(stereoParamID, stereotypeName);
        String profileParamID = isUnapply ? Constants.UNAPPLY_PROFILE_PARAMETER_ID
                : Constants.APPLY_PROFILE_PARAMETER_ID;
        parameterMap.put(profileParamID, profileName);
        contributionParameter.parameters = parameterMap;

        return new CommandContributionItem(contributionParameter);
    }
}
