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
 * {@link Logger} that wraps another Logger.
 */
@SuppressWarnings("WeakerAccess")
public class LoggerWrapper implements Logger {

  /**
   * The {@link Logger} being wrapped by this LoggerWrapper.
   */
  private Logger mBaseLogger;

  /**
   * Constructor that takes a base {@link Logger} to wrap.
   * @param baseLogger The Logger to wrap.
   */
  public LoggerWrapper(Logger baseLogger) {
    mBaseLogger = baseLogger;
  }

  @Override
  public int getLevel() {
    return mBaseLogger.getLevel();
  }

  @Override
  public void setLevel(int level) {
    mBaseLogger.setLevel(level);
  }

  @Override
  public boolean ignoreUncaughtExceptions() {
    return mBaseLogger.ignoreUncaughtExceptions();
  }

  @Override
  public boolean isLoggable(int level) {
    return mBaseLogger.isLoggable(level);
  }

  @Override
  public int d(String tag, String msg) {
    return mBaseLogger.d(tag, msg);
  }

  @Override
  public int d(String tag, String msg, Throwable tr) {
    return mBaseLogger.d(tag, msg, tr);
  }

  @Override
  public int e(String tag, String msg) {
    return mBaseLogger.e(tag, msg);
  }

  @Override
  public int e(String tag, String msg, Throwable tr) {
    return mBaseLogger.e(tag, msg, tr);
  }

  @Override
  public int i(String tag, String msg) {
    return mBaseLogger.i(tag, msg);
  }

  @Override
  public int i(String tag, String msg, Throwable tr) {
    return mBaseLogger.i(tag, msg, tr);
  }

  @Override
  public int v(String tag, String msg) {
    return mBaseLogger.v(tag, msg);
  }

  @Override
  public int v(String tag, String msg, Throwable tr) {
    return mBaseLogger.v(tag, msg, tr);
  }

  @Override
  public int w(String tag, String msg) {
    return mBaseLogger.w(tag, msg);
  }

  @Override
  public int w(String tag, String msg, Throwable tr) {
    return mBaseLogger.w(tag, msg, tr);
  }

  /**
   * Returns the {@link Logger} being wrapped by this LoggerWrapper.
   * @return The Logger being wrapped.
   */
  public Logger getBaseLogger() {
    return mBaseLogger;
  }
}
