####################
#!/bin/sh
####################

#version=$1

#if [ ! -n "$version" ]; then
    #echo -e "Please input deploy version: \c"
	#read version
#fi

echo "Begin deploy release ..."

#mvn versions:set -pl ./ -DnewVersion=$version

mvn versions:set -pl ./ -DremoveSnapshot=true
mvn clean deploy -Dmaven.test.skip=true
mvn versions:revert

#mvn versions:set -pl ./ -DnewVersion=$version-SNAPSHOT
#mvn versions:commit

echo "end"
