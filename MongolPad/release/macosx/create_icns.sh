#!/bin/sh

sips -z 512 512 mnglpad.png --out mnglpad.iconset/icon_512x512.png
sips -z 256 256 mnglpad.png --out mnglpad.iconset/icon_256x256.png
sips -z 128 128 mnglpad.png --out mnglpad.iconset/icon_128x128.png
sips -z 64 64 mnglpad.png --out mnglpad.iconset/icon_64x64.png
sips -z 32 32 mnglpad.png --out mnglpad.iconset/icon_32x32.png
sips -z 16 16 mnglpad.png --out mnglpad.iconset/icon_16x16.png

iconutil --convert icns mnglpad.iconset

