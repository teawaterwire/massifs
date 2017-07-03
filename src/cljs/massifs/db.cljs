(ns massifs.db
  (:require [cljs.spec.alpha :as s]))

(def default-db
  {:time-left 0
   :score nil
   :massif-to-find nil
   :massif-revealed? false
   :massif-highlighted nil
   :highlight-type :success
   :massifs-data nil})


(s/def ::time-left int?)
(s/def ::score (s/nilable int?))
(s/def ::slug string?)
(s/def ::name string?)
(s/def ::zone string?)
(s/def ::path string?)
(s/def ::massif (s/keys :req-un [::slug ::name ::zone ::path]))
(s/def ::massif-to-find (s/nilable ::massif))
(s/def ::massif-revealed? boolean?)
(s/def ::massif-highlighted (s/nilable ::massif))
(s/def ::highlight-type #{:success :danger})
(s/def ::massifs-data (s/coll-of ::massif))
(s/def ::db (s/keys :req-un [::time-left
                             ::score
                             ::massif-to-find
                             ::massif-revealed?
                             ::massif-highlighted
                             ::highlight-type
                             ::massifs-data]))

;; Stolen from re-frame todomvc
(defn check-and-throw
  "throw an exception if db doesn't match the spec"
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))
