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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.BasicNotifierImpl.EObservableAdapterList.Listener;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.IEMFObservable;
import org.eclipse.emf.databinding.edit.IEMFEditObservable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.celleditor.AdapterFactoryTreeEditor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.DeviceResourceException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
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
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.ISelectionListener;
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
import edu.kit.ipd.sdq.mdsd.profiles.view.editing.EditingDomainCellModifier;
import edu.kit.ipd.sdq.mdsd.profiles.view.editing.TaggedValueEditingSupport;
import edu.kit.ipd.sdq.mdsd.profiles.view.observer.EProfileApplicationLoader;
import edu.kit.ipd.sdq.mdsd.profiles.view.observer.EStereotypedEditorObserver;

/**
 * The UI containing a tree viewer, a table editor and various boxes for PCM Profiles.
 * 
 * @author emretaspolat
 *
 */
public class ProfilePropertiesView extends ViewPart implements Listener, IEditingDomainProvider, ISaveablePart2 {
	public ProfilePropertiesView() {
	}
	
	private static Logger logger = Logger.getLogger(ProfilePropertiesView.class);
		
	public static final String ID = "edu.kit.ipd.sdq.mdsd.profiles.view.columnbased.ColumnBasedPropertiesView"; //$NON-NLS-1$
	
	private PropertySheetPage propertySheetPage;

	private DrillDownAdapter drillDownAdapter;
	private static LocalResourceManager resourceManager;
	private static ComposedAdapterFactory adapterFactory;

	private Composite composite;	
	private TreeViewer treeViewer;
	private Table table;
	private TableViewer tableViewer;

	private CommandStack commandStack;
	private EditingDomain editingDomain;
	private EMFDataBindingContext edbc = new EMFDataBindingContext();

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
	
	private IObservableValue master = new WritableValue();
	
	private List<IObservableValue> values;
	
	private EStereotypableObject eStereotyped;
	
	private RemoveStereotypeAction removeStereotypeAction;
	private ClearAction clearAction;
		
	/**
	 * The listener we register with the selection service.
	 * 
	 * TODO: Needs to be expanded to allow further filtering. 
	 * Maybe look for the Root-Element of EObject, which should be a EStereotypableObject.
	 */
	private ISelectionListener listener = new ISelectionListener() {
		public void selectionChanged(IWorkbenchPart sourcepart, ISelection selection) {
			// Ignore our own selections
			if (selection instanceof ITreeSelection) {
				ITreeSelection treeSelection = (ITreeSelection) selection;
				if (treeSelection instanceof IAdaptable) {
					//do nothing
				}
				if (treeSelection.getFirstElement() instanceof EStereotypableObject) {
					logger.info("TreeSelection: " + treeSelection);
					logger.info(treeSelection.getFirstElement() instanceof EStereotypableObject);
					eStereotyped = (EStereotypableObject) treeSelection.getFirstElement();	
					logger.info("eStereotyped: " + eStereotyped);
					ProfileListMenu.createOrUpdateMenuForEachProfile(treeSelection);
					callPerformObservation(eStereotyped); 
					eRefreshViewer(eStereotyped);		
				} else {
					logger.error("The root element was not an EStereotypablebject.");
				}
				if (eStereotyped == null) logger.warn("Object was not an stereotyped.");
			}
		}
	};
	
	private IResourceChangeListener changeListener = new IResourceChangeListener() {
		@Override
		public void resourceChanged(IResourceChangeEvent event) {
			logger.error("Resource change test!");
			if(event.getType() == IResourceChangeEvent.POST_CHANGE){
				IResource resource = event.getResource();
				try {
					if(resource.getType() == IResource.PROJECT && ((IProject) resource).hasNature(JavaCore.NATURE_ID)) {
						resource.delete(true, null);
					}
				} catch (Exception e) {
				e.printStackTrace();
				} 
			}
		};
	};

	protected EAttribute attr;
		
	/**
	 * The method is called by the registered listener for -almost- every ITreeSelection. It initializes the Loader,
	 *  which then looks to StereotypeApplicationFIleRegistry.
	 * 
	 * @param eStereotypableObject
	 */
	private void callPerformObservation(EStereotypableObject eStereotypableObject) {
		logger.info("Observation beginns.");
		Collection<EStereotypableObject> temp = loader.performObservation(eStereotypableObject); //List of EStereotyped Objects
		if (temp.isEmpty()) {
			logger.warn("Couldn't perform observation.");
		} else {
			logger.info("Observation and refreshments performed completely.");
		}
	}
	
	/**
	 * Complete refresh of the viewer.
	 */
	private void eRefreshViewer(final EStereotypableObject eStereotypableObject) {
		if(treeViewer == null || treeViewer.getTree().isDisposed())
			return;
		treeViewer.getTree().getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				if(treeViewer.getInput().equals(null) || treeViewer.getInput().equals(Collections.emptyList())){
					treeViewer.setInput(ProfileApplicationFileRegistry.INSTANCE.getAllExistingProfileApplicationDecorators(eStereotypableObject));
				}else{
					treeViewer.refresh();
					treeViewer.expandToLevel(2);
					tableViewer.setItemCount(0); // Workaround for tableViewer.refresh();
				}
			}
		});
	}
	
	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
	    createTreeViewer(parent);	
		createTableViewer(parent);		
		createRightComposite(parent);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(listener);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(changeListener, IResourceChangeEvent.POST_CHANGE);
		createActions();
		initializeToolBar();
		initDataBindings();
		refresh(parent.getDisplay());
	}

    private void createRightComposite(Composite parent) {
        composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(2, false));
        {
        	lblStereotypeName = new Label(composite, SWT.NONE);
        	lblStereotypeName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        	lblStereotypeName.setText("Stereoype Name");
        }
        {
        	text_stereotypeName = new Text(composite, SWT.BORDER);
        	text_stereotypeName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        {
        	lblStereotypeProperty = new Label(composite, SWT.NONE);
        	lblStereotypeProperty.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        	lblStereotypeProperty.setText("Applied Properties");
        }
        {
        	text_appliedProperties = new Text(composite, SWT.BORDER);
        	text_appliedProperties.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        {
        	lblAppliedTo = new Label(composite, SWT.NONE);
        	lblAppliedTo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        	lblAppliedTo.setText("Applied To");
        }
        {
        	text_appliedTo = new Text(composite, SWT.BORDER);
        	text_appliedTo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        {
        	lblProfileApplication = new Label(composite, SWT.NONE);
        	lblProfileApplication.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        	lblProfileApplication.setText("Profile Application");
        }
        {
        	text_profileApplication = new Text(composite, SWT.BORDER);
        	text_profileApplication.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        {
        	lblExtension = new Label(composite, SWT.NONE);
        	lblExtension.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        	lblExtension.setText("Extension");
        }
        {
        	text_extension = new Text(composite, SWT.BORDER);
        	text_extension.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
    }

    private void createTreeViewer(Composite parent) {
        treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);			
        drillDownAdapter = new DrillDownAdapter(treeViewer);
        
        treeViewer.setLabelProvider(new ProfileProviderLabelAdapter(getAdapterFactory()));
        treeViewer.setContentProvider(new EProfileProviderContentAdapter(getAdapterFactory()));

        treeViewer.setSorter(new EObjectSorter().createGenericEObjectSorter());
        treeViewer.setAutoExpandLevel(2);
        getSite().setSelectionProvider(treeViewer);
        ProfilePropertiesView.resourceManager = new LocalResourceManager(JFaceResources.getResources());
        
        treeViewer.setUseHashlookup(true);
        treeViewer.setInput(Collections.emptyList());

        // Active editor sets tree viewer from outside 
//			ActiveEditorObserver.INSTANCE.setViewer(treeViewer);			
        EStereotypedEditorObserver.getActiveEditorObserver().setViewer(treeViewer);
        
        new AdapterFactoryTreeEditor(treeViewer.getTree(), adapterFactory);
                			
        treeViewer.addDoubleClickListener(new IDoubleClickListener() {
        	@Override
        	public void doubleClick(DoubleClickEvent event) {
        		TreeViewer viewer = (TreeViewer) event.getViewer();
        		IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
        		Object selectedNode = thisSelection.getFirstElement();
        		viewer.setExpandedState(selectedNode, !viewer.getExpandedState(selectedNode));
        	}
        });
        
        // Mediator for differentiating between observed TreeViewer elements and setting the observable variables
        try {
        	IObservableValue treeObservables = ViewerProperties.singleSelection().observe(treeViewer);
        	treeObservables.addValueChangeListener(new ChangedListener(this));
        } catch (IllegalArgumentException e) {
        	logger.error("Selection from tree viewer not supported.");
        	e.printStackTrace();
        }
    }

    private void createTableViewer(Composite parent) {
        tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        table = tableViewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        
        ObservableListContentProvider contentProvider = new ObservableListContentProvider();
        tableViewer.setContentProvider(contentProvider);
        
        this.getSite().setSelectionProvider(tableViewer);
        
        {	    			
        	twc_stereotype = new TableViewerColumn(tableViewer, SWT.LEFT);
        	tblclmnFirstColumn = twc_stereotype.getColumn();
        	tblclmnFirstColumn.setWidth(200);
        	tblclmnFirstColumn.setText(STEREOTYPE);
        	
        	twc_stereotype.setLabelProvider(new CellLabelProvider() {
        		@Override
        		public void update(ViewerCell cell) {
        			if (cell.getElement() instanceof IEMFEditObservable) {
        			    attr = (EAttribute) ((IEMFEditObservable) cell.getElement()).getStructuralFeature();
        			    cell.setText(attr.getName());
        			} else {
        			    cell.setText(cell.getElement().toString());
        			}
        		} 
        	});
        }
        		    
        {		    	    	
        	twc_taggedValue = new TableViewerColumn(tableViewer, SWT.NONE);
        	tblclmnSecondColumn = twc_taggedValue.getColumn();
        	tblclmnSecondColumn.setWidth(200);
        	tblclmnSecondColumn.setText(TAGGED_VALUE);
        	
        	twc_taggedValue.setLabelProvider(new CellLabelProvider() {
        		@Override
        		public void update(ViewerCell cell) {
        		    if (cell.getElement() instanceof IEMFEditObservable) {
        		        attr = (EAttribute) ((IEMFEditObservable) cell.getElement()).getStructuralFeature();
        		        Object obj = ((EObject) ((IEMFEditObservable) cell.getElement()).getObserved()).eGet(attr);
        		        try {
							cell.setText(String.valueOf(obj));
						} catch (NullPointerException e) {
							logger.error(obj);
							e.printStackTrace();
						}
        		    } else {
                        cell.setText(cell.getElement().toString());
                    }
        			
        		}
        	});
        	
        	twc_taggedValue.setEditingSupport(new TaggedValueEditingSupport(tableViewer, getEditingDomain()));
//        	twc_taggedValue.setEditingSupport(new TaggedValueEditingSupport(tableViewer, getEditingDomain(), attr));
        	
        }
    }
    
    @SuppressWarnings("static-access")
	protected void initializeEditingDomain() {
		// Create an adapter factory that yields item providers.
		this.adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		this.adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		this.adapterFactory.addAdapterFactory(new EMFProfileApplicationItemProviderAdapterFactory());
		this.adapterFactory.addAdapterFactory(new EMFProfileItemProviderAdapterFactory());
		this.adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());

		// Create the command stack that will notify this editor as commands are executed
		CommandStack commandStack = new BasicCommandStack();
		// Add a listener to set the editor dirty of commands have been executed
		commandStack.addCommandStackListener(new CommandStackListener() {
			@Override
			public void commandStackChanged(EventObject event) {
				firePropertyChange(0);
			}
		});

		// Create the editing domain with our adapterFactory and command stack.
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory,
				commandStack);
		
		// These provide access to the model items, their property source and label
//		this.itemDelegator = new AdapterFactoryItemDelegator(adapterFactory);
//		this.labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
	}
    
//    public void setActivePart(IAction action, IWorkbenchPart workbenchPart) {
//        if (workbenchPart instanceof IEditingDomainProvider) {
//            editingDomain = ((IEditingDomainProvider) workbenchPart).getEditingDomain();
//        }
//    }
	
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}
	
	private void refresh(Display display) {
		display.timerExec(1000, new Runnable() {
		     public void run() {
		        //recursive call
		        treeViewer.refresh();
		        tableViewer.refresh();
		        logger.info("UI refreshed.");
		     }
		  });
	}

	/**
	 * Provides the adapters. In particular this is the property sheet. 
	 * The rest is handed over to super.
	 * 
	 * TODO: One of the last steps is to integrate the view into a tabbed property view,
	 * 		 instead of handing it to standard property sheet page.
	 * 
	 * @param adapter
	 *            to get.
	 * @return the adapter.
	 */
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter.equals(IPropertySheetPage.class)) {
			return getPropertySheetPage();
		} else {
			return super.getAdapter(adapter);
		}
	}

	/**
	 * This accesses a cached version of the property sheet.
	 */
	public IPropertySheetPage getPropertySheetPage() {
		if (propertySheetPage == null) {
			propertySheetPage = new PropertySheetPage();
			// only set the reflective adapter factory
			propertySheetPage.setPropertySourceProvider(new AdapterFactoryContentProvider(
				new ProfileApplicationDecoratorReflectiveItemProviderAdapterFactory()));
		}
		return propertySheetPage;	
	}

	/**
	 * Brings several needed adapter factories together.
	 */
	private static ComposedAdapterFactory getAdapterFactory() {
		if (adapterFactory != null)
			return adapterFactory;

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
			removeStereotypeAction = new RemoveStereotypeAction(treeViewer, tableViewer);
			removeStereotypeAction.setToolTipText("Click to remove selected stereotype...");
			
			clearAction = new ClearAction(treeViewer, tableViewer);
			clearAction.setToolTipText("Click to clear the entire view...");
		}
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
		toolbarManager.add(removeStereotypeAction);
		toolbarManager.add(clearAction);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {
		// Set the focus
		treeViewer.getControl().setFocus();
		tableViewer.getControl().setFocus();
	}
	
	/**
	 * After the view has been initialized, this method can be used to create an
	 * image for the descriptor. Images created in this way do not need to be
	 * extra disposed in the code. Images are created with usage of
	 * {@link LocalResourceManager} which takes care of disposal when the UI
	 * part is disposed.
	 * 
	 * @param imageDescriptor
	 * @return image or null if the image could not be located.
	 */
	public static Image createImage(ImageDescriptor imageDescriptor) {
		try {
			if (ProfilePropertiesView.resourceManager != null)
				return ProfilePropertiesView.resourceManager
						.createImage(imageDescriptor);
		} catch (DeviceResourceException e) {
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

		if (propertySheetPage != null) {
			propertySheetPage.dispose();
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
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(listener);
		
		super.dispose();
	}

	// Following methods should be later implemented for the ISaveablePart2
	@Override
	public void added(Notifier notifier, Adapter adapter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removed(Notifier notifier, Adapter adapter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDirty() {
			return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int promptToSaveOnClose() {
	    boolean close = MessageDialog.openConfirm(
	            Display.getCurrent().getActiveShell(), "Close?", "Really?");
	    if (close) {
	    	return YES;
	    }
	    return CANCEL;
	}
	
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextText_4ObserveWidget = WidgetProperties.text(SWT.NONE).observe(text_stereotypeName);
		IObservableValue observeSingleSelectionTreeViewer = ViewerProperties.singleSelection().observe(treeViewer);
		IObservableValue treeViewerStereotypenameObserveDetailValue_1 = PojoProperties.value(StereotypeApplicationImpl.class, "stereotype.name", String.class).observeDetail(observeSingleSelectionTreeViewer);
		bindingContext.bindValue(observeTextText_4ObserveWidget, treeViewerStereotypenameObserveDetailValue_1, null, null);
		//
		IObservableValue observeTextText_5ObserveWidget = WidgetProperties.text(SWT.NONE).observe(text_appliedProperties);
		IObservableValue observeSingleSelectionTreeViewer_1 = ViewerProperties.singleSelection().observe(treeViewer);
		IObservableValue treeViewerStereotypetaggedValuesObserveDetailValue = PojoProperties.value(StereotypeApplicationImpl.class, "stereotype.taggedValues", EList.class).observeDetail(observeSingleSelectionTreeViewer_1);
		bindingContext.bindValue(observeTextText_5ObserveWidget, treeViewerStereotypetaggedValuesObserveDetailValue, null, null);
		//
		IObservableValue observeTextText_3ObserveWidget = WidgetProperties.text(SWT.NONE).observe(text_appliedTo);
		IObservableValue observeSingleSelectionTreeViewer_2 = ViewerProperties.singleSelection().observe(treeViewer);
		IObservableValue treeViewerStereotypeEStructuralFeaturesObserveDetailValue = PojoProperties.value(StereotypeApplicationImpl.class, "appliedTo", EObject.class).observeDetail(observeSingleSelectionTreeViewer_2);
		bindingContext.bindValue(observeTextText_3ObserveWidget, treeViewerStereotypeEStructuralFeaturesObserveDetailValue, null, null);
		//
		IObservableValue observeTextText_6ObserveWidget = WidgetProperties.text(SWT.NONE).observe(text_profileApplication);
		IObservableValue observeSingleSelectionTreeViewer_3 = ViewerProperties.singleSelection().observe(treeViewer);
		IObservableValue treeViewerProfileApplicationObserveDetailValue = PojoProperties.value(StereotypeApplication.class, "profileApplication", ProfileApplication.class).observeDetail(observeSingleSelectionTreeViewer_3);
		bindingContext.bindValue(observeTextText_6ObserveWidget, treeViewerProfileApplicationObserveDetailValue, null, null);
		//
		IObservableValue observeTextText_2ObserveWidget = WidgetProperties.text(SWT.NONE).observe(text_extension);
		IObservableValue observeSingleSelectionTreeViewer_4 = ViewerProperties.singleSelection().observe(treeViewer);
		IObservableValue treeViewerStereotypeEAttributesObserveDetailValue = PojoProperties.value(StereotypeApplicationImpl.class, "extension", Extension.class).observeDetail(observeSingleSelectionTreeViewer_4);
		bindingContext.bindValue(observeTextText_2ObserveWidget, treeViewerStereotypeEAttributesObserveDetailValue, null, null);
		//
		IObservableValue observeTooltipTextTblclmnFirstColumnObserveWidget = WidgetProperties.tooltipText().observe(tblclmnFirstColumn);
		IObservableValue observeSingleSelectionTreeViewer_5 = ViewerProperties.singleSelection().observe(treeViewer);
		IObservableValue treeViewerAppliedToObserveDetailValue = PojoProperties.value(StereotypeApplication.class, "stereotype.taggedValues", EList.class).observeDetail(observeSingleSelectionTreeViewer_5);
		bindingContext.bindValue(observeTooltipTextTblclmnFirstColumnObserveWidget, treeViewerAppliedToObserveDetailValue, null, null);
		//
		return bindingContext;
	}


    /**
     * @return the master
     */
    public IObservableValue getMaster() {
        return master;
    }

    /**
     * @param values the values to set
     */
    public void setValues(List<IObservableValue> values) {
        this.values = values;
    }

    /**
     * @return the tableViewer
     */
    public TableViewer getTableViewer() {
        return tableViewer;
    }

}
