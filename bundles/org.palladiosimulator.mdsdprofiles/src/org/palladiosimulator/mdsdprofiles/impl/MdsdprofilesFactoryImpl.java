/**
 */
package org.palladiosimulator.mdsdprofiles.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.palladiosimulator.mdsdprofiles.MdsdprofilesFactory;
import org.palladiosimulator.mdsdprofiles.MdsdprofilesPackage;
import org.palladiosimulator.mdsdprofiles.ProfileableElement;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 *
 * @generated
 */
public class MdsdprofilesFactoryImpl extends EFactoryImpl implements MdsdprofilesFactory {

    /**
     * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public static MdsdprofilesFactory init() {
        try {
            final MdsdprofilesFactory theMdsdprofilesFactory = (MdsdprofilesFactory) EPackage.Registry.INSTANCE
                    .getEFactory(MdsdprofilesPackage.eNS_URI);
            if (theMdsdprofilesFactory != null) {
                return theMdsdprofilesFactory;
            }
        } catch (final Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new MdsdprofilesFactoryImpl();
    }

    /**
     * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public MdsdprofilesFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EObject create(final EClass eClass) {
        switch (eClass.getClassifierID()) {
        case MdsdprofilesPackage.PROFILEABLE_ELEMENT:
            return this.createProfileableElement();
        default:
            throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ProfileableElement createProfileableElement() {
        final ProfileableElementImpl profileableElement = new ProfileableElementImpl();
        return profileableElement;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public MdsdprofilesPackage getMdsdprofilesPackage() {
        return (MdsdprofilesPackage) this.getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @deprecated
     * @generated
     */
    @Deprecated
    public static MdsdprofilesPackage getPackage() {
        return MdsdprofilesPackage.eINSTANCE;
    }

} // MdsdprofilesFactoryImpl
