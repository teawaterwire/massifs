(ns massifs.events
  (:require [re-frame.core :as rf]
            [ajax.core :as ajax]
            [massifs.db :as db]))

(def debug-interceptor [(when ^boolean js/goog.DEBUG rf/debug)])

(rf/reg-event-fx
 :initialize-db
 (fn [_ _]
   {:db db/default-db
    :http-xhrio {:method :get
                 :uri "/alps.json"
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success [:set-massifs]
                 :on-failure [:set-massifs nil]}}))

(rf/reg-event-db
 :set-massifs
 debug-interceptor
 (fn [db [_ data]]
   (assoc db :massifs-data data)))

(rf/reg-event-db
 :tick
 ; debug-interceptor
 (fn [{:keys [ time-left] :as db}]
   (if (pos? time-left)
     (assoc db :time-left (dec time-left))
     db)))

(rf/reg-event-db
 :new-massif-to-find
 debug-interceptor
 (fn [{:keys [massifs-data] :as db}]
   (update db :massif-to-find (fn [m] (loop []
                                        (let [m' (rand-nth massifs-data)]
                                          (if (= m m') (recur)
                                            m')))))))

(rf/reg-event-fx
 :massif-found
 debug-interceptor
 (fn [{:keys [db]} [_ massif]]
   {:db (-> db
            (update :score #(+ % (- 30 (:tries db))))
            (assoc :massif-highlighted massif :highlight-type :success
                   :tries 0))
    :dispatch-later [{:ms 600 :dispatch [:set :massif-highlighted nil]}]
    :dispatch [:new-massif-to-find]}))

(rf/reg-event-fx
 :massif-not-found
 debug-interceptor
 (fn [{:keys [db]} [_ massif]]
   {:db (-> db
            (update :tries inc)
            (assoc :massif-highlighted massif :highlight-type :danger))
    :dispatch-later [{:ms 600 :dispatch [:set :massif-highlighted nil]}]}))

(rf/reg-event-fx
 :massif-clicked
 debug-interceptor
 (fn [{:keys [db]} [_ massif]]
   (let [event (if (= massif (:massif-to-find db))
                 :massif-found
                 :massif-not-found)]
     {:dispatch [event massif]})))

(rf/reg-event-fx
 :start
 debug-interceptor
 (fn [{:keys [db]}]
   {:db (assoc db :time-left 30 :score 0)
    :dispatch [:new-massif-to-find]}))

(rf/reg-event-db
 :set
 debug-interceptor
 (fn [db [_ k v]]
   (assoc db k v)))
