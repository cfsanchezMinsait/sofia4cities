#!/bin/sh

#
# Copyright Indra Sistemas, S.A.
# 2013-2018 SPAIN
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#      http://www.apache.org/licenses/LICENSE-2.0
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
# ------------------------------------------------------------------------

buildImage()
{
	echo "Docker image generation with spotify plugin for Sofia2 module: "$1 
	mvn clean package docker:build -Dmaven.test.skip=true
}

buildConfigDB()
{
	echo "ConfigDB image generation with Docker CLI: "
	docker build -t sofia2/configdb:$1 .
}

buildSchedulerDB()
{
	echo "SchedulerDB image generation with Docker CLI: "
	docker build -t sofia2/schedulerdb:$1 .
}

buildRealTimeDB()
{
	echo "RealTimeDB image generation with Docker CLI: "
	docker build -t sofia2/realtimedb:$1 .
}

echo "##########################################################################################"
echo "#                                                                                        #"
echo "#   _____             _                                                                  #"              
echo "#  |  __ \           | |                                                                 #"            
echo "#  | |  | | ___   ___| | _____ _ __                                                      #"
echo "#  | |  | |/ _ \ / __| |/ / _ \ '__|                                                     #"
echo "#  | |__| | (_) | (__|   <  __/ |                                                        #"
echo "#  |_____/ \___/ \___|_|\_\___|_|                                                        #"                
echo "#                                                                                        #"
echo "# Sofia2 Docker Image generation                                                         #"
echo "# arg1 (opt) --> -1 if only want to create images for persistence layer                  #"
echo "#                                                                                        #"
echo "##########################################################################################"
echo "Continue? y/n: "

read confirmation

if [ "$confirmation" != "y" ]; then
	exit 1
fi

homepath=$PWD

# Only create persistence layer
if [ -z "$1" ]; then
    # Control Panel image
	cd $homepath/../modules/control-panel/
	buildImage "Control Panel"
	
	# IoTBroker image
	cd $homepath/../modules/iotbroker/sofia2-iotbroker-boot/	
	buildImage "IoT Broker"
	
	# API manager image
	cd $homepath/../modules/api-manager/	
	buildImage "API Manager"
fi

# Generates images only if they are not present in local docker registry
if [[ "$(docker images -q sofia2/configdb 2> /dev/null)" == "" ]]; then
	cd $homepath/dockerfiles/configdb
	buildConfigDB latest
fi

if [[ "$(docker images -q sofia2/schedulerdb 2> /dev/null)" == "" ]]; then
	cd $homepath/dockerfiles/schedulerdb
	buildSchedulerDB latest
fi

if [[ "$(docker images -q sofia2/realtimedb 2> /dev/null)" == "" ]]; then
	cd $homepath/dockerfiles/realtimedb
	buildRealTimeDB latest
fi

echo "Docker images successfully generated!"

exit 0