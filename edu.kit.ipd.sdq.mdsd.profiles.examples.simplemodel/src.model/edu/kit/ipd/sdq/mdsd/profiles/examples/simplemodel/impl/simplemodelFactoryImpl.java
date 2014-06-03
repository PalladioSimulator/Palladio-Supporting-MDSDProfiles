/**
 */
package edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.impl;

import edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class simplemodelFactoryImpl extends EFactoryImpl implements simplemodelFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static simplemodelFactory init() {
        try {
            simplemodelFactory thesimplemodelFactory = (simplemodelFactory)EPackage.Registry.INSTANCE.getEFactory(simplemodelPackage.eNS_URI);
            if (thesimplemodelFactory != null) {
                return thesimplemodelFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new simplemodelFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public simplemodelFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case simplemodelPackage.A: return (EObject)createA();
            case simplemodelPackage.B: return (EObject)createB();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public A createA() {
        AImpl a = new AImpl();
        return a;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public B createB() {
        BImpl b = new BImpl();
        return b;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public simplemodelPackage getsimplemodelPackage() {
        return (simplemodelPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static simplemodelPackage getPackage() {
        return simplemodelPackage.eINSTANCE;
    }

} //simplemodelFactoryImpl
