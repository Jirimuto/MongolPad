#!/bin/sh
#
#$Id: mnglpad.sh,v 1.10 2017/08/18 16:44:54 jrmt Exp $
echo mnglpad - Mongolian NotePad $JAVA_HOME

# set directory to overwrite 
JAVA_HOME=./linux/jre9mngl

## Check Java Home
if [ $JAVA_HOME ]; then
  JAVA=$JAVA_HOME/bin/java
else
  JAVA=./linux/jre9mngl/bin/java
fi

if [ ! -e $JAVA ];then
  echo "Java not exists..............."
  tar xzvf linux/jre9mngl.tar.gz -C linux
fi


CLASSPATH=mnglpad.jar

$JAVA -Xms128m -Xmx2048m -classpath $CLASSPATH com.mnglpad.Main &

