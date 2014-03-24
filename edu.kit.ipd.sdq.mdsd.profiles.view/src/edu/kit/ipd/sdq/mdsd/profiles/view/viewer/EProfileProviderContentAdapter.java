package edu.kit.ipd.sdq.mdsd.profiles.view.viewer;

import java.util.Iterator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.modelversioning.emfprofile.application.registry.ui.providers.ProfileProviderContentAdapter;

import edu.kit.ipd.sdq.mdsd.profiles.metamodelextension.EStereotypableObject;

public class EProfileProviderContentAdapter extends
		ProfileProviderContentAdapter implements IResourceChangeListener {
	
	private AdapterFactoryContentProvider provider;
	private IResource input;
	private TreeViewer treeViewer;

	public EProfileProviderContentAdapter(ComposedAdapterFactory adapterFactory) {
		super(adapterFactory);
		provider = new AdapterFactoryContentProvider(adapterFactory);
	}

	public void dispose() {
		if (input != null) {
			input.getWorkspace().removeResourceChangeListener(this);
			input = null;
		}	
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//		provider.inputChanged(viewer, oldInput, newInput);

        if (this.input != null) {
            this.input.getWorkspace().removeResourceChangeListener(this);
        }

        try {
			this.input = (IResource) newInput;
		} catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (this.input != null) {
            this.input.getWorkspace().addResourceChangeListener(this,
                    IResourceChangeEvent.POST_CHANGE);
        }

        this.treeViewer = (TreeViewer) viewer;
		
//		if(oldInput != null) {
//			removeListenerFrom((EStereotypableObject)oldInput);
//		}
//		if(newInput != null) {
//			addListenerTo((EStereotypableObject)newInput);
//		}
	}
	
//	/** Because the domain model does not have a richer
//	 * listener model, recursively remove this listener
//	 * from each child box of the given box. */
//	@SuppressWarnings("rawtypes")
//	protected void removeListenerFrom(EStereotypableObject eStereotypableObject) {
//		eStereotypableObject.removeListener((IResourceChangeListener) this);
//		for (Iterator iterator = eStereotypableObject.getStereotypeApplications().iterator(); iterator.hasNext();) {
//			EStereotypableObject eObject = (EStereotypableObject) iterator.next();
//			removeListenerFrom(eObject);
//		}
//	}
//	
//	/** Because the domain model does not have a richer
//	 * listener model, recursively add this listener
//	 * to each child box of the given box. */
//	@SuppressWarnings("rawtypes")
//	protected void addListenerTo(EStereotypableObject eStereotypableObject) {
//		eStereotypableObject.addListener((IResourceChangeListener) this);
//		for (Iterator iterator = eStereotypableObject.getStereotypeApplications().iterator(); iterator.hasNext();) {
//			EStereotypableObject eObject = (EStereotypableObject) iterator.next();
//			addListenerTo(eObject);
//		}
//	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		treeViewer.refresh();
	}
}
