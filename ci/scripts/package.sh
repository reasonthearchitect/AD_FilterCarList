#!/bin/sh

cd resource-filterCarList

export TERM=${TERM:-dumb}

gradle -Dorg.gradle.native=false build

ls build/libs
cp build/libs/filterCarList.jar ../resource-jar
cp Dockerfile ../resource-jar

