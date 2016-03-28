(ns server-spike.core
  (:require [clojure.java.io :as io])
  (:require [clojure.data.json :as json])
  (import [java.net ServerSocket])
  (:gen-class))

(defn- receive [socket]
  ;"Read a line of text data from a socket"
  (.readLine (io/reader socket)))

(defn- send-request [socket msg]
  ;"Send the given string message out over the given socket"
  (let [writer (io/writer socket)]
    (.write writer msg)
    (.flush writer)))

(defn- send-player-option[socket]
  (println "[Server] Sending player option to client...")
  (send-request socket (str (json/write-str { "action" "Display"
                                             "1" "HvH",
                                             "2" "RvH" ,
                                             "3" "HvR"
                                             }) "\n"))
  (send-request socket (str (json/write-str {"action" "Read-CL-Input"}) "\n"))
  )

(defn- read-input[socket]
  (println "[Server] Sending read request...")
  (send-request socket (str (json/write-str {"action" "Display-Next-Move"}) "\n"))
  )

(defn- serve-persistent [port handler]
  (let [running (atom true)]
    (future
      (with-open [server-sock (ServerSocket. port)]
        (while @running
          (with-open [sock (.accept server-sock)]
            (println "[Server] serving....")
            (let [msg-in (receive sock)
                  msg-out (handler msg-in)]
              (println "[Server] Message in is: " msg-in)
              (println "[Server] Message out is: " msg-out)

              (cond
                (= msg-in "Request: Get-Player-Options") (send-player-option sock)
                (= msg-in "Request: Ask-for-input") (read-input sock)
                )
              ))))) running))

(defn -main[]
  (println "Welcome to the Server Spike!")
  (serve-persistent 8080 #(.toUpperCase %)))
