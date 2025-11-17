#!/usr/bin/env bash
set -euo pipefail
for d in */ ; do
  if [ -f "$d/pom.xml" ]; then
    echo "Building $d"
    (cd "$d" && mvn -DskipTests package)
  fi
done
