(ns massifs.views
  (:require [re-frame.core :as rf]
            [reagent.core :refer [atom]]))

(def ->hex {:gold "#C98910"
            :silver "#A8A8A8"
            :bronze "#965A38"
            ; :blue-grey "#eceff1"
            ; :blue-grey "#929292"
            :blue-grey "transparent"
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
          :stroke-width 3
          :fill-opacity 0.4
          ; :fill (if (true? highlighted?) (get ->hex @(rf/subscribe [:get :highlight-type])) "transparent")
          :fill (get ->hex (if (true? highlighted?)
                             @(rf/subscribe [:get :highlight-type])
                             (if @hovered? :bronze :blue-grey)))
          :stroke (get ->hex (if @hovered? :gold :silver))}]))))

(defn main-panel []
  (if-let [massifs @(rf/subscribe [:massifs-data])]
    [:div.has-text-centered

     (if-let [score @(rf/subscribe [:get :score])]
       [:div.box.absolute.r0.m40
        [:h1.title.is1 "Score : " [:strong score]]
        [:progress.progress {:value @(rf/subscribe [:get :time-left]) :max "30"}]]
       ;  [:section.hero.is-bold.is-dark.absolute.l0
      ;   [:div.hero-body
      ;    [:div.container
      ;     [:h1.title.is1 "Score : " [:strong score]]
      ;     [:progress.progress {:value @(rf/subscribe [:get :time-left]) :max "30"}]]]]

       )

     (if-not (pos? @(rf/subscribe [:get :time-left]))
       [:div.is-overlay.z1
        [:section.hero.is-fullheight {:style {:background "rgba(50, 115, 220, 0.8)"}}
         [:div.hero-body
          [:div.container
           [:button.button.is-primary.is-large
            {:on-click #(rf/dispatch [:start])}
            "JOUER"]]]]]
       (let [{:keys [name zone]} @(rf/subscribe [:get :massif-to-find])]
         [:div.box.absolute.l0.is-unselectable.m40
          [:h1.title.is-1 name]
          [:h2.subtitle zone]]
         ;  [:section.hero.is-bold.is-primary.absolute.r0
        ;   [:div.hero-body
        ;    [:div.container.is-unselectable
        ;     [:h1.title name]
        ;     [:h2.subtitle zone]]]]
         ))


     [:div.relative.m40
      [:img.absolute.z-1 {:src "img/alpes.png"
                          :style {:outline (str "5px dashed " (:silver ->hex))}}]
      [:svg
       ; {:width 294 :height 410 :view-box "361 20 147 205"}
       ; {:width 876 :height 917 :view-box "580 72 876 917"}
       ; {:width 1338 :height 1889 :view-box "118 72 1338 1889"}
      ;  {:width 400 :height 700 :view-box "118 72 1338 1889"}
       {:width 410 :height 577 :view-box "118 72 1338 1889"}
       ; {:width 410 :height 575 :view-box "118 72 1338 1889" :background-image "url(/img/alpes.png)"}
       [:g
        (for [{:keys [slug] :as massif} massifs] ^{:key slug}
          [massif-path massif])]]]
     ]))
