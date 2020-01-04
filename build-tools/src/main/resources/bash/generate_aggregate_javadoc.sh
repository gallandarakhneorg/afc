#!/usr/bin/env bash

CDIR=`dirname "$0"`

MODULE_DEFS='!build-tools,!maven/maventools,!maven/tag-replacer,!core/testtools,!core/maths/mathgeomtesting,!core/maths/math,!advanced/slf4j/slf4j-backwardcompat,!advanced/slf4j/slf4j-maven,!p2/p2-repository'

if [ -z "$JAVA_HOME" ]
then
	JAVA_HOME=`$CDIR/java_home`
fi

export JAVA_HOME
echo "JAVA_HOME=$JAVA_HOME"

exec mvn -Dmaven.test.skip=true -Dcheckstyle.skip=true clean generate-sources org.arakhne.afc.maven:tag-replacer:generatereplacesrc compile javadoc:aggregate -pl "$MODULE_DEFS" "$@"
