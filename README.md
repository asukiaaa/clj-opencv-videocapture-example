# clj-opencv-videocapture-example

A Clojure project of video capturing with using opencv.
This project was forked from [clj-opencv-swing-example](https://github.com/asukiaaa/clj-opencv-swing-example).

# Usage

## Compile opencv files for java

For ubuntu17.10.

```
sudo apt install build-essential cmake openjdk-8-jdk ant
mkdir gitprojects
cd gitprojects
# clone version 3.4.0 source
git clone --branch 3.4.0 --depth 1 git@github.com:opencv/opencv.git opencv_source
cd opencv_source
mkdir build
cd build
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64/
cmake -D BUILD_SHARED_LIBS=OFF ..
make -j4
```

## Copy opencv files for java to this project

```
cd [this proejct dir]
mkdir lib
# jar file
cp ~/gitprojects/opencv_source/build/bin/opencv-340.jar lib/

# needs so file for linux
# needs dylib file for mac
# needs dll file for windows
cp ~/gitprojects/opencv_source/build/lib/libopencv_java340.so lib/ # for linux
```

For ubuntu17.10, run following command to activate stack guard.
```
sudo apt install execstack
cd [this proejct dir]
execstack -c lib/libopencv_java340.so
```

## Run
```
cd [this project dir]
lein run
```

# License

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.

# References

- [clojureでopencv javaを利用して、カメラで取得した画像に対して顔検出を行う方法](http://asukiaaa.blogspot.com/2018/01/clojureopencv-java.html)
- [Install commands example](https://github.com/milq/milq/blob/master/scripts/bash/install-opencv.sh)
- [opencv for java](http://docs.opencv.org/3.0-beta/doc/tutorials/introduction/desktop_java/java_dev_intro.html)
- [play opencv in clojure](http://keens.github.io/blog/2015/06/07/clojuredeopencv3_0totawamureru/)
- [opencv java doc](http://docs.opencv.org/java/3.1.0/)
- [opencv3.2 + clojure](http://qiita.com/woxtu/items/bf39e3d53cbf60396d2c)
- [show opencv mat on swing](http://qiita.com/JackMasaki/items/79b883ca5084d7586008)
- [swing JFrame in clojure](http://hifistar.hatenablog.com/entry/2016/04/10/175525)
- [opencv load image to GUI on java](http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/)
- [how to show imagebuffer to jframe](http://stackoverflow.com/questions/299495/how-to-add-an-image-to-a-jpanel)
- [ImShow-Java-OpenCV](https://github.com/master-atul/ImShow-Java-OpenCV/blob/master/ImShow_JCV/src/com/atul/JavaOpenCV/Imshow.java)
- [content pane remove all](http://stackoverflow.com/questions/9347076/how-to-remove-all-components-from-a-jframe-in-java)
- [How to git clone a specific tag](https://stackoverflow.com/questions/20280726/how-to-git-clone-a-specific-tag/24102558)
