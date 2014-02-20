(ns angularjs-study.database
  (:require [clojure.java.jdbc :as j])
  (:import  [java.sql SQLException]
            [com.mchange.v2.c3p0 ComboPooledDataSource]))

(def ^{:private true} db (atom nil))

(defn- prepare-datasource
  [config]
  (let [cpds (doto (ComboPooledDataSource.)
               (.setDriverClass (:classname config))
               (.setJdbcUrl (str "jdbc:" (:subprotocol config) ":" (:subname config)))
               (.setUser (:user config))
               (.setPassword (:password config))
               (.setMaxPoolSize 1)
               (.setMinPoolSize 1)
               (.setInitialPoolSize 1))]
    {:datasource cpds}))

(defn db-initialize!
  ""
  [config]
  (reset! db (->> config
                  (prepare-datasource)
                  (j/get-connection)
                  (j/add-connection config))))

(defn db-terminate!
  ""
  []
  (try
    (when @db
      (when-let [conn (j/get-connection @db)] (.close conn)))
    (finally
      (reset! db nil))))

(defn get-dbspec
  ""
  []
  @db)
