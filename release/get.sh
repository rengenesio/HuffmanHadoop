#!/bin/bash

rm -r ../../Files/$1.hadoopdir/*

hdfs dfs -get /files/$1.dir/codification ../../Files/$1.hadoopdir/
hdfs dfs -get /files/$1.dir/compressed/part-r-00000 ../../Files/$1.hadoopdir/compressed
hdfs dfs -get /files/$1.dir/decompressed ../../Files/$1.hadoopdir/
