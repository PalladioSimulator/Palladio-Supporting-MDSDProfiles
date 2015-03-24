package edu.kit.ipd.sdq.mdsd.profiles.ui.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.AbstractContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.services.IServiceLocator;
import org.modelversioning.emfprofile.Profile;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofile.registry.IProfileProvider;
import org.modelversioning.emfprofile.registry.IProfileRegistry;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
import edu.kit.ipd.sdq.mdsd.profiles.ui.ProfilesUIConstants;
import edu.kit.ipd.sdq.mdsd.profiles.util.datastructures.Pair;

/**
 * @author Max Kramer
 * 
 */

public class ProfileListMenu extends CompoundContributionItem {
    private static final Logger LOGGER = Logger.getLogger(ProfileListMenu.class);

    private static List<Pair<Profile, MenuManager>> profileSubmenuPairs = null;

    /**
     * Creates or updates a context menu for the current selection if the first element is a
     * stereotypable eObject.
     * 
     * @param treeSelection
     *            the current selection
     */
    public static void createOrUpdateMenuForEachProfile(final ITreeSelection treeSelection) {
        Object firstElement = treeSelection.getFirstElement();
        if (firstElement instanceof EStereotypableObject) {
            if (profileSubmenuPairs == null) {
                final Collection<IProfileProvider> registeredProfileProviders = IProfileRegistry.INSTANCE
                        .getRegisteredProfileProviders();
                profileSubmenuPairs = new ArrayList<Pair<Profile, MenuManager>>(registeredProfileProviders.size());
                IMenuService menuService = (IMenuService) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                        .getService(IMenuService.class);

                AbstractContributionFactory viewMenuAddition = new AbstractContributionFactory(
                        ProfilesUIConstants.MENU_LOCATION, null) {
                    @Override
                    public void createContributionItems(final IServiceLocator serviceLocator,
                            final IContributionRoot additions) {

                        for (final IProfileProvider profileProvider : registeredProfileProviders) {
                            String profileDescription = profileProvider.getProfileDescription();
                            Profile profile = profileProvider.getProfile();
                            MenuManager submenu = new MenuManager(profileDescription,
                                    ProfilesUIConstants.PROFILE_LIST_MENU_ID);
                            IContributionItem dynamicItem = new CompoundContributionItem(
                                    ProfilesUIConstants.DYNAMIC_LIST_ID) {
                                @Override
                                protected IContributionItem[] getContributionItems() {
                                    IContributionItem[] contributionItems = new IContributionItem[1];
                                    ProfileSubmenu profileSubmenu = new ProfileSubmenu(profile);
                                    contributionItems[0] = profileSubmenu;
                                    return contributionItems;
                                }

                            };
                            profileSubmenuPairs.add(new Pair<Profile, MenuManager>(profile, submenu));
                            submenu.add(dynamicItem);
                            updateSubmenuVisibility(submenu, profile);

                            // FIXME add a visible when clause so that
                            // the menu
                            // is only visible when an instance of
                            // EStereotypableObject is selected
                            Expression visibleWhen = new Expression() {

                                @Override
                                public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
                                    return getSubmenuVisibility(profile);
                                }
                            };
                            additions.addContributionItem(submenu, visibleWhen);
                        }
                    }
                };
                menuService.addContributionFactory(viewMenuAddition);
            } else {
                for (Pair<Profile, MenuManager> profileSubmenuPair : profileSubmenuPairs) {
                    Profile profile = profileSubmenuPair.getFirst();
                    MenuManager submenu = profileSubmenuPair.getSecond();
                    updateSubmenuVisibility(submenu, profile);
                }
            }
        }

    }

    private static void updateSubmenuVisibility(MenuManager submenu, Profile profile) {
        EvaluationResult shallBeVisible = getSubmenuVisibility(profile);
        boolean newVisibility = EvaluationResult.TRUE.equals(shallBeVisible);
        submenu.setVisible(newVisibility);
    }

    private static EvaluationResult getSubmenuVisibility(Profile profile) {
        EStereotypableObject eStereotypableObject = ProfilesUIConstants.getEStereotypableObjectFromCurrentSelection();
        if (eStereotypableObject != null) {
            EList<Stereotype> applicableStereotypes = eStereotypableObject.getApplicableStereotypes(profile);
            EList<StereotypeApplication> stereotypeApplications = eStereotypableObject
                    .getStereotypeApplications(profile);
            boolean applicableOrApplied = !applicableStereotypes.isEmpty() || !stereotypeApplications.isEmpty();
            if (applicableOrApplied) {
                LOGGER.error("asd applicableStereotypes" + applicableStereotypes + " stereotypeApplications "
                        + stereotypeApplications);
                return EvaluationResult.TRUE;
            }
        }
        LOGGER.error("visible false");
        return EvaluationResult.FALSE;
    }

    @Override
    protected IContributionItem[] getContributionItems() {
        // this menu is only a placeholder for dynamically created menus,
        // therefore it never return contribution items
        return null;
    }

}
