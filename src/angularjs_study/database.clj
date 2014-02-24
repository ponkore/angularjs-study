(ns angularjs-study.database
  (:require [angularjs-study.config :as config])
  (:import  [com.mchange.v2.c3p0 ComboPooledDataSource]))

(defn- pool
  ""
  [spec]
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass (:classname spec))
               (.setJdbcUrl (str "jdbc:" (:subprotocol spec) ":" (:subname spec)))
               (.setUser (:user spec))
               (.setPassword (:password spec))
               (.setMaxPoolSize 10)
               (.setMinPoolSize 1)
               (.setInitialPoolSize 1))]
    {:datasource cpds}))

(def ^{:private true} pooled-db
  ""
  (delay (pool config/db-spec)))

(defn db-connection
  ""
  []
  @pooled-db)
