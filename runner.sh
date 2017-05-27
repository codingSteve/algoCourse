#!/bin/bash

echo $0 $* 
date

#-XX:+UseConcMarkSweepGC 
time java -Xmx3G -XX:+UseG1GC \
          -server -d64                   \
          -cp .                          \
          $* 



