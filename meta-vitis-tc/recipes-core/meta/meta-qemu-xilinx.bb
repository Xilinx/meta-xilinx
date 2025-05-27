SUMMARY = "Meta package for building a installable qemu SDK"
LICENSE = "MIT"


PLNX_ADD_VAI_SDK = ""

TOOLCHAIN_TARGET_TASK = ""
TOOLCHAIN_HOST_TASK = "nativesdk-sdk-provides-dummy meta-environment-${MACHINE}"

# QEMU specific items
QEMU_HOST_TASK = "nativesdk-qemu nativesdk-qemu-xilinx nativesdk-qemu-xilinx-common nativesdk-qemu-xilinx-multiarch-helper nativesdk-qemu-helper"

# Optional bootgen to hand the '-bootbin' argument of the multiarch-helper
QEMU_HOST_TASK += "nativesdk-bootgen"

# Optional qemu-devicetrees
QEMU_HOST_TASK += "nativesdk-qemu-devicetrees"

# Tool to work on qemuboot.conf files
QEMU_HOST_TASK += "nativesdk-qemuboot-tool"

# Add wic to allow for image manipulation
QEMU_HOST_TASK += "nativesdk-wic"

# Not all helpers build or run on mingw32
QEMU_HOST_TASK:sdkmingw32 = "nativesdk-qemu-xilinx nativesdk-qemu-xilinx-common"

TOOLCHAIN_HOST_TASK += "${QEMU_HOST_TASK}"

MULTIMACH_TARGET_SYS = "${SDK_ARCH}-nativesdk${SDK_VENDOR}-${SDK_OS}"
PACKAGE_ARCH = "${SDK_ARCH}_${SDK_OS}"
PACKAGE_ARCHS = ""
TARGET_ARCH = "none"
TARGET_OS = "none"

SDK_PACKAGE_ARCHS += "buildtools-dummy-${SDKPKGSUFFIX}"

TOOLCHAIN_OUTPUTNAME ?= "${SDK_ARCH}-qemu-xilinx-nativesdk-standalone-${DISTRO_VERSION}"

SDK_TITLE = "QEMU Xilinx"

RDEPENDS = "${TOOLCHAIN_HOST_TASK}"

EXCLUDE_FROM_WORLD = "1"

inherit populate_sdk
inherit toolchain-scripts-base
inherit nopackages

deltask install
deltask populate_sysroot

do_populate_sdk[stamp-extra-info] = "${PACKAGE_ARCH}"

REAL_MULTIMACH_TARGET_SYS = "none"

create_sdk_files:append () {
        rm -f ${SDK_OUTPUT}/${SDKPATH}/site-config-*
        rm -f ${SDK_OUTPUT}/${SDKPATH}/environment-setup-*
        rm -f ${SDK_OUTPUT}/${SDKPATH}/version-*

        # Generate new (mini) sdk-environment-setup file
        script=${1:-${SDK_OUTPUT}/${SDKPATH}/environment-setup-${SDK_SYS}}
        touch $script
        echo 'export PATH=${SDKPATHNATIVE}${bindir_nativesdk}:${SDKPATHNATIVE}${base_sbindir_nativesdk}:$PATH' >> $script
        echo 'export OECORE_NATIVE_SYSROOT="${SDKPATHNATIVE}"' >> $script
        echo 'export GIT_SSL_CAINFO="${SDKPATHNATIVE}${sysconfdir}/ssl/certs/ca-certificates.crt"' >>$script
        echo 'export SSL_CERT_FILE="${SDKPATHNATIVE}${sysconfdir}/ssl/certs/ca-certificates.crt"' >>$script

        toolchain_create_sdk_version ${SDK_OUTPUT}/${SDKPATH}/version-${SDK_SYS}

        cat >> $script <<EOF
if [ -d "\$OECORE_NATIVE_SYSROOT/environment-setup.d" ]; then
        for envfile in \$OECORE_NATIVE_SYSROOT/environment-setup.d/*.sh; do
                . \$envfile
        done
fi
EOF

        if [ "${SDKMACHINE}" = "i686" ]; then
                echo 'export NO32LIBS="0"' >>$script
                echo 'echo "$BB_ENV_PASSTHROUGH_ADDITIONS" | grep -q "NO32LIBS"' >>$script
                echo '[ $? != 0 ] && export BB_ENV_PASSTHROUGH_ADDITIONS="NO32LIBS $BB_ENV_PASSTHROUGH_ADDITIONS"' >>$script
        fi
}

# buildtools-tarball doesn't need config site
TOOLCHAIN_NEED_CONFIGSITE_CACHE = ""

# The recipe doesn't need any default deps
INHIBIT_DEFAULT_DEPS = "1"

