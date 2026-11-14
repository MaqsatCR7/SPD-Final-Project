#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
./compile.sh
mkdir -p out-test
javac -cp out -d out-test tests/library/LibraryTests.java
java -cp out:out-test library.LibraryTests
