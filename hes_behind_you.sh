#!/usr/bin/env bash

set -e

PROJECT_DIR="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
mkdir -p "$PROJECT_DIR/bin"

javac --release 8 -d "$PROJECT_DIR/bin" "$PROJECT_DIR/src/App.java"
cd "$PROJECT_DIR"
exec java -cp "$PROJECT_DIR/bin" App