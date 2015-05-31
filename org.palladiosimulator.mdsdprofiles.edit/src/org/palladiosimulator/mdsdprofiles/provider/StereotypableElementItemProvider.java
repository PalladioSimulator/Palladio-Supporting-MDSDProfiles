/**
 */
package org.palladiosimulator.mdsdprofiles.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.provider.EModelElementItemProvider;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.modelversioning.emfprofile.Stereotype;
import org.modelversioning.emfprofileapplication.StereotypeApplication;
import org.palladiosimulator.mdsdprofiles.MdsdprofilesPackage;
import org.palladiosimulator.mdsdprofiles.StereotypableElement;

/**
 * This is the item provider adapter for a
 * {@link org.palladiosimulator.mdsdprofiles.StereotypableElement} object. <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class StereotypableElementItemProvider extends EModelElementItemProvider {
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public StereotypableElementItemProvider(final AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
     * This returns the property descriptors for the adapted class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(final Object object) {
        if (this.itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);

            this.addProfileableElementPropertyDescriptor(object);
            this.addTaggedValuesPropertyDescriptor(object);
        }
        return this.itemPropertyDescriptors;
    }

    private void addTaggedValuesPropertyDescriptor(final Object object) {

        if (object instanceof StereotypableElement) {
            final StereotypableElement stereotypableElement = (StereotypableElement) object;

            for (final StereotypeApplication stereotypeApplication : stereotypableElement.getStereotypeApplications()) {
                final Stereotype stereotype = stereotypeApplication.getExtension().getSource();

                for (final EStructuralFeature attribute : stereotype.getTaggedValues()) {
                    // stereotype.getName();
                    // attribute.getName();
                    // stereotypeApplication.eGet(taggedValue);

                    // FIXME Tagged values do not work yet; enable them here [Lehrig]
                    this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(
                            ((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(),
                            this.getResourceLocator(),
                            stereotype.getName() + "::" + attribute.getName(),
                            "Tagged value for attribute \"" + attribute.getName() + "\" of stereotype \""
                                    + stereotype.getName() + "\"", EcorePackage.Literals.ECLASS__EALL_ATTRIBUTES, true,
                            false, false, null, null, null));
                }
            }
        }
    }

    /**
     * This adds a property descriptor for the Profileable Element feature. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void addProfileableElementPropertyDescriptor(final Object object) {
        this.itemPropertyDescriptors.add(this.createItemPropertyDescriptor(
                ((ComposeableAdapterFactory) this.adapterFactory).getRootAdapterFactory(), this.getResourceLocator(),
                this.getString("_UI_StereotypableElement_profileableElement_feature"), this.getString(
                        "_UI_PropertyDescriptor_description", "_UI_StereotypableElement_profileableElement_feature",
                        "_UI_StereotypableElement_type"),
                MdsdprofilesPackage.Literals.STEREOTYPABLE_ELEMENT__PROFILEABLE_ELEMENT, false, false, false, null,
                null, null));
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    @Override
    public String getText(final Object object) {
        return this.getString("_UI_StereotypableElement_type");
    }

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached
     * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}
     * . <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void notifyChanged(final Notification notification) {
        this.updateChildren(notification);
        super.notifyChanged(notification);
    }

    /**
     * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children that
     * can be created under this object. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected void collectNewChildDescriptors(final Collection<Object> newChildDescriptors, final Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);
    }

    /**
     * Return the resource locator for this item provider's resources. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator() {
        return MdsdprofilesEditPlugin.INSTANCE;
    }

}
