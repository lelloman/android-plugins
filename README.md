A POC of an android app that can load plugins at runtime.

There are 2 AndroidStudio projects, one is in the folder MyNiceApps (the app that will run the plugins) and the other is in PluginA floder.
AndroidPlugins folder contains the library module on which both the apps depend on.

When PluginA builds the apk it copies the output into the assets folder of MyNiceApps/app for the sake of simplicity.
