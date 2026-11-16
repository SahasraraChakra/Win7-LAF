#!/bin/sh
# Requires a JDK (javac + jar). Produces win7-laf.jar
set -e
rm -rf out && mkdir out
javac -d out -encoding UTF-8 $(find src -name '*.java')
mkdir -p out/aero/res && cp src/aero/res/* out/aero/res/
jar cfe win7-laf.jar aero.demo.Showcase -C out .
echo built win7-laf.jar
