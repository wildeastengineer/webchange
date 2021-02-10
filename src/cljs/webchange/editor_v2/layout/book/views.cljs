(ns webchange.editor-v2.layout.book.views
  (:require
    [webchange.editor-v2.layout.components.activity-action.views :refer [activity-actions]]
    [webchange.editor-v2.layout.components.activity-stage.views :refer [select-stage]]
    [webchange.editor-v2.layout.components.interpreter_stage.views :refer [interpreter-stage]]
    [webchange.editor-v2.layout.components.object-selector.views :refer [object-selector]]
    [webchange.editor-v2.layout.components.share-button.views :refer [share-button]]
    [webchange.editor-v2.scene-diagram.views-diagram :refer [dialogs-diagram]]
    [webchange.editor-v2.layout.views-skeleton :refer [skeleton]]))

(defn layout
  [{:keys [course-id scene-data]}]
  [skeleton {:top-left-component  [:div
                                   [select-stage]
                                   [object-selector]
                                   [activity-actions {:course-id  course-id
                                                      :scene-data scene-data}]
                                   [share-button]]
             :top-right-component [interpreter-stage]
             :bottom-component    [dialogs-diagram {:scene-data scene-data}]}])