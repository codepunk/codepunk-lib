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

package com.codepunk.codepunklib.util.plugin;

/**
 * <p>
 * Class that manages and supplies plugins based on supplied criteria. This criteria may or may
 * not change over the lifecycle of the app; therefore it is good practice to always using the
 * {@link PluginManager#get(Object)} method each time you need to reference a plugin rather than
 * saving the returned value. Upon calling {@link PluginManager#get(Object)}, PluginManager
 * will automatically determine if a new plugin needs to be created based on the given criteria.
 * </p>
 * <p>
 * Plugins allow you to avoid multiple <code>if</code>/<code>else</code> or <code>switch</code>
 * statements to perform certain behaviors based on a set of criteria. This can range from logging
 * information only if running in a development environment, or performing a certain action only
 * if a user is logged in, showing extra controls if the user has admin rights, showing a
 * "what's new" dialog the first time the app is run after an upgrade, or optionally showing a
 * special screen if it is a user's birthday.
 * </p>
 * <p>
 * As an example, suppose you wanted to show an encouraging message when someone used the app on a
 * Monday. You could easily include an <code>if</code> statement to accomplish this. But imagine
 * that you also wanted to change the background color on Mondays, and add an extra button that
 * was only visible on Mondays, and so on. Soon your code would be littered with <code>if</code>
 * statements testing whether or not it was Monday.
 * </p>
 * <p>
 * You could instead set up a Monday PluginManager:
 * <pre>
 * public class MondayManager extends PluginManager&lt;MondayManager.MondayPlugin, Calendar&gt; {
 *   private static MondayManager sInstance;
 * 
 *   public static synchronized MondayManager getInstance() {
 *     if (sInstance == null) {
 *       sInstance = new MondayManager();
 *     }
 *     return sInstance;
 *   }
 * 
 *   private MondayManager() {
 *   }
 * 
 *   &#064;Override
 *   protected MondayPlugin newPlugin(Calendar calendar) {
 *     Log.d("MondayManager", "newPlugin");
 *     return (isMonday(calendar) ?
 *         new IsMondayPlugin() :
 *         new IsNotMondayPlugin());
 *   }
 * 
 *   &#064;Override
 *   protected boolean isPluginDirty(
 *       MondayPlugin mondayPlugin,
 *       Calendar oldCalendar,
 *       Calendar calendar) {
 *     boolean wasMonday = isMonday(oldCalendar);
 *     boolean isMonday = isMonday(calendar);
 *     return (wasMonday != isMonday);
 *   }
 * 
 *   private static boolean isMonday(Calendar calendar) {
 *     return (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY);
 *   }
 * 
 *   public interface MondayPlugin {
 *     void showOptionalGreeting(Context context);
 *   }
 * 
 *   private static class IsMondayPlugin implements MondayPlugin {
 *     &#064;Override
 *     public void showOptionalGreeting(Context context) {
 *       Toast.makeText(
 *           context,
 *           "Looks like someone has a case of the Mondays!",
 *           Toast.LENGTH_LONG)
 *           .show();
 *     }
 *   }
 * 
 *   private static class IsNotMondayPlugin implements MondayPlugin {
 *     &#064;Override
 *     public void showOptionalGreeting(Context context) {
 *       //  No action
 *     }
 *   }
 * }
 * </pre>
 * </p>
 * <p>
 * In the above example, we created a <code>MondayPlugin</code> with a single method,
 * <code>showOptionalGreeting</code>. In the example mentioned previously, we could have also
 * included a "<code>setBackgroundColor</code>" method, and so on. Since the current date/time
 * is constantly changing, we pass that along with each call to {@link PluginManager#get(Object)}.
 * PluginManager in turn calls {@link PluginManager#isPluginDirty(Object, Object, Object)} and
 * {@link PluginManager#newPlugin(Object)} as appropriate to determine whether the existing plugin
 * (if any) is "dirty" and a new one needs to be created.
 * </p>
 * <p>To make use of this new plugin, we can code something like the following in an Activity:
 * <pre>
 * public class MainActivity extends Activity {
 *   &#064;Override
 *   protected void onCreate(Bundle savedInstanceState) {
 *     Calendar now = Calendar.getInstance();
 *     MondayManager.getInstance().get(now).showOptionalGreeting(this);
 *   }
 * }
 * </pre>
 * </p>
 * <p>
 * This is, of course, a rough example but it shows the power of using PluginManagers. Note that we
 * have avoided <code>if</code>/<code>else</code> or <code>switch</code> statements in our
 * <code>MainActivity</code> class, which makes for cleaner and more modular code. Another benefit
 * is that if you are testing a new feature, that feature can be implemented as a plugin. If
 * at any time you want to remove that feature entirely, it becomes much easier because it is
 * completely contained in its own plugin class and the calling class (in this case, MainActivity)
 * doesn't need to be modified, or even aware that the plugin was removed.
 * </p>
 * @param <Plugin> An interface or class that will be managed by this PluginManager.
 * @param <Params> A class representing a parameter that may or may not change during the
 *                 application lifecycle.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class PluginManager<Plugin, Params> {

  /**
   * The currently-active plugin (if any).
   */
  private Plugin mActivePlugin;

  /**
   * The parameters used to create the currently-active plugin.
   */
  private Params mActiveParams;

  /**
   * Class that listens for when plugins are activated and deactivated.
   */
  private PluginListener<Plugin, Params> mPluginListener;

  /**
   * Sets the PluginListener that will listen for when plugins are activated and deactivated.
   * @param pluginListener The listener. Can be <code>null</code>.
   */
  public void setPluginListener(PluginListener<Plugin, Params> pluginListener) {
    mPluginListener = pluginListener;
  }

  /**
   * Returns a plugin appropriate to the supplied <code>params</code>.
   * @param params The parameters used to create a new plugin.
   * @return The currently-active plugin.
   */
  public Plugin get(Params params) {
    Plugin plugin = mActivePlugin;
    if (plugin == null || isPluginDirty(plugin, mActiveParams, params)) {
      if (plugin != null) {
        onDeactivatePlugin(plugin);
        if (mPluginListener != null) {
          mPluginListener.onDeactivatePlugin(plugin);
        }
      }

      plugin = newPlugin(params);

      mActivePlugin = plugin;
      mActiveParams = params;
      onActivatePlugin(plugin, params);
      if (mPluginListener != null) {
        mPluginListener.onActivatePlugin(plugin, params);
      }
    }
    return plugin;
  }

  /**
   * Returns the active plugin (if any).
   * @return The active plugin.
   */
  public Plugin getActivePlugin() {
    return mActivePlugin;
  }

  /**
   * Called when a plugin is activated (i.e. created).
   * @param plugin The newly-active plugin.
   * @param params The parameters used to create the plugin.
   */
  protected void onActivatePlugin(Plugin plugin, Params params) {
    // No action
  }

  /**
   * Called when a plugin is about to be deactivated (i.e. destroyed).
   * @param plugin The plugin that is about to be deactivated.
   */
  protected void onDeactivatePlugin(Plugin plugin) {
    // No action
  }

  /**
   * Creates a new plugin based on the supplied <code>params</code>.
   * @param params The parameters to use to create the new plugin.
   * @return The new plugin.
   */
  protected abstract Plugin newPlugin(Params params);

  /**
   * Method that determines whether a plugin is "dirty". That is, based on the supplied
   * <code>params</code>, whether a new plugin needs to be created or not.
   * @param plugin The currently-active plugin.
   * @param oldParams The parameters that were used to create the currently-active plugin.
   * @param params The new parameters that help determine whether the currently-active plugin is
   *               "dirty" and a new plugin needs to be created.
   * @return Whether the supplied plugin is "dirty" based on the given parameters.
   */
  protected abstract boolean isPluginDirty(Plugin plugin, Params oldParams, Params params);

  /**
   * Interface that serves as a listener for when plugins are activated (i.e. created) and
   * deactivated (destroyed).
   * @param <Plugin> An interface or class that will be managed by this PluginManager.
   * @param <Params> A class representing a parameter that may or may not change during the
   *                 application lifecycle.
   */
  public interface PluginListener<Plugin, Params> {
    void onActivatePlugin(Plugin plugin, Params params);
    void onDeactivatePlugin(Plugin plugin);
  }
}
