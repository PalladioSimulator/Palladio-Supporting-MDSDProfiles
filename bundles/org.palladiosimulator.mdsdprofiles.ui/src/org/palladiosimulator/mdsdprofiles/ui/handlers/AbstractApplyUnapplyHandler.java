package org.palladiosimulator.mdsdprofiles.ui.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.ui.celleditor.FeatureEditorDialog;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.modelversioning.emfprofile.provider.EMFProfileItemProviderAdapterFactory;

/**
 * Handles apply and unapply operations to a given target element via an FeatureEditorDialog.
 * Subject of application is a set of EObject-extending elements.
 * 
 * @author Sebastian Lehrig
 */
public abstract class AbstractApplyUnapplyHandler extends AbstractHandler {

    protected static final ILabelProvider LABEL_PROVIDER = getLabelProvider();

    public AbstractApplyUnapplyHandler() {
        super();
    }

    @Override
    public Object execute(final ExecutionEvent event) throws ExecutionException {
        applyUnapplyStateChanged(event);

        return null;
    }

    protected abstract void applyUnapplyStateChanged(final ExecutionEvent event) throws ExecutionException;

    private static ILabelProvider getLabelProvider() {
        final EMFProfileItemProviderAdapterFactory adapterFactory = new EMFProfileItemProviderAdapterFactory();
        return new AdapterFactoryLabelProvider(adapterFactory);
    }

    @SuppressWarnings("unchecked")
    protected static <TARGET_ELEMENT_TYPE> TARGET_ELEMENT_TYPE getTargetElement(final ExecutionEvent event)
            throws ExecutionException {
        final ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
        if (!(selection instanceof IStructuredSelection)) {
            throw new RuntimeException("Selection [" + selection + "] needs to be an IStructuredSelection!");
        }
        final Object object = ((IStructuredSelection) selection).getFirstElement();

        return (TARGET_ELEMENT_TYPE) object;
    }

    @SuppressWarnings("unchecked")
    protected static <TARGET_ELEMENT_TYPE extends EObject, PROFILE_ELEMENT_TYPE> EList<PROFILE_ELEMENT_TYPE> getUpdatedProfileElementsFromDialog(
            final ExecutionEvent event, final TARGET_ELEMENT_TYPE targetElement,
            final List<PROFILE_ELEMENT_TYPE> currentValues, final List<PROFILE_ELEMENT_TYPE> choiceOfValues,
            final String displayName) throws ExecutionException {
        final FeatureEditorDialog dialog = new FeatureEditorDialog(HandlerUtil.getActiveShellChecked(event),
                LABEL_PROVIDER, targetElement, targetElement.eClass().getEAllStructuralFeatures().get(0).getEType(),
                currentValues, displayName, choiceOfValues, false, true, true);
        dialog.open();
        return (EList<PROFILE_ELEMENT_TYPE>) dialog.getResult();
    }

    static public EditingDomain getEditingDomainFor(final EObject object) {
        final Resource resource = object.eResource();
        if (resource != null) {
            IEditingDomainProvider editingDomainProvider = (IEditingDomainProvider) EcoreUtil.getExistingAdapter(
                    resource, IEditingDomainProvider.class);
            if (editingDomainProvider != null) {
                return editingDomainProvider.getEditingDomain();
            } else {
                final ResourceSet resourceSet = resource.getResourceSet();
                if (resourceSet instanceof IEditingDomainProvider) {
                    return ((IEditingDomainProvider) resourceSet).getEditingDomain();
                } else if (resourceSet != null) {
                    editingDomainProvider = (IEditingDomainProvider) EcoreUtil.getExistingAdapter(resourceSet,
                            IEditingDomainProvider.class);
                    if (editingDomainProvider != null) {
                        return editingDomainProvider.getEditingDomain();
                    }
                }
            }
        }

        return null;
    }
}