@Title	mnglpad - Mongolian NotePad Package
@Rem $Id: win_pkg2msi.bat,v 1.1 2020/08/07 04:19:56 jrmt Exp $
@Echo mnglpad - Mongolian NotePad Package

@Rem Set/Overwrite JAVA_HOME 
@Rem explicitly here for different versions, etc. e.g.
@Rem
@Rem SET JAVA_HOME=C:\j2sdk1.7.0_67

:CHECK_JAVA:
@if not "%JAVA_HOME%" == "" goto JAVA_HOME_OK
@Set JPKG=javafxpackager
@goto START

:JAVA_HOME_OK
@Set JAVA=%JAVA_HOME%\bin\javafxpackager

:START
mkdir package\windows
copy release\mnglpad\mnglpad.ico package\windows

@%JPKG% -version

@%JPKG% -deploy -native msi -srcfiles release/mnglpad/mnglpad.jar -appclass com.mnglpad.Main -name mnglpad -title "Traditional Mongolian NotePad" -Bicon="mnglpad.ico"  -vendor "Almas" -BjvmOptions=-Xms32m -BjvmOptions=-Xmx1024m -outdir deploy -outfile mnglpad -v

copy deploy\bundles\mnglpad-1.0.msi mnglpad.msi

del deploy\bundles\mnglpad-1.0.msi
