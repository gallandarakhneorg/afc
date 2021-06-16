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

if [ -z "$MVN_CMD" ]
then
	export MVN_CMD=`which mvn`
fi

echo "MVN_CMD=${MVN_CMD}"
echo "JAVA_HOME=$JAVA_HOME"
echo "settings=$SETTINGSFILE"
echo "user=$ARAKHNEORG_USER"

exec "${MVN_CMD}" deploy -DskipTests -Dcheckstyle.skip=true --settings "$SETTINGSFILE"

