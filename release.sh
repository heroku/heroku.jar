#!/usr/bin/env bash

set -e

rm -f */pom.xml.releaseBackup
rm -f pom.xml.releaseBackup
rm -f release.properties

mvn release:prepare release:perform
