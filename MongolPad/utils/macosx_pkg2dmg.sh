#!/bin/bash

mkdir -p package/macosx
cp release/macosx/mnglpad.icns package/macosx

jdk=$(/usr/libexec/java_home)
$jdk/bin/javafxpackager -version

$jdk/bin/javafxpackager -deploy -native dmg -srcfiles release/mnglpad/mnglpad.jar -appclass com.mnglpad.Main -name mnglpad -title "Traditional Mongolian NotePad" -vendor "Almas" -BjvmOptions=-Xms32m -BjvmOptions=-Xmx1024m -outdir deploy -outfile mnglpad -v

mv deploy/bundles/mnglpad.dmg ./mnglpad.dmg

