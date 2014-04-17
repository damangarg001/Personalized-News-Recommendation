#!/usr/bin/bash

username=$1

#javac UserTopTenTweets.java
#java UserTopTenTweets $username
cd src/SearchPart/
g++ query.cpp

./a.out ter_index.txt sec_index.txt pri_index.txt UrlFile $username
