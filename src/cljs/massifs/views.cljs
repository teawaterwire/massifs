(ns massifs.views
  (:require [re-frame.core :as rf]
            [reagent.core :refer [atom]]))

(def ->hex {:gold "#C98910"
            :silver "#A8A8A8"
            :bronze "#965A38"
            :transparent "transparent"
            :danger "#ff3860"
            :success "#23d160"})

(defn massif-path [{:keys [path] :as massif}]
  (let [hovered? (atom false)]
    (fn []
      (let [highlighted? (= massif @(rf/subscribe [:get :massif-highlighted]))]
        [:path.pointer
         {:d path
          :on-click #(rf/dispatch [:massif-clicked massif])
          :on-mouse-enter #(reset! hovered? true)
          :on-mouse-leave #(reset! hovered? false)
          :stroke-width 4
          :fill-opacity 0.4
          :fill (get ->hex (if (true? highlighted?)
                             @(rf/subscribe [:get :highlight-type])
                             (if @hovered? :bronze :transparent)))
          :stroke (get ->hex (if @hovered? :gold :silver))}]))))

(defn main-panel []
  (if-let [massifs @(rf/subscribe [:massifs-data])]
    [:div.container

     (let [{:keys [name zone] :or {name "Jeu des Massifs" zone "⛰⛰⛰"}} @(rf/subscribe [:get :massif-to-find])
           score @(rf/subscribe [:get :score])]
       [:nav.level.mt20.is-mobile
        [:div.level-item.has-text-centered
         [:div
          [:p.heading "sablier"]
          [:p.title @(rf/subscribe [:get :time-left])]]]
        [:div.level-item.has-text-centered
         [:div
          [:p.heading zone]
          [:h1.title name]]]
        [:div.level-item.has-text-centered
         [:div
          [:p.heading "score"]
          [:p.title (+ 0 score)]]]])

     [:div.relative {:style {:width "410px" :height "577px" :margin "auto"}}
      (if-not (pos? @(rf/subscribe [:get :time-left]))
        [:div.is-overlay {:style {:background-color "rgba(0,0,0,0.5)"}}
         [:div.has-text-centered.absolute {:style {:top "50%" :transform "translateY(-50%)" :width "100%"}}
          [:button.button.is-light.is-large {:on-click #(rf/dispatch [:start])} "JOUER"]]])
      [:img.absolute.z-1
       {:src "img/alpes.png"
        :style {:outline (str "5px dashed " (:silver ->hex))}}]
      [:svg
       {:width 410 :height 577 :view-box "118 72 1338 1889"}
       [:g
        (for [{:keys [slug] :as massif} massifs] ^{:key slug}
          [massif-path massif])]]]]))
