(ns starcity.datomic.migrations.seed-test-applications-8-4-16
  (:require [starcity.config.datomic :as config]
            [starcity.models.util :refer [one]]
            [datomic.api :as d]
            [clj-time.coerce :as c]
            [clj-time.core :as t]))



(def seed-test-applications
  {:starcity/seed-test-applications
   {:txes [(fn [conn]
             (let [application-id  (d/tempid config/partition)
                   application-id2 (d/tempid config/partition)]
               [{:db/id                                   application-id
                 :member-application/current-address      {:address/lines       "7255 Wild Currant Way\nLower Floor"
                                                           :address/city        "Oakland"
                                                           :address/state       "CA"
                                                           :address/postal-code "94611"}
                 :member-application/desired-properties   [[:property/internal-name "52gilbert"]
                                                           [:property/internal-name "229ellis"]
                                                           [:property/internal-name "2072mission"]]
                 :member-application/desired-license      (:db/id (one (d/db conn) :license/term 6))
                 :member-application/locked               true
                 :member-application/has-pet              false
                 :member-application/desired-availability (c/to-date (t/date-time 2016 9 1))
                 :member-application/community-fitness    {:community-fitness/why-interested          "Because."
                                                           :community-fitness/prior-community-housing "Dorms."
                                                           :community-fitness/skills                  "Emacs. Codez."
                                                           :community-fitness/free-time               "Photography."
                                                           :community-fitness/dealbreakers            "Stupidity."}
                 :member-application/submitted-at         (c/to-date (t/date-time 2016 8 1))}
                {:account/member-application application-id
                 :db/id                      [:account/email "test@test.com"]}
                {:db/id               (d/tempid config/partition)
                 :plaid/account       [:account/email "test@test.com"]
                 :plaid/income        [{;:plaid-income/last-year         50000
                                        ;:plaid-income/last-year-pre-tax 70000
                                        ;:plaid-income/projected-yearly  80000
                                        :plaid-income/income-streams
                                        [{:income-stream/active     true
                                          :income-stream/confidence 1.0
                                          :income-stream/period     15
                                          :income-stream/income     6666}
                                         {:income-stream/active     true
                                          :income-stream/confidence 1.0
                                          :income-stream/period     15
                                          :income-stream/income     6666}]}]
                 :plaid/bank-accounts [{:bank-account/available-balance 4000.0
                                        :bank-account/current-balance   3889.0
                                        :bank-account/type              "depository"
                                        :bank-account/subtype           "checking"
                                        :bank-account/institution-type  "wells"}
                                       {:bank-account/available-balance 4000.0
                                        :bank-account/current-balance   3889.0
                                        :bank-account/type              "mortgage"
                                        :bank-account/subtype           "mortgage"
                                        :bank-account/institution-type  "wells"}
                                       {:bank-account/available-balance 4000.0
                                        :bank-account/current-balance   3889.0
                                        :bank-account/type              "other"
                                        :bank-account/subtype           "rewards"
                                        :bank-account/institution-type  "wells"}
                                       {:bank-account/available-balance 4000.0
                                        :bank-account/current-balance   3889.0
                                        :bank-account/type              "depository"
                                        :bank-account/subtype           "checking"
                                        :bank-account/institution-type  "wells"}]}

                ;; BEGIN SECOND APPLICATION
                {:db/id                                   application-id2
                 :member-application/current-address      {:address/lines       "533 Divisadero St."
                                                           :address/city        "San Francisco"
                                                           :address/state       "CA"
                                                           :address/postal-code "94103"}
                 :member-application/approved             true
                 :member-application/desired-properties   [[:property/internal-name "52gilbert"]]
                 :member-application/desired-license      (:db/id (one (d/db conn) :license/term 1))
                 :member-application/locked               true
                 :member-application/desired-availability (c/to-date (t/date-time 2016 10 15))
                 :member-application/pet                  {:pet/type   "dog"
                                                           :pet/breed  "Corgi"
                                                           :pet/weight 25}
                 :member-application/has-pet              true
                 :member-application/community-fitness    {:community-fitness/why-interested          "Because."
                                                           :community-fitness/prior-community-housing "Dorms."
                                                           :community-fitness/skills                  "Emacs. Codez."
                                                           :community-fitness/free-time               "Photography."}
                 :member-application/submitted-at         (c/to-date (t/date-time 2016 8 5))}
                {:account/member-application application-id2
                 :db/id                      [:account/email "onboarding@test.com"]}
                {:db/id               (d/tempid config/partition)
                 :income-file/account [:account/email "onboarding@test.com"]
                 :income-file/path    "data/income-uploads/285873023222771/starcity-kitchen.png"}
                ;; Security deposit that indicates approval
                {:db/id                            (d/tempid config/partition)
                 :security-deposit/account         [:account/email "onboarding@test.com"]
                 :security-deposit/amount-required 2300}]))]
    :requires [:starcity/add-member-application-schema
               :starcity/add-community-fitness-schema
               :starcity/add-plaid-schema
               :starcity/add-pet-schema
               :starcity/seed-licenses
               :starcity/seed-test-accounts
               :starcity/add-income-files-schema-8-3-16
               :starcity/seed-mission
               :starcity/seed-union-square
               :starcity/seed-gilbert]}})
