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

import java.util.Collection;
import java.util.Map;

/**
 * Utility class that provides useful {@link Collection}- and {@link Map}-related methods.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class CollectionUtils {

  /**
   * Returns <code>true</code> if the given {@link Collection} is null or contains no elements.
   * @param c A collection.
   * @return <code>true</code> if the given collection is null or contains no elements.
   */
  public static boolean isEmpty(Collection c) {
    return (c == null || c.isEmpty());
  }

  /**
   * Returns <code>true</code> if the given {@link Map} is null or contains no elements.
   * @param m A map.
   * @return <code>true</code> if the given map is null or contains no elements.
   */
  public static boolean isEmpty(Map m) {
    return (m == null || m.isEmpty());
  }

  /**
   * Private constructor.
   */
  private CollectionUtils() {
  }
}
