(ns database.core
  "The core set of functionality associated with the database."
  (:require [crux.api :as crux])
  (:import [java.util UUID]))

(def truth-uuid (UUID/randomUUID))
(def science-uuid  (UUID/randomUUID))
(def materialism-uuid (UUID/randomUUID))

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
    :type :type}
   {:crux.db/id :synonym
    :name "Synonym"
    :description "Synonyms of a entry."
    :type :type}
   {:crux.db/id science-uuid
    :name "Science"
    :description "The only method for obtaining knowledge about the natural world."
    :synonym #{truth-uuid
               materialism-uuid}
    :type :object}
   {:crux.db/id truth-uuid
    :name "Truth"
    :description "A collection of data vis a vis some fact."
    :synonym #{science-uuid
               materialism-uuid}
    :type :object}
   {:crux.db/id materialism-uuid
    :name "Philosophical Materialism"
    :description "The belief that that all things in the world are material."
    :synonym #{science-uuid
               truth-uuid}
    :type :object}])

(defn init-new-db
  "Creates a new database instance."
  []
  (let [database (crux/start-node {})]
    (crux/submit-tx database
                    (map (fn [node] [:crux.tx/put node]) -default-nodes))
    database))

                                        ;(defn )

                                        ;(init-new-db)
