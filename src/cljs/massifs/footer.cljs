(ns massifs.footer
  (:require [reagent.core :refer [atom]]))

(def scores (atom nil))

; (defn max-score [scores]
;   (apply max (map :score scores)))
;
; (defn get-scores []
;   (let [grouped-scores (group-by :username (vals @scores))
;         max-scores (map (fn [[uname scores]] [uname (max-score scores)]) grouped-scores)
;         sorted-scored (sort-by second > max-scores)]
;     sorted-scored))

(defn get-scores []
  (sort-by second > @scores))

(defn footer []
  (let [fref (.. js/firebase (database) (ref "usernames"))
        _ (.. fref (on "value"
                       (fn [snap]
                         (reset! scores
                                 (js->clj (.val snap) :keywordize-keys true)))))]
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
          (get-scores))]]
       
      ;  [:footer.footer
      ;   [:div.container
      ;    [:div.content.has-text-centered
      ;     [:p
      ;      [:strong "Le Jeu des Massifs"]
      ;      " est proposé par "
      ;      [:strong [:a {:href "https://fr.booctin.com/mountain"} "Booctin'"]]]
      ;     [:p [:small "Merci aux massifs d'avoir jouer le jeu."]]]]]
       ])))
