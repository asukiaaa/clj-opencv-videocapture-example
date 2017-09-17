(ns clj-opencv-swing-example.core
  (:import [java.awt.image BufferedImage]
           [javax.swing JFrame JLabel ImageIcon WindowConstants SwingUtilities]
           [org.opencv.core Core Mat CvType]
           [org.opencv.imgcodecs Imgcodecs]
           [org.opencv.imgproc Imgproc]
           [org.opencv.videoio VideoCapture])
  (:require [clojure.core.async :as async]))

(def j-frame (new JFrame))
(def mat-frame (new Mat))
(def cv-camera (new VideoCapture 0))

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

(defn show-mat [j-frame mat & {:keys [title set-visible close-operation]}]
  (when title
    (.setTitle j-frame title))
  (when-not (nil? set-visible)
    (.setVisible j-frame set-visible))
  (-> j-frame
      (.getContentPane)
      (.removeAll))
  (.setSize j-frame (.width mat) (.height mat))
  (.add j-frame (->> (mat->buffered-image mat)
                   (new ImageIcon)
                   (new JLabel)))
  (.revalidate j-frame)
  (.repaint j-frame)
  (when close-operation
    (.setDefaultCloseOperation j-frame close-operation)))

(defn -main []
  (prn :start-main)
  (if (.isOpened cv-camera)
    (loop [take 0]
      (.read cv-camera mat-frame)
      #_(Imgcodecs/imwrite "capture.jpg" mat-frame)
      (if (= (.getTitle j-frame) "")
        (show-mat j-frame mat-frame
                  :title "captured camera image on opencv in clojure"
                  :set-visible true
                  :close-operation WindowConstants/EXIT_ON_CLOSE)
        (show-mat j-frame mat-frame))
      (recur (inc take))))
  (prn :cannot-open-camera))
