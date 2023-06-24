@echo off
@chcp 65001>nul
echo Creating a JAR file…
call c:\_jEdit\findJava.bat
"%javaPath%\java" -Dfile.encoding=UTF8 -jar "c:\_jEdit\PackJar.jar" "C:\_Sync\Vyucba\Materialy\@Robot\GRobot" "GRobot"
echo.
echo The process of JAR creation has finished.
if not "-nowait" == "%1" (
	echo Press any key.
	@pause>nul
)