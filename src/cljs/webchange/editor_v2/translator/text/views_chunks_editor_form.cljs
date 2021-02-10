(ns webchange.editor-v2.translator.text.views-chunks-editor-form
  (:require
    [cljs-react-material-ui.reagent :as ui]
    [webchange.editor-v2.translator.text.core :refer [chunks->parts parts->chunks]]
    [webchange.editor-v2.translator.text.views-text-chunks :refer [text-chunks]]))

(def text-input-params {:placeholder "Enter description text"
                        :variant     "outlined"
                        :margin      "normal"
                        :multiline   true
                        :full-width  true})

(defn- event->value
  [event]
  (.. event -target -value))

(defn chunks-editor-form
  [{:keys [text chunks on-change]}]
  (let [handle-text-change (fn [event]
                             (let [current-text (event->value event)
                                   current-parts (clojure.string/split current-text #" ")
                                   current-chunks (parts->chunks current-text current-parts)]
                               (on-change {:text   current-text
                                           :chunks current-chunks})))
        handle-parts-change (fn [event]
                              (let [current-parts-str (event->value event)
                                    original-stripped (clojure.string/replace text #" " "")
                                    value-stripped (clojure.string/replace current-parts-str #" " "")]
                                (when (= original-stripped value-stripped)
                                  (let [current-parts (clojure.string/split current-parts-str #" ")
                                        current-chunks (parts->chunks text current-parts)]
                                    (on-change {:chunks current-chunks})))))]
    (let [parts (chunks->parts text chunks)]
      [ui/grid {:container true
                :spacing   16
                :justify   "space-between"}
       [ui/grid {:item true :xs 12}
        [ui/text-field (merge text-input-params
                              {:label     "Origin"
                               :value     (or text "")
                               :on-change handle-text-change})]]
       [ui/grid {:item true :xs 12}
        [ui/text-field (merge text-input-params
                              {:label       "Parts"
                               :value       (clojure.string/join " " parts)
                               :on-change   handle-parts-change
                               :helper-text "Use space to divide text into chunks"})]]
       [ui/grid {:item true :xs 12}
        [text-chunks {:parts parts}]]])))