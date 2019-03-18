#!/usr/bin/env bash

git checkout master
./gradlew dokka
git checkout gh-pages
cp -avr library/build/javadoc/* ./
git add -A
git commit -m "updating JavaDoc"
rm -rf library/build/javadocs
echo "JavaDoc updated"
