(ns axnl.core
  (:gen-class)
  (:require [crux.api :as crux])
  (:import [java.util UUID]))




(crux/submit-tx db
                (mapv (fn [node] [:crux.tx/put node]) nodes))

(sort (crux/q (crux/db db)
              `{:find [object]
                :where [[object :name "Test"]]
                :full-results? true}))

(defn add-node
  "Add a node to the dataset."
  [name description]
  (crux/submit-tx db
                  [[:crux.tx/put
                    {:crux.db/id (UUID/randomUUID)
                     :name name
                     :description description
                     :type :node}]]))

(defn search-nodes
  "Search nodes by their names first and text content second."
  [words]
  (crux/q (crux/db db)
          `(:find [name description]
                  :where [[_ :name name]
                          [_ :description description]])))

(search-nodes "test")
(add-node "Test" "This is a test.")

(defn del-note
  "Delete a node and the associated"
  [])

(defn mod-note [])

(defn -main []
  (println "hello world"))
