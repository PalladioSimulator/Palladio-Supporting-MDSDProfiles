package edu.kit.ipd.sdq.mdsd.profiles.view.viewer;

import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.BasicNotifierImpl.EObservableAdapterList.Listener;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.edit.IEMFEditObservable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.resource.DeviceResourceException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.modelversioning.emfprofile.Extension;
import org.modelversioning.emfprofile.application.registry.ui.providers.ProfileApplicationDecoratorReflectiveItemProviderAdapterFactory;
import org.modelversioning.emfprofile.application.registry.ui.providers.ProfileProviderContentAdapter;
import org.modelversioning.emfprofile.application.registry.ui.providers.ProfileProviderLabelAdapter;
import org.modelversioning.emfprofile.provider.EMFProfileItemProviderAdapterFactory;
import org.modelversioning.emfprofileapplication.ProfileApplication;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.modelversioning.emfprofileapplication.impl.StereotypeApplicationImpl;
import org.modelversioning.emfprofileapplication.provider.EMFProfileApplicationItemProviderAdapterFactory;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;
import edu.kit.ipd.sdq.mdsd.profiles.registry.ProfileApplicationFileRegistry;
import edu.kit.ipd.sdq.mdsd.profiles.ui.menu.ProfileListMenu;
import edu.kit.ipd.sdq.mdsd.profiles.view.action.ClearAction;
import edu.kit.ipd.sdq.mdsd.profiles.view.action.RemoveStereotypeAction;
import edu.kit.ipd.sdq.mdsd.profiles.view.databinding.ChangedListener;
import edu.kit.ipd.sdq.mdsd.profiles.view.databinding.TaggedValueEditingSupport;
import edu.kit.ipd.sdq.mdsd.profiles.view.observer.EProfileApplicationLoader;
import edu.kit.ipd.sdq.mdsd.profiles.view.observer.EStereotypedEditorObserver;
import edu.kit.ipd.sdq.mdsd.profiles.view.utility.EObjectSorter;

/**
 * The UI containing a tree viewer, a table editor and various boxes for PCM Profiles.
 * 
 * @author emretaspolat
 * 
 */
public class ProfilePropertiesView extends ViewPart implements Listener, IEditingDomainProvider {
    public ProfilePropertiesView() {
    }

    private static final Logger LOGGER = Logger.getLogger(ProfilePropertiesView.class);

    public static final String ID = "edu.kit.ipd.sdq.mdsd.profiles.view.columnbased.ColumnBasedPropertiesView"; //$NON-NLS-1$

    private PropertySheetPage propertySheetPage;

    private DrillDownAdapter drillDownAdapter;
    private static LocalResourceManager resourceManager;
    private static ComposedAdapterFactory adapterFactory;

    private Composite composite;
    private TreeViewer treeViewer;
    private Table table;
    private TableViewer tableViewer;

    private EditingDomain editingDomain;

    private Text text_extension;
    private Text text_appliedTo;
    private Text text_stereotypeName;
    private Text text_appliedProperties;
    private Text text_profileApplication;

    private Label lblExtension;
    private Label lblProfileApplication;
    private Label lblAppliedTo;
    private Label lblStereotypeProperty;
    private Label lblStereotypeName;

    private TableColumn tblclmnFirstColumn;
    private TableViewerColumn twc_stereotype;
    private TableColumn tblclmnSecondColumn;
    private TableViewerColumn twc_taggedValue;
    public static final String STEREOTYPE = "Stereotype Feature";
    public static final String TAGGED_VALUE = "Tagged Value";
    public static final String[] columnProperties = { STEREOTYPE, TAGGED_VALUE };

    String eStructuralFeatureName_appliedTo = "appliedTo";
    String eStructuralFeatureName_extension = "extension";
    String eStructuralFeatureName_profileApplication = "profileApplication";

    EProfileApplicationLoader loader = new EProfileApplicationLoader();
    IAdapterManager manager = Platform.getAdapterManager();

    private final IObservableValue master = new WritableValue();

    private List<IObservableValue> values;
    protected EStructuralFeature attr;

    private EStereotypableObject eStereotyped;

    private RemoveStereotypeAction removeStereotypeAction;
    private ClearAction clearAction;

    /**
     * The listener we register with the selection service.
     * 
     * TODO: Needs to be expanded to allow further filtering. Maybe look for the Root-Element of
     * EObject, which should be a EStereotypableObject.
     */
    private final ISelectionListener listener = new ISelectionListener() {

        private EStereotypableObject localEStereotyped;

        @Override
        public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
            // Ignore our own selections
            if (sourcepart instanceof IEditorPart && selection instanceof ITreeSelection) {
                ITreeSelection treeSelection = (ITreeSelection) selection;
                if (treeSelection instanceof IAdaptable) {
                    // do nothing
                    // FIXME dead code?
                }
                Object firstElement = treeSelection.getFirstElement();

                if (firstElement == null) {
                    return;
                }

                if (firstElement instanceof EStereotypableObject) {
                    EStereotypableObject firstEStereotypableObject = (EStereotypableObject) firstElement;
                    LOGGER.debug("TreeSelection: " + treeSelection);
                    LOGGER.debug("true");
                    ProfilePropertiesView.this.eStereotyped = firstEStereotypableObject;
                    LOGGER.debug("eStereotyped: " + ProfilePropertiesView.this.eStereotyped);
                    boolean stereotypesApplied = firstEStereotypableObject.getAppliedStereotypes().isEmpty();
                    ProfileListMenu.createOrUpdateMenuForEachProfile(treeSelection);
                    if (!stereotypesApplied) {
                        ProfilePropertiesView.this.callPerformObservation(ProfilePropertiesView.this.eStereotyped);
                        ProfilePropertiesView.this.eRefreshViewer(ProfilePropertiesView.this.eStereotyped);
                        ProfilePropertiesView.this.tableViewer.setItemCount(0);
                    } else {
                        // The following two lines of code is temporary and have to out-commented if
                        // to
                        // work on the "load twice"-problem
                        // And the first selection from a resource must than be a
                        // EStereotypableObject
                        // WITH APLIED STEREOTYPES
                        // ProfilePropertiesView.this.callPerformObservation(ProfilePropertiesView.this.eStereotyped);
                        // ProfilePropertiesView.this.eRefreshViewer(ProfilePropertiesView.this.eStereotyped);
                        ProfilePropertiesView.this.tableViewer.setItemCount(0);
                        LOGGER.debug("The selected element '" + firstEStereotypableObject
                                + "' wasn't applied any stereotypes.");
                    }
                } else {
                    ProfilePropertiesView.this.tableViewer.setItemCount(0);
                    LOGGER.debug("The selected element '" + firstElement + "' was not an EStereotypableObject.");
                }
            }
        }
    };

    // extrahiere die Listeners
    private final IPartListener partListener = new IPartListener() {
        private IWorkbenchPart activePart;

        @Override
        public void partActivated(IWorkbenchPart part) {
            if (part instanceof IEditorPart) {
                activePart = part;
                LOGGER.info("Active part: " + part.getTitle());
            }
        }

        @Override
        public void partClosed(IWorkbenchPart part) {
            if (part != activePart) {
                activePart = null;
                // ProfileApplicationFileRegistry.INSTANCE.clear();
                treeViewer.setInput(Collections.emptyList());
                LOGGER.info("Closed part: " + part.getTitle());
            }
        }

        @Override
        public void partBroughtToTop(IWorkbenchPart part) {
            if (part instanceof IEditorPart) {
                activePart = part;
                LOGGER.info("Top part: " + part.getTitle());
            }
        }

        @Override
        public void partDeactivated(IWorkbenchPart part) {
            if (part == activePart) {
                activePart = null;
                LOGGER.info("Deactivated part: " + part.getTitle());
            }
        }

        @Override
        public void partOpened(IWorkbenchPart part) {
            if (part instanceof IEditorPart) {
                activePart = part;
                treeViewer.setInput(Collections.emptyList());
                LOGGER.info("Opened part: " + part.getTitle());
            }
        }
    };

    private final IResourceChangeListener changeListener = new IResourceChangeListener() {
        @Override
        public void resourceChanged(final IResourceChangeEvent event) {
            LOGGER.debug("Resource change test!");
            if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
                final IResource resource = event.getResource();
                if (resource == null) {
                    throw new RuntimeException("IResourceChangeEvent did not provide a resource");
                }

                try {
                    if (resource.getType() == IResource.PROJECT && ((IProject) resource).hasNature(JavaCore.NATURE_ID)) {
                        resource.delete(true, null);
                    }
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        };
    };

    /**
     * The method is called by the registered listener for -almost- every ITreeSelection. It
     * initializes the Loader, which then looks to StereotypeApplicationFileRegistry.
     * 
     * @param eStereotypableObject
     */
    private Collection<EStereotypableObject> callPerformObservation(final EStereotypableObject eStereotypableObject) {
        LOGGER.info("Observation beginns.");
        // List of estereotyped objects
        Collection<EStereotypableObject> temp = this.loader.performObservation(eStereotypableObject);
        if (temp.isEmpty()) {
            LOGGER.warn("Couldn't perform observation.");
            return null;
        } else {
            LOGGER.info("Observation and refreshments performed completely.");
            return temp;
        }
    }

    /**
     * Complete refresh of the viewer.
     */
    private void eRefreshViewer(final EStereotypableObject eStereotypableObject) {
        if (this.treeViewer == null || this.treeViewer.getTree().isDisposed()) {
            return;
        }
        final EProfileApplicationLoader tempLoader = loader;
        this.treeViewer.getTree().getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (ProfilePropertiesView.this.treeViewer.getInput().equals(Collections.emptyList())) {
                    ProfilePropertiesView.this.treeViewer.setInput(ProfileApplicationFileRegistry.INSTANCE
                            .getAllExistingProfileApplicationDecorators(eStereotypableObject));
                } else if (ProfilePropertiesView.this.treeViewer.getInput().equals(
                        tempLoader.getProfileApplicationDecorator(eStereotypableObject))) {
                    ProfilePropertiesView.this.treeViewer.setInput(ProfileApplicationFileRegistry.INSTANCE
                            .getAllExistingProfileApplicationDecorators(eStereotypableObject));
                    ;
                    ProfilePropertiesView.this.treeViewer.refresh();
                    ProfilePropertiesView.this.treeViewer.expandToLevel(2);
                    ProfilePropertiesView.this.tableViewer.setItemCount(0); // Workaround for
                                                                            // tableViewer.refresh();
                }
            }
        });
    }

    private IWorkbenchPage getActivePage() {
        return getSite().getPage();
    }

    /**
     * Create contents of the view part.
     * 
     * @param parent
     */
    @Override
    public void createPartControl(final Composite parent) {
        this.createTreeViewer(parent);
        this.createTableViewer(parent);
        this.createRightComposite(parent);
        this.getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this.listener);
        getActivePage().addPartListener(partListener);
        ResourcesPlugin.getWorkspace().addResourceChangeListener(this.changeListener, IResourceChangeEvent.POST_CHANGE);
        this.createActions();
        this.initializeToolBar();
        this.initDataBindings();
        this.refresh(parent.getDisplay());
    }

    private void createTreeViewer(final Composite parent) {
        this.treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        this.drillDownAdapter = new DrillDownAdapter(this.treeViewer);

        this.treeViewer.setLabelProvider(new ProfileProviderLabelAdapter(getAdapterFactory()));
        // Using our own content adapter to support an IResourceChangeListener
        // this.treeViewer.setContentProvider(new
        // EProfileProviderContentAdapter(getAdapterFactory()));
        this.treeViewer.setContentProvider(new ProfileProviderContentAdapter(getAdapterFactory()));

        this.treeViewer.setSorter(new EObjectSorter().createGenericEObjectSorter());
        this.treeViewer.setAutoExpandLevel(2);
        this.getSite().setSelectionProvider(this.treeViewer);
        ProfilePropertiesView.resourceManager = new LocalResourceManager(JFaceResources.getResources());
        this.treeViewer.setUseHashlookup(true);
        this.treeViewer.setInput(Collections.emptyList());

        // Active editor sets tree viewer from outside
        // ActiveEditorObserver.INSTANCE.setViewer(treeViewer);
        EStereotypedEditorObserver.getActiveEditorObserver().setViewer(this.treeViewer);

        new AdapterFactoryTreeEditor(this.treeViewer.getTree(), adapterFactory);

        this.treeViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(final DoubleClickEvent event) {
                final TreeViewer viewer = (TreeViewer) event.getViewer();
                final IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
                final Object selectedNode = thisSelection.getFirstElement();
                viewer.setExpandedState(selectedNode, !viewer.getExpandedState(selectedNode));
            }
        });

        // Mediator for differentiating between observed TreeViewer elements and setting the
        // observable variables
        try {
            final IObservableValue treeObservables = ViewerProperties.singleSelection().observe(this.treeViewer);
            treeObservables.addValueChangeListener(new ChangedListener(this));
        } catch (final IllegalArgumentException e) {
            LOGGER.error("Selection from tree viewer not supported.");
            e.printStackTrace();
        }
    }

    private void createTableViewer(final Composite parent) {
        this.tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        this.table = this.tableViewer.getTable();
        this.table.setLinesVisible(true);
        this.table.setHeaderVisible(true);

        final ObservableListContentProvider contentProvider = new ObservableListContentProvider();
        this.tableViewer.setContentProvider(contentProvider);

        this.getSite().setSelectionProvider(this.tableViewer);

        {
            this.twc_stereotype = new TableViewerColumn(this.tableViewer, SWT.LEFT);
            this.tblclmnFirstColumn = this.twc_stereotype.getColumn();
            this.tblclmnFirstColumn.setWidth(200);
            this.tblclmnFirstColumn.setText(STEREOTYPE);

            this.twc_stereotype.setLabelProvider(new CellLabelProvider() {
                @Override
                public void update(final ViewerCell cell) {
                    if (cell.getElement() instanceof IEMFEditObservable) {
                        ProfilePropertiesView.this.attr = ((IEMFEditObservable) cell.getElement())
                                .getStructuralFeature();
                        cell.setText(ProfilePropertiesView.this.attr.getName());
                    } else {
                        cell.setText(cell.getElement().toString());
                    }
                }
            });
        }

        {
            this.twc_taggedValue = new TableViewerColumn(this.tableViewer, SWT.NONE);
            this.tblclmnSecondColumn = this.twc_taggedValue.getColumn();
            this.tblclmnSecondColumn.setWidth(200);
            this.tblclmnSecondColumn.setText(TAGGED_VALUE);

            this.twc_taggedValue.setLabelProvider(new CellLabelProvider() {
                @Override
                public void update(final ViewerCell cell) {
                    if (cell.getElement() instanceof IEMFEditObservable) {
                        ProfilePropertiesView.this.attr = ((IEMFEditObservable) cell.getElement())
                                .getStructuralFeature();
                        final Object obj = ((EObject) ((IEMFEditObservable) cell.getElement()).getObserved())
                                .eGet(ProfilePropertiesView.this.attr);
                        try {
                            if (obj != null) {
                                cell.setText(String.valueOf(obj));
                            } else {
                                // Temporary workaround for freshly applied stereotypes, that have
                                // initially null-values
                                cell.setText("NULL - DON'T TOUCH IT, PLEASE FIRST USE PROPERTIES VIEW TO SET THE INITIAL VALUE");
                            }
                        } catch (final NullPointerException e) {
                            LOGGER.error(obj);
                            e.printStackTrace();
                        }
                    } else {
                        cell.setText(cell.getElement().toString());
                    }

                }
            });

            this.twc_taggedValue.setEditingSupport(new TaggedValueEditingSupport(this.tableViewer, this
                    .getEditingDomain()));

        }
    }

    private void createRightComposite(final Composite parent) {
        this.composite = new Composite(parent, SWT.NONE);
        this.composite.setLayout(new GridLayout(2, false));
        {
            this.lblStereotypeName = new Label(this.composite, SWT.NONE);
            this.lblStereotypeName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
            this.lblStereotypeName.setText("Stereoype Name");
        }
        {
            this.text_stereotypeName = new Text(this.composite, SWT.BORDER);
            this.text_stereotypeName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        {
            this.lblStereotypeProperty = new Label(this.composite, SWT.NONE);
            this.lblStereotypeProperty.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
            this.lblStereotypeProperty.setText("Applied Properties");
        }
        {
            this.text_appliedProperties = new Text(this.composite, SWT.BORDER);
            this.text_appliedProperties.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        {
            this.lblAppliedTo = new Label(this.composite, SWT.NONE);
            this.lblAppliedTo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
            this.lblAppliedTo.setText("Applied To");
        }
        {
            this.text_appliedTo = new Text(this.composite, SWT.BORDER);
            this.text_appliedTo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        {
            this.lblProfileApplication = new Label(this.composite, SWT.NONE);
            this.lblProfileApplication.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
            this.lblProfileApplication.setText("Profile Application");
        }
        {
            this.text_profileApplication = new Text(this.composite, SWT.BORDER);
            this.text_profileApplication.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        {
            this.lblExtension = new Label(this.composite, SWT.NONE);
            this.lblExtension.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
            this.lblExtension.setText("Extension");
        }
        {
            this.text_extension = new Text(this.composite, SWT.BORDER);
            this.text_extension.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
    }

    protected void initializeEditingDomain() {
        // Create an adapter factory that yields item providers.
        ProfilePropertiesView.adapterFactory = new ComposedAdapterFactory(
                ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
        ProfilePropertiesView.adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
        ProfilePropertiesView.adapterFactory.addAdapterFactory(new EMFProfileApplicationItemProviderAdapterFactory());
        ProfilePropertiesView.adapterFactory.addAdapterFactory(new EMFProfileItemProviderAdapterFactory());
        ProfilePropertiesView.adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

        // Create the command stack that will notify this editor as commands are executed
        final CommandStack commandStack = new BasicCommandStack();
        // Add a listener to set the editor dirty of commands have been executed
        commandStack.addCommandStackListener(new CommandStackListener() {
            @Override
            public void commandStackChanged(final EventObject event) {
                ProfilePropertiesView.this.firePropertyChange(0);
            }
        });

        // Create the editing domain with our adapterFactory and command stack.
        this.editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack);
    }

    @Override
    public EditingDomain getEditingDomain() {
        return this.editingDomain;
    }

    private void refresh(final Display display) {
        display.timerExec(1000, new Runnable() {
            @Override
            public void run() {
                // recursive call
                ProfilePropertiesView.this.treeViewer.refresh();
                ProfilePropertiesView.this.tableViewer.refresh();
                LOGGER.info("UI refreshed.");
            }
        });
    }

    /**
     * Provides the adapters. In particular this is the property sheet. The rest is handed over to
     * super.
     * 
     * TODO: One of the last steps is to integrate the view into a tabbed property view, instead of
     * handing it to standard property sheet page.
     * 
     * @param adapter
     *            to get.
     * @return the adapter.
     */
    @Override
    public Object getAdapter(@SuppressWarnings("rawtypes") final Class adapter) {
        if (adapter.equals(IPropertySheetPage.class)) {
            return this.getPropertySheetPage();
        } else {
            return super.getAdapter(adapter);
        }
    }

    /**
     * This accesses a cached version of the property sheet.
     */
    public IPropertySheetPage getPropertySheetPage() {
        if (this.propertySheetPage == null) {
            this.propertySheetPage = new PropertySheetPage();
            // only set the reflective adapter factory
            this.propertySheetPage.setPropertySourceProvider(new AdapterFactoryContentProvider(
                    new ProfileApplicationDecoratorReflectiveItemProviderAdapterFactory()));
        }
        return this.propertySheetPage;
    }

    /**
     * Brings several needed adapter factories together.
     */
    private static ComposedAdapterFactory getAdapterFactory() {
        if (adapterFactory != null) {
            return adapterFactory;
        }

        adapterFactory = new ComposedAdapterFactory();

        adapterFactory.addAdapterFactory(new EMFProfileItemProviderAdapterFactory());
        adapterFactory.addAdapterFactory(new EMFProfileApplicationItemProviderAdapterFactory());
        adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

        return adapterFactory;
    }

    /**
     * Create the actions.
     */
    private void createActions() {
        {
            this.removeStereotypeAction = new RemoveStereotypeAction(this.treeViewer, this.tableViewer);
            this.removeStereotypeAction.setToolTipText("Click to remove selected stereotype...");

            this.clearAction = new ClearAction(this.treeViewer, this.tableViewer);
            this.clearAction.setToolTipText("Click to clear the entire view...");
        }
    }

    /**
     * Initialize the toolbar.
     */
    private void initializeToolBar() {
        final IToolBarManager toolbarManager = this.getViewSite().getActionBars().getToolBarManager();
        toolbarManager.add(this.removeStereotypeAction);
        toolbarManager.add(this.clearAction);
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    @Override
    public void setFocus() {
        // Set the focus
        this.treeViewer.getControl().setFocus();
        this.tableViewer.getControl().setFocus();
    }

    /**
     * After the view has been initialized, this method can be used to create an image for the
     * descriptor. Images created in this way do not need to be extra disposed in the code. Images
     * are created with usage of {@link LocalResourceManager} which takes care of disposal when the
     * UI part is disposed.
     * 
     * @param imageDescriptor
     * @return image or null if the image could not be located.
     */
    public static Image createImage(final ImageDescriptor imageDescriptor) {
        try {
            if (ProfilePropertiesView.resourceManager != null) {
                return ProfilePropertiesView.resourceManager.createImage(imageDescriptor);
            }
        } catch (final DeviceResourceException e) {
            // e.printStackTrace();
        }
        return null;
    }

    /**
     * Disposes the elements for memory save if they are no more needed
     */
    @Override
    public void dispose() {
        EStereotypedEditorObserver.getActiveEditorObserver().cleanUp();

        if (this.propertySheetPage != null) {
            this.propertySheetPage.dispose();
        }

        if (adapterFactory != null) {
            adapterFactory.dispose();
            adapterFactory = null;
        }
        // dispose our resource manager for images
        if (ProfilePropertiesView.resourceManager != null) {
            ProfilePropertiesView.resourceManager.dispose();
        }

        // important: We need do unregister our listener when the view is disposed
        this.getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this.listener);
        this.getSite().getPage().removePartListener(this.partListener);

        super.dispose();
    }

    @Override
    public void added(final Notifier notifier, final Adapter adapter) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removed(final Notifier notifier, final Adapter adapter) {
        // TODO Auto-generated method stub

    }

    protected DataBindingContext initDataBindings() {
        final DataBindingContext bindingContext = new DataBindingContext();
        //
        final IObservableValue observeTextText_4ObserveWidget = WidgetProperties.text(SWT.NONE).observe(
                this.text_stereotypeName);
        final IObservableValue observeSingleSelectionTreeViewer = ViewerProperties.singleSelection().observe(
                this.treeViewer);
        final IObservableValue treeViewerStereotypenameObserveDetailValue_1 = PojoProperties.value(
                StereotypeApplicationImpl.class, "stereotype.name", String.class).observeDetail(
                observeSingleSelectionTreeViewer);
        bindingContext.bindValue(observeTextText_4ObserveWidget, treeViewerStereotypenameObserveDetailValue_1, null,
                null);
        //
        final IObservableValue observeTextText_5ObserveWidget = WidgetProperties.text(SWT.NONE).observe(
                this.text_appliedProperties);
        final IObservableValue observeSingleSelectionTreeViewer_1 = ViewerProperties.singleSelection().observe(
                this.treeViewer);
        final IObservableValue treeViewerStereotypetaggedValuesObserveDetailValue = PojoProperties.value(
                StereotypeApplicationImpl.class, "stereotype.taggedValues", EList.class).observeDetail(
                observeSingleSelectionTreeViewer_1);
        bindingContext.bindValue(observeTextText_5ObserveWidget, treeViewerStereotypetaggedValuesObserveDetailValue,
                null, null);
        //
        final IObservableValue observeTextText_3ObserveWidget = WidgetProperties.text(SWT.NONE).observe(
                this.text_appliedTo);
        final IObservableValue observeSingleSelectionTreeViewer_2 = ViewerProperties.singleSelection().observe(
                this.treeViewer);
        final IObservableValue treeViewerStereotypeEStructuralFeaturesObserveDetailValue = PojoProperties.value(
                StereotypeApplicationImpl.class, "appliedTo", EObject.class).observeDetail(
                observeSingleSelectionTreeViewer_2);
        bindingContext.bindValue(observeTextText_3ObserveWidget,
                treeViewerStereotypeEStructuralFeaturesObserveDetailValue, null, null);
        //
        final IObservableValue observeTextText_6ObserveWidget = WidgetProperties.text(SWT.NONE).observe(
                this.text_profileApplication);
        final IObservableValue observeSingleSelectionTreeViewer_3 = ViewerProperties.singleSelection().observe(
                this.treeViewer);
        final IObservableValue treeViewerProfileApplicationObserveDetailValue = PojoProperties.value(
                StereotypeApplication.class, "profileApplication", ProfileApplication.class).observeDetail(
                observeSingleSelectionTreeViewer_3);
        bindingContext.bindValue(observeTextText_6ObserveWidget, treeViewerProfileApplicationObserveDetailValue, null,
                null);
        //
        final IObservableValue observeTextText_2ObserveWidget = WidgetProperties.text(SWT.NONE).observe(
                this.text_extension);
        final IObservableValue observeSingleSelectionTreeViewer_4 = ViewerProperties.singleSelection().observe(
                this.treeViewer);
        final IObservableValue treeViewerStereotypeEAttributesObserveDetailValue = PojoProperties.value(
                StereotypeApplicationImpl.class, "extension", Extension.class).observeDetail(
                observeSingleSelectionTreeViewer_4);
        bindingContext.bindValue(observeTextText_2ObserveWidget, treeViewerStereotypeEAttributesObserveDetailValue,
                null, null);
        //
        final IObservableValue observeTooltipTextTblclmnFirstColumnObserveWidget = WidgetProperties.tooltipText()
                .observe(this.tblclmnFirstColumn);
        final IObservableValue observeSingleSelectionTreeViewer_5 = ViewerProperties.singleSelection().observe(
                this.treeViewer);
        final IObservableValue treeViewerAppliedToObserveDetailValue = PojoProperties.value(
                StereotypeApplication.class, "stereotype.taggedValues", EList.class).observeDetail(
                observeSingleSelectionTreeViewer_5);
        bindingContext.bindValue(observeTooltipTextTblclmnFirstColumnObserveWidget,
                treeViewerAppliedToObserveDetailValue, null, null);
        //
        return bindingContext;
    }

    /**
     * @return the master
     */
    public IObservableValue getMaster() {
        return this.master;
    }

    /**
     * @param values
     *            the values to set
     */
    public void setValues(final List<IObservableValue> values) {
        this.values = values;
    }

    /**
     * @return the tableViewer
     */
    public TableViewer getTableViewer() {
        return this.tableViewer;
    }

}
