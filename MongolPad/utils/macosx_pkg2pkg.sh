#!/bin/bash

mkdir -p package/macosx
cp release/macosx/mnglpad.icns package/macosx

jdk=$(/usr/libexec/java_home)
$jdk/bin/javafxpackager -version

$jdk/bin/javafxpackager -deploy -native image -srcfiles release/mnglpad/mnglpad.jar -appclass com.mnglpad.Main -name mnglpad -title "Traditional Mongolian NotePad" -vendor "Almas" -BjvmOptions=-Xms32m -BjvmOptions=-Xmx1024m -outdir deploy -outfile mnglpad -v

mkdir -p deploy/bundles/mnglpad/sample/
cp release/mnglpad/sample/*.* deploy/bundles/mnglpad/sample/
cp release/mnglpad/mnglpad.sh deploy/bundles/mnglpad/
cp release/mnglpad/mnglpad.jar deploy/bundles/mnglpad/
cp release/mnglpad/*.txt deploy/bundles/mnglpad/

mkdir -p deploy/bundles/mnglpad/keyboardjp
cp release/mnglpad/macosx/keyboard/keyboardjp/*.* deploy/bundles/mnglpad/keyboardjp/
mkdir -p deploy/bundles/mnglpad/keyboardus
cp release/mnglpad/macosx/keyboard/keyboardus/*.* deploy/bundles/mnglpad/keyboardus/

cd deploy/bundles/

tar zcf mnglpad_macosx.tar.gz *

mv mnglpad_macosx.tar.gz ../../

rm -rf mnglpad.app
rm -rf mnglpad

cd ../..


