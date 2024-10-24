# re-dash-inspector

A Flutter DevTools extension for [re-dash](https://github.com/htihospitality/re-dash)

# Features

## Inspector

- Visualize the contents of the re-dash app database ([AppDB](https://github.com/htihospitality/re-dash/blob/main/doc/04-databases.md#appdb)) during development
- Free text search anything
- Drill down into nested data structures (maps & lists)

## Recorder

- Start & stop a recording of all events
- See a diff of the database changes effected by each event
- Free text search anything
- Use the Path editor to zone in to only the parts of interest in the database


> _This extension only supports the [AppDB](https://github.com/htihospitality/re-dash/blob/main/doc/04-databases.md#appdb) database backend for re-dash. When using another database backend (eg. [Drift](https://github.com/htihospitality/re-dash/blob/main/doc/04-databases.md#drift)) it will typically ship with their own DevTools extenstion eg. the [drift_db_viewer](https://drift.simonbinder.eu/docs/community_tools/#drift_db_viewer)_

# Screenshots

## Inspector

![inspector](https://raw.githubusercontent.com/htihospitality/re-dash-inspector/main/doc/images/inspector.png)

## Recorder

![recorder](https://raw.githubusercontent.com/htihospitality/re-dash-inspector/main/doc/images/recorder.png)

# Demo Videos

## Inspector

[Inspector.webm](https://github.com/user-attachments/assets/35d1258a-c82d-4f36-ae60-725c27892afa)

## Recorder

[Recorder.webm](https://github.com/user-attachments/assets/eca39e8a-4985-41b0-ad8c-9e99b13a9196)


# Prerequisites

- [re-dash](https://github.com/htihospitality/re-dash) v1.1.2 minimum
- The contents of the app db need to be serializable to/from EDN using ClojureDart's `pr-str` and `cljd.reader/read-string`

# Usage

Add `re_dash_inspector` to your `dev_dependencies` in `pubspec.yaml`

```yaml
dev_dependencies:
  re_dash_inspector: ^0.1.0-dev.0
```

then register the `debug` re-dash interceptor targeting dev-tools either in selected event handlers or globally in `main` like

```clojure
(rd/reg-global-interceptor
    (rd/debug {:target :target/dev-tools}))
```

See [Debugging](https://github.com/htihospitality/re-dash/blob/main/doc/02-debugging.md#the-debug-interceptor) for more options using the `debug` interceptor.

# Performance

## App

The DevTools extension is updated with a new copy of the app-db contents every time a `:db` effect mutates the database. This is done via serializing the entire app-db using `pr-str` before sending it to DevTools where it will be deserialized using `cljd.reader/read-string` before being rendered on screen.

Keep this in mind while developing, as it might add some overhead to your application during each re-dash event loop. Importantly this will not impact your compiled application at all, only during development mode - if even noticeable at all.

The `debug` interceptor will only attempt to send the serialized database to DevTools if

- the app is running in debug mode
- the inspector has an active connection to re-dash

## Extension

When the Recorder is started, each event's app-db contents will be diff'ed using ClojureDart's `clojure.data/diff` implementation. This work is done in the DevTools extension process, not the app's process, so the app will not incur this performance overhead, but the DevTools process will. This work is offloaded onto 4 [Web Workers](https://developer.mozilla.org/en-US/docs/Web/API/Web_Workers_API/Using_web_workers) running in the background, but depending on the size of the app-db being diff'ed, you might notice some UI performance degradation in this re-dash-inspector UI as the results of these diff's are being rendered on screen.

# Privacy

The [debug](https://github.com/htihospitality/re-dash/blob/main/doc/02-debugging.md) interceptor sends the contents of the app-db to the locally running [Flutter DevTools](https://docs.flutter.dev/tools/devtools) using the [`postEvent`](https://api.flutter.dev/flutter/dart-developer/postEvent.html) function call. At no point does the data leave the local machine and all processing is done locally in the DevTools process.
