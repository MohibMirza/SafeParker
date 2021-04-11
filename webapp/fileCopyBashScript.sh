#!/bin/bash
# A script to copy a json file between directories.

rm ./static/crimeReport.json
cp ../backend/crimeReport.json ./static/

echo "File Copy Done"
