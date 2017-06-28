(ns massifs.footer
  (:require [reagent.core :refer [atom]]))

(defn footer []
  (let [scores (atom nil)
        fref (.. js/firebase (database) (ref "scores"))
        _ (.. fref (on "value"
                       (fn [snap]
                         (reset! scores
                                 (js->clj (.val snap) :keywordize-keys true)))))]
    (fn []
      [:div.container.m40
       [:h2.subtitle.is-2 " ⏳ Histoire, mémoire et gloire :"]
       [:table.table
        [:thead
         [:tr
          [:th "Position"]
          [:th "Pseudonyme"]
          [:th "Score"]]]
        [:tbody
         (map-indexed
          (fn [i [k {:keys [username score]}]] ^{:key k}
            [:tr
             [:td "#" (inc i)]
             [:td username]
             [:td score]])
          (sort-by (fn [[_ {:keys [score]}]] score) > @scores))]]])))
