@Title	mnglpad - Mongolian NotePad %1%
@Rem $Id: mnglpad.bat,v 1.10 2017/08/18 22:50:37 jrmt Exp $
@Echo mnglpad - Mongolian NotePad

:CHECK_JAVA:
@Rem if not "%JAVA_HOME%" == "" goto JAVA_HOME_OK
@Set JAVA=windows\jre9mngl\bin\javaw
@goto START

:JAVA_HOME_OK
@Rem Ignore the JAVA_HOME environment variable
@Set JAVA=%JAVA_HOME%\bin\javaw

:START
@Set CLASSPATH=mnglpad.jar
@start %JAVA% -Xms128m -Xmx2048m -classpath %CLASSPATH% com.mnglpad.Main %1

@exit
