/**
 */
package myType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>My Types</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link myType.MyTypes#getTypes <em>Types</em>}</li>
 * </ul>
 * </p>
 *
 * @see myType.MyTypePackage#getMyTypes()
 * @model
 * @generated
 */
public interface MyTypes extends EObject {
	/**
	 * Returns the value of the '<em><b>Types</b></em>' containment reference list.
	 * The list contents are of type {@link myType.MyType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Types</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Types</em>' containment reference list.
	 * @see myType.MyTypePackage#getMyTypes_Types()
	 * @model containment="true"
	 * @generated
	 */
	EList<MyType> getTypes();

} // MyTypes
