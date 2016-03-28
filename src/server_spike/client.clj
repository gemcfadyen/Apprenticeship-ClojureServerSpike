(ns client-spike.core
  (:require [clojure.java.io :as io])
  (import [java.net Socket])
  (import [java.io BufferedWriter BufferedReader])
  (:gen-class)
  )

  ;"Write a string to a BufferedWriter, then flush it."
(defn- write-to-buffer [^BufferedWriter output-stream ^String message]
  (println "writing message " message)
  (.write output-stream message)
  (.flush output-stream))

(defn- write-to [socket message]
  ;"Send a string over the socket."
  (println "Writing " message " to buffer")
  (write-to-buffer (io/writer socket) message))

(defn- write-line [socket message]
  ;"Send a line over the socket."
  (println "writing line " message)
  (write-to socket (str message "\n")))

(defn- create-client[socket request]
  (println "writing " request)
  (write-line socket request))

(defn- receive [socket]
  (.readLine (io/reader socket)))

(defn- loop-receive [socket]
            (let [running (atom true)]
              (println "waiting for msg...")
             (while @running
               (let[msg-rec (receive socket)]
                 (println "Received from server " msg-rec)
                 ))))

(defn -main[]
  (println "Welcome to Tic Tac Toe")
    (create-client (Socket. "localhost" 8080) "Get-Player-Options")
    (create-client (Socket. "localhost" 8080) "Ask-for-input")
    (loop-receive (Socket. "localhost" 8080) )
    )
