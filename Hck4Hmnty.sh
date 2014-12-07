#!/bin/bash
# This is the bash shell script that will pipe output from server to sms 


curl http://textbelt.com/text -d number=$1 -d "message=$2"

