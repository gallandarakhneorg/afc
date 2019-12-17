#!/usr/bin/env bash

# The following lines enable to generate the API doc for a part of the Maven modules
#MODULES=(io.sarl.lang.core io.sarl.core io.sarl.util)

#SOURCE_PATH=""
#for module in "${MODULES[@]}"
#do
#	module_path="plugins/${module}/target/generated-sources/java"
#	if [ -z "${SOURCE_PATH}" ]
#	then
#		SOURCE_PATH="${module_path}"
#	else
#		SOURCE_PATH="${SOURCE_PATH}:${module_path}"
#	fi
#done
#exec mvn "$@" -Dmaven.test.skip=true -Dcheckstyle.skip=true -Dsourcepath=${SOURCE_PATH} clean generate-sources compile javadoc:aggregate

exec mvn "$@" -Dmaven.test.skip=true -Dcheckstyle.skip=true clean generate-sources compile javadoc:aggregate
