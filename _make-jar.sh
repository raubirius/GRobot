#!/bin/bash
echo "Creating a JAR file…"
javaPath=$(./_findJava.sh)
"$javaPath/java" -Dfile.encoding=UTF8 -jar "/Users/Shared/_jEdit/PackJar.jar" "/Users/romanhorvath/Library/CloudStorage/OneDrive-TrnavskáuniverzitavTrnave/_Sync/Vyucba/Materialy/@Robot/GRobot" "GRobot"
echo
echo "The process of JAR creation has finished."
if [ "$1" != "-nowait" ]; then
	read -n 1 -s -r -p "Press any key."
fi