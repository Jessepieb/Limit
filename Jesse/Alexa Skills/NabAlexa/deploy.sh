#!/usr/bin/env bash

# Build zip file to push to AWS. 
echo 'installing package...'
npm install > /dev/null
echo 'install complete...'

echo 'creating build...'
mkdir submission
zip -X -r submission/index.zip * -x build build/* *.xlsx Skills Skills/* test test/* speechAssets speechAssets/* index.zip deploy.sh > /dev/null
echo 'build complete...'

cd submission
# declare -a OTHER_ALIASES=(LIVE LIVE-1)

# GET Total number of functions
echo 'getting number of versions...'
FUNCTION_AMT=`aws lambda list-versions-by-function --function-name $FNAME | jq '.Versions|length'`
echo "Function amount: $FUNCTION_AMT"

# Update function or create a new one if not exits
echo 'updating lambda...'
aws lambda update-function-code --function-name $FNAME --zip-file fileb://index.zip | grep Version
aws lambda update-function-configuration --function-name $FNAME --environment "Variables={APP_ID=$APP_ID}"

# Publish a new version and get the new version number
version=`aws lambda publish-version --function-name $FNAME | grep Version | awk '{gsub(/[",]/, "", $2);print $2}'`
echo "Version: $version"

# Point the alias to the newly updated one
echo 'updating alias to the new version...'
UPDATE=`aws lambda update-alias --function-name $FNAME --name $ALIAS --function-version $version`
echo $UPDATE

echo 'deleting old versions...'
if [ $FUNCTION_AMT -gt 5 ]
then
  START_VERSION=`aws lambda list-versions-by-function --function-name $FNAME | jq '.Versions[1].Version' | awk '{gsub(/[",]/, "", $1);print $1}'`
  echo $START_VERSION
  echo $FUNCTION_AMT
  for ((i=$START_VERSION; i < $START_VERSION + $FUNCTION_AMT - 5; ++i))
  do
  	aws lambda delete-function --function-name $FNAME --qualifier $i
  done
fi

cd ..