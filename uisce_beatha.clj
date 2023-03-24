(require '[babashka.pods :as pods]
         '[babashka.http-client :as http]
         '[babashka.cli :as cli]
         '[babashka.curl :as curl])

(use '[clojure.string :only [split trim]])

(pods/load-pod 'retrogradeorbit/bootleg "0.1.9")

(require '[pod.retrogradeorbit.bootleg.utils :refer [convert-to]]
         '[pod.retrogradeorbit.hickory.select :as s])


(def cli-opts
  {:categories {:alias :c
                :desc "Search by category. Leave empty to get a list of all categories. Pass in a comma separated string to search for specific categories e.g. \"rum, irish\" "}})

(def base-url "http://oregonliquorsearch.com")
(def age-verification-form-path "/servlet/WelcomeController")
(def home-path "/servlet/FrontController?view=home&action=categoriesdisplay")
(def category-base-path "/servlet/FrontController?view=browsecategoriesallsubcategories&action=select&category=")

(defn safe-url [path]
  (str base-url path))

(defn cookie []
  (-> (curl/post (safe-url age-verification-form-path)
                 {:form-params {:btnSubmit "I'm 21 or older"}
                  :follow-redirects false})
      (get-in [:headers "set-cookie"])
      (split #";")
      first))

(defn get-with-cookies
  "Wraps a get request with an age-verified cookie in the headers"
  [path]
  (curl/get (safe-url path) {:headers {"Cookie" (cookie)}}))

(defn get-page
  [path]
  (-> (get-with-cookies path)
      :body
      trim
      (convert-to :hickory)))

(defn search
  [{:keys [opts]}])

(defn categories
  [{:keys [opts]}]
  (println opts)
  (dorun
   (map println
        (->> (get-page home-path)
             (s/select (s/child
                        (s/tag :li)
                        (s/tag :a)))
             (map (fn [el] (first (:content el))))))))

(defn help
  [_]
  (println
   (str "search\n"
        (cli/format-opts {:spec cli-opts}))))

(def table
  [{:cmds ["search"] :fn categories :spec cli-opts}
   {:cmds [] :fn help}])

(cli/dispatch table *command-line-args*)

