(ns clj-multishot.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [ring.adapter.jetty :as jetty]))

(def downstream
  "http://localhost:8090")

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
  (let [result (forward-request req downstream)]
    (println "Request" req)
    (println "Result" result)
    {:status 200
     :headers (:headers result)
     :body (str (:body result))}))

(defn -main []
    (jetty/run-jetty app {:port 8080}))
