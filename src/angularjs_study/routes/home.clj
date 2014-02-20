(ns angularjs-study.routes.home
  (:use compojure.core)
  (:require [angularjs-study.views.layout :as layout]
            [angularjs-study.util :as util]
            [noir.response :refer [json]]
            [clojure.java.jdbc :as j]
            [angularjs-study.database :as db]))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(def sample-data
  [{:name "Moroni", :allowance 50, :paid true},
   {:name "Tiancum", :allowance 53, :paid false},
   {:name "Jacob", :allowance 27, :paid false},
   {:name "Nephi", :allowance 29, :paid false},
   {:name "Enos", :allowance 34, :paid false},
   {:name "Ether", :allowance 42, :paid false},
   {:name "Alma", :allowance 43, :paid true},
   {:name "Jared", :allowance 21, :paid true}])

(defn sample-data2
  []
  (j/with-db-connection [db (db/get-dbspec)]
    (j/query db ["select 駅コード, 駅名称 from 駅名マスタ where 駅コード like ?||'%'" "618"] :as-arrays? true)))

;; usage:
;; (j/with-db-connection [db (db/get-dbspec)]
;;   (j/query db ["select table_name from user_tables where table_name = ?" table_name]))

(defn- make-matcher
  [pattern]
  (try
    (partial re-seq (re-pattern pattern))
    (catch java.util.regex.PatternSyntaxException e
      nil))) ;; ignore

(defn- find-data
  [matcher keywords data]
  (let [match-fn (fn [rec]
                   (->> rec
                        ((apply juxt keywords))
                        (map str)
                        (some matcher)))]
    (filter #(match-fn %) data)))

(defn- sample-records
  [request]
  (let [param (:query-params request)
        input (get param "address")
        keywords '(:name :allowance)
        all-data (sample-data2)]
    (if-let [matcher (make-matcher input)]
      (find-data matcher keywords all-data)
      []))) ;; return empty array when could not create matcher

(defn ng-sample
  [id]
  (if (and (= (count id) 2) (re-seq #"\d{2}" id))
    (layout/render (str "ng-sample-" id ".html") {:content "hoge"})
    (str "what??")))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/ng-sample/:id" [id] (ng-sample id))
  (GET "/foo" request (json (sample-records request))))
