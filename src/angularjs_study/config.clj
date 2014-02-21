(ns angularjs-study.config)

(defn- oracle-dbspec
  ""
  [host port service user password]
  {:classname "oracle.jdbc.driver.OracleDriver"
   :subprotocol "oracle"
   :subname (str "thin:@" host ":" port ":" service)
   :user user
   :password password})

(def db-spec
  ""
  (oracle-dbspec "IPADDR.OR.HOSTNAME.HERE" "1521" "SERVICE-NAME" "USERID" "PASSWORD"))
