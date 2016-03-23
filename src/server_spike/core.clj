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

(defn- send-player-option[socket]
  (println "[Server] Sending player option to client...")
  (send-request socket "Display: Please enter a player choice(1) HvH (2) RvH (3) HvR\n")
  (send-request socket "Read: CL-Input\n")
  )

(defn- read-input[socket]
  (println "[Server] Sending read request...")
  (send-request socket "Display: Please enter your next move\n")
  )

(defn- to-number [input]
  (Integer/parseInt input))

(defn- validate-input-is-numeric [input]
  (try
    (to-number input)
    true
    (catch Exception e
      false
      )
    ))

(defn- validate-input[input socket]
  (println "[Server] Validating input...")
  (if (validate-input-is-numeric (subs input 10))
    (send-request socket "Display: Please enter your next move\n")
    (send-request socket "Display: Please re-enter an input\n"))
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
      ;          (.contains msg-in "Validate:") (validate-input msg-in)

                )
              ))))) running))

(defn -main[]
  (println "Welcome to the Server Spike!")
  (serve-persistent 8080 #(.toUpperCase %)))
