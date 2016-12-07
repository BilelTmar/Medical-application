#!/bin/bash

<<EOD
This file exists because maven sucks and is unable to perform remote deploys.
Also, the maven-plugin-glassfish does exactly *NOT* work, it's just a waste of time.

If you're more guru, please go on and do deployment "the right way".

EOD

RUN_BUILD="1"
COPY="1"

while [[ $# -gt 0 ]]; do
    key="$1"

    case "$key" in
        "--no-build")
            RUN_BUILD="0"
            ;;
        "--redeploy-only")
            RUN_BUILD="0"
            COPY="0"
            ;;
        *)
            ;;
    esac
    shift
done

if [[ $RUN_BUILD -gt 0 ]]; then
    mvn -P production -DskipTests=true clean
    mvn -T8 -P production -DskipTests=true compile
    mvn -T8 -P production -DskipTests=true install
else
    echo "Not building application again."
fi

ARCHIVE="MedicalKnowledge-1.0-SNAPSHOT.war"

if [ -x target/$ARCHIVE ]; then
    echo "Archive target/$ARCHIVE does not exist! Unable to deploy."
    return -1
fi

if [[ $COPY -gt 0 ]]; then
    scp target/$ARCHIVE prokimedo:/tmp/
    sleep 2
else
    echo "Triggering re-deploy of existing archive on server."
fi

ssh prokimedo '/opt/payara41/glassfish/bin/asadmin --port=4848 --passwordfile /tmp/asadmin.password'\
    'deploy --force=true --type war --name ProKimedO-Standards --contextroot "/standards/api"' "/tmp/$ARCHIVE"



