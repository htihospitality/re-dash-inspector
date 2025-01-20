(ns hti.re-dash-inspector.worker
  (:require
   [cljs.reader :as r]
   [clojure.data :as data]
   [clojure.walk :as walk]))

(defn- sanitize
  "Transforms an app DB snapshot before performing a diff."
  [m]

  (walk/postwalk
   (fn [el]
     (if (map-entry? el)
       (let [[key val] el]
         [key

          (cond
            ;; Empty maps behave weirdly when doing clojure.data/diff
            ;; Here we merely reset them to {}
            (and (map? val) (empty? val)) {}
            ;; Empty lists behave weirdly when doing clojure.data/diff
            ;; Here we merely reset them to '()
            (and (list? val) (empty? val)) '()

            :else val)])
       el))
   m))

(defn- on-message
  "Produces a diff on a pair of app DB snapshots for the inspector's recording feature."
  [event]

  (let [data (some-> event .-data r/read-string)
        event-id (:rd.inspector.event/id data)
        event-number (:rd.inspector.event/number data)
        db-before (:rd.inspector.event/db-before data)
        db-after (:rd.inspector.event/db-after data)
        diff (take 2 (data/diff (sanitize db-before) (sanitize db-after)))]

    (.postMessage
     js/self

     (prn-str {:rd.inspector.event/id event-id
               :rd.inspector.event/number event-number
               :rd.inspector.event/diff diff}))))

(set! (.-onmessage js/self) on-message)