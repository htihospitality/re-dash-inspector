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

# Usage

Add `re_dash_inspector` to your `dev_dependencies` in `pubspec.yaml`

```yaml
dev_dependencies:
  re_dash_inspector: ^0.0.5
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

# Known issues

When hot restarting the app, DevTools sometimes pauses the app's main isolate for some unknown reason.

We automatically recover from this situation by detecting a pause event and automatically resume, so there is no manual action needed from the developer.

One caveat is that for some reason a half painted pause/resume notification remains in a banner on top of the DevTools UI. This notification seems unresponsive, but also does not affect anything negatively, i.e. it can merely be ignored. But in case you do want to get rid of it, just refresh the browser tab and it will be gone. This seems to be an issue in DevTools itself (albeit speculation) so hopefully it gets fixed in DevTools at some point.

Example of the UI glitch that can safely be ignored:
![resume-notification](https://github.com/user-attachments/assets/8e2ddec4-47b8-4e6b-bc01-2a57eb099435)


