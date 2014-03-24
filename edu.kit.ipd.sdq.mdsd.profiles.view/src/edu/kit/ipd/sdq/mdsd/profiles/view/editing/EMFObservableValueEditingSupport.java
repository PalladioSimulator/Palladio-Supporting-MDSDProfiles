package edu.kit.ipd.sdq.mdsd.profiles.view.editing;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.property.value.IValueProperty;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFObservables;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.databinding.viewers.ObservableValueEditingSupport;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ViewerCell;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

import edu.kit.ipd.sdq.mdsd.profiles.view.utility.FeatureGetterUtility;

public abstract class EMFObservableValueEditingSupport extends ObservableValueEditingSupport {
	
	// Use EMFDataBindingContext instead of DataBindingContext
	private EMFDataBindingContext dbc;
	
	public EMFObservableValueEditingSupport(ColumnViewer viewer, EMFDataBindingContext dbc) {
		super(viewer, dbc);
		this.dbc = dbc;
	}

	public static EditingSupport create(ColumnViewer viewer,
			EMFDataBindingContext dbc, 
			final CellEditor cellEditor,
			final IValueProperty cellEditorProperty,
			final IValueProperty elementProperty) {
		
		return new EMFObservableValueEditingSupport(viewer, dbc) {
			
			protected IObservableValue doCreateCellEditorObservable(CellEditor cellEditor) {
				return cellEditorProperty.observe(cellEditor);
			}

			protected IObservableValue doCreateElementObservable(Object element, ViewerCell cell) {
//				return elementProperty.observe(element);
				return EMFObservables.observeValue((EObject) element, FeatureGetterUtility.getFeatureForStereotypeApplication());
			}

			protected CellEditor getCellEditor(Object element) {
				return cellEditor;
			}
		};
	}
	
//	protected Binding createBinding(IObservableValue target, IObservableValue model) {
//		return dbc.bindValue(target, model, new EMFUpdateValueStrategy(UpdateValueStrategy.POLICY_CONVERT), 
//				new EMFUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
//	}
	
}

