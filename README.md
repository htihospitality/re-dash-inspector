# re-dash-inspector

A Flutter DevTools extension for [re-dash](https://github.com/htihospitality/re-dash)

# Features

- Visualize the contents of the re-dash app database ([AppDB](https://github.com/htihospitality/re-dash/blob/main/doc/04-databases.md#appdb)) during development
- Free text search anything
- Drill down into nested map values

> _This extension only supports the [AppDB](https://github.com/htihospitality/re-dash/blob/main/doc/04-databases.md#appdb) database backend for re-dash. When using another database backend (eg. [Drift](https://github.com/htihospitality/re-dash/blob/main/doc/04-databases.md#drift)) it will typically ship with their own DevTools extenstion eg. the [drift_db_viewer](https://drift.simonbinder.eu/docs/community_tools/#drift_db_viewer)_

# Demo

[re-dash-inspector.webm](https://github.com/user-attachments/assets/e6864f98-e97f-4d9a-8624-ac06c71c9cef)

# Prerequisites

- [re-dash](https://github.com/htihospitality/re-dash) v1.1.0 minimum 
- The contents of the app db need to be serializable to/from EDN using ClojureDart's `pr-str` and `cljd.reader/read-string`
- The data in the inspector will only update if the inspector is currently open and visible on the screen while the database is mutated. If not, new changes will not reflect until a new event mutates the database and the inspector window is open at the time.

# Usage

Add `re_dash_inspector` to your `dev_dependencies` in `pubspec.yaml`

```yaml
dev_dependencies:
  re_dash_inspector: ^0.0.2
```

then register the `debug` re-dash interceptor targeting dev-tools either in selected event handlers or globally in `main` like

```clojure
(rd/reg-global-interceptor
    (rd/debug {:target :target/dev-tools}))
```

See [Debugging](https://github.com/htihospitality/re-dash/blob/main/doc/02-debugging.md) for more options using the `debug` interceptor.

# Performance

The DevTools extension is updated with a new copy of the app-db contents every time a `:db` effect mutates the database. This is done via serializing the entire app-db using `pr-str` before sending it to DevTools where it will be deserialized using `cljd.reader/read-string` before being rendered on screen.

Keep this in mind while developing, as it might add some overhead to your application during each re-dash event loop. Importantly this will not impact your compiled application at all, only during development mode - if even noticeable at all.

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


