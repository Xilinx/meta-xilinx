# When building for petalinux, we want to enable multilib support
EXTRA_OECONF:append:xilinx-standalone:baremetal-multilib-tc = " \
        --enable-multilib \
"

# PetaLinux adds SIZE to the environment
do_install:append () {
        mkdir -p ${D}${SDKPATHNATIVE}/environment-setup.d
        echo "export SIZE=\"${TARGET_PREFIX}size\"" > ${D}${SDKPATHNATIVE}/environment-setup.d/binutils.sh
}

FILES:${PN} += "${SDKPATHNATIVE}/environment-setup.d/binutils.sh"
