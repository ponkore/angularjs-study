(defproject angularjs-study "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [lib-noir "0.8.3"]
                 [compojure "1.1.8"]
                 [ring-server "0.3.1"]
                 [selmer "0.6.6"]
                 [com.taoensso/timbre "3.2.1"]
                 [com.postspectacular/rotor "0.1.0"]
                 [com.taoensso/tower "2.0.2"]
                 [markdown-clj "0.9.44"]
                 [environ "0.4.0"]
                 [org.clojure/java.jdbc "0.3.3"]
                 [c3p0/c3p0 "0.9.1.2"]
                 [c3p0/c3p0-oracle-thin-extras "0.9.0.2"]
                 [com.oracle/ojdbc6 "11.2.0.4"]]
  :repl-options {:init-ns angularjs-study.repl}
  :plugins [[lein-ring "0.8.10"]
            [lein-environ "0.4.0"]]
  :ring {:handler angularjs-study.handler/app
         :init    angularjs-study.handler/init
         :destroy angularjs-study.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production {:ring {:open-browser? false
                       :stacktraces?  false
                       :auto-reload?  false}}
   :dev {:dependencies [[ring-mock "0.1.5"]
                        [ring/ring-devel "1.2.2"]]
         :env {:selmer-dev true}}}
  :min-lein-version "2.0.0")
