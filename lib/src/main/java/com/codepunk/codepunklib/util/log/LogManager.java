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

import android.os.Process;

import com.codepunk.codepunklib.BuildConfig;
import com.codepunk.codepunklib.support.v1.ObjectsCompat;
import com.codepunk.codepunklib.util.log.FormattingLogger.Placeholder;
import com.codepunk.codepunklib.util.plugin.PluginManager;

import java.lang.Thread.UncaughtExceptionHandler;

import static android.util.Log.ERROR;

/**
 * <p>
 * Plugin manager that handles all logging depending on logging level. The plugin returned by this
 * manager is an instance of {@link FormattingLogger}.
 * </p>
 *
 * <p>
 * To get a {@link FormattingLogger} instance, use the {@link LogManager#get(Object)}:
 * <pre>
 * FormattingLogger logger = LogManager.getInstance().get(Log.DEBUG);
 * if (logger.isLoggable(Log.WARN) {
 *   logger.w("Low memory");
 * }
 * </pre>
 * </p>
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class LogManager extends PluginManager<FormattingLogger, Integer>
    implements UncaughtExceptionHandler {

  /**
   * For singleton creation.
   */
  private static final Object sLock = new Object();

  /**
   * The singleton instance.
   */
  private static LogManager sInstance;

  /**
   * The default uncaught exception handler.
   */
  private final UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

  /**
   * The string format that will be used to format log tags.
   */
  private String mTagFormat;

  /**
   * The arguments (including {@link Placeholder}s) that will be used to format
   * <code>mTagFormat</code> when generating log messages.
   */
  private Object[] mTagArgs;

  /**
   * The string format that will be used to format log messages.
   */
  private String mMsgFormat;

  /**
   * The arguments (including {@link Placeholder}s) that will be used to format
   * <code>mMsgFormat</code> when generating log messages.
   */
  private Object[] mMsgArgs;

  /**
   * Simple constructor that also accounts for logging uncaught exceptions.
   */
  private LogManager() {
    mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    Thread.setDefaultUncaughtExceptionHandler(this);
  }

  /**
   * Gets a LogManager instance.
   * @return A LogManager instance.
   */
  public static LogManager getInstance() {
    synchronized (sLock) {
      if (sInstance == null) {
        sInstance = new LogManager();
      }
      return sInstance;
    }
  }

  @Override
  public FormattingLogger get(Integer level) {
    ObjectsCompat.requireNonNull(level, "level must not be null");
    return super.get(level);
  }

  /**
   * Creates a new {@link FormattingLogger} plugin using the supplied level.
   * @param level The level to use to create the new plugin.
   * @return The new plugin.
   */
  @Override
  protected FormattingLogger newPlugin(Integer level) {
    final FormattingLogger logger = new FormattingLogger(new LogcatLogger(level));
    if (mTagFormat != null) {
      logger.setTagFormat(mTagFormat, mTagArgs);
    }
    if (mMsgFormat != null) {
      logger.setMsgFormat(mMsgFormat, mMsgArgs);
    }
    return logger;
  }

  /**
   * Determines if the current {@link FormattingLogger} plugin instance is dirty and needs to be
   * recreated.
   * @param logger The current {@link FormattingLogger} plugin instance.
   * @param oldLevel The level used to create the current instance.
   * @param level The level to check against the existing level.
   * @return True if the plugin is dirty and needs to be recreated.
   */
  @Override
  protected boolean isPluginDirty(
      FormattingLogger logger, Integer oldLevel, Integer level) {
    return (!ObjectsCompat.equals(oldLevel, level));
  }

  /**
   * Optionally logs uncaught exceptions using the active {@link FormattingLogger}.
   * @param thread The thread on which the exception occurred.
   * @param tr The exception to log.
   */
  @Override
  public void uncaughtException(Thread thread, Throwable tr) {
    FormattingLogger logger = getActivePlugin();
    if (logger != null && logger.isLoggable(ERROR) && !logger.ignoreUncaughtExceptions()) {
      String msg = "FATAL EXCEPTION: " +
          thread.getName() +
          "\nProcess: " + BuildConfig.APPLICATION_ID + ", " +
          "PID: " + Process.myPid();
      logger.getBaseLogger().e("AndroidRuntime", msg, tr);
    }
    mDefaultUncaughtExceptionHandler.uncaughtException(thread, tr);
  }

  /**
   * Sets the tag format and arguments for formatted log statements.
   * @param format The format string to use for log tags.
   * @param args The format arguments to use for log tags.
   */
  public void setTagFormat(String format, Object... args) {
    mTagFormat = format;
    mTagArgs = args;
    FormattingLogger logger = getActivePlugin();
    if (logger != null) {
      logger.setTagFormat(format, args);
    }
  }

  /**
   * Sets the message format and arguments for formatted log statements.
   * @param format The format string to use for log messages.
   * @param args The format arguments to use for log messages.
   */
  public void setMsgFormat(String format, Object... args) {
    mMsgFormat = format;
    mMsgArgs = args;
    FormattingLogger logger = getActivePlugin();
    if (logger != null) {
      logger.setMsgFormat(format, args);
    }
  }
}
