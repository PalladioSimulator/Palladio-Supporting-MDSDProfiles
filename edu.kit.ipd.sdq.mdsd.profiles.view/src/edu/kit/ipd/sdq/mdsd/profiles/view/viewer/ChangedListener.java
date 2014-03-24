package edu.kit.ipd.sdq.mdsd.profiles.view.viewer;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPart;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofile.application.registry.ProfileApplicationDecorator;
import org.modelversioning.emfprofileapplication.ProfileApplication;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

import edu.kit.ipd.sdq.mdsd.profiles.view.utility.FeatureGetterUtility;

/**
 * ChangedListener, der auf �nderungen der Selektion im TreeViewer reagiert und entsprechend dem
 * View das selektierte StereotypeApplication-Objekt �bergibt.
 * 
 * @author kuester
 * 
 */
public class ChangedListener implements IValueChangeListener {

    private static Logger logger = Logger.getLogger(ProfilePropertiesView.class);

    private ProfilePropertiesView view = null;
    
    EMFDataBindingContext edbc = new EMFDataBindingContext();

	private IObservableValue value;

//	private EditingDomain editingDomain;
//	
//	public void setActivePart(IAction action, IWorkbenchPart workbenchPart) {
//	    if (workbenchPart instanceof IEditingDomainProvider) {
//	        editingDomain = ((IEditingDomainProvider) workbenchPart).getEditingDomain();
//	    }
//	}

    public ChangedListener(ProfilePropertiesView view) {
        this.view = view;
    }

    @Override
    public void handleValueChange(ValueChangeEvent event) {
        if (event.diff.getNewValue() instanceof StereotypeApplication) {
            logger.info("TreeViewer selecetion:" + event.diff.getNewValue());
            StereotypeApplication stereotypeApplication = (StereotypeApplication) event.diff.getNewValue();
            logger.info("Selected StereotypeAplication: " + stereotypeApplication);

            Stereotype stereotype = ((StereotypeApplication) event.diff.getNewValue()).getStereotype();
            logger.info("Selected Stereotype: " + stereotype);
            view.getMaster().setValue(stereotypeApplication);

            List<IObservableValue> values = new ArrayList<IObservableValue>();
            for (EAttribute attribute : FeatureGetterUtility
                    .getFeatureListOfStereotypeApplication((stereotypeApplication))) {
                value = EMFEditProperties.value(view.getEditingDomain(), attribute).observeDetail(view.getMaster());
                values.add(value);
            }
            view.setValues(values);
            IObservableList observableList = new WritableList();
            observableList.addAll(values);
            view.getTableViewer().setInput(observableList);
            
//            edbc.bindList((IObservableList) view.getTableViewer().getInput(), observableList);

        } else if (event.diff.getNewValue() instanceof ProfileApplicationDecorator) {
            ProfileApplicationDecorator pad = (ProfileApplicationDecorator) event.diff.getNewValue();
            view.getMaster().setValue((ProfileApplication) pad.getProfileApplications());
        }
    }

}
