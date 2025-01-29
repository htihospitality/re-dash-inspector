import 'dart:js_interop';
import 'package:web/web.dart';

// See: https://github.com/dart-lang/web/issues/251

DedicatedWorkerGlobalScope getWorkerGlobalScope() {
  return globalContext as DedicatedWorkerGlobalScope;
}

void listen(h) {
  getWorkerGlobalScope().onmessage = h;
}

