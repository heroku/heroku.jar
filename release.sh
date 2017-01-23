#!/usr/bin/env bash

set -e

if [ -f .release.profile ]; then
 source .release.profile
else
  echo "Skipping release profile..."
  if [ -z "$HEROKU_TEST_USERS" ]; then
    echo "HEROKU_TEST_USERS is empty!"
    exit 1
  fi
fi

rm -f */pom.xml.releaseBackup
rm -f pom.xml.releaseBackup
rm -f release.properties

mvn release:prepare release:perform
