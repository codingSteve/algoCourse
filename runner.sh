#!/bin/bash

echo $0 $* 

time java -Xmx3G -XX:+UseConcMarkSweepGC \
          -server -d64                   \
          -cp .                          \
          $* 



