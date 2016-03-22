(ns server-spike.core
  (:require [clojure.java.io :as io])
  (import [java.net ServerSocket])
  (:gen-class)
  )

(defn- receive [socket]
  ;"Read a line of text data from a socket"
  (.readLine (io/reader socket)))

(defn- send-request [socket msg]
  ;"Send the given string message out over the given socket"
  (let [writer (io/writer socket)]
    (.write writer msg)
    (.flush writer)))

(defn- serve [port handler]
  (with-open [server-sock (ServerSocket. port)
              sock (.accept server-sock)]
    (let [msg-in (receive sock)
          msg-out (handler msg-in) ]
      (println "Message in is: " msg-in)
      (println "Message out is: " msg-out)
     ; (send-request sock msg-out)

      )))

(defn- serve-persistent [port handler]
  (let [running (atom true)]
    (future
      (with-open [server-sock (ServerSocket. port)]
        (while @running
          (with-open [sock (.accept server-sock)]
            (println "serving....")
            (let [msg-in (receive sock)
                  msg-out (handler msg-in)]
              (println "Message in is: " msg-in)
              (println "Message out is: " msg-out)
              (send-request sock msg-out)))))) running))

(defn -main[]
  (println "Welcome to the Server Spike")
  (serve-persistent 8080 #(.toUpperCase %))
  )
