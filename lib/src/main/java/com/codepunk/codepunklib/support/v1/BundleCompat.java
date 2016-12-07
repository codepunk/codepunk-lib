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
import android.os.Bundle;

/**
 * Helper for accessing features in {@link Bundle} introduced after API level 1 in a backwards
 * compatible fashion.
 */
@SuppressWarnings("unused")
public class BundleCompat {

  /**
   * The implementation instance.
   */
  private static final BundleCompatImpl IMPL;

  /**
   * Creates the implementation instance based on the current build version.
   */
  static {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
      IMPL = new BundleCompatHoneycombMr1();
    } else {
      IMPL = new BundleCompatBase();
    }
  }

  /**
   * Private constructor.
   */
  private BundleCompat() {
  }

  /**
   * A convenience method to handle getting the value associated with the given key (or
   * defaultValue if no mapping of the desired type exists for the given key or if a null value is
   * explicitly associated with the given key) for all versions.
   * @param bundle The bundle to get the value.
   * @param key The key to use while getting the value.
   * @param defaultValue Value to return if key does not exist or if a null value is associated
   *                     with the given key.
   * @return The CharSequence value associated with the given key, or defaultValue if no valid
   * CharSequence object is currently mapped to that key.
   */
  public static CharSequence getCharSequence(Bundle bundle, String key, CharSequence defaultValue) {
    return IMPL.getCharSequence(bundle, key, defaultValue);
  }

  /**
   * A convenience method to handle getting the value associated with the given key (or
   * defaultValue if no mapping of the desired type exists for the given key or if a null value is
   * explicitly associated with the given key) for all versions.
   * @param bundle The bundle to get the value.
   * @param key The key to use while getting the value.
   * @param defaultValue Value to return if key does not exist or if a null value is associated
   *                     with the given key.
   * @return The CharSequence value associated with the given key, or defaultValue if no valid
   * CharSequence object is currently mapped to that key.
   */
  public static String getString(Bundle bundle, String key, String defaultValue) {
    return IMPL.getString(bundle, key, defaultValue);
  }

  interface BundleCompatImpl {
    CharSequence getCharSequence(Bundle bundle, String key, CharSequence defaultValue);
    String getString(Bundle bundle, String key, String defaultValue);
  }

  private static class BundleCompatBase implements BundleCompatImpl {
    @Override
    public CharSequence getCharSequence(Bundle bundle, String key, CharSequence defaultValue) {
      final CharSequence cs = bundle.getCharSequence(key);
      return (cs == null) ? defaultValue : cs;
    }

    @Override
    public String getString(Bundle bundle, String key, String defaultValue) {
      final String s = bundle.getString(key);
      return (s == null) ? defaultValue : s;
    }
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
  private static class BundleCompatHoneycombMr1 implements BundleCompatImpl {
    @Override
    public CharSequence getCharSequence(Bundle bundle, String key, CharSequence defaultValue) {
      return bundle.getCharSequence(key, defaultValue);
    }

    @Override
    public String getString(Bundle bundle, String key, String defaultValue) {
      return bundle.getString(key, defaultValue);
    }
  }
}
