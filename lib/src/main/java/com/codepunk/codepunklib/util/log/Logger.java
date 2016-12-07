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

/**
 * Interface for logging operations.
 */
@SuppressWarnings({"WeakerAccess"})
public interface Logger {

  /**
   * Returns the logging level for this Logger.
   * @return The Logging level.
   */
  int getLevel();

  /**
   * Sets the logging level for this Logger.
   * @param level The new logging level.
   */
  void setLevel(int level);

  /**
   * Returns whether this Logger will process or ignore uncaught exceptions.
   * @return <code>true</code> if this Logger ignores uncaught exceptions and <code>false</code>
   * if it processes them.
   */
  boolean ignoreUncaughtExceptions();

  /**
   * Checks to see whether or not a log is loggable at the specified level.
   * @param level The level to check.
   * @return Whether or not this is allowed to be logged.
   */
  boolean isLoggable(int level);

  /**
   * Send a {@link android.util.Log#DEBUG} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  int d(String tag, String msg);

  /**
   * Send a {@link android.util.Log#DEBUG} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  int d(String tag, String msg, Throwable tr);

  /**
   * Send an {@link android.util.Log#ERROR} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  int e(String tag, String msg);

  /**
   * Send an {@link android.util.Log#ERROR} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  int e(String tag, String msg, Throwable tr);

  /**
   * Send an {@link android.util.Log#INFO} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  int i(String tag, String msg);

  /**
   * Send an {@link android.util.Log#INFO} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  int i(String tag, String msg, Throwable tr);

  /**
   * Send a {@link android.util.Log#VERBOSE} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  int v(String tag, String msg);

  /**
   * Send a {@link android.util.Log#VERBOSE} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  int v(String tag, String msg, Throwable tr);

  /**
   * Send a {@link android.util.Log#WARN} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  int w(String tag, String msg);

  /**
   * Send a {@link android.util.Log#WARN} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  int w(String tag, String msg, Throwable tr);
}
