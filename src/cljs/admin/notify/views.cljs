(ns admin.notify.views
  (:require [re-frame.core :refer [subscribe dispatch]]
            [starcity.components.notifications :as n]))

(defn- notification [idx {:keys [message type]}]
  (let [f (case type
            :success n/success
            :error   n/danger
            n/danger)]
    [f message #(dispatch [:notify/delete idx])]))

(defn notifications []
  (let [notifications (subscribe [:notify/all])]
    (fn []
      ;; TODO: SASS
      [:section.section
       {:style (if (empty? @notifications)
                 {:display "none"}
                 {:padding-bottom 0})}
       [:div.container.notifications
        (doall
         (map-indexed
          (fn [idx n]
            ^{:key (str "notification-" idx)} [notification idx n])
          @notifications))]])))
