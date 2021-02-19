(ns webchange.templates.library.flipbook.remove-page
  (:require
    [webchange.templates.library.flipbook.page-number :refer [update-pages-numbers]]
    [webchange.templates.library.flipbook.stages :refer [update-stages]]
    [webchange.templates.library.flipbook.utils :refer [get-page-data]]))

(defn- remove-from-list
  [list index]
  (-> (concat (subvec list 0 index)
              (subvec list (inc index)))
      (vec)))

(defn- remove-page-from-book
  [activity-data book-name page-number]
  (update-in activity-data [:objects book-name :pages] remove-from-list page-number))

(defn- remove-object
  [activity-data object-name]
  (let [object-data (get-in activity-data [:objects object-name])
        updated-activity-data (update-in activity-data [:objects] dissoc object-name)]
    (case (:type object-data)
      "group" (->> (:children object-data)
                   (map keyword)
                   (reduce remove-object updated-activity-data))
      updated-activity-data)))

(defn- remove-action
  [activity-data action-name]
  (update-in activity-data [:actions] dissoc action-name))

(defn remove-page
  [activity-data {:keys [page-side stage]} page-params]
  (let [{:keys [book-name page-data page-number]} (get-page-data activity-data stage page-side)]
    (if (:removable? page-data)
      (-> activity-data
          (remove-object (keyword (:object page-data)))
          (remove-action (keyword (:action page-data)))
          (remove-page-from-book book-name page-number)
          (update-stages {:book-name book-name})
          (update-pages-numbers page-params))
      activity-data)))