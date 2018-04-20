#!/usr/bin/env bash

set -e

unset HEROKU_TEST_USERS

./mvnw release:clean release:prepare -DdryRun

./mvnw release:clean release:prepare

./mvnw release:perform
