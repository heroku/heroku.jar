#!/usr/bin/env bash

set -e

unset HEROKU_TEST_USERS

rm -f */pom.xml.versionsBackup && rm -f pom.xml.versionsBackup && rm -f */pom.xml.releaseBackup && rm -f pom.xml.releaseBackup && rm -f release.properties

mvn release:prepare release:perform
