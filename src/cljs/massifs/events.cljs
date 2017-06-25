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
 debug-interceptor
 (fn [{:keys [ time-left] :as db}]
   (if (pos? time-left)
     (assoc db :time-left (dec time-left))
     db)))

(rf/reg-event-db
 :new-massif-to-find
 debug-interceptor
 (fn [{:keys [massifs-data] :as db}]
   (assoc db :massif-to-find (rand-nth massifs-data))))

(rf/reg-event-fx
 :start
 debug-interceptor
 (fn [{:keys [db]}]
   {:db (assoc db :time-left 30)
    :dispatch [:new-massif-to-find]}))

(rf/reg-event-db
 :set
 debug-interceptor
 (fn [db [_ k v]]
   (assoc db k v)))
