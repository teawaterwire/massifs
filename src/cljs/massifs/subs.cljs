(ns massifs.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 :massifs-data
 (fn [db]
   (:massifs-data db)))

(re-frame/reg-sub
 :get
 (fn [db [_ k]]
   (get db k)))
