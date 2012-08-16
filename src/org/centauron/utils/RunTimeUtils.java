package org.centauron.utils;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;

/**
 * Utilities to instantiate classes and access their runtime information.
 *
 * @author <a href="mailto:robert.reiner@smartics.de">Robert Reiner</a>
 * @version $Revision:591 $
 */
public final class RunTimeUtils
{
  private RunTimeUtils()
  {
  }
  public static Object loadInstance(final ClassLoader classLoader,
      final FieldDoc fieldDoc) throws ClassNotFoundException,
    NullPointerException
  {
    final ClassDoc classDoc = fieldDoc.containingClass();
    final String className = classDoc.qualifiedName();
    final Class<?> clazz = Class.forName(className, true, classLoader);
    final String fieldName = fieldDoc.name();
    if (classDoc.isEnum())
    {
      return loadEnumInstance(clazz, fieldName);
    }
    else
    {
      return loadStaticFieldInstance(clazz, fieldName);
    }
  }

  /**
   * Loads the class referenced by the given <code>classDoc</code>.
   *
   * @param classLoader the class loader to load the referenced class.
   * @return the class instance.
   * @throws ClassNotFoundException if the class of the field or any depending
   *           classes cannot be loaded.
   * @throws NullPointerException if <code>classLoader</code> or
   *           <code>classDoc</code> is <code>null</code>.
   */
  public static Class<?> loadClass(final ClassLoader classLoader,
      final ClassDoc classDoc) throws ClassNotFoundException,
    NullPointerException
  {
    final String className = classDoc.qualifiedName();
    final Class<?> clazz = Class.forName(className, true, classLoader);
    return clazz;
  }

  /**
   * Loads the enumeration element instance with the given identifier from the
   * given class.
   *
   * @param clazz the enumeration class that contains the requested enumeration
   *          element.
   * @param identifier the identifier of the enumeration element to return.
   * @return the enumeration element instance or <code>null</code> if the class
   *         is not an enumeration or the enumeration does not contain an
   *         element with a name matching the given identifier.
   * @throws NullPointerException if <code>clazz</code> or
   *           <code>identifier</code> is <code>null</code>.
   */
  public static Enum<?> loadEnumInstance(final Class<?> clazz,
      final String identifier) throws NullPointerException
  {
    final Enum<?>[] elements = (Enum[]) clazz.getEnumConstants();
    if (elements != null)
    {
      for (Enum<?> element : elements)
      {
        final String elementName = ((Enum<?>) element).name();
        if (identifier.equals(elementName))
        {
          return element;
        }
      }
    }

    return null;
  }

  /**
   * Loads the static property instance with the given identifier from the given
   * class.
   *
   * @param clazz the class that contains the requested static property.
   * @param identifier the identifier of the static property to return.
   * @return the static property instance or <code>null</code> if the class does
   *         not contain a static property with a name matching the given
   *         identifier.
   * @throws NullPointerException if <code>clazz</code> or
   *           <code>identifier</code> is <code>null</code>.
   */
  public static Object loadStaticFieldInstance(final Class<?> clazz,
      final String identifier) throws NullPointerException
  {
    try
    {
      final Field field = clazz.getDeclaredField(identifier);
      field.setAccessible(true);
      final int modifiers = field.getModifiers();
      if (Modifier.isStatic(modifiers))
      {
        return field.get(null);
      }
    }
    catch (final Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

}