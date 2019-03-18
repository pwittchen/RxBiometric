#!/usr/bin/env bash

git checkout master
./gradlew dokka
git checkout gh-pages
rm -rf library/
cp -avr library/build/javadoc/* ./
git add -A
git commit -m "updating JavaDoc"
rm -rf library/build/docs
echo "JavaDoc updated"
