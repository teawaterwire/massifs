(ns massifs.views
  (:require [re-frame.core :as rf]
            [reagent.core :refer [atom]]))

(def metal->hex {:gold "#C98910"
                 :silver "#A8A8A8"
                 :bronze "#965A38"})

(defn handle-massif-click [massif massif-to-find]
  (if (= massif massif-to-find)
    (rf/dispatch [:new-massif-to-find])))

(defn massif-path [{:keys [path] :as massif}]
  (let [hovered? (atom false)]
    (fn []
      [:path.pointer
       {:d path
        :on-click #(handle-massif-click massif @(rf/subscribe [:get :massif-to-find]))
        :on-mouse-enter #(reset! hovered? true)
        :on-mouse-leave #(reset! hovered? false)
        :fill (get metal->hex (if @hovered? :bronze :silver))}])))

(defn main-panel []
  (if-let [massifs @(rf/subscribe [:massifs-data])]
    [:div.has-text-centered

     (if-not (pos? @(rf/subscribe [:get :time-left]))
       [:div.is-overlay
        [:section.hero.is-fullheight {:style {:background "rgba(50, 115, 220, 0.67)"}}
         [:div.hero-body
          [:div.container
           (if-let [score @(rf/subscribe [:get :score])]
             [:h1.title.is1 "Score: " score])
           [:button.button.is-primary.is-large
            {:on-click #(rf/dispatch [:start])}
            "JOUER"]]]]]
       (let [{:keys [name zone]} @(rf/subscribe [:get :massif-to-find])]
         [:section.hero.is-bold.is-primary
          [:div.hero-body
           [:div.container
            [:h1.title name]
            [:h2.subtitle zone]]]]))

     [:svg {:width 294 :height 410 :view-box "361 20 147 205"}
      [:g
       (for [{:keys [slug] :as massif} massifs] ^{:key slug}
         [massif-path massif])]]]))
