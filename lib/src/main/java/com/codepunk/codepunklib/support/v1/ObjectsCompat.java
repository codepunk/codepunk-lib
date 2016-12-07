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

package com.codepunk.codepunklib.support.v1;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Helper for accessing features in {@link Objects} introduced after API level 1 in a backwards
 * compatible fashion.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ObjectsCompat {

  /**
   * The implementation instance.
   */
  private static final ObjectsCompatImpl IMPL;

  /**
   * Creates the implementation instance based on the current build version.
   */
  static {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      IMPL = new ObjectsCompatNougat();
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      IMPL = new ObjectsCompatKitKat();
    } else {
      IMPL = new ObjectsCompatBase();
    }
  }

  /**
   * Private constructor.
   */
  private ObjectsCompat() {
  }

  /**
   * Returns <code>true</code> if the arguments are equal to each other and <code>false</code>
   * otherwise. Consequently, if both arguments are <code>null</code>, <code>true</code> is
   * returned and if exactly one argument is <code>null</code>, <code>false</code> is returned.
   * Otherwise, equality is determined by using the {@link Object#equals equals} method of the
   * first argument.
   *
   * @param a An object.
   * @param b An object to be compared with <code>a</code> for equality.
   * @return <code>true</code> if the arguments are equal to each other and <code>false</code>
   * otherwise.
   * @see {@link Object#equals(Object)}
   */
  public static boolean equals(Object a, Object b) {
    return IMPL.equals(a, b);
  }

  /**
   * Returns <code>true</code> if the arguments are deeply equal to each other and
   * <code>false</code> otherwise.
   *
   * Two <code>null</code> values are deeply equal.  If both arguments are * arrays, the algorithm
   * in {@link Arrays#deepEquals(Object[], Object[]) Arrays.deepEquals} is used to determine
   * equality. Otherwise, equality is determined by using the {@link Object#equals equals}
   * method of the first argument.
   *
   * @param a An object.
   * @param b An object to be compared with <code>a</code> for deep equality.
   * @return <code>true</code> if the arguments are deeply equal to each other and
   * <code>false</code> otherwise.
   * @see {@link Arrays#deepEquals(Object[], Object[])}
   * @see {@link Objects#equals(Object, Object)}
   */
  public static boolean deepEquals(Object a, Object b) {
    return IMPL.deepEquals(a, b);
  }

  /**
   * Returns the hash code of a non-<code>null</code> argument and 0 for a <code>null</code>
   * argument.
   *
   * @param o An object.
   * @return The hash code of a non-<code>null</code> argument and 0 for a <code>null</code>
   * argument.
   * @see {@link Object#hashCode}
   */
  public static int hashCode(Object o) {
    return IMPL.hashCode(o);
  }

  /**
   * Generates a hash code for a sequence of input values. The hash
   * code is generated as if all the input values were placed into an
   * array, and that array were hashed by calling {@link
   * Arrays#hashCode(Object[])}.
   *
   * <p>This method is useful for implementing {@link
   * Object#hashCode()} on objects containing multiple fields. For
   * example, if an object that has three fields, <code>x</code>, <code>y</code>, and
   * <code>z</code>, one could write:
   *
   * <blockquote><pre>
   * &#064;Override public int hashCode() {
   *     return Objects.hash(x, y, z);
   * }
   * </pre></blockquote>
   *
   * <b>Warning: When a single object reference is supplied, the returned
   * value does not equal the hash code of that object reference.</b> This
   * value can be computed by calling {@link #hashCode(Object)}.
   *
   * @param values The values to be hashed.
   * @return A hash value of the sequence of input values.
   * @see {@link Arrays#hashCode(Object[])}
   * @see {@link java.util.List#hashCode}
   */
  public static int hash(Object... values) {
    return IMPL.hash(values);
  }

  /**
   * Returns the result of calling <code>toString</code> for a non-<code>null</code> argument and
   * <code>"null"</code> for a <code>null</code> argument.
   *
   * @param o An object.
   * @return The result of calling <code>toString</code> for a non-<code>null</code> argument and
   * <code>"null"</code> for a <code>null</code> argument.
   * @see {@link Object#toString}
   * @see {@link String#valueOf(Object)}
   */
  public static String toString(Object o) {
    return IMPL.toString(o);
  }

  /**
   * Returns the result of calling <code>toString</code> on the first argument if the first
   * argument is not <code>null</code> and returns the second argument otherwise.
   *
   * @param o An object.
   * @param nullDefault String to return if the first argument is <code>null</code>.
   * @return The result of calling <code>toString</code> on the first argument if it is not
   * <code>null</code> and the second argument otherwise.
   * @see {@link Objects#toString(Object)}
   */
  public static String toString(Object o, String nullDefault) {
    return IMPL.toString(o, nullDefault);
  }

  /**
   * Returns 0 if the arguments are identical and <code>c.compare(a, b)</code> otherwise.
   * Consequently, if both arguments are <code>null</code> 0 is returned.
   *
   * <p>Note that if one of the arguments is <code>null</code>, a {@link NullPointerException} may
   * or may not be thrown depending on what ordering policy, if any, the
   * {@link Comparator Comparator} chooses to have for <code>null</code> values.
   *
   * @param <T> The type of the objects being compared.
   * @param a An object.
   * @param b An object to be compared with <code>a</code>.
   * @param c The {@link Comparator} to compare the first two arguments
   * @return 0 if the arguments are identical and <code>c.compare(a, b)</code> otherwise.
   * @see {@link Comparable}
   * @see {@link Comparator}
   */
  public static <T> int compare(T a, T b, Comparator<? super T> c) {
    return IMPL.compare(a, b, c);
  }

  /**
   * Checks that the specified object reference is not <code>null</code>. This method is designed
   * primarily for doing parameter validation in methods and constructors, as demonstrated below:
   * <blockquote><pre>
   * public Foo(Bar bar) {
   *     this.bar = Objects.requireNonNull(bar);
   * }
   * </pre></blockquote>
   *
   * @param <T> The type of the reference.
   * @param obj The object reference to check for nullity.
   * @return <code>obj</code> if not <code>null</code>.
   * @throws NullPointerException if <code>obj</code> is <code>null</code>
   */
  public static <T> T requireNonNull(T obj) {
    return IMPL.requireNonNull(obj);
  }

  /**
   * Checks that the specified object reference is not <code>null</code> and throws a customized
   * {@link NullPointerException} if it is. This method is designed primarily for doing parameter
   * validation in methods and constructors with multiple parameters, as demonstrated below:
   * <blockquote><pre>
   * public Foo(Bar bar, Baz baz) {
   *     this.bar = Objects.requireNonNull(bar, "bar must not be null");
   *     this.baz = Objects.requireNonNull(baz, "baz must not be null");
   * }
   * </pre></blockquote>
   *
   * @param obj The object reference to check for nullity.
   * @param message Detail message to be used in the event that a {@link NullPointerException} is
   *                thrown.
   * @param <T> The type of the reference.
   * @return <code>obj</code> if not <code>null</code>.
   * @throws NullPointerException if <code>obj</code> is <code>null</code>
   */
  public static <T> T requireNonNull(T obj, String message) {
    return IMPL.requireNonNull(obj, message);
  }

  /**
   * Returns <code>true</code> if the provided reference is <code>null</code> otherwise
   * returns <code>false</code>.
   *
   * @apiNote This method exists to be used as a {@link java.util.function.Predicate}, 
   * <code>filter(Objects::nonNull)</code>
   *
   * @param obj A reference to be checked against <code>null</code>.
   * @return <code>true</code> if the provided reference is <code>null</code> otherwise
   * <code>false</code>.
   *
   * @see {@link java.util.function.Predicate}
   * @since 1.8
   */
  public static boolean isNull(Object obj) {
    return IMPL.isNull(obj);
  }

  /**
   * Returns <code>true</code> if the provided reference is non-<code>null</code> otherwise
   * returns <code>false</code>.
   *
   * @apiNote This method exists to be used as a {@link java.util.function.Predicate}, 
   * <code>filter(Objects::nonNull)</code>
   *
   * @param obj A reference to be checked against <code>null</code>.
   * @return <code>true</code> if the provided reference is non-<code>null</code>
   * otherwise <code>false</code>.
   *
   * @see {@link java.util.function.Predicate}
   * @since 1.8
   */
  public static boolean nonNull(Object obj) {
    return IMPL.nonNull(obj);
  }

  /**
   * Checks that the specified object reference is not <code>null</code> and throws a customized
   * {@link NullPointerException} if it is.
   *
   * <p>Unlike the method {@link #requireNonNull(Object, String)}, this method allows creation of
   * the message to be deferred until after the null check is made. While this may confer a
   * performance advantage in the non-null case, when deciding to call this method care should be
   * taken that the costs of creating the message supplier are less than the cost of just creating
   * the string message directly.
   *
   * @param obj The object reference to check for nullity.
   * @param messageSupplier Supplier of the detail message to be used in the event that a 
   * {@code NullPointerException} is thrown.
   * @param <T> The type of the reference.
   * @return <code>obj</code> if not <code>null</code>.
   * @throws NullPointerException if <code>obj</code> is <code>null</code>
   * @since 1.8
   */
  public static <T> T requireNonNull(T obj, Supplier<String> messageSupplier) {
    return IMPL.requireNonNull(obj, messageSupplier);
  }

  interface ObjectsCompatImpl {
    boolean equals(Object a, Object b);
    boolean deepEquals(Object a, Object b);
    int hashCode(Object o);
    int hash(Object... values);
    String toString(Object o);
    String toString(Object o, String nullDefault);
    <T> int compare(T a, T b, Comparator<? super T> c);
    <T> T requireNonNull(T o);
    <T> T requireNonNull(T o, String message);
    boolean isNull(Object obj);
    boolean nonNull(Object obj);
    <T> T requireNonNull(T obj, Supplier<String> messageSupplier);
  }

  private static class ObjectsCompatBase implements ObjectsCompatImpl {
    @Override
    public boolean equals(Object a, Object b) {
      return (a == b) || (a != null && a.equals(b));
    }

    @Override
    public boolean deepEquals(Object a, Object b) {
      if (a == b) {
        return true;
      } else if (a == null || b == null) {
        return false;
      } else if (a instanceof Object[] && b instanceof Object[])
        return Arrays.deepEquals((Object[]) a, (Object[]) b);
      else if (a instanceof byte[] && b instanceof byte[])
        return Arrays.equals((byte[]) a, (byte[]) b);
      else if (a instanceof short[] && b instanceof short[])
        return Arrays.equals((short[]) a, (short[]) b);
      else if (a instanceof int[] && b instanceof int[])
        return Arrays.equals((int[]) a, (int[]) b);
      else if (a instanceof long[] && b instanceof long[])
        return Arrays.equals((long[]) a, (long[]) b);
      else if (a instanceof char[] && b instanceof char[])
        return Arrays.equals((char[]) a, (char[]) b);
      else if (a instanceof float[] && b instanceof float[])
        return Arrays.equals((float[]) a, (float[]) b);
      else if (a instanceof double[] && b instanceof double[])
        return Arrays.equals((double[]) a, (double[]) b);
      else if (a instanceof boolean[] && b instanceof boolean[])
        return Arrays.equals((boolean[]) a, (boolean[]) b);
      else
        return a.equals(b);
    }

    @Override
    public int hashCode(Object o) {
      return o != null ? o.hashCode() : 0;
    }

    @Override
    public int hash(Object... values) {
      return Arrays.hashCode(values);
    }

    @Override
    public String toString(Object o) {
      return String.valueOf(o);
    }

    @Override
    public String toString(Object o, String nullDefault) {
      return (o != null) ? o.toString() : nullDefault;
    }

    @Override
    public <T> int compare(T a, T b, Comparator<? super T> c) {
      return (a == b) ? 0 :  c.compare(a, b);
    }

    @Override
    public <T> T requireNonNull(T o) {
      if (o == null)
        throw new NullPointerException();
      return o;
    }

    @Override
    public <T> T requireNonNull(T o, String message) {
      if (o == null)
        throw new NullPointerException(message);
      return o;
    }

    @Override
    public boolean isNull(Object obj) {
      return obj == null;
    }

    @Override
    public boolean nonNull(Object obj) {
      return obj != null;
    }

    @Override
    public <T> T requireNonNull(T obj, Supplier<String> messageSupplier) {
      // Calling this method requires API 24. It shouldn't compile if minSdkVersion < 24
      // and therefore this statement should be unreachable.
      throw new UnsupportedOperationException();
    }
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  private static class ObjectsCompatKitKat extends ObjectsCompatBase {
    @Override
    public boolean equals(Object a, Object b) {
      return Objects.equals(a, b);
    }

    @Override
    public boolean deepEquals(Object a, Object b) {
      return Objects.deepEquals(a, b);
    }

    @Override
    public int hashCode(Object o) {
      return Objects.hashCode(o);
    }

    @Override
    public int hash(Object... values) {
      return Objects.hash(values);
    }

    @Override
    public String toString(Object o) {
      return Objects.toString(o);
    }

    @Override
    public String toString(Object o, String nullDefault) {
      return Objects.toString(o, nullDefault);
    }

    @Override
    public <T> int compare(T a, T b, Comparator<? super T> c) {
      return Objects.compare(a, b, c);
    }

    @Override
    public <T> T requireNonNull(T o) {
      return Objects.requireNonNull(o);
    }

    @Override
    public <T> T requireNonNull(T o, String message) {
      return Objects.requireNonNull(o, message);
    }
  }

  @TargetApi(Build.VERSION_CODES.N)
  private static class ObjectsCompatNougat extends ObjectsCompatKitKat {
    @Override
    public boolean isNull(Object obj) {
      return Objects.isNull(obj);
    }

    @Override
    public boolean nonNull(Object obj) {
      return Objects.nonNull(obj);
    }

    @Override
    public <T> T requireNonNull(T obj, Supplier<String> messageSupplier) {
      return Objects.requireNonNull(obj, messageSupplier);
    }
  }
}
