(ns massifs.views
  (:require [re-frame.core :as rf]
            [massifs.footer :refer [footer]]
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
      (let [highlighted? (= massif @(rf/subscribe [:get :massif-highlighted]))
            revealed? (and (= massif @(rf/subscribe [:get :massif-to-find]))
                           (true? @(rf/subscribe [:get :massif-revealed?])))]
        [:path.pointer
         {:d path
          :on-click #(rf/dispatch [:massif-clicked massif])
          :on-mouse-enter #(reset! hovered? true)
          :on-mouse-leave #(reset! hovered? false)
          :stroke-width 4
          :fill-opacity (if revealed? 1 0.4)
          :fill (get ->hex (if (true? highlighted?)
                             @(rf/subscribe [:get :highlight-type])
                             (if @hovered? :bronze (if revealed? :gold :transparent))))
          :stroke (get ->hex (if @hovered? :gold :silver))}]))))

(defn nav []
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
       [:p.title (+ 0 score)]]]]))

(defn save! [username score saved?]
  (reset! saved? false)
  (.. js/firebase (database) (ref "scores/")
      (push)
      (set #js {:username username :score score})
      (then (fn [] (reset! saved? true)))))

(defn save-card []
  (let [saved? (atom nil)]
    (fn [username score]
      (when (and (some? score) (not @saved?))
        [:div.m40.has-text-left.white.p10
         [:div.field
          [:label.label "Pseudonyme d'usage"]
          [:p.control
           [:input.input {:type "text" :placeholder "La Reine des Massifs"
                          :value username
                          :on-change #(rf/dispatch [:set :username (.. % -target -value)])}]]]
         [:div.field
          [:p.control
           [:button.button.iis-outlined.is-primary
            {:class (if (false? @saved?) "is-loading")
             :disabled (empty? username) :on-click #(save! username score saved?)}
            "Écrire l'histoire"]]]]))))

(defn overlay []
  [:div.is-overlay {:style {:background-color "rgba(0,0,0,0.5)"}}
   (let [score @(rf/subscribe [:get :score])
         username @(rf/subscribe [:get :username])]
     [:div.has-text-centered.absolute {:style {:top "50%" :transform "translateY(-50%)" :width "100%"}}
      [save-card username score]
      [:button.button.is-light.is-large {:on-click #(rf/dispatch [:start])}
       (if (nil? score) "JOUER" "REJOUER")]])])

(defn main-panel []
  (if-let [massifs @(rf/subscribe [:massifs-data])]
    [:div.container.is-unselectable
     [nav]
     [:div.relative {:style {:width "410px" :height "577px" :margin "auto"}}
      (if-not (pos? @(rf/subscribe [:get :time-left]))
        [overlay])
      [:img.absolute.z-1
       {:src "img/alpes.png"
        :style {:outline (str "5px dashed " (:silver ->hex))}}]
      [:svg
       {:width 410 :height 577 :view-box "118 72 1338 1889"}
       [:g
        (for [{:keys [slug] :as massif} massifs] ^{:key slug}
          [massif-path massif])]]]
     [footer]]))
