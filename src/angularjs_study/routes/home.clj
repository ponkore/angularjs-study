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

(defn- sample-data
  [param]
  (j/with-db-connection [db (db/db-connection)]
    (j/query db ["select hogecode, hogename from hogemaster where hogecode like ?||'%'" param])))

(defn- sample-records
  [request]
  (let [param (:query-params request)
        input (get param "address")
        keywords '(:hogecode :hogename)
        all-data (sample-data)]
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
