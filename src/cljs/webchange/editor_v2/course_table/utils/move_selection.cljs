(ns webchange.editor-v2.course-table.utils.move-selection
  (:require
    [re-frame.core :as re-frame]
    [webchange.editor-v2.course-table.state.selection :as selection-state]
    [webchange.editor-v2.course-table.utils.move-selection-horizontally :refer [move-selection-left move-selection-right]]))

(defn update-selection
  [{:keys [direction selection] :as params}]
  (case direction
    :left (move-selection-left params)
    :right (move-selection-right params)
    selection))

(defn move-selection
  [direction columns]
  (let [selection @(re-frame/subscribe [::selection-state/selection])
        new-selection (update-selection {:columns   columns
                                         :direction direction
                                         :selection (:data selection)})]
    (when-not (= new-selection (:data selection))
      (re-frame/dispatch [::selection-state/set-selection (:type selection) new-selection]))))
