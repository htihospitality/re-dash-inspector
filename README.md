# re-dash-inspector

A Flutter DevTools extension for re-dash

# Features

- Visualize the contents of the re-dash app database during development
- Free text search anything
- Drill down into nested map values

# Demo

[re-dash-inspector.webm](https://github.com/user-attachments/assets/e6864f98-e97f-4d9a-8624-ac06c71c9cef)

# Prerequisites

- The contents of the app db need to be serializable to/from EDN using ClojureDart's `pr-str` and `cljd.reader/read-string`
- The data in the inspector will only update if the inspector is currently open and visible on the screen while the database is mutated. If not, new changes will not reflect until a new event mutates the database and the inspector window is open at the time.

# Usage

Add `re_dash_inspector` to your `dev_dependencies` in `pubspec.yaml`

```clojure
dev_dependencies:
  re_dash_inspector: ^0.0.1
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
