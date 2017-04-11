Java Virtual Machine Options
============================

Full list available for hotspot on [Oracle's website](http://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html)

    time java -d64 -server \
              -XX:-PrintInlining \
              -XX:-PrintCompilation\
              -XX:-PrintGCTimeStamps  \
              -cp . \
              SomeClass 

    time java -d64 -server \
              -XX:+PrintInlining \
              -XX:+PrintCompilation\
              -XX:+PrintGCTimeStamps  \
              -cp . \
              SomeClass