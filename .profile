#!/bin/sh

export HEROKU_TEST_USERS=$(openssl des3 -d -salt -in .heroku.test.users.des3 -k $HEROKU_TEST_USERS_DES_KEY)
