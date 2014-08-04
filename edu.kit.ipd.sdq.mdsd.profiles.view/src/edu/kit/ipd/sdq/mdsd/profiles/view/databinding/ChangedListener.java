package edu.kit.ipd.sdq.mdsd.profiles.view.databinding;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.ecore.EAttribute;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofile.application.registry.ProfileApplicationDecorator;
import org.modelversioning.emfprofileapplication.StereotypeApplication;

import edu.kit.ipd.sdq.mdsd.profiles.view.utility.FeatureGetterUtility;
import edu.kit.ipd.sdq.mdsd.profiles.view.viewer.ProfilePropertiesView;

/**
 * ChangedListener, der auf �nderungen der Selektion im TreeViewer reagiert und entsprechend dem
 * View das selektierte StereotypeApplication-Objekt �bergibt.
 * 
 * @author kuester
 * 
 */
public class ChangedListener implements IValueChangeListener {

    private static final Logger LOGGER = Logger.getLogger(ProfilePropertiesView.class);

    private ProfilePropertiesView view = null;

    EMFDataBindingContext edbc = new EMFDataBindingContext();

    private IObservableValue value;

    public ChangedListener(final ProfilePropertiesView view) {
        this.view = view;
    }

    @Override
    public void handleValueChange(final ValueChangeEvent event) {
        if (event.diff.getNewValue() instanceof StereotypeApplication) {
            LOGGER.info("TreeViewer selecetion:" + event.diff.getNewValue());
            final StereotypeApplication stereotypeApplication = (StereotypeApplication) event.diff.getNewValue();
            LOGGER.info("Selected StereotypeAplication: " + stereotypeApplication);

            final Stereotype stereotype = ((StereotypeApplication) event.diff.getNewValue()).getStereotype();
            LOGGER.info("Selected Stereotype: " + stereotype);
            this.view.getMaster().setValue(stereotypeApplication);

            final List<IObservableValue> values = new ArrayList<IObservableValue>();
            for (final EAttribute attribute : FeatureGetterUtility
                    .getFeatureListOfStereotypeApplication((stereotypeApplication))) {
                this.value = EMFEditProperties.value(this.view.getEditingDomain(), attribute).observeDetail(
                        this.view.getMaster());
                values.add(this.value);
            }
            this.view.setValues(values);
            final IObservableList observableList = new WritableList();
            observableList.addAll(values);
            this.view.getTableViewer().setInput(observableList);

        } else if (event.diff.getNewValue() instanceof ProfileApplicationDecorator) {
            this.view.getTableViewer().setItemCount(0);
            throw new ClassCastException();
            // final ProfileApplicationDecorator pad = (ProfileApplicationDecorator)
            // event.diff.getNewValue();
            // this.view.getMaster().setValue(pad.getProfileApplications());
        }
    }

}
