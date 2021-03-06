(ns starcity.models.plaid
  (:require [starcity.datomic :refer [conn]]
            [starcity.util :refer [remove-nil]]
            [starcity.models.util :refer :all]
            [starcity.config :refer [config]]
            [datomic.api :as d]
            [plumbing.core :refer [assoc-when]]
            [taoensso.timbre :as timbre]))

;; =============================================================================
;; Helpers
;; =============================================================================

(defn- extract-income-stream
  [{:keys [active confidence days monthly_income period]}]
  (ks->nsks :income-stream
            (remove-nil
             {:active     active
              :confidence (float confidence)
              :days       days
              :income     monthly_income
              :period     period})))

(defn- extract-income
  [{:keys [income_streams last_year_income last_year_income_before_tax
           projected_yearly_income projected_yearly_income_before_tax]}]
  (ks->nsks :plaid-income
            (remove-nil
             {:last-year                last_year_income
              :last-year-pre-tax        last_year_income_before_tax
              :projected-yearly         projected_yearly_income
              :projected-yearly-pre-tax projected_yearly_income_before_tax
              :obtained-at              (java.util.Date.)
              :income-streams           (map extract-income-stream income_streams)})))

(defn- extract-account
  [{:keys [balance institution_type subtype type meta]}]
  (let [available-balance (when-let [x (:available balance)] (float x))
        current-balance   (when-let [x (:current balance)] (float x))
        credit-limit      (when-let [x (:limit meta)] (float x))]
    (ks->nsks :bank-account
              (remove-nil
               (assoc-when
                {:type             type
                 :institution-type institution_type
                 :obtained-at      (java.util.Date.)}
                :credit-limit credit-limit
                :current-balance current-balance
                :available-balance available-balance
                :subtype subtype)))))

(defn- add-income-tx
  [entity-id access-token {:keys [accounts income]}]
  {:db/id               entity-id
   :plaid/income        (extract-income income)
   :plaid/bank-accounts (map extract-account accounts)
   :plaid/access-token  access-token})

;; =============================================================================
;; API
;; =============================================================================

(defn by-account-id
  [account-id]
  (one (d/db conn) :plaid/account account-id))

(defn create!
  [account-id public-token access-token]
  (let [ent (ks->nsks :plaid {:account                  account-id
                              :public-token             public-token
                              :access-token             access-token
                              :access-token-obtained-at (java.util.Date.)})
        tid (d/tempid (get-in config [:datomic :partition]))
        tx  @(d/transact conn [(assoc ent :db/id tid)])]
    (d/resolve-tempid (d/db conn) (:tempids tx) tid)))

(defn add-income-data!
  [access-token data]
  (let [ent (one (d/db conn) :plaid/access-token access-token)]
    (d/transact conn [(add-income-tx (:db/id ent) access-token data)])))
