(defproject clj-multishot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.7.6"]
                 [ring/ring-jetty-adapter "1.0.0"]]
  :plugins [[lein-ring "0.8.7"]]
  :ring {:handler clj-multishot.core/app}
  :main clj-multishot.core)
