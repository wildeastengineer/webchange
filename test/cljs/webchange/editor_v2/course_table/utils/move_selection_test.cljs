(ns webchange.editor-v2.course-table.utils.move-selection-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.editor-v2.course-table.utils.move-selection :refer [update-selection]]))

(def columns (->> [:level :lesson :idx :concepts :activity :skills]
                  (map (fn [id] {:id id}))))

(def default-selection {:level      1
                        :lesson     1
                        :lesson-idx 0
                        :activity   "cinema"
                        :field      :skills})

(deftest test-move-selection--left
  (let [direction :left]
    (testing "move from middle position"
      (let [data {:columns   columns
                  :direction direction
                  :selection (merge default-selection
                                    {:field :concepts})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:field :idx})]
        (is (= actual-result expected-result))))
    (testing "move from left position"
      (let [data {:columns   columns
                  :direction direction
                  :selection (merge default-selection
                                    {:field :level})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:field :level})]
        (is (= actual-result expected-result))))))

(deftest test-move-selection--right
  (let [direction :right]
    (testing "move from middle position"
      (let [data {:columns   columns
                  :direction direction
                  :selection (merge default-selection
                                    {:field :concepts})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:field :activity})]
        (is (= actual-result expected-result))))
    (testing "move from right position"
      (let [data {:columns   columns
                  :direction direction
                  :selection (merge default-selection
                                    {:field :skills})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:field :skills})]
        (is (= actual-result expected-result))))))
