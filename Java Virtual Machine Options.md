Java Virtual Machine Options
============================

Full list available for hotspot on [Oracle's website](http://docs.oracle.com/javase/8/docs/technotes/tools/unix/java.html)

    time java -d64 -server            \
              -cp .                   \
              Scheduler --times 5 --test
    

    time java -d64 -server                   \
              -XX:+PrintCompilation          \
              -XX:+PrintGCTimeStamps         \
              -Xmx8G                         \
              -cp .                          \
              Kruksal --times 5 --test       \
                      --times 1 --hamFile clustering_big.txt

    time java -d64 -server                   \
              -Xmx8G                         \
              -XX:+PrintGCTimeStamps         \
              -cp .                          \
              Kruksal --times 5 --test       \
                      --times 1 --hamFile clustering_big.txt


