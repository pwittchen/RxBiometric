#!/usr/bin/env bash

git checkout master
./gradlew dokka
git checkout gh-pages
rm -rf *
cp -avr library/build/docs/javadoc/* ./
git add -A
git commit -m "updating JavaDoc"
rm -rf library/build/docs
echo "JavaDoc updated"
