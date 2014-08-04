#!/bin/sh

export HEROKU_TEST_USERS=$(openssl des3 -d -salt -in .heroku.test.users.des3 -k $HEROKU_TEST_USERS_DES_KEY)

openssl des3 -d -salt -in .gpg-private-key.asc.des -out gpg-private-key.asc -k $GPG_PRIVATE_KEY_DES_KEY
gpg --import gpg-private-key.asc > /dev/null
