/**
 */
package edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>A</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.A#getBs <em>Bs</em>}</li>
 *   <li>{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.A#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.simplemodelPackage#getA()
 * @model
 * @generated
 */
public interface A extends Super {
    /**
     * Returns the value of the '<em><b>Bs</b></em>' containment reference list.
     * The list contents are of type {@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.B}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bs</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bs</em>' containment reference list.
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.simplemodelPackage#getA_Bs()
     * @model containment="true"
     * @generated
     */
    EList<B> getBs();

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.simplemodelPackage#getA_Name()
     * @model required="true"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link edu.kit.ipd.sdq.mdsd.profiles.examples.simplemodel.A#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

} // A
