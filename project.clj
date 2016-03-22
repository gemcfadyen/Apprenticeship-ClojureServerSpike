(defproject server-spike "0.1.0-SNAPSHOT"
  :description "Spike for a client/server pair"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0-RC2"]]
  :profiles {:dev {:dependencies [[speclj "3.3.1"]]}}
  :plugins [[speclj "3.3.1"]]
  :main ^:skip-aot server-spike.core
  :test-paths ["spec"])
