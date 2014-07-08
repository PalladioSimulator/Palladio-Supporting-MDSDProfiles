package edu.kit.ipd.sdq.mdsd.profiles.ui.menu;

import java.util.Collection;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.AbstractContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.services.IServiceLocator;
import org.modelversioning.emfprofile.registry.IProfileProvider;
import org.modelversioning.emfprofile.registry.IProfileRegistry;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
import edu.kit.ipd.sdq.mdsd.profiles.ui.Constants;

/**
 * @author Max Kramer
 * 
 */

public class ProfileListMenu extends CompoundContributionItem {

    private static boolean firstTime = true;

    /**
     * Creates or updates a context menu for the current selection if the first
     * element is a stereotypable eObject.
     * 
     * @param treeSelection
     *            the current selection
     */
    public static void createOrUpdateMenuForEachProfile(
            final ITreeSelection treeSelection) {
        Object firstElement = treeSelection.getFirstElement();
        if (firstElement instanceof EStereotypableObject) {
            if (firstTime) {
                IMenuService menuService =
                        (IMenuService) PlatformUI.getWorkbench()
                                .getActiveWorkbenchWindow()
                                .getService(IMenuService.class);

                AbstractContributionFactory viewMenuAddition =
                        new AbstractContributionFactory(
                                Constants.MENU_LOCATION, null) {
                            @Override
                            public void createContributionItems(
                                    final IServiceLocator serviceLocator,
                                    final IContributionRoot additions) {
                                Collection<IProfileProvider> registeredProfileProviders =
                                        IProfileRegistry.INSTANCE
                                                .getRegisteredProfileProviders();

                                for (final IProfileProvider profileProvider : registeredProfileProviders) {
                                    String profileDescription =
                                            profileProvider
                                                    .getProfileDescription();
                                    MenuManager submenu =
                                            new MenuManager(
                                                    profileDescription,
                                                    Constants.PROFILE_LIST_MENU_ID);
                                    IContributionItem dynamicItem =
                                            new CompoundContributionItem(
                                                    Constants.DYNAMIC_LIST_ID) {
                                                @Override
                                                protected IContributionItem[]
                                                        getContributionItems() {
                                                    IContributionItem[] contributionItems =
                                                            new IContributionItem[1];
                                                    ProfileSubmenu profileSubmenu =
                                                            new ProfileSubmenu(
                                                                    profileProvider
                                                                            .getProfile());
                                                    contributionItems[0] =
                                                            profileSubmenu;
                                                    return contributionItems;
                                                }
                                            };
                                    submenu.add(dynamicItem);

                                    // FIXME add a visible when clause so that
                                    // the menu
                                    // is only visible when an instance of
                                    // EStereotypableObject is selected
                                    additions
                                            .addContributionItem(submenu, null);
                                }
                            }
                        };
                menuService.addContributionFactory(viewMenuAddition);
            }
            firstTime = false;
        }

    }

    @Override
    protected IContributionItem[] getContributionItems() {
        // this menu is only a placeholder for dynamically created menus,
        // therefore it never return contribution items
        return null;
    }

}
