#!/usr/bin/env bash
cd "$(dirname "$0")"
[ -d out ] || ./compile.sh
java -cp out library.Main "$@"
