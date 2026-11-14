#!/usr/bin/env bash
# Build:  ./compile.sh      Run demo: ./run.sh      Menu: ./run.sh menu      Tests: ./test.sh
set -e
cd "$(dirname "$0")"
rm -rf out
mkdir -p out
javac -d out $(find src -name "*.java")
echo "Compiled OK -> out/"
