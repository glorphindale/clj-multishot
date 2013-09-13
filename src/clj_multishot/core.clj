(ns clj-multishot.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [ring.adapter.jetty :as jetty]))

(def downstream
  "http://localhost:8090")

(defn forward-request [req server]
  (let [{:keys [uri request-method query-string]} req
        full-url (str server uri (#(if % (str "&" %)) query-string))
        result (client/request {:method request-method
                                :url full-url})]
    result))

(defn app [req]
  (let [result (forward-request req downstream)]
    (println "Request" req)
    (println "Result" result)
    {:status 200
     :headers {"Content-Type" "text/plain"}
     :body (str (:body result))}))

(defn -main []
    (jetty/run-jetty app {:port 8080}))
