#!/usr/bin/env bash

gradle bootRepackage
cf push sunshine-forge -p ./build/libs/sunshine-forge-0.0.1-SNAPSHOT.jar
