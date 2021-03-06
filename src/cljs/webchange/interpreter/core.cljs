(ns webchange.interpreter.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require
    [re-frame.core :as re-frame]
    [webchange.subs :as subs]
    [webchange.events :as events]
    [react-spring :refer [Spring]]
    [reagent.core :as r]
    [react-konva :refer [Stage, Layer, Group, Rect]]
    [konva :refer [Tween]]
    [webchange.common.kimage :refer [kimage]]
    [webchange.common.anim :as anim]
    [cljs-http.client :as http]
    [cljs.core.async :refer [<!]]
    ["gsap/umd/TweenMax" :refer [TweenMax Power1]]
    #_[gsap/all :refer [TweenLite TweenMax BezierPlugin]]
    ))

(def default-assets [{:url "/raw/audio/background/POL-daily-special-short.mp3" :size 10 :type "audio"}
                     {:url "/raw/audio/effects/NFF-fruit-collected.mp3" :size 1 :type "audio"}
                     {:url "/raw/audio/effects/NFF-glitter.mp3", :size 1, :type "audio"}
                     {:url "/raw/audio/effects/NFF-robo-elastic.mp3" :size 1 :type "audio"}
                     {:url "/raw/audio/effects/NFF-rusted-thing.mp3" :size 1 :type "audio"}
                     {:url "/raw/audio/effects/NFF-zing.mp3", :size 1, :type "audio"}
                     {:url "/raw/img/ui/back_button_01.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/back_button_02.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/close_button_01.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/close_button_02.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/play_button_01.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/play_button_02.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/reload_button_01.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/reload_button_02.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/settings_button_01.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/settings_button_02.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/settings/music.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/settings/music_icon.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/settings/sound_fx.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/settings/sound_fx_icon.png", :size 1, :type "image"}
                     {:url "/raw/img/ui/settings/settings.png", :size 1, :type "image"}

                     {:url "/raw/anim/senoravaca/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/senoravaca/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/senoravaca/skeleton.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton2.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton3.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton4.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton5.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton6.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton7.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton8.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton9.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/senoravaca/skeleton10.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/vera/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera/skeleton.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera/skeleton2.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera/skeleton3.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/vera-go/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera-go/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera-go/skeleton.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-go/skeleton2.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-go/skeleton3.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/vera-45/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera-45/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera-45/skeleton.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-45/skeleton2.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-45/skeleton3.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-45/skeleton4.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-45/skeleton5.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-45/skeleton6.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-45/skeleton7.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/vera-90/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera-90/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/vera-90/skeleton.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/vera-90/skeleton2.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/rock/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/rock/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/rock/skeleton.png", :size 1, :type "anim-texture"}
                     {:url "/raw/anim/rock/skeleton2.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/mari/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/mari/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/mari/skeleton.png", :size 1, :type "anim-texture"}

                     {:url "/raw/anim/boxes/skeleton.atlas", :size 1, :type "anim-text"}
                     {:url "/raw/anim/boxes/skeleton.json", :size 1, :type "anim-text"}
                     {:url "/raw/anim/boxes/skeleton.png", :size 1, :type "anim-texture"}])

(def assets (atom {}))

(defn put-data
  [data key]
  (swap! assets assoc (name key) data))

(defn get-data
  [key]
  (get @assets key))

(defn has-data
  [key]
  (contains? @assets key))

(defn create-tagged-key
  [tag key]
  (str tag "_" key))

(defn new-blob
  [arraybuffer]
  (js/Blob. [arraybuffer]))

(defn get-data-as-blob [key]
  (-> key
      get-data
      new-blob))

(defn get-data-as-url
  [key]
  (let [object-url-key (create-tagged-key "object-url" key)]
    (when-not (has-data object-url-key)
      (-> key
          get-data-as-blob
          js/URL.createObjectURL
          (put-data object-url-key)))
    (get-data object-url-key)))

(def host "/api")
(def resources "")
(def http-cache (atom {}))

(defn get-url [url]
  (when (not (contains? @http-cache url))
    (let [response (http/get url {:with-credentials? false})]
      (swap! http-cache assoc url response)))
  (get @http-cache url))

(defn course-url
  [course-id]
  (str host "/courses/" course-id))

(defn get-course
  [course-id]
  (let [url (course-url course-id)]
    (get-url url)))

(defn scene-url
  [course-id scene-id]
  (str host "/courses/" course-id "/scenes/" scene-id))

(defn get-scene
  [course-id scene-id]
  (let [url (scene-url course-id scene-id)]
    (get-url url)))

(defn progress-url
  [course-id]
  (str host "/courses/" course-id "/current-progress"))

(defn get-progress
  [course-id]
  (let [url (progress-url course-id)]
    (get-url url)))

(defn lessons-url
  [course-id]
  (str host "/courses/" course-id "/lesson-sets"))

(defn get-lessons
  [course-id]
  (let [url (lessons-url course-id)]
    (get-url url)))

(defn get-total-size
  [assets]
  (->> assets
       (map :size)
       (reduce +)))

(defn load-base-asset
  [asset progress]
  (go (let [response (<! (http/get (str resources (:url asset)) {:response-type :array-buffer :with-credentials? false}))]
        (put-data (:body response) (:url asset))
        (swap! progress + (:size asset)))))

(defn load-asset
  ([asset]
   (let [progress (atom 0)]
     (load-asset asset progress)))
  ([asset progress]
    (case (-> asset :type keyword)
    :anim-text (anim/load-anim-text asset progress)
    :anim-texture (anim/load-anim-texture asset progress)
    (load-base-asset asset progress))))

(defn load-assets
  [assets on-asset-progress on-asset-complete]
  (let [total (get-total-size assets)
        current-progress (atom 0)]
    (add-watch current-progress :inc
               (fn [_ _ _ n]
                 (on-asset-progress (Math/round (* n (/ 100 total))))
                 (if (>= n total)
                   (on-asset-complete))))
    (if (> total 0)
      (doseq [asset assets]
        (load-asset asset current-progress))
      (on-asset-complete))))

(defn load-course
  [course-id cb]
  (go (let [course-response (<! (get-course course-id))
            course (:body course-response)]
        (cb course))))

(defn load-scene
  [course-id scene-id cb]
  (go (let [scene-response (<! (get-scene course-id scene-id))
            scene (:body scene-response)]
        (load-assets (concat default-assets (:assets scene))
                     #(re-frame/dispatch [::events/set-loading-progress scene-id %])
                     #(re-frame/dispatch [::events/set-scene-loaded scene-id true]))
        (cb scene))))

(defn load-progress
  [course-id cb]
  (go (let [response (<! (get-progress course-id))
            result (-> response :body :progress :data)]
        (cb result))))

(defn load-lessons
  [course-id cb on-asset-progress on-asset-complete]
  (go (let [response (<! (get-lessons course-id))
            result (-> response :body)]
        (load-assets (:assets result) on-asset-progress on-asset-complete)
        (cb result))))

(defn length
  [cx cy x y]
  (Math/sqrt (+ (Math/pow (- cx x) 2) (Math/pow (- cy y) 2))))

(defn transition-duration
  [component to]
  (let [cx (-> component (.-attrs) (.-x))
        cy (-> component (.-attrs) (.-y))
        {:keys [x y duration speed]} to]
    (cond
      (> duration 0) duration
      (> speed 0) (/ (length cx cy x y) speed)
      :else (/ (length cx cy x y) 100))))

(defn interpolate
  [{:keys [component to on-ended]}]
  (let [duration (transition-duration @component to)
        params (-> to
                   (assoc :duration duration)
                   (assoc :onFinish on-ended))]
    (cond
      (:loop to)
        (-> params
            (assoc :node @component)
            (assoc :onFinish (fn [] (this-as t (.reset t) (.play t))))
            clj->js
            Tween.
            .play)
      (:bezier to)
        (let [layer (.getLayer @component)]
          (TweenMax.to @component (:duration to) (clj->js (-> to
                                                              #_(assoc :ease Power1.easeInOut)
                                                              (assoc :onUpdate #(.draw layer))
                                                              (assoc :onComplete on-ended)))))
      :else (.to @component (clj->js params)))
    ))

(defn collide?
  [shape1 shape2]
  (let [r1 (.getClientRect @shape1)
        r2 (.getClientRect @shape2)
        r1x (.-x r1)
        r1y (.-y r1)
        r1width (.-width r1)
        r1height (.-height r1)
        r2x (.-x r2)
        r2y (.-y r2)
        r2width (.-width r2)
        r2height (.-height r2)]
    (not (or (> r2x (+ r1x r1width))
             (< (+ r2x r2width) r1x)
             (> r2y (+ r1y r1height))
             (< (+ r2y r2height) r1y)))))

(defn animation-sequence->actions [{:keys [target track offset data] :as action}]
  (into [] (map (fn [{:keys [start end anim]}]
                  {:type "sequence-data"
                   :data [{:type "empty" :duration (* (- start offset) 1000)}
                          {:type "animation" :target target :track track :id anim}
                          {:type "empty" :duration (* (- end start) 1000)}
                          {:type "remove-animation" :target target :track track}]})
                data)))

(defn animation-sequence->audio-action [{:keys [start duration audio] :as action}]
  (if audio
    {:type "audio"
    :id audio
    :start start
    :duration duration}))