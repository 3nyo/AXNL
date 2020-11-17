(ns database.core
  "The core set of functionality associated with the database."
  (:require [crux.api :as crux]))

(def -default-nodes
  [{:crux.db/id :name
    :name "Name"
    :description "Symbols that are used to represent entities in a human-readable form."
    :type :type}
   {:crux.db/id :description
    :name "Description"
    :description "A textual description of the object. Links to nothing."
    :type :type}
   {:crux.db/id :type
    :name "Type"
    :description "Types are the data surrounding an edge."
    :type :type}])

(defn init-new-db
  "Creates a new database instance."
  []
  (crux/submit-tx (crux/start-node {})
                  (map (fn [node] [:crux.tx/put node]) -default-nodes)))

(defn )

(init-new-db)
