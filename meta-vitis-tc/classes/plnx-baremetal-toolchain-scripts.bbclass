#
# Copyright OpenEmbedded Contributors
#
# SPDX-License-Identifier: MIT
#
# Based on the version in oe-core Langdale
#
# This is optimized specifically for baremetal builds where we have a single
# common toolchain for each multilib.  This configuration is unique to
# petalinux prebuilt toolchains and thus not applicable upstream.

# This function creates an environment-setup-script for use in a deployable SDK
toolchain_create_sdk_env_script:xilinx-standalone () {
	# Create environment setup script.  Remember that $SDKTARGETSYSROOT should
	# only be expanded on the target at runtime.
	base_sbindir=${10:-${base_sbindir_nativesdk}}
	base_bindir=${9:-${base_bindir_nativesdk}}
	sbindir=${8:-${sbindir_nativesdk}}
	sdkpathnative=${7:-${SDKPATHNATIVE}}
	prefix=${6:-${prefix_nativesdk}}
	bindir=${5:-${bindir_nativesdk}}
	libdir=${4:-${libdir}}
	sysroot=${3:-${SDKTARGETSYSROOT}}
	multimach_target_sys=${2:-${REAL_MULTIMACH_TARGET_SYS}}
	script=${1:-${SDK_OUTPUT}/${SDKPATH}/environment-setup-$multimach_target_sys}
	rm -f $script
	touch $script

	echo '# Check for LD_LIBRARY_PATH being set, which can break SDK and generally is a bad practice' >> $script
	echo '# http://tldp.org/HOWTO/Program-Library-HOWTO/shared-libraries.html#AEN80' >> $script
	echo '# http://xahlee.info/UnixResource_dir/_/ldpath.html' >> $script
	echo '# Only disable this check if you are absolutely know what you are doing!' >> $script
	echo 'if [ ! -z "${LD_LIBRARY_PATH:-}" ]; then' >> $script
	echo "    echo \"Your environment is misconfigured, you probably need to 'unset LD_LIBRARY_PATH'\"" >> $script
	echo "    echo \"but please check why this was set in the first place and that it's safe to unset.\"" >> $script
	echo '    echo "The SDK will not operate correctly in most cases when LD_LIBRARY_PATH is set."' >> $script
	echo '    echo "For more references see:"' >> $script
	echo '    echo "  http://tldp.org/HOWTO/Program-Library-HOWTO/shared-libraries.html#AEN80"' >> $script
	echo '    echo "  http://xahlee.info/UnixResource_dir/_/ldpath.html"' >> $script
	echo '    return 1' >> $script
	echo 'fi' >> $script

	echo "${EXPORT_SDK_PS1}" >> $script
	echo 'export SDKTARGETSYSROOT='"$sysroot" >> $script
	EXTRAPATH=""
	for i in ${CANADIANEXTRAOS}; do
		EXTRAPATH="$EXTRAPATH:$sdkpathnative$bindir/${ORIGINAL_TARGET_ARCH}${ORIGINAL_TARGET_VENDOR}-$i"
	done
	echo "export PATH=$sdkpathnative$bindir:$sdkpathnative$sbindir:$sdkpathnative$base_bindir:$sdkpathnative$base_sbindir:$sdkpathnative$bindir/../${HOST_SYS}/bin:$sdkpathnative$bindir/${ORIGINAL_TARGET_SYS}"$EXTRAPATH':$PATH' >> $script
	echo 'export PKG_CONFIG_SYSROOT_DIR=$SDKTARGETSYSROOT' >> $script
	echo 'export PKG_CONFIG_PATH=$SDKTARGETSYSROOT'"$libdir"'/pkgconfig:$SDKTARGETSYSROOT'"$prefix"'/share/pkgconfig' >> $script
	echo 'export CONFIG_SITE=${SDKPATH}/site-config-'"${multimach_target_sys}" >> $script
	echo "export OECORE_NATIVE_SYSROOT=\"$sdkpathnative\"" >> $script
	echo 'export OECORE_TARGET_SYSROOT="$SDKTARGETSYSROOT"' >> $script
	echo "export OECORE_ACLOCAL_OPTS=\"-I $sdkpathnative/usr/share/aclocal\"" >> $script
	echo 'export OECORE_BASELIB="${baselib}"' >> $script
	echo 'export OECORE_TARGET_ARCH="${TARGET_ARCH}"' >>$script
	echo 'export OECORE_TARGET_OS="${TARGET_OS}"' >>$script

	echo 'unset command_not_found_handle' >> $script

	toolchain_shared_env_script
}

toolchain_shared_env_script:xilinx-standalone () {
	echo 'export CC="${ORIGINAL_TARGET_PREFIX}gcc ${TARGET_CC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
	echo 'export CXX="${ORIGINAL_TARGET_PREFIX}g++ ${TARGET_CC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
	echo 'export CPP="${ORIGINAL_TARGET_PREFIX}gcc -E ${TARGET_CC_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
	echo 'export AS="${ORIGINAL_TARGET_PREFIX}as ${TARGET_AS_ARCH}"' >> $script
	echo 'export LD="${ORIGINAL_TARGET_PREFIX}ld ${TARGET_LD_ARCH} --sysroot=$SDKTARGETSYSROOT"' >> $script
	echo 'export GDB=${ORIGINAL_TARGET_PREFIX}gdb' >> $script
	echo 'export STRIP=${ORIGINAL_TARGET_PREFIX}strip' >> $script
	echo 'export RANLIB=${ORIGINAL_TARGET_PREFIX}ranlib' >> $script
	echo 'export OBJCOPY=${ORIGINAL_TARGET_PREFIX}objcopy' >> $script
	echo 'export OBJDUMP=${ORIGINAL_TARGET_PREFIX}objdump' >> $script
	echo 'export READELF=${ORIGINAL_TARGET_PREFIX}readelf' >> $script
	echo 'export AR=${ORIGINAL_TARGET_PREFIX}ar' >> $script
	echo 'export NM=${ORIGINAL_TARGET_PREFIX}nm' >> $script
	echo 'export M4=m4' >> $script
	echo 'export TARGET_PREFIX=${ORIGINAL_TARGET_PREFIX}' >> $script
	echo 'export CONFIGURE_FLAGS="--target=${TARGET_SYS} --host=${TARGET_SYS} --build=${SDK_ARCH}-linux --with-libtool-sysroot=$SDKTARGETSYSROOT"' >> $script
	echo 'export CFLAGS="${TARGET_CFLAGS}"' >> $script
	echo 'export CXXFLAGS="${TARGET_CXXFLAGS}"' >> $script
	echo 'export LDFLAGS="${TARGET_LDFLAGS}"' >> $script
	echo 'export CPPFLAGS="${TARGET_CPPFLAGS}"' >> $script
	echo 'export KCFLAGS="--sysroot=$SDKTARGETSYSROOT"' >> $script
	echo 'export OECORE_DISTRO_VERSION="${DISTRO_VERSION}"' >> $script
	echo 'export OECORE_SDK_VERSION="${SDK_VERSION}"' >> $script
	echo 'export ARCH=${ARCH}' >> $script
	echo 'export CROSS_COMPILE=${ORIGINAL_TARGET_PREFIX}' >> $script
	echo 'export OECORE_TUNE_CCARGS="${TUNE_CCARGS}"' >> $script

    cat >> $script <<EOF

# Append environment subscripts
if [ -d "\$OECORE_TARGET_SYSROOT/environment-setup.d" ]; then
    for envfile in \$OECORE_TARGET_SYSROOT/environment-setup.d/*.sh; do
	    . \$envfile
    done
fi
if [ -d "\$OECORE_NATIVE_SYSROOT/environment-setup.d" ]; then
    for envfile in \$OECORE_NATIVE_SYSROOT/environment-setup.d/*.sh; do
	    . \$envfile
    done
fi
EOF
}

#####
# Following is copied from meta-mingw/classes/toolchain-scripts-mingw32.bbclass
# Based off of the oe-core meta/classes/toolchain-scripts.bbclass version
toolchain_create_sdk_env_script:sdkmingw32:xilinx-standalone () {
	# Create environment setup script
	sdkpathnative=${7:-${SDKPATHNATIVE}}
	prefix=${6:-${prefix_nativesdk}}
	bindir=${5:-${bindir_nativesdk}}
	libdir=${4:-${libdir}}
	sysroot=${3:-${SDKTARGETSYSROOT}}
	multimach_target_sys=${2:-${REAL_MULTIMACH_TARGET_SYS}}
	script=${1:-${SDK_OUTPUT}/${SDKPATH}/environment-setup-$multimach_target_sys}.bat
	rm -f $script
	touch $script
	# Be sure to use the 'short' path, so we can have deeper directories.
	echo 'set SDKROOT=%~sdp0%' >> $script

	# Convert to mingw32 subpaths
	sysroot='%SDKROOT%'${sysroot##${SDKPATH}}
	sdkpathnative='%SDKROOT%'${sdkpathnative##${SDKPATH}}

	echo 'set SDKTARGETSYSROOT='"$sysroot" >> $script
	EXTRAPATH=""
	for i in ${CANADIANEXTRAOS}; do
		EXTRAPATH="$EXTRAPATH;$sdkpathnative$bindir/${ORIGINAL_TARGET_ARCH}${ORIGINAL_TARGET_VENDOR}-$i"
	done
	echo "set PATH=$sdkpathnative$bindir;$sdkpathnative$bindir/../${HOST_SYS}/bin;$sdkpathnative$bindir/${ORIGINAL_TARGET_SYS}"$EXTRAPATH';%PATH%' >> $script
	echo 'set PKG_CONFIG_SYSROOT_DIR=%SDKTARGETSYSROOT%' >> $script
	echo 'set PKG_CONFIG_PATH=%SDKTARGETSYSROOT%'"$libdir"'/pkgconfig' >> $script
	echo 'set CONFIG_SITE=%SDKROOT%/site-config-'"${multimach_target_sys}" >> $script
	echo "set OECORE_NATIVE_SYSROOT=$sdkpathnative" >> $script
	echo 'set OECORE_TARGET_SYSROOT=%SDKTARGETSYSROOT%' >> $script
	echo "set OECORE_ACLOCAL_OPTS=-I $sdkpathnative/usr/share/aclocal" >> $script
	echo 'set OECORE_BASELIB=${baselib}' >> $script
	echo 'set OECORE_TARGET_ARCH=${TARGET_ARCH}' >> $script
	echo 'set OECORE_TARGET_OS=${TARGET_OS}' >> $script

	toolchain_shared_env_script

	# Change unix '/' to Win32 '\'
	sed -e 's,/,\\,g' -i $script

	# set has some annoying properties:
	# 1) If it is successful %ERRORLEVEL% is unchanged (as opposed to being set
	#	 to 0 to indicate success)
	# 2) Making an assignment like "set A=" is considered an error and sets
	#	 %ERRORLEVEL% to 1.
	#
	# Practically, this means that if any of the set calls make an empty
	# assignment that error will be propagated. To prevent this, a command is
	# run to ensure that the "exit code" of this script is 0
	echo "@%COMSPEC% /C exit 0 > NUL" >> $script

	# Make the file windows friendly...
	awk 'sub("$", "\r")' $script > $script.new
	mv $script.new $script
}

toolchain_shared_env_script:sdkmingw32:xilinx-standalone () {
	echo 'set CC=${ORIGINAL_TARGET_PREFIX}gcc ${TARGET_CC_ARCH} --sysroot=%SDKTARGETSYSROOT%' >> $script
	echo 'set CXX=${ORIGINAL_TARGET_PREFIX}g++ ${TARGET_CC_ARCH} --sysroot=%SDKTARGETSYSROOT%' >> $script
	echo 'set CPP=${ORIGINAL_TARGET_PREFIX}gcc -E ${TARGET_CC_ARCH} --sysroot=%SDKTARGETSYSROOT%' >> $script
	echo 'set AS=${ORIGINAL_TARGET_PREFIX}as ${TARGET_AS_ARCH}' >> $script
	echo 'set LD=${ORIGINAL_TARGET_PREFIX}ld ${TARGET_LD_ARCH} --sysroot=%SDKTARGETSYSROOT%' >> $script
	echo 'set GDB=${ORIGINAL_TARGET_PREFIX}gdb' >> $script
	echo 'set STRIP=${ORIGINAL_TARGET_PREFIX}strip' >> $script
	echo 'set RANLIB=${ORIGINAL_TARGET_PREFIX}ranlib' >> $script
	echo 'set OBJCOPY=${ORIGINAL_TARGET_PREFIX}objcopy' >> $script
	echo 'set OBJDUMP=${ORIGINAL_TARGET_PREFIX}objdump' >> $script
	echo 'set AR=${ORIGINAL_TARGET_PREFIX}ar' >> $script
	echo 'set NM=${ORIGINAL_TARGET_PREFIX}nm' >> $script
	echo 'set M4=m4' >> $script
	echo 'set TARGET_PREFIX=${ORIGINAL_TARGET_PREFIX}' >> $script
	echo 'set CONFIGURE_FLAGS=--target=${TARGET_SYS} --host=${TARGET_SYS} --build=${SDK_ARCH}-linux --with-libtool-sysroot=%SDKTARGETSYSROOT%' >> $script
	echo 'set CFLAGS=${TARGET_CFLAGS}' >> $script
	echo 'set CXXFLAGS=${TARGET_CXXFLAGS}' >> $script
	echo 'set LDFLAGS=${TARGET_LDFLAGS}' >> $script
	echo 'set CPPFLAGS=${TARGET_CPPFLAGS}' >> $script
	echo 'set KCFLAGS=--sysroot=%SDKTARGETSYSROOT%' >> $script
	echo 'set OECORE_DISTRO_VERSION=${DISTRO_VERSION}' >> $script
	echo 'set OECORE_SDK_VERSION=${SDK_VERSION}' >> $script
	echo 'set ARCH=${ARCH}' >> $script
	echo 'set CROSS_COMPILE=${ORIGINAL_TARGET_PREFIX}' >> $script

	cat >> $script <<EOF

@REM Append environment subscripts

@IF EXIST %OECORE_TARGET_SYSROOT%\\environment-setup.d (
   FOR %%x IN (%OECORE_TARGET_SYSROOT%\\environment-setup.d\\*.bat) DO call "%%x"
)

@IF EXIST %OECORE_NATIVE_SYSROOT%\\environment-setup.d (
   FOR %%x IN (%OECORE_NATIVE_SYSROOT%\\environment-setup.d\\*.bat) DO call "%%x"
)
EOF
}
