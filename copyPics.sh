#!/bin/sh
#1、确定源文件中图片路径及其后缀
srcModule="/e/workspace/ASProjects/AndroidTest_AS/testmodule";
subPath="/src/main/res/";
targetModule="/e/workspace/ASProjects/AndroidTest_AS/protoui";
PICSUFFIX=".png"
#2、拷贝到目标路径下
for picName in \
	ic_key \
	ic_voice \
	ic_display \
	ic_actions \
	ic_place \
	ic_menu
do
	for pathSrc in `find $srcModule$subPath -name "$picName$PICSUFFIX"`
	do
		pathTarget=$(echo $pathSrc | sed -e s#"$srcModule"#"$targetModule"#g | sed -e s#"$picName$PICSUFFIX"##g)
		echo $pathTarget
		cp -f $pathSrc $pathTarget
	done
done