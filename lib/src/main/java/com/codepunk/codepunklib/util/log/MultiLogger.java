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

package com.codepunk.codepunklib.util.log;

import java.util.HashSet;

import static android.util.Log.ASSERT;
import static android.util.Log.DEBUG;
import static android.util.Log.ERROR;
import static android.util.Log.INFO;
import static android.util.Log.VERBOSE;
import static android.util.Log.WARN;

/**
 * A {@link Logger} that maintains a set of Loggers and performs operations across the set.
 */
public class MultiLogger extends HashSet<Logger>
    implements Logger {

  /**
   * Returns the minimum logging level of all {@link Logger}s in the set.
   * @return The minimum logging level.
   */
  @Override
  public int getLevel() {
    int minLevel = ASSERT;
    for (Logger logger : this) {
      int level = logger.getLevel();
      if (level < minLevel) {
        minLevel = level;
      }
    }
    return minLevel;
  }

  /**
   * Sets the logging level of all {@link Logger}s in the set.
   * @param level The new logging level.
   */
  @Override
  public void setLevel(int level) {
    for (Logger logger : this) {
      logger.setLevel(level);
    }
  }

  /**
   * Returns whether all {@link Logger}s in the set will ignore uncaught exceptions.
   * @return <code>true</code> if all {@link Logger}s in the set will ignore uncaught exceptions,
   * and <code>false</code> otherwise.
   */
  @Override
  public boolean ignoreUncaughtExceptions() {
    for (Logger logger : this) {
      if (!logger.ignoreUncaughtExceptions()) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns whether any {@link Logger}s in the set are loggable at the given level.
   * @param level The level to check.
   * @return Whether any Loggers in the set are loggable at the given level.
   */
  @Override
  public boolean isLoggable(int level) {
    return level >= getLevel();
  }

  /**
   * Sends a {@link android.util.Log#DEBUG} message to all {@link Logger}s in the set.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  @Override
  public int d(String tag, String msg) {
    int written = 0;
    for (Logger logger : this) {
      if (logger.isLoggable(DEBUG)) {
        written += logger.d(tag, msg);
      }
    }
    return written;
  }

  /**
   * Sends a {@link android.util.Log#DEBUG} message to all {@link Logger}s in the set.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  @Override
  public int d(String tag, String msg, Throwable tr) {
    int written = 0;
    for (Logger logger : this) {
      if (logger.isLoggable(DEBUG)) {
        written += logger.d(tag, msg, tr);
      }
    }
    return written;
  }

  /**
   * Sends an {@link android.util.Log#ERROR} message to all {@link Logger}s in the set.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  @Override
  public int e(String tag, String msg) {
    int written = 0;
    for (Logger logger : this) {
      if (logger.isLoggable(ERROR)) {
        written += logger.e(tag, msg);
      }
    }
    return written;
  }

  /**
   * Sends an {@link android.util.Log#ERROR} message to all {@link Logger}s in the set.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  @Override
  public int e(String tag, String msg, Throwable tr) {
    int written = 0;
    for (Logger logger : this) {
      if (logger.isLoggable(ERROR)) {
        written += logger.e(tag, msg, tr);
      }
    }
    return written;
  }

  /**
   * Sends an {@link android.util.Log#INFO} message to all {@link Logger}s in the set.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  @Override
  public int i(String tag, String msg) {
    int written = 0;
    for (Logger logger : this) {
      if (logger.isLoggable(INFO)) {
        written += logger.i(tag, msg);
      }
    }
    return written;
  }

  /**
   * Sends an {@link android.util.Log#INFO} message to all {@link Logger}s in the set.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  @Override
  public int i(String tag, String msg, Throwable tr) {
    int written = 0;
    for (Logger logger : this) {
      if (logger.isLoggable(INFO)) {
        written += logger.i(tag, msg, tr);
      }
    }
    return written;
  }

  /**
   * Sends a {@link android.util.Log#VERBOSE} message to all {@link Logger}s in the set.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  @Override
  public int v(String tag, String msg) {
    int written = 0;
    for (Logger logger : this) {
      if (logger.isLoggable(VERBOSE)) {
        written += logger.v(tag, msg);
      }
    }
    return written;
  }

  /**
   * Sends a {@link android.util.Log#VERBOSE} message to all {@link Logger}s in the set.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  @Override
  public int v(String tag, String msg, Throwable tr) {
    int written = 0;
    for (Logger logger : this) {
      if (logger.isLoggable(VERBOSE)) {
        written += logger.v(tag, msg, tr);
      }
    }
    return written;
  }

  /**
   * Sends a {@link android.util.Log#WARN} message to all {@link Logger}s in the set.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  @Override
  public int w(String tag, String msg) {
    int written = 0;
    for (Logger logger : this) {
      if (logger.isLoggable(WARN)) {
        written += logger.w(tag, msg);
      }
    }
    return written;
  }

  /**
   * Sends a {@link android.util.Log#WARN} message to all {@link Logger}s in the set.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  @Override
  public int w(String tag, String msg, Throwable tr) {
    int written = 0;
    for (Logger logger : this) {
      if (logger.isLoggable(WARN)) {
        written += logger.w(tag, msg, tr);
      }
    }
    return written;
  }
}
