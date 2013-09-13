(ns clj-multishot.core
  (:gen-class)
  (:require [clj-http.client :as client]
            [ring.adapter.jetty :as jetty]
            [clojure.string :as string]))

(def downstreams-list)

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
    (doseq [downstream (rest downstreams-list)]
      (future (forward-request req downstream)))
    (forward-request req (first downstreams-list))))

(defn parse-downstreams [raw]
  (map #(str "http://" %) (string/split raw #",")))

(defn -main [& {port "-port"
                downstreams "-downstreams"
                :or {port 8080
                     downstreams "localhost:8090"}}]
  (println "Use '-port 8080' option to specify port and '-downstreams server1:port,server2:port' to specify downstream servers")
  (println "Running on port" port "with downstreams:" downstreams)
  (with-redefs [downstreams-list (parse-downstreams downstreams)]
    (jetty/run-jetty app {:port (Integer. port)})))
