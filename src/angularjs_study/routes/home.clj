(ns angularjs-study.routes.home
  (:use compojure.core)
  (:require [angularjs-study.views.layout :as layout]
            [angularjs-study.util :as util]
            [noir.response :refer [json]]))

(defn home-page []
  (layout/render
    "home.html" {:content (util/md->html "/md/docs.md")}))

(defn about-page []
  (layout/render "about.html"))

(defn- sample-records
  []
  [{:name "Moroni", :allowance 50, :paid true},
   {:name "Tiancum", :allowance 53, :paid false},
   {:name "Jacob", :allowance 27, :paid false},
   {:name "Nephi", :allowance 29, :paid false},
   {:name "Enos", :allowance 34, :paid false},
   {:name "Ether", :allowance 42, :paid false},
   {:name "Alma", :allowance 43, :paid true},
   {:name "Jared", :allowance 21, :paid true}])

(defn ng-sample
  [id]
  (layout/render
    "ng-sample-07.html" {:content "hoge"}))

(defroutes home-routes
  (GET "/" [] (home-page))
  (GET "/about" [] (about-page))
  (GET "/ng-sample/:id" [id] (ng-sample id))
  (GET "/test" [] (json (sample-records))))
