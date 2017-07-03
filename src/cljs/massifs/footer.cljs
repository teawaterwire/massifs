(ns massifs.leaderboard
  (:require [reagent.core :refer [atom]]))

(def scores (atom nil))

(defn get-scores []
  (sort-by second > @scores))

(defn hook-firebase []
  (let [fref (.. js/firebase (database) (ref "usernames"))
        handler #(reset! scores (js->clj (.val %) :keywordize-keys true))]
    (.. fref (on "value" handler))))

(defn leaderboard []
  (hook-firebase)
  (fn []
    [:div.m40
     [:table.table
      [:thead
       [:tr
        [:th "Position"]
        [:th "Pseudonyme"]
        [:th "Score"]]]
      [:tbody
       (map-indexed
        (fn [i [username score]] ^{:key username}
          [:tr
           [:th (get ["🥇" "🥈" "🥉"] i (inc i))]
           [:td username]
           [:td score]])
        (get-scores))]]]))
