keytool -delete -alias mnglpad -keystore ..\keystore\myKeystore -storepass myPassword
keytool -delete -alias mnglpadTest -keystore ..\keystore\myKeystore -storepass myPassword

keytool -genkey -keyalg rsa -alias mnglpad -dname "cn=Jirimutu, ou=MONGOL PROJECT, o=ALMAS INC.,l=Chiyoda-ku, ST=Tokyo, c=JA" -keypass myPassword -keystore ..\keystore\myKeystore -storepass myPassword  -validity 365 

keytool -list -keystore ..\keystore\myKeystore -storepass myPassword

jarsigner -signedjar mnglpad.jar  release/mnglpad/mnglpad.jar mnglpad -keystore keystore\myKeystore -storepass myPassword
copy mnglpad.jar  release/mnglpad/mnglpad.jar

echo keytool -export -keystore keystore\myKeystore -alias mnglpad -storepass myPassword -file mnglpad.cer

echo keytool -genkey -keyalg rsa -alias mnglpadTest -dname "cn=Jirimutu, ou=MONGOL PROJECT, o=ALMAS INC.,l=Chiyoda-ku, ST=Tokyo, c=JA" -keypass myPassword -keystore keystore\myKeystore -storepass myPassword  -validity 365 
echo keytool -selfcert -alias mnglpadTest -dname "cn=Jirimutu, ou=MONGOL PROJECT, o=ALMAS INC.,l=Chiyoda-ku, ST=Tokyo, c=JA" -keypass myPassword -validity 180 -keystore keystore\myKeystore -storepass myPassword
echo keytool -printcert -file keystore\mnglpadTest.cer

