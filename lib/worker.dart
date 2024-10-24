import 'dart:js_interop';
import 'package:web/web.dart';

Worker createWorker() {

  // This is done in dart as the .toJS Extension type
  // don't yet seem to work on CLJD

  String workerFileName = 'worker.js';
  return Worker(workerFileName.toJS);
}
