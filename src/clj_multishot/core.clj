(ns clj-multishot.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [ring.adapter.jetty :as jetty]))

(def downstreams
  ["http://localhost:8090" "http://localhost:8091"])

(defn forward-request [req server]
  (let [{:keys [uri request-method headers query-string body]} req
        full-url (str server
                      uri
                      (#(if % (str "&" %)) query-string))
        result (client/request {:method request-method
                                :url full-url
                                :headers (dissoc headers "content-length")
                                :body body})]
    result))

(defn app [req]
  (println "Received request")
  (doseq [downstream (rest downstreams)]
    (println "Sending to" downstream)
    (future (forward-request req downstream)))

  (let [result (forward-request req (first downstreams))]
    {:status 200
     :headers (:headers result)
     :body (:body result)}))

(defn -main []
    (jetty/run-jetty app {:port 8080}))
