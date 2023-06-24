@echo off
@chcp 65001>nul
echo Running the full process…
cd c:\_Sync\Vyucba\Materialy\@Robot\GRobot
call _clean-up.bat -nowait
call _compile-all.bat -nowait
call _make-jar.bat -nowait
call _GRobot-copy.bat -nowait
call _clean-up.bat -nowait
echo.
echo The process has finished.
if not "-nowait" == "%1" (
	echo Press any key.
	@pause>nul
)