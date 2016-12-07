/*
 * Copyright 2016 Codepunk, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codepunk.codepunklib.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Utility class that provides useful {@link Enum}-related methods.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class EnumUtils {

  /**
   * Convenience method that allows a default value to be passed to
   * {@link Enum#valueOf(Class, String)} for <code>name</code> values that do not match any
   * enum constant.
   * @param enumType The Class object of the enum type from which to return a constant.
   * @param name The name of the constant to return.
   * @param defaultValue A default enum constant to return if <code>name</code> doesn't match any
   *                     enum constant.
   * @param <E> The Enum type.
   * @return The enum constant of the specified enum type with the specified name, or
   * <code>defaultValue</code> if no matching enum constant was found.
   */
  public static <E extends Enum<E>> E valueOf(Class<E> enumType, String name, E defaultValue) {
    if(name == null) {
      return null;
    } else {
      try {
        return Enum.valueOf(enumType, name);
      } catch (IllegalArgumentException e) {
        return defaultValue;
      }
    }
  }

  /**
   * Convenience method that calls {@link Enum#valueOf(Class, String)} but returns
   * <code>null</code> if no match was found rather than throwing an
   * {@link IllegalArgumentException}.
   * @param enumType The Class object of the enum type from which to return a constant.
   * @param name The name of the constant to return.
   * @param <E> The Enum type.
   * @return The enum constant of the specified enum type with the specified name, or
   * <code>null</code> if no matching enum constant was found.
   */
  public static <E extends Enum<E>> E valueOf(Class<E> enumType, String name) {
    return valueOf(enumType, name, null);
  }

  /**
   * Convenience method that builds a lookup map of keys to Enum values. Can be used when coding
   * a <code>valueOf</code> or <code>fromFoo</code> static method in a custom Enum class.
   * @param enumClass The Enum class.
   * @param <E> The Enum type. Must also implement {@link LookupKeyProvider}.
   * @param <T> The type of key used in lookup operations.
   * @return A lookup map of keys to Enum values.
   * <p>Use this method to build a lookup map of key values to enum values as follows:
   * <pre>
   * public enum MyEnum implements EnumUtils.LookupKeyProvider&lt;String&gt; {
   *   VALUE1("key1"),
   *   VALUE2("key2"),
   *   VALUE3("key3"),
   *   VALUE4("key4");
   *
   *   private static Map<String, MyEnum> sLookupMap;
   *
   *   private String mKey;
   *
   *   MyEnum(String key) {
   *     mKey = key;
   *   }
   *
   *   public static MyEnum fromKey(String key) {
   *     if (sLookupMap == null) {
   *       sLookupMap = EnumUtils.buildLookupMap(MyEnum.class);
   *     }
   *     return sLookupMap.get(key);
   *   }
   *
   *   &#064;Override
   *   public String getLookupKey() {
   *     return mKey;
   *   }
   * }
   * </pre>
   * </p>
   */
  public static <E extends Enum<E> & LookupKeyProvider<T>, T> Map<T, E> buildLookupMap(
      Class<E> enumClass) {
    E[] values = enumClass.getEnumConstants();
    final Map<T, E> map = new LinkedHashMap<>(values.length);
    for (final E value : values) {
      map.put(value.getLookupKey(), value);
    }
    return map;
  }

  /**
   * Private constructor.
   */
  private EnumUtils() {
  }

  /**
   * Interface for Enum classes that provide a lookup key for each value.
   * @param <T> The type of key used in lookup operations.
   */
  public interface LookupKeyProvider<T> {
    T getLookupKey();
  }
}
