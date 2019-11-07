####################
#!/bin/sh
####################

echo "Begin deploy snapshot ......"

mvn clean deploy -Dmaven.test.skip=true
