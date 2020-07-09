#!/bin/sh

if test $TRAVIS_PULL_REQUEST == "false"; then
    user1="$CI_TEST_USER1_NAME"
    token1="$CI_TEST_USER1_TOKEN"

    user2="$CI_TEST_USER2_NAME"
    token2="$CI_TEST_USER2_TOKEN"

    export HEROKU_TEST_USERS=\[\{\"username\":\"${user1}\",\"apikey\":\"${token1}\",\"defaultuser\":\"true\"\},\{\"username\":\"${user2}\",\"apikey\":\"${token2}\",\"defaultuser\":\"false\"\}\]
fi
