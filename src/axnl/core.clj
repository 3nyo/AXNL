(ns axnl.core
  (:gen-class)
  (:require [crux.api :as crux]
            [database.core :as db]
            [clojure.string :as str])
  (:import [java.util UUID]))




(def database (db/init-new-db))

(defn add-node
  "Add a node to the dataset."
  [name description]
  (crux/submit-tx database
                  [[:crux.tx/put
                    {:crux.db/id (UUID/randomUUID)
                     :name name
                     :description description
                     :type :object}]]))

(defn search-nodes
  "Search nodes by their names first and text content second."
  [word]
  (crux/q (crux/db database)
          `{:find [id]
            :where [[id :name ~word]]
            :full-results? true}))

(defn list-all-nodes
  "List all nodes in the database."
  []
  (crux/q (crux/db database)
          `{:find [id]
            :where [id :crux.db/id _]}))

(defn node-by-id
  "Fetch a node by its UUID."
  [node-id]
  (-> (crux/q (crux/db database)
              `{:find [id]
                :where [[id :crux.db/id ~node-id]]
                :full-results? true})
      first
      first))

(defn node->text
  "Converts a node to its textual representation."
  [node-datastructure]
  (->> node-datastructure
       first
       (map (fn [kv-pair]
              (let [[key
                     val] kv-pair]
                [key (if (set? val)
                       (->> val
                            (map #(crux/q (crux/db database)
                                          `{:find [object name]
                                            :where [[id :crux.db/id ~%1]
                                                    [id :name name]]}))
                            (map first)
                            (into #{}))
                       val)])))
       (map (fn [kv-pair]
              (let [[key
                     val] kv-pair]
                (if (set? val)
                  (->> val
                       (map (fn [edge] (str
                                        (subs (str key) 1)
                                        ": " (second edge) " id: " (first edge))))
                       (str/join "\n"))
                  (str (subs (str key) 1) ": " val)))))
       (str/join "\n")))

(defn link-nodes
  "Link two nodes given their UUIDs."
  [src dest type]
  (crux/submit-tx database
                  [[:crux.tx/put
                    (assoc (node-by-id src)
                           type [dest])]]))

(defn list-nodes
  "List all the nodes with a name."
  [name]
  (->> name
       search-nodes
       (map node->text)
       (str/join "\n------\n")
       (println)))
