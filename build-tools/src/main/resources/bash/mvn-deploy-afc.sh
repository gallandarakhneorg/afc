#!/usr/bin/env bash

CDIR=`dirname $0`

RCFILE="$HOME/.afcrc"

SETTINGSFILE="$CDIR/../maven/deploy-settings.xml"

echo "rc=$RCFILE"

if [ -r "$RCFILE" ]
then
	source "$RCFILE"
fi

echo "settings=$SETTINGSFILE"
echo "user=$ARAKHNEORG_USER"

exec mvn deploy -DskipTests -Dcheckstyle.skip=true --settings "$SETTINGSFILE"
