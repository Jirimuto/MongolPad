#!/bin/bash

mkdir -p package/linux
cp release/mnglpad/mnglpad.png package/linux

jdk=$JAVA_HOME
$jdk/bin/javapackager -version

$jdk/bin/javapackager -deploy -native rpm -srcfiles release/mnglpad/mnglpad.jar -appclass com.mnglpad.Main -name mnglpad -title "Traditional Mongolian NotePad" -vendor "Almas" -BjvmOptions=-Xms32m -BjvmOptions=-Xmx1024m -outdir deploy -outfile mnglpad -v

mv deploy/bundles/mnglpad-1.0-1.x86_64.rpm ./mnglpad.rpm


