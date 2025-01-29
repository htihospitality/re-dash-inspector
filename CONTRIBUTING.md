# Development

This section details some notes regarding contributing to the development of this extension itself.

> _These commands assumes you have [babashka](https://github.com/babashka/babashka) installed_

## DevTools Simulator

The DevTools simulator allows for fast feedback during development of an extension. After cloning this project start the simulator with:

```shell
bb run
```
Now, when making changes to the ClojureDart source, your changes will get reloaded in the simulator.

### Connect your app

Start the app you want to connect with the extension running in the simulator by copying the VM service url of your app into the simulator and `connect`.

The VM service url is normally printed to the console after starting your app for example:

```
A Dart VM Service on Linux is available at: http://127.0.0.1:39641/2WAFXuRKqwY=/
```

## DevTools

When you're done developing and would like to test the extension in the actual DevTools connected to your app, run:

```shell
## This will compile ClojureDart, build the Flutter web artifacts,
## and copy the assets to the `extension/devtools/build` folder

bb build-copy
```

validate the build with

```shell
bb validate
```

now, in your app's `pubspec.yaml` reference the extension using a path coordinate like

```yaml
dev_dependencies:
  re_dash_inspector:
    path: /path/to/re_dash_inspector
```

Start your app, open Flutter DevTools, and you should see a tab for the re_dash extension.

## Worker

The recorder makes use of web workers for diffing data structures. As web workers run in their own process seperate from the main app, it needs to be compiled seperately as well.

If changes were made to `worker.cljd` then run the worker's compile step from the `./worker` folder first:

```bash
bb compile
```

and afterward run the main app's `bb run` or `bb build-copy` etc.