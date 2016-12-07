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

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Locale;

/**
 * <p>
 * A special {@link Logger} that automatically formats log messages with pertinent information
 * before sending.
 * </p>
 *
 * <p>
 * The {@link Placeholder} class is used to specify tag and message formats that will be used when
 * formatting log messages. Each placeholder takes the place of a piece of information (i.e. class
 * name, method name, line number, etc.) that are only known at runtime.
 * </p>
 *
 * <p>
 * To use FormattingLogger:
 * </p>
 *
 * <p>
 * (Optional) Set the tag format string and arguments. For example, to have a tag that always
 * displays the fully-qualified class name where the log message originated, use the following:
 * </p>
 * <pre>
 * FormattingLogger logger = new FormattingLogger();
 * logger.setTagFormat("%s", Placeholder.CLASS_NAME);
 * </pre>
 *
 * <p>
 * If you want the tag to display a friendly message along with the name of the (simple) class
 * name and method in which the log message orginated, use the following:
 * </p>
 * <pre>
 * logger.setTagFormat("Hi! %s.%s", Placeholder.SIMPLE_CLASS_NAME, Placeholder.METHOD_NAME);
 * </pre>
 *
 * <p>
 * If you want the tag to display the filename in which the log message originated along with a
 * custom message specified at the time the message is logged, use the following:
 * </p>
 * <pre>
 * logger.setTagFormat("%s: %s", Placeholder.FILE_NAME, Placeholder.SUPPLIED);
 * </pre>
 *
 * <p>
 * Then you can specify a custom tag string that will take the placed of the <code>SUPPLIED</code>
 * placeholder:
 * </p>
 * <pre>
 * logger.d("Custom Tag", "My message.");
 * </pre>
 *
 * <p>
 * (Optional) Set the message format string and arguments. The same concepts used in setting the
 * tag format string and arguments apply here as well.
 * </p>
 *
 * <p>
 * When sending log messages, if you have set up a tag that expects no
 * <code>Placeholder.SUPPLIED</code> strings, then you can leave out the tag argument altogether:
 * </p>
 * <pre>
 * logger.d("My message.");
 * </pre>
 *
 * <p>
 * You can also pass a {@link Throwable}:
 * </p>
 * <pre>
 * logger.e("Something went wrong.", new IllegalArgumentException());
 * </pre>
 *
 * <p>
 * Note that you <b>must</b> include <code>Placeholder.SUPPLIED</code> in your tag or message
 * formats as described above in order for any tag or message string you pass along at runtime
 * to appear in the formatted log message.
 * </p>
 *
 * <p>
 * For example, if you call the following:
 * </p>
 * <pre>
 * logger.setTagFormat("%s", Placeholder.SIMPLE_CLASS_NAME);
 * logger.setMsgFormat("%s", Placeholder.FILE_NAME);
 * logger.d("My Tag", "My message.");
 * </pre>
 *
 * <p>
 * Then neither "My Tag" nor "My message" will appear in the formatted log message. You can
 * address this by including the <code>Placeholder.SUPPLIED</code> somewhere in your formats:
 * </p>
 * <pre>
 * logger.setTagFormat("%s: %s", Placeholder.SIMPLE_CLASS_NAME, Placeholder.SUPPLIED);
 * logger.setMsgFormat("%s: %s", Placeholder.FILE_NAME, Placeholder.SUPPLIED);
 * logger.d("My Tag", "My message.");
 * </pre>
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class FormattingLogger extends LoggerWrapper {

  /**
   * Placeholders that will be used to generate formatted logging messages.
   */
  public enum Placeholder {
    /**
     * An enum constant that serves as a placeholder for the full class name in a log message.
     */
    CLASS_NAME,

    /**
     * An enum constant that serves as a placeholder for the file name in a log message.
     */
    FILE_NAME,

    /**
     * An enum constant that serves as a placeholder for the hash code in a log message.
     */
    HASH_CODE,

    /**
     * An enum constant that serves as a placeholder for the line number in a log message.
     */
    LINE_NUMBER,

    /**
     * An enum constant that serves as a placeholder for the method name in a log message.
     */
    METHOD_NAME,

    /**
     * An enum constant that serves as a placeholder for the package name in a log message.
     */
    PACKAGE,

    /**
     * An enum constant that serves as a placeholder for the simple class name in a log message.
     */
    SIMPLE_CLASS_NAME,

    /**
     * <p>
     * An enum constant that serves as a placeholder for the message that is passed along to the
     * log message.
     * </p>
     *
     * <p>
     * In the following example, any placeholders of <code>Placeholder.SUPPLIED</code> specified
     * in {@link FormattingLogger#setTagFormat(String, Object[])} or
     * {@link FormattingLogger#setMsgFormat(String, Object[])} will be replaced with the string
     * "This is the supplied message".
     * </p>
     * <pre>
     * FormattingLogger logger = ...;
     * logger.d("This is the supplied message");
     * </pre>
     */
    SUPPLIED
  }

  /**
   * The default log tag format.
   */
  private static final String DEFAULT_TAG_FORMAT = "%s";

  /**
   * The default log tag arguments.
   */
  private static final Object[] DEFAULT_TAG_ARGS = new Object[] {
      Placeholder.SUPPLIED };

  /**
   * The default log message format.
   */
  private static final String DEFAULT_MSG_FORMAT = "%s(%s:%d) %s";

  /**
   * The default log message arguments.
   */
  private static final Object[] DEFAULT_MSG_ARGS = new Object[] {
      Placeholder.METHOD_NAME,
      Placeholder.FILE_NAME,
      Placeholder.LINE_NUMBER,
      Placeholder.SUPPLIED };

  /**
   * Empty string for calling log methods without specifying a tag value.
   */
  private static final String EMPTY_STRING = "";

  /**
   * Used to truncate tag strings that are too long.
   */
  private static final String ELLIPSIS = "…";

  /**
   * Character that indicates an anonymous class in full class names.
   */
  private static final String ANONYMOUS_CLASS_INDICATOR = "$";

  /**
   * Anonymous class indicator in regex form.
   */
  private static final String ANONYMOUS_CLASS_INDICATOR_REGEX = "\\$";

  /**
   * Default result for class names that cannot be determined at runtime.
   */
  private static final String UNKNOWN_CLASS_NAME = "[Unknown]";

  /**
   * The maximum tag length.
   */
  private static final int MAX_TAG_LENGTH = 23;

  /**
   * The string format that will be used to format log tags.
   */
  private String mTagFormat = DEFAULT_TAG_FORMAT;

  /**
   * The arguments (including {@link Placeholder}s) that will be used to format
   * <code>mTagFormat</code> when generating log messages.
   */
  private Object[] mTagArgs = DEFAULT_TAG_ARGS;

  /**
   * The string format that will be used to format log messages.
   */
  private String mMsgFormat = DEFAULT_MSG_FORMAT;

  /**
   * The arguments (including {@link Placeholder}s) that will be used to format
   * <code>mMsgFormat</code> when generating log messages.
   */
  private Object[] mMsgArgs = DEFAULT_MSG_ARGS;

  /**
   * Constructor that takes a base {@link Logger} to which log statements will be sent after
   * formatting.
   * @param baseLogger The base {@link Logger} to which formatted log statements will be sent.
   */
  public FormattingLogger(Logger baseLogger) {
    super(baseLogger);
  }

  /**
   * Returns the {@link Class} that corresponds to the given {@link StackTraceElement}.
   * @param element A {@link StackTraceElement}.
   * @return The Class object for the class with the name specified by <code>element</code>.
   * @throws ClassNotFoundException If the class cannot be located.
   */
  private static Class getClass(@NonNull StackTraceElement element)
      throws ClassNotFoundException {
    String className = element.getClassName();
    if (className.contains(ANONYMOUS_CLASS_INDICATOR)) {
      className = className.split(ANONYMOUS_CLASS_INDICATOR_REGEX)[0];
    }
    return Class.forName(className);
  }

  /**
   * Returns the first {@link StackTraceElement} in the stack trace found in <code>tr</code>
   * that was called from outside of this class.
   * @param tr The {@link Throwable} containing the stack trace.
   * @param index The number of levels in the stack trace that result from methods in this class.
   * @return The first significant {@link StackTraceElement}; that is, the first element
   * representing a call made outside of this class.
   */
  private static StackTraceElement getSignificantStackTraceElement(Throwable tr, int index) {
    StackTraceElement element;
    try {
      if (tr == null) {
        element = (new Throwable()).getStackTrace()[index + 1];
      } else if (tr.getCause() == null) {
        element = tr.getStackTrace()[0];
      } else {
        element = tr.getCause().getStackTrace()[0];
      }
    } catch (Exception e) {
      element = null;
    }
    return element;
  }

  /**
   * Returns the concrete information from the given {@link StackTraceElement} that is represented
   * by the given Placeholder.
   * @param element The {@link StackTraceElement}.
   * @param placeholder The {@link Placeholder} to replace.
   * @return The information represented by the Placeholder.
   */
  private static Object getFromElement(StackTraceElement element, Placeholder placeholder) {
    try {
      switch (placeholder) {
        case FILE_NAME:
          return element.getFileName();
        case HASH_CODE:
          return element.hashCode();
        case LINE_NUMBER:
          return element.getLineNumber();
        case METHOD_NAME:
          return element.getMethodName();
        case PACKAGE: {
          return getClass(element).getPackage().getName();
        }
        case CLASS_NAME:
        case SIMPLE_CLASS_NAME:
        default: {
          String name;
          Class cls = getClass(element);
          do {
            if (cls == null) {
              name = UNKNOWN_CLASS_NAME;
            } else if (placeholder == Placeholder.CLASS_NAME) {
              name = cls.getName();
            } else {
              name = cls.getSimpleName();
            }
          } while (TextUtils.isEmpty(name));
          return name;
        }
      }
    } catch (ClassNotFoundException e) {
      return UNKNOWN_CLASS_NAME;
    }
  }

  /**
   * Formats a format string with the supplied arguments, replacing {@link Placeholder}s with
   * concrete information found in <code>element</code> as well as <code>supplied</code>.
   * @param format The format string.
   * @param args Arguments referenced by the format specifiers in the format string. Can be
   *             any object type as well as {@link Placeholder}, which will result in the arg
   *             being replaced with concrete information before being placed in the format string.
   * @param element A {@link StackTraceElement}.
   * @param supplied The text that was originally supplied to this log call.
   * @param maxLength An optional maximum string length. This is needed because Logcat log
   *                  statements have a maximum tag length of 23.
   * @return A formatted string.
   * @see String#format(Locale, String, Object...)
   */
  private static String format(
      String format,
      Object[] args,
      StackTraceElement element,
      String supplied,
      int maxLength) {
    String formattedString;
    if (ArrayUtils.isEmpty(args) || element == null) {
      formattedString = format;
    } else {
      Object[] resolvedArgs = new Object[args.length];
      for (int i = 0; i < args.length; i++) {
        if (!(args[i] instanceof Placeholder)) {
          resolvedArgs[i] = args[i];
        } else if (args[i] == Placeholder.SUPPLIED) {
          resolvedArgs[i] = supplied;
        } else {
          resolvedArgs[i] = getFromElement(element, (Placeholder) args[i]);
        }
      }
      formattedString = String.format(Locale.US, format, resolvedArgs);
    }
    if (formattedString != null && formattedString.length() > maxLength) {
      formattedString = formattedString.substring(0, maxLength - 1).concat(ELLIPSIS);
    }
    return formattedString;
  }

  /**
   * Formats a format string with the supplied arguments, replacing {@link Placeholder}s with
   * concrete information found in <code>element</code> as well as <code>supplied</code>.
   * @param format The format string.
   * @param args Arguments referenced by the format specifiers in the format string. Can be
   *             any object type as well as {@link Placeholder}, which will result in the arg
   *             being replaced with concrete information before being placed in the format string.
   * @param element A {@link StackTraceElement}.
   * @param supplied The text that was originally supplied to this log call.
   * @return A formatted string.
   * @see String#format(Locale, String, Object...)
   */
  private static String format(
      String format,
      Object[] args,
      StackTraceElement element,
      String supplied) {
    return format(format, args, element, supplied, Integer.MAX_VALUE);
  }

  /**
   * Sets the tag format and arguments for formatted log statements.
   * @param format The format string to use for log tags.
   * @param args The format arguments to use for log tags.
   */
  public void setTagFormat(String format, Object... args) {
    mTagFormat = format;
    mTagArgs = args;
  }

  /**
   * Sets the message format and arguments for formatted log statements.
   * @param format The format string to use for log messages.
   * @param args The format arguments to use for log messages.
   */
  public void setMsgFormat(String format, Object... args) {
    mMsgFormat = format;
    mMsgArgs = args;
  }

  @Override
  public int d(String tag, String msg) {
    return d(tag, msg, null, 1);
  }

  @Override
  public int d(String tag, String msg, Throwable tr) {
    return d(tag, msg, tr, 1);
  }

  @Override
  public int e(String tag, String msg) {
    return e(tag, msg, null, 1);
  }

  @Override
  public int e(String tag, String msg, Throwable tr) {
    return e(tag, msg, tr, 1);
  }

  @Override
  public int i(String tag, String msg) {
    return i(tag, msg, null, 1);
  }

  @Override
  public int i(String tag, String msg, Throwable tr) {
    return i(tag, msg, tr, 1);
  }

  @Override
  public int v(String tag, String msg) {
    return v(tag, msg, null, 1);
  }

  @Override
  public int v(String tag, String msg, Throwable tr) {
    return v(tag, msg, tr, 1);
  }

  @Override
  public int w(String tag, String msg) {
    return w(tag, msg, null, 1);
  }

  @Override
  public int w(String tag, String msg, Throwable tr) {
    return w(tag, msg, tr, 1);
  }

  /**
   * Send formatted a {@link android.util.Log#DEBUG} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @param index The number of calls in the stack trace (before this call) that originated within
   *              this class.
   * @return The number of bytes written.
   */
  private int d(String tag, String msg, Throwable tr, int index) {
    StackTraceElement element = getSignificantStackTraceElement(tr, index + 1);
    return super.d(
        format(mTagFormat, mTagArgs, element, tag, MAX_TAG_LENGTH),
        format(mMsgFormat, mMsgArgs, element, msg),
        tr);
  }

  /**
   * Send formatted a {@link android.util.Log#ERROR} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @param index The number of calls in the stack trace (before this call) that originated within
   *              this class.
   * @return The number of bytes written.
   */
  private int e(String tag, String msg, Throwable tr, int index) {
    StackTraceElement element = getSignificantStackTraceElement(tr, index + 1);
    return super.e(
        format(mTagFormat, mTagArgs, element, tag, MAX_TAG_LENGTH),
        format(mMsgFormat, mMsgArgs, element, msg),
        tr);
  }

  /**
   * Send formatted a {@link android.util.Log#INFO} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @param index The number of calls in the stack trace (before this call) that originated within
   *              this class.
   * @return The number of bytes written.
   */
  private int i(String tag, String msg, Throwable tr, int index) {
    StackTraceElement element = getSignificantStackTraceElement(tr, index + 1);
    return super.i(
        format(mTagFormat, mTagArgs, element, tag, MAX_TAG_LENGTH),
        format(mMsgFormat, mMsgArgs, element, msg),
        tr);
  }

  /**
   * Send formatted a {@link android.util.Log#VERBOSE} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @param index The number of calls in the stack trace (before this call) that originated within
   *              this class.
   * @return The number of bytes written.
   */
  private int v(String tag, String msg, Throwable tr, int index) {
    StackTraceElement element = getSignificantStackTraceElement(tr, index + 1);
    return super.v(
        format(mTagFormat, mTagArgs, element, tag, MAX_TAG_LENGTH),
        format(mMsgFormat, mMsgArgs, element, msg),
        tr);
  }

  /**
   * Send formatted a {@link android.util.Log#WARN} log message.
   * @param tag Used to identify the source of a log message. It usually identifies the class or
   *            activity where the log call occurs.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @param index The number of calls in the stack trace (before this call) that originated within
   *              this class.
   * @return The number of bytes written.
   */
  private int w(String tag, String msg, Throwable tr, int index) {
    StackTraceElement element = getSignificantStackTraceElement(tr, index + 1);
    return super.w(
        format(mTagFormat, mTagArgs, element, tag, MAX_TAG_LENGTH),
        format(mMsgFormat, mMsgArgs, element, msg),
        tr);
  }

  /**
   * Send a formatted {@link android.util.Log#DEBUG} log message.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  public int d(String msg) {
    return d(EMPTY_STRING, msg, null, 1);
  }

  /**
   * Send formatted a {@link android.util.Log#DEBUG} log message.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  public int d(String msg, Throwable tr) {
    return d(EMPTY_STRING, msg, tr, 1);
  }

  /**
   * Send a formatted {@link android.util.Log#ERROR} log message.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  public int e(String msg) {
    return e(EMPTY_STRING, msg, null, 1);
  }

  /**
   * Send formatted a {@link android.util.Log#ERROR} log message.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  public int e(String msg, Throwable tr) {
    return e(EMPTY_STRING, msg, tr, 1);
  }

  /**
   * Send a formatted {@link android.util.Log#INFO} log message.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  public int i(String msg) {
    return i(EMPTY_STRING, msg, null, 1);
  }

  /**
   * Send formatted a {@link android.util.Log#INFO} log message.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  public int i(String msg, Throwable tr) {
    return i(EMPTY_STRING, msg, tr, 1);
  }

  /**
   * Send a formatted {@link android.util.Log#VERBOSE} log message.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  public int v(String msg) {
    return v(EMPTY_STRING, msg, null, 1);
  }

  /**
   * Send formatted a {@link android.util.Log#VERBOSE} log message.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  public int v(String msg, Throwable tr) {
    return v(EMPTY_STRING, msg, tr, 1);
  }

  /**
   * Send a formatted {@link android.util.Log#WARN} log message.
   * @param msg The message you would like logged.
   * @return The number of bytes written.
   */
  public int w(String msg) {
    return w(EMPTY_STRING, msg, null, 1);
  }

  /**
   * Send formatted a {@link android.util.Log#WARN} log message.
   * @param msg The message you would like logged.
   * @param tr An exception to log.
   * @return The number of bytes written.
   */
  public int w(String msg, Throwable tr) {
    return w(EMPTY_STRING, msg, tr, 1);
  }
}
