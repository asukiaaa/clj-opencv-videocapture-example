(defproject clj-opencv-videocapture-example "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[clj-time "0.14.2"]
                 [org.clojure/clojure "1.9.0"]
                 [org.clojure/core.async "0.4.474"]]
  :jvm-opts ["-Djava.library.path=lib"]
  :resource-paths ["lib/opencv-340.jar"]
  :injections [(clojure.lang.RT/loadLibrary org.opencv.core.Core/NATIVE_LIBRARY_NAME)]
  :main clj-opencv-videocapture-example.core)
