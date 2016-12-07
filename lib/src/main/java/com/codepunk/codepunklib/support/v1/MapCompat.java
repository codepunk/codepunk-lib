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

import java.util.Map;

/**
 * Helper for accessing features in {@link Map} introduced after API level 1 in a backwards
 * compatible fashion.
 */
public class MapCompat {

  /**
   * The implementation instance.
   */
  private static final MapCompatImpl IMPL;

  /**
   * Creates the implementation instance based on the current build version.
   */
  static {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      IMPL = new MapCompatImplNougat();
    } else {
      IMPL = new MapCompatImplBase();
    }
  }

  /**
   * Returns the value to which the specified key is mapped, or defaultValue if this map contains
   * no mapping for the key.
   * @param map The map to get the value.
   * @param key The key whose associated value is to be returned.
   * @param defaultValue The default mapping of the key.
   * @return The value to which the specified key is mapped, or defaultValue if this map contains
   * no mapping for the key
   */
  public static <K, V> V getOrDefault(Map<K, V> map, Object key, V defaultValue) {
    return IMPL.getOrDefault(map, key, defaultValue);
  }

  /**
   * Private constructor.
   */
  private MapCompat() {
  }

  interface MapCompatImpl {
    <K, V> V getOrDefault(Map<K, V> map, Object key, V defaultValue);
  }

  private static class MapCompatImplBase implements MapCompatImpl {
    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public <K, V> V getOrDefault(Map<K, V> map, Object key, V defaultValue) {
      return (map.containsKey(key) ? map.get(key) : defaultValue);
    }
  }

  @TargetApi(Build.VERSION_CODES.N)
  private static class MapCompatImplNougat implements MapCompatImpl {
    @SuppressWarnings("Since15")
    @Override
    public <K, V> V getOrDefault(Map<K, V> map, Object key, V defaultValue) {
      return map.getOrDefault(key, defaultValue);
    }
  }
}
