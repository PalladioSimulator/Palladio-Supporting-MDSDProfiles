/**
 */
package myType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>My Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link myType.MyType#getTypeID <em>Type ID</em>}</li>
 *   <li>{@link myType.MyType#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see myType.MyTypePackage#getMyType()
 * @model
 * @generated
 */
public interface MyType extends EObject {
	/**
	 * Returns the value of the '<em><b>Type ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type ID</em>' attribute.
	 * @see #setTypeID(int)
	 * @see myType.MyTypePackage#getMyType_TypeID()
	 * @model
	 * @generated
	 */
	int getTypeID();

	/**
	 * Sets the value of the '{@link myType.MyType#getTypeID <em>Type ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type ID</em>' attribute.
	 * @see #getTypeID()
	 * @generated
	 */
	void setTypeID(int value);

	/**
	 * Returns the value of the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type Name</em>' attribute.
	 * @see #setTypeName(String)
	 * @see myType.MyTypePackage#getMyType_TypeName()
	 * @model
	 * @generated
	 */
	String getTypeName();

	/**
	 * Sets the value of the '{@link myType.MyType#getTypeName <em>Type Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Name</em>' attribute.
	 * @see #getTypeName()
	 * @generated
	 */
	void setTypeName(String value);

} // MyType
