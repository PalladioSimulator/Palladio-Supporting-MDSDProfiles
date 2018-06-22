/**
 */
package myType;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see myType.MyTypeFactory
 * @model kind="package"
 * @generated
 */
public interface MyTypePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "myType";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://myType/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "myType";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	MyTypePackage eINSTANCE = myType.impl.MyTypePackageImpl.init();

	/**
	 * The meta object id for the '{@link myType.impl.MyTypeImpl <em>My Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see myType.impl.MyTypeImpl
	 * @see myType.impl.MyTypePackageImpl#getMyType()
	 * @generated
	 */
	int MY_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Type ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_TYPE__TYPE_ID = 0;

	/**
	 * The feature id for the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_TYPE__TYPE_NAME = 1;

	/**
	 * The number of structural features of the '<em>My Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_TYPE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>My Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_TYPE_OPERATION_COUNT = 0;


	/**
	 * The meta object id for the '{@link myType.impl.MyTypesImpl <em>My Types</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see myType.impl.MyTypesImpl
	 * @see myType.impl.MyTypePackageImpl#getMyTypes()
	 * @generated
	 */
	int MY_TYPES = 1;

	/**
	 * The feature id for the '<em><b>Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_TYPES__TYPES = 0;

	/**
	 * The number of structural features of the '<em>My Types</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_TYPES_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>My Types</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MY_TYPES_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link myType.MyType <em>My Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>My Type</em>'.
	 * @see myType.MyType
	 * @generated
	 */
	EClass getMyType();

	/**
	 * Returns the meta object for the attribute '{@link myType.MyType#getTypeID <em>Type ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type ID</em>'.
	 * @see myType.MyType#getTypeID()
	 * @see #getMyType()
	 * @generated
	 */
	EAttribute getMyType_TypeID();

	/**
	 * Returns the meta object for the attribute '{@link myType.MyType#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Name</em>'.
	 * @see myType.MyType#getTypeName()
	 * @see #getMyType()
	 * @generated
	 */
	EAttribute getMyType_TypeName();

	/**
	 * Returns the meta object for class '{@link myType.MyTypes <em>My Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>My Types</em>'.
	 * @see myType.MyTypes
	 * @generated
	 */
	EClass getMyTypes();

	/**
	 * Returns the meta object for the containment reference list '{@link myType.MyTypes#getTypes <em>Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Types</em>'.
	 * @see myType.MyTypes#getTypes()
	 * @see #getMyTypes()
	 * @generated
	 */
	EReference getMyTypes_Types();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	MyTypeFactory getMyTypeFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link myType.impl.MyTypeImpl <em>My Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see myType.impl.MyTypeImpl
		 * @see myType.impl.MyTypePackageImpl#getMyType()
		 * @generated
		 */
		EClass MY_TYPE = eINSTANCE.getMyType();

		/**
		 * The meta object literal for the '<em><b>Type ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MY_TYPE__TYPE_ID = eINSTANCE.getMyType_TypeID();

		/**
		 * The meta object literal for the '<em><b>Type Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MY_TYPE__TYPE_NAME = eINSTANCE.getMyType_TypeName();

		/**
		 * The meta object literal for the '{@link myType.impl.MyTypesImpl <em>My Types</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see myType.impl.MyTypesImpl
		 * @see myType.impl.MyTypePackageImpl#getMyTypes()
		 * @generated
		 */
		EClass MY_TYPES = eINSTANCE.getMyTypes();

		/**
		 * The meta object literal for the '<em><b>Types</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MY_TYPES__TYPES = eINSTANCE.getMyTypes_Types();

	}

} //MyTypePackage
