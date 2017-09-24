(ns clj-opencv-videocapture-example.core
  (:import [java.awt.image BufferedImage]
           [javax.swing JFrame JLabel ImageIcon WindowConstants SwingUtilities]
           [org.opencv.core Core Mat CvType MatOfRect Point Scalar]
           [org.opencv.imgcodecs Imgcodecs]
           [org.opencv.imgproc Imgproc]
           [org.opencv.objdetect CascadeClassifier]
           [org.opencv.videoio VideoCapture])
  (:require [clojure.core.async :as async]
            [clj-time.core :as t]
            [clj-time.local :as l]))

(def j-frame (new JFrame))
(def mat-frame (new Mat))
(def cv-camera (new VideoCapture 0))
(def face-detector (new CascadeClassifier "resources/lbpcascade_frontalface.xml"))
(def face-detections (new MatOfRect))

(defn mat->buffered-image [mat]
  (let [gray? (= (.channels mat) 1)
        type (if gray?
               BufferedImage/TYPE_BYTE_GRAY
               BufferedImage/TYPE_3BYTE_BGR)
        image (new BufferedImage (.width mat) (.height mat) type)
        target-pixels (-> image
                          (.getRaster)
                          (.getDataBuffer)
                          (.getData))
        raster (.getRaster image)
        buffer (byte-array (* (.width mat) (.height mat) (if gray? 1 3)))]
    (.get mat 0 0 buffer)
    (System/arraycopy buffer 0 target-pixels 0 (alength buffer))
    image))

(defn config-j-frame [j-frame & {:keys [title set-visible close-operation]}]
  (when title
    (.setTitle j-frame title))
  (when-not (nil? set-visible)
    (.setVisible j-frame set-visible))
  (when close-operation
    (.setDefaultCloseOperation j-frame close-operation)))

(defn show-mat [j-frame mat]
  (-> j-frame
      (.getContentPane)
      (.removeAll))
  (.setSize j-frame (.width mat) (.height mat))
  (.add j-frame (->> (mat->buffered-image mat)
                   (new ImageIcon)
                   (new JLabel)))
  (.revalidate j-frame)
  (.repaint j-frame))

(defn check-faces [mat-frame]
  (.detectMultiScale face-detector mat-frame face-detections)
  #_(prn :face-count (.size (.toList face-detections)))
  (doall
   (for [rect (.toArray face-detections)]
     (Imgproc/rectangle mat-frame
                        (new Point (.-x rect) (.-y rect))
                        (new Point (+ (.-x rect) (.-width rect)) (+ (.-y rect) (.-height rect)))
                        (new Scalar 0, 255, 0)))))

(defn show-fps [mat-frame fps]
  (Imgproc/putText mat-frame
                   (str fps "fps")
                   (new Point 10 40)
                   Core/FONT_HERSHEY_SIMPLEX
                   1                  ; Scale
                   (new Scalar 0 0 0) ; Color
                   4))                ; Thickness

(defn one-line-log [fps]
  (print "\r" (format "%3dfps" fps))
  (flush))

(defn -main []
  (prn :start-main)
  (config-j-frame j-frame
                  :title "captured camera image on opencv in clojure"
                  :set-visible true
                  :close-operation WindowConstants/EXIT_ON_CLOSE)
  (if (.isOpened cv-camera)
    (loop [times-in-sec []]
      (let [one-sec-ago (t/minus (l/local-now) (t/seconds 1))
            times-in-sec (->> (conj times-in-sec (l/local-now))
                              (filter #(t/before? one-sec-ago %)))
            fps (count times-in-sec)]
        (one-line-log fps)
        (.read cv-camera mat-frame)
        (check-faces mat-frame)
        (show-fps mat-frame fps)
        (show-mat j-frame mat-frame)
        #_(Imgcodecs/imwrite "capture.jpg" mat-frame)
        (recur times-in-sec)))
    (prn :cannot-open-camera)))
