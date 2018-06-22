/**
 */
package org.palladiosimulator.mdsdprofiles;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each
 * non-abstract class of the model. <!-- end-user-doc -->
 *
 * @see org.palladiosimulator.mdsdprofiles.MdsdprofilesPackage
 * @generated
 */
public interface MdsdprofilesFactory extends EFactory {

    /**
     * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    MdsdprofilesFactory eINSTANCE = org.palladiosimulator.mdsdprofiles.impl.MdsdprofilesFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Profileable Element</em>'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return a new object of class '<em>Profileable Element</em>'.
     * @generated
     */
    ProfileableElement createProfileableElement();

    /**
     * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the package supported by this factory.
     * @generated
     */
    MdsdprofilesPackage getMdsdprofilesPackage();

} // MdsdprofilesFactory
