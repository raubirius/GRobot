@echo off
chcp 65001
call c:\_jEdit\findJava.bat
echo javaPath: %javaPath%
"%javaPath%\javadoc.exe" -source 1.8 -noindex -notree -nohelp -link https://docs.ORACLE.com/javase/8/docs/api/ -d doc -encoding UTF-8 -docencoding UTF-8 -notimestamp -nodeprecatedlist -nonavbar -Xdoclint:-html --allow-script-in-comments -cp ../../../GRobot knižnica.podpora