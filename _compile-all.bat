@echo off
chcp 65001>nul
if not exist _sources.txt (
	cd knižnica
	dir /s /B *.java>..\_sources.txt
	cd ..
)
call c:\_jEdit\findJava.bat
"%javaPath%\javac.exe" -Xlint:all -J-Dfile.encoding=UTF-8 -encoding UTF-8 @_sources.txt
echo DONE.
echo Press any key.
@pause>nul