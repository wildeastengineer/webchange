(ns webchange.editor-v2.course-table.utils.move-selection-vertically)

(defn- get-level-rows
  [level rows]
  (->> rows
       (filter (fn [row]
                 (= (:level row) level)))))

(defn- get-lesson-rows
  [lesson level rows]
  (->> rows
       (get-level-rows level)
       (filter (fn [row]
                 (= (:lesson row) lesson)))))

(defn- get-lesson-position
  [lesson level rows]
  (->> (get-level-rows level rows)
       (map-indexed vector)
       (some (fn [[idx row]]
               (and (= (:lesson row) lesson)
                    idx)))))

;(defn get-lesson-activities
;  [lesson level table-data]
;  (let [[_ lesson-data] (get-lesson lesson level table-data)]
;    ))

(defn- get-item-idx
  [item list]
  (some (fn [[idx list-item]]
          (and (= list-item item) idx))
        (map-indexed vector list)))

(defn- get-table-map
  "Return levels and lessons count info:
  {:levels-ids [2]
   :levels     {2 {:lessons-ids [1 2]
                   :lessons     {1 {:activities-count 9}
                                 2 {:activities-count 10}}}}}"
  [rows]
  (reduce (fn [result {:keys [level lesson]}]
            (-> result
                (update-in [:levels-ids] concat [level])
                (update-in [:levels-ids] distinct)
                (update-in [:levels level :lessons-ids] concat [lesson])
                (update-in [:levels level :lessons-ids] distinct)
                (update-in [:levels level :lessons lesson :activities-count] inc)))
          {}
          rows))

(defn move-selection-up
  [{:keys [selection table-data]}]
  (print ">> move-selection-top")
  (print "selection" selection)
  (let [{:keys [level lesson lesson-idx]} selection]
    (if (> lesson-idx 0)
      (update selection :lesson-idx dec)
      (let [table-map (get-table-map table-data)
            lesson-position (get-item-idx lesson (get-in table-map [:levels level :lessons-ids]))]
        (if (> lesson-position 0)
          (let [previous-lesson-id (nth (get-in table-map [:levels level :lessons-ids]) (dec lesson-position))
                previous-lesson-activities-count (dec (get-in table-map [:levels level :lessons previous-lesson-id :activities-count]))]
            (-> selection
                (assoc :lesson previous-lesson-id)
                (assoc :lesson-idx previous-lesson-activities-count)))
          selection)

        ))))
