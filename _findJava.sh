#!/bin/bash
javaPath=$(which javac)
if [ -z "$javaPath" ]; then
	echo "Javac not found. Please install it or check your PATH variable."
	exit 1
fi
javaDir=$(dirname "$javaPath")
echo $javaDir