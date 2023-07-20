#!/bin/bash
echo "Running the full process…"
cd /Users/romanhorvath/Library/CloudStorage/OneDrive-TrnavskáuniverzitavTrnave/_Sync/Vyucba/Materialy/@Robot/GRobot
./_clean-up.sh -nowait
./_compile-all.sh -nowait
./_make-jar.sh -nowait
./_GRobot-copy.sh -nowait
./_clean-up.sh -nowait
echo
echo "The process has finished."
if [ "$1" != "-nowait" ]; then
	read -n 1 -s -r -p "Press any key."
fi