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

To support a large recursive number of recursive function calls, you can use the `Xss` option to set a larger stack size. Default is 1024 KB.

    time java -d64 -server                     \
              -Xmx10G                          \
              -Xss1G                           \
              -XX:+PrintGCTimeStamps           \
              -cp .                            \
              Knapsack --times 1 --test --quiet\
                       --file knapsack1.txt    \
                       --rfile knapsack_big.txt


