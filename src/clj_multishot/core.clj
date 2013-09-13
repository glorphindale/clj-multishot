(ns clj-multishot.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [ring.adapter.jetty :as jetty]))

(def downstreams
  ["http://localhost:8090" "http://localhost:8091"])

(defn forward-request
  "take a Ring-compatible request map, forward it to the specified server
  and return the response"
  [req server]
  (let [{:keys [uri request-method headers query-string body]} req
        full-url (str server
                      uri
                      (#(if % (str "?" %)) query-string))]
    (client/request {:method request-method
                     :url full-url
                     :headers (dissoc headers "content-length")
                     :body body})))

(defn app [inc-req]
  (let [req (assoc inc-req :body (slurp (:body inc-req)))]
    (println "Received request" (:uri req) (:query-string req))
    (doseq [downstream (rest downstreams)]
      (future (forward-request req downstream)))
    (forward-request req (first downstreams))))

(defn -main []
    (jetty/run-jetty app {:port 8080}))
