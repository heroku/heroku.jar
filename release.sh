#!/usr/bin/env bash

set -e

unset HEROKU_TEST_USERS

./mvnw ${MAVEN_CUSTOM_OPTS:-} release:clean release:prepare -DdryRun

./mvnw ${MAVEN_CUSTOM_OPTS:-} release:clean release:prepare

./mvnw ${MAVEN_CUSTOM_OPTS:-} release:perform
