(ns server-spike.core-spec
  (:require [speclj.core :refer :all])
  (:require [server-spike.core :refer :all])
  (:require [clojure.java.io :refer [reader writer]])
  (import [java.net Socket ConnectException]))


(defn connect-socket [port address]
  (try
    (Socket. address port)
    (catch ConnectException e
      (println "retrying socket connection")
      (connect-socket port address))))


(defn request-response [request]
  (with-open [socket (connect-socket 8080 "localhost")
              out (writer socket)
              in (reader socket)]
    (.write out request)
    (.flush out)
    (.readLine in)))

(describe "Server Spike"
          (before-all
            (-main))
          (it "connects to a client socket"
              (should= "hello-world" (request-response "Display: hello world\n"))))
