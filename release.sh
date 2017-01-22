#!/usr/bin/env bash

set -e

mvn release:prepare release:perform
