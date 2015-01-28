package edu.kit.ipd.sdq.mdsd.profiles.util.bridges;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * A utility class hiding details of the Ecore metamodel part of the Eclipse Modeling Framework API for recurring tasks that are
 * not project-specific.<br/>
 * <br/>
 * (Note that it is disputable whether this class conforms to the bridge pattern as we are currently only providing one
 * implementation and the "abstractions" can be regarded as low-level.)
 *
 * @author Max E. Kramer
 */
public final class EcoreBridge {
   /** Utility classes should not have a public or default constructor. */
   private EcoreBridge() {
   }

   /**
    * Returns whether the given feature is an attribute of type String.
    *
    * @param feature
    *           a feature
    * @return {@code true} if feature is a String attribute and {@code false} otherwise
    */
   public static boolean isStringAttribute(final EStructuralFeature feature) {
      return isAttributeOfJavaType(feature, "String");
   }

   /**
    * Returns whether the given attribute is of type String.
    *
    * @param attribute
    *           an attribute
    * @return {@code true} if attribute is of type String and {@code false} otherwise
    */
   public static boolean isStringAttribute(final EAttribute attribute) {
      return isAttributeOfJavaType(attribute, "String");
   }

   /**
    * Returns whether the given feature is an attribute that has a java type of the given name (e.g. "String" for
    * "java.lang.String").
    *
    * @param feature
    *           a feature
    * @param typeName
    *           a java.lang type name
    * @return {@code true} if attribute is of type boolean and {@code false} otherwise
    */
   public static boolean isAttributeOfJavaType(final EStructuralFeature feature, final String typeName) {
      if (feature instanceof EAttribute) {
         return isAttributeOfJavaType((EAttribute) feature, typeName);
      }
      return false;
   }

   /**
    * Returns whether the given attribute has a java type of the given name (e.g. "String" for "java.lang.String").
    *
    * @param attribute
    *           an attribute
    * @param typeName
    *           a java.lang type name
    * @return {@code true} if attribute is of type boolean and {@code false} otherwise
    */
   public static boolean isAttributeOfJavaType(final EAttribute attribute, final String typeName) {
      if (attribute != null) {
         EClassifier attributeType = attribute.getEType();
         if (attributeType != null) {
            return ("java.lang." + typeName).equals(attributeType.getInstanceClassName());
         }
      }
      return false;
   }
}
