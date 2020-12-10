(ns webchange.editor-v2.course-table.utils.move-selection-test
  (:require
    [cljs.test :refer [deftest testing is]]
    [webchange.editor-v2.course-table.utils.move-selection :refer [update-selection]]))

(def columns (->> [:level :lesson :idx :concepts :activity :skills]
                  (map (fn [id] {:id id}))))

(def table-data [{:level 1, :lesson 1, :lesson-idx 0}
                 {:level 1, :lesson 1, :lesson-idx 1}
                 {:level 1, :lesson 1, :lesson-idx 2}

                 {:level 1, :lesson 2, :lesson-idx 0}
                 {:level 1, :lesson 2, :lesson-idx 1}
                 {:level 1, :lesson 2, :lesson-idx 2}

                 {:level 2, :lesson 3, :lesson-idx 0}
                 {:level 2, :lesson 3, :lesson-idx 1}
                 {:level 2, :lesson 3, :lesson-idx 2}

                 {:level 2, :lesson 4, :lesson-idx 0}
                 {:level 2, :lesson 4, :lesson-idx 1}
                 {:level 2, :lesson 4, :lesson-idx 2}])

(def default-selection {:level      1
                        :lesson     1
                        :lesson-idx 0
                        :field      :skills})

#_(deftest test-move-selection--left
    (let [direction :left]
      (testing "move from middle position"
        (let [data {:columns   columns
                    :direction direction
                  :table-data table-data
                    :selection (merge default-selection
                                      {:field :concepts})}
              actual-result (update-selection data)
              expected-result (merge default-selection
                                     {:field :idx})]
          (is (= actual-result expected-result))))
      (testing "move from left position"
        (let [data {:columns   columns
                    :direction direction
                  :table-data table-data
                    :selection (merge default-selection
                                      {:field :level})}
              actual-result (update-selection data)
              expected-result (merge default-selection
                                     {:field :level})]
          (is (= actual-result expected-result))))))

#_(deftest test-move-selection--right
    (let [direction :right]
      (testing "move from middle position"
        (let [data {:columns   columns
                    :direction direction
                  :table-data table-data
                    :selection (merge default-selection
                                      {:field :concepts})}
              actual-result (update-selection data)
              expected-result (merge default-selection
                                     {:field :activity})]
          (is (= actual-result expected-result))))
      (testing "move from right position"
        (let [data {:columns   columns
                    :direction direction
                  :table-data table-data
                    :selection (merge default-selection
                                      {:field :skills})}
              actual-result (update-selection data)
              expected-result (merge default-selection
                                     {:field :skills})]
          (is (= actual-result expected-result))))))

(deftest test-move-selection--uo
  (let [direction :up]
    (testing "move from middle position"
      (let [data {:columns    columns
                  :direction  direction
                  :table-data table-data
                  :selection  (merge default-selection
                                     {:level 1, :lesson 1, :lesson-idx 1})}
            actual-result (update-selection data)
            expected-result (merge default-selection
                                   {:level 1, :lesson 1, :lesson-idx 0})]
        (is (= actual-result expected-result))))

    (testing "move from 1st lesson row"
        (let [data {:columns   columns
                    :direction direction
                  :table-data table-data
                    :selection (merge default-selection
                                      {:level 2, :lesson 4, :lesson-idx 0})}
              actual-result (update-selection data)
              expected-result (merge default-selection
                                     {:level 2, :lesson 3, :lesson-idx 2})]
          (is (= actual-result expected-result))))

    (testing "move from 1st level row"
        (let [data {:columns   columns
                    :direction direction
                  :table-data table-data
                    :selection (merge default-selection
                                      {:level 2, :lesson 3, :lesson-idx 0})}
              actual-result (update-selection data)
              expected-result (merge default-selection
                                     {:level 1, :lesson 2, :lesson-idx 2})]
          (is (= actual-result expected-result))))

    (testing "move from 1st table row"
        (let [data {:columns   columns
                    :direction direction
                  :table-data table-data
                    :selection (merge default-selection
                                      {:level 1, :lesson 1, :lesson-idx 0})}
              actual-result (update-selection data)
              expected-result (merge default-selection
                                     {:level 1, :lesson 1, :lesson-idx 0})]
          (is (= actual-result expected-result))))))

#_(deftest test-move-selection--top
    (let [direction :top]
      (testing "move from middle position"
        (let [data {:columns   columns
                    :direction direction
                  :table-data table-data
                    :selection (merge default-selection
                                      {:level 1, :lesson 1, :lesson-idx 1})}
              actual-result (update-selection data)
              expected-result (merge default-selection
                                     {:level 1, :lesson 1, :lesson-idx 2})]
          (is (= actual-result expected-result))))

      (testing "move from last lesson row"
        (let [data {:columns   columns
                    :direction direction
                  :table-data table-data
                    :selection (merge default-selection
                                      {:level 1, :lesson 1, :lesson-idx 2})}
              actual-result (update-selection data)
              expected-result (merge default-selection
                                     {:level 1, :lesson 2, :lesson-idx 0})]
          (is (= actual-result expected-result))))

      (testing "move from last level row"
        (let [data {:columns   columns
                    :direction direction
                  :table-data table-data
                    :selection (merge default-selection
                                      {:level 1, :lesson 2, :lesson-idx 2})}
              actual-result (update-selection data)
              expected-result (merge default-selection
                                     {:level 2, :lesson 3, :lesson-idx 0})]
          (is (= actual-result expected-result))))

      (testing "move from last table row"
        (let [data {:columns   columns
                    :direction direction
                  :table-data table-data
                    :selection (merge default-selection
                                      {:level 2, :lesson 4, :lesson-idx 2})}
              actual-result (update-selection data)
              expected-result (merge default-selection
                                     {:level 2, :lesson 4, :lesson-idx 2})]
          (is (= actual-result expected-result))))))
