for cmd in xargs file; do
	if ! command -v $cmd > /dev/null 2>&1; then
		echo "The command '$cmd' is required by the relocation script, please install it first. Abort!"
		exit 1
	fi
done

# fix dynamic loader paths in all ELF SDK binaries
# allow symlinks to be accessed via the find command too
native_sysroot=$($SUDO_EXEC cat $env_setup_script |grep 'OECORE_NATIVE_SYSROOT='|cut -d'=' -f2|tr -d '"')
dl_path=$($SUDO_EXEC find $native_sysroot/lib/ -maxdepth 1 -name "ld-linux*")
if [ "$dl_path" = "" ] ; then
	echo "SDK could not be set up. Relocate script unable to find ld-linux.so. Abort!"
	exit 1
fi
executable_files=$($SUDO_EXEC find $native_sysroot -type f \
	\( -perm -0100 -o -perm -0010 -o -perm -0001 \) -printf "'%h/%f' ")
if [ "x$executable_files" = "x" ]; then
   echo "SDK relocate failed, could not get executalbe files"
   exit 1
fi

tdir=`mktemp -d`
if [ x$tdir = x ] ; then
   echo "SDK relocate failed, could not create a temporary directory"
   exit 1
fi
cat <<EOF >> $tdir/relocate_sdk.sh
#!/bin/sh
for py in python python2 python3
do
	PYTHON=\`which \${py} 2>/dev/null\`
	if [ \$? -eq 0 ]; then
		break;
	fi
done

if [ x\${PYTHON} = "x"  ]; then
	echo "SDK could not be relocated.  No python found."
	exit 1
fi
\${PYTHON} ${env_setup_script%/*}/relocate_sdk.py $target_sdk_dir $dl_path $executable_files
EOF

$SUDO_EXEC mv $tdir/relocate_sdk.sh ${env_setup_script%/*}/relocate_sdk.sh
$SUDO_EXEC chmod 755 ${env_setup_script%/*}/relocate_sdk.sh
rm -rf $tdir
# Run for either relocate = 1 or = 2
if [ $relocate = 1 -o $relocate = 2 ] ; then
	$SUDO_EXEC ${env_setup_script%/*}/relocate_sdk.sh
	if [ $? -ne 0 ]; then
		echo "SDK could not be set up. Relocate script failed. Abort!"
		exit 1
	fi
fi
if [ $relocate = 2 ] ; then
	tdir=`mktemp -d`
	if [ x$tdir = x ] ; then
		echo "SDK relocate failed, could not create a temporary directory"
		exit 1
	fi
	$SUDO_EXEC ${PYTHON} ${env_setup_script%/*}/relocate-wrapper.py $target_sdk_dir > $tdir/relocate.log 2>&1
	if [ $? -ne 0 ]; then
		cat $tdir/relocate.log
		echo "SDK could not be set up. Runtime-Relocate script failed. Abort!"
		rm -rf $tdir
		exit 1
	fi
	rm -rf $tdir

	for env_setup_scripts in `ls $target_sdk_dir/environment-setup-*`; do
		cat << EOF > ${env_setup_scripts}.new
if [ -n "\$BASH_SOURCE" ]; then
    THIS_SCRIPT=\$BASH_SOURCE
elif [ -n "\$ZSH_NAME" ]; then
    THIS_SCRIPT=\$0
else
    THIS_SCRIPT="\$(pwd)/$env_setup_scripts"
    if [ ! -e "\$THIS_SCRIPT" ]; then
        echo "Error: \$THIS_SCRIPT doesn't exist!" >&2
        echo "Please run this script in sdk directory." >&2
        exit 1
    fi
fi

THIS_SCRIPT=\$(realpath \${THIS_SCRIPT})
SDK_BASE_PATH=\$(dirname \${THIS_SCRIPT})
echo "Configuring environment for base path of \$SDK_BASE_PATH"

EOF
		cat ${env_setup_scripts} >> ${env_setup_scripts}.new
		$SUDO_EXEC sed -e "s:$target_sdk_dir:\${SDK_BASE_PATH}:g" -i ${env_setup_scripts}.new
		mv ${env_setup_scripts}.new ${env_setup_scripts}
	done
fi

# replace @SDKPATH@ with the new prefix in all text files: configs/scripts/etc.
# replace the host perl with SDK perl.
for replace in "$target_sdk_dir -maxdepth 1" "$native_sysroot"; do
	$SUDO_EXEC find $replace -type f
done | xargs -n100 file | grep ":.*\(ASCII\|script\|source\).*text" | \
    awk -F': ' '{printf "\"%s\"\n", $1}' | \
    grep -Fv -e "$target_sdk_dir/environment-setup-" \
             -e "$target_sdk_dir/relocate_sdk" \
             -e "$target_sdk_dir/post-relocate-setup" \
             -e "$target_sdk_dir/${0##*/}" | \
    xargs -n100 $SUDO_EXEC sed -i \
        -e "s:$SDK_BUILD_PATH:$target_sdk_dir:g" \
        -e "s:^#! */usr/bin/perl.*:#! /usr/bin/env perl:g" \
        -e "s: /usr/bin/perl: /usr/bin/env perl:g"

if [ $? -ne 0 ]; then
	echo "Failed to replace perl. Relocate script failed. Abort!"
	exit 1
fi

# change all symlinks pointing to @SDKPATH@
for l in $($SUDO_EXEC find $native_sysroot -type l); do
	$SUDO_EXEC ln -sfn $(readlink $l|$SUDO_EXEC sed -e "s:$SDK_BUILD_PATH:$target_sdk_dir:") $l
	if [ $? -ne 0 ]; then
		echo "Failed to setup symlinks. Relocate script failed. Abort!"
		exit 1
    fi
done

echo done
