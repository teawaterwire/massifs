(ns massifs.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [massifs.events]
            [massifs.subs]
            [massifs.views :as views]
            [massifs.config :as config]
            [day8.re-frame.http-fx]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn init-ticks []
  (js/setInterval #(re-frame/dispatch [:tick]) 1000))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (init-ticks)
  (dev-setup)
  (mount-root))
