(ns starcity.models.property
  (:require [starcity.datomic :refer [conn]]
            [starcity.config :as config]
            [starcity.models.util :refer :all]
            [starcity.datomic.util :refer :all]
            [datomic.api :as d]
            [starcity.config :as config]))

;; =============================================================================
;; API
;; =============================================================================

;; =============================================================================
;; Queries

(defn available-leases
  [property]
  (->> (d/q '[:find ?ls
              :in $ ?p
              :where [?p :property/available-leases ?ls]]
            (d/db conn) (:db/id property))
       (map first)))

(defn available-units
  "Retrieve all units that are currently available.

  A property is considered *available* iff it has a value under
  its :unit/available-on attribute."
  [property-id]
  (->> (d/q '[:find ?units
              :in $ ?property
              :where
              [?property :property/units ?units]
              [?units :unit/available-on _]]
            (d/db conn) property-id)
       (map first)))

;; =============================================================================
;; Transactions

(defn exists?
  [property-id]
  (:property/name (one (d/db conn) property-id)))

(defn create! [name internal-name units-available]
  (let [entity (ks->nsks :property
                         {:name            name
                          :internal-name   internal-name
                          :units-available units-available})
        tid    (d/tempid (config/datomic-partition))
        tx     @(d/transact conn [(assoc entity :db/id tid)])]
    (d/resolve-tempid (d/db conn) (:tempids tx) tid)))
