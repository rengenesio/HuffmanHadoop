#!/bin/bash

hdfs dfs -rm -r /files/$1.dir
hadoop jar huffmanhadoop.jar $1
