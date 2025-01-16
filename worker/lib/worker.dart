import 'dart:js_interop';
import 'package:web/web.dart';

// See: https://github.com/dart-lang/web/issues/251

DedicatedWorkerGlobalScope getWorkerGlobalScope() {
  return globalContext as DedicatedWorkerGlobalScope;
}

// Dummy handler to demostrate it could work in native dart.
// This handler should not be needed as we'd ideally pass it in
// to listen() as a parameter from CLJD

void handler(Event event) {
    print('Worker received message...');
} 

// The h parameter coming from CLJD is a closure of dynamic > dynamic
// and must somehow be converted to a JavaScriptFunction to be used
// as the onmessage handler.

void listen(h) {
  print('Worker listening...');
  getWorkerGlobalScope().onmessage = handler.toJS; // instead of handler we should use h
}

