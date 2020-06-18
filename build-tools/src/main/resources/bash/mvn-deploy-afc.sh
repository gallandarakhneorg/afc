#!/usr/bin/env bash

CDIR=`dirname "$0"`

if [ -z "$JAVA_HOME" ]
then
	JAVA_HOME=`$CDIR/java_home`
fi

export JAVA_HOME

RCFILE="$HOME/.afcrc"

SETTINGSFILE="$CDIR/../maven/deploy-settings.xml"

echo "rc=$RCFILE"

if [ -r "$RCFILE" ]
then
	source "$RCFILE"
fi

if [ -z "$MVN" ]
then
	MVN=mvn
fi

echo "JAVA_HOME=$JAVA_HOME"
echo "settings=$SETTINGSFILE"
echo "user=$ARAKHNEORG_USER"
echo "MVN=$mvn"

exec $MVN deploy -DskipTests -Dcheckstyle.skip=true --settings "$SETTINGSFILE"
