#!/bin/sh

echo " _____           _           _   ______       _                              _ "
echo "|  __ \         (_)         | | |  ____|     | |                            | |"
echo "| |__) | __ ___  _  ___  ___| |_| |__   _ __ | |__   __   _ __   ___ ___  __| |"
echo "|  ___/ '__/ _ \| |/ _ \/ __| __|  __| | '_ \| '_ \ / _ \| '_ \ / __/ _ \/ _  |"
echo "| |   | | | (_) | |  __/ (__| |_| |____| | | | | | | (_| | | | | (_|  __/ (_| |"
echo "|_|   |_|  \___/| |\___|\___|\__|______|_| |_|_| |_|\__,_|_| |_|\___\___|\__,_|"
echo "               _/ |                                                            "
echo "              |__/                                                             "
echo ""
echo "Welcome in discord bot executable generator"
echo "Linux DEB edition"
echo ""
echo "==========================================="
echo ""
echo "Information about application"
echo ""
echo "==========================================="
echo ""
echo "Enter name of application: "
read name
echo "Enter version of application: "
read version
echo "Enter description of application: "
read description
echo "Enter copyright of application: "
read copyright
echo "Enter vendor: "
read vendor
echo "Enter about URL: "
read about
echo ""
echo "==========================================="
echo ""
echo "Paths"
echo ""
echo "==========================================="
echo ""
echo "Enter directory with input JAR: "
read input
echo "Enter directory with output DEB: "
read output
echo "Enter full name of JAR file (with .jar): "
read jar
echo "Enter full name of main class (with package): "
read main
echo "Enter path to icon (.PNG): "
read icon
echo "Enter path to file with license: "
read license
echo ""
echo "==========================================="
echo ""
echo "Build"
echo ""
echo "==========================================="
echo ""
echo "Enter additional jpackage args: "
read additional
echo ""
echo "Building installer..."

jpackage --name "$name" --app-version "$version" --description "$description" --copyright "$copyright" --vendor "$vendor" --about-url "$about" --input "$input" --dest "$output" --main-jar "$jar" --main-class "$main" --icon "$icon" --license-file "$license" --type deb $additional

echo "Done!"
