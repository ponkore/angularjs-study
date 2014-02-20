(ns angularjs-study.handler
  (:require [compojure.core :refer [defroutes]]
            [angularjs-study.routes.home :refer [home-routes]]
            [noir.util.middleware :as middleware]
            [compojure.route :as route]
            [taoensso.timbre :as timbre]
            [com.postspectacular.rotor :as rotor]
            [selmer.parser :as parser]
            [environ.core :refer [env]]
            [angularjs-study.database :as db]))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(defn oracle-dbspec
  ""
  [host port service user password]
  {:classname "oracle.jdbc.driver.OracleDriver"
   :subprotocol "oracle"
   :subname (str "thin:@" host ":" port ":" service)
   :user user
   :password password})

(def db-config
  ""
  (oracle-dbspec "172.23.200.120" "1521" "TESTFL" "JRWZAIF3" "JRWZAIF3"))

(defn init
  "init will be called once when
   app is deployed as a servlet on
   an app server such as Tomcat
   put any initialization code here"
  []
  (timbre/set-config!
    [:appenders :rotor]
    {:min-level :info
     :enabled? true
     :async? false ; should be always false for rotor
     :max-message-per-msecs nil
     :fn rotor/append})

  (timbre/set-config!
    [:shared-appender-config :rotor]
    {:path "angularjs_study.log" :max-size (* 512 1024) :backlog 10})

  (if (env :selmer-dev) (parser/cache-off!))

  ;; TODO: initialize error handling
  (db/db-initialize! db-config)
  ;; TODO: initialize error handling

  (timbre/info "angularjs-study started successfully"))

(defn destroy
  "destroy will be called when your application
   shuts down, put any clean up code here"
  []
  ;; TODO: logging
  (db/db-terminate!)
  ;; TODO: logging

  (timbre/info "angularjs-study is shutting down..."))

(defn template-error-page [handler]
  (if (env :selmer-dev)
    (fn [request]
      (try
        (handler request)
        (catch clojure.lang.ExceptionInfo ex
          (let [{:keys [type error-template] :as data} (ex-data ex)]
            (if (= :selmer-validation-error type)
              {:status 500
               :body (parser/render error-template data)}
              (throw ex))))))
    handler))

(def app (middleware/app-handler
           ;; add your application routes here
           [home-routes app-routes]
           ;; add custom middleware here
           :middleware [template-error-page]
           ;; add access rules here
           :access-rules []
           ;; serialize/deserialize the following data formats
           ;; available formats:
           ;; :json :json-kw :yaml :yaml-kw :edn :yaml-in-html
           :formats [:json-kw :edn]))
