#!/usr/bin/env bash

mkdir -p $HOME/.binaries
cd $HOME/.binaries

MAVEN_VERSION="$1"

if [ -z "$MAVEN_VERSION" ]
then
	echo "Unspecified Maven version" >&2
	exit 255
fi

if [ '!' -d "apache-maven-${MAVEN_VERSION}" ]
then
  if [ '!' -f "apache-maven-${MAVEN_VERSION}-bin.zip" ]
  then 
    wget "https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.zip" || exit 1
  fi
  echo "Installing maven ${MAVEN_VERSION}"
  unzip -qq "apache-maven-${MAVEN_VERSION}-bin.zip" || exit 1
  rm -f "apache-maven-${MAVEN_VERSION}-bin.zip"
fi

exit 0
