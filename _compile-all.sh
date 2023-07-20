#!/bin/bash
if [ ! -f "_sources.txt" ]; then
	cd knižnica
	# find . -name "*.java" -type f > ../_sources.txt
	# find . -name "*.java" -type f | sed 's|^\./||' > ../_sources.txt
	while IFS= read -r -d '' file
	do
		echo "$(cd "$(dirname "$file")"; pwd -P)/$(basename "$file")"
	done < <(find . -name "*.java" -type f -print0) > ../_sources.txt
	cd ..
fi
echo "Compiling all files…"
# Ak je potrebné, upravte cestu k javac:
# javaPath="/Library/Java/JavaVirtualMachines/jdk-1.8.jdk/Contents/Home/bin"
javaPath=$(./_findJava.sh)
# javaPath=$(/full/path/to/_findJava.sh)
"$javaPath/javac" -Xlint:all -J-Dfile.encoding=UTF-8 -encoding UTF-8 @_sources.txt
echo
echo "The compilation process has finished."
if [ "$1" != "-nowait" ]; then
	read -n 1 -s -r -p "Press any key."
fi