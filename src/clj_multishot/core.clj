(ns clj-multishot.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [ring.adapter.jetty :as jetty]))

(def downstream
  "http://localhost:8090")

(defn app [req]
  (println (str req) (:method req))
  (let [{:keys [uri request-method]} req
        result (client/request {:method request-method
                                :url (str downstream uri)})]
    (println "Request" req)
    (println "Result" result)
    {:status 200
    :headers {"Content-Type" "text/plain"}
    :body (str (:body result))}))

(defn -main []
    (jetty/run-jetty app {:port 8080}))
