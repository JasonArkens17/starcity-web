(ns starcity.datomic.migrations.initial.add-unit-schema)

(def add-unit-schema
  {:starcity/add-unit-schema
   {:txes [[{:db/id                 #db/id[:db.part/db]
             :db/ident              :unit/name
             :db/valueType          :db.type/string
             :db/cardinality        :db.cardinality/one
             :db/fulltext           true
             :db/doc                "Name of the unit."
             :db.install/_attribute :db.part/db}

            {:db/id                 #db/id[:db.part/db]
             :db/ident              :unit/description
             :db/valueType          :db.type/string
             :db/cardinality        :db.cardinality/one
             :db/fulltext           true
             :db/doc                "Description of the unit."
             :db.install/_attribute :db.part/db}

            {:db/id                 #db/id[:db.part/db]
             :db/ident              :unit/price
             :db/valueType          :db.type/float
             :db/cardinality        :db.cardinality/one
             :db/doc                "Additional per-month price of this unit on top of monthly lease."
             :db.install/_attribute :db.part/db}

            {:db/id                 #db/id[:db.part/db]
             :db/ident              :unit/available-on
             :db/valueType          :db.type/instant
             :db/cardinality        :db.cardinality/one
             :db/doc                "The date that this unit is available for lease."
             :db.install/_attribute :db.part/db}

            {:db/id                 #db/id[:db.part/db]
             :db/ident              :unit/floor
             :db/valueType          :db.type/long
             :db/cardinality        :db.cardinality/one
             :db/doc                "The floor that this unit is on."
             :db.install/_attribute :db.part/db}

            {:db/id                 #db/id[:db.part/db]
             :db/ident              :unit/dimensions
             :db/valueType          :db.type/ref
             :db/cardinality        :db.cardinality/one
             :db/isComponent        true
             :db/doc                "The dimensions of this unit."
             :db.install/_attribute :db.part/db}

            ;; =============================================================================
            ;; Dimension

            {:db/id                 #db/id[:db.part/db]
             :db/ident              :unit-dimension/height
             :db/valueType          :db.type/float
             :db/cardinality        :db.cardinality/one
             :db/doc                "Height of unit in feet."
             :db.install/_attribute :db.part/db}

            {:db/id                 #db/id[:db.part/db]
             :db/ident              :unit-dimension/width
             :db/valueType          :db.type/float
             :db/cardinality        :db.cardinality/one
             :db/doc                "Width of unit in feet."
             :db.install/_attribute :db.part/db}

            {:db/id                 #db/id[:db.part/db]
             :db/ident              :unit-dimension/length
             :db/valueType          :db.type/float
             :db/cardinality        :db.cardinality/one
             :db/doc                "Length/depth of unit in feet."
             :db.install/_attribute :db.part/db}]]}})
