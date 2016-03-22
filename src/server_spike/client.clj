(ns server-spike.client
  (:require [clojure.java.io :as io])
  (import [java.net Socket])
  (import [java.io BufferedWriter BufferedReader])
  (:gen-class)
  )

(defn ^:private write-to-buffer
    "Write a string to a BufferedWriter, then flush it."
      [^BufferedWriter output-stream ^String string]
        (.write output-stream string)
          (.flush output-stream))

(defn write-to
    "Send a string over the socket."
      [socket message]
        (write-to-buffer (io/writer socket) message))

(defn write-line
    "Send a line over the socket."
      [socket message]
        (write-to socket (str message "\n")))

(defn- create-client[hostname port]
  (Socket. hostname port)
  (write-line "Hello Moto"))

(defn -main[]
  (println "This is the client of your ship whose calling")
  (create-client "localhost" 8080)
  )


