# TIInit_11.8.32.bts is required for bluetooth support but this particular
# version is not available in the linux-firmware repository.
#
SUMMARY = "TI Bluetooth firmware files for use with Linux kernel"
SECTION = "kernel"

LICENSE = "Firmware-ti-bt"

LIC_FILES_CHKSUM = "file://LICENSE.ti-bt;md5=f39eac9f4573be5b012e8313831e72a9"

# No common license for this, so be sure to include it
NO_GENERIC_LICENSE[Firmware-ti-bt] = "LICENSE.ti-bt"

SRC_URI = "git://git.ti.com/ti-bt/service-packs.git;protocol=https;branch=master"
SRCREV = "c290f8af9e388f37e509ecb111a1b64572b7c225"

S = "${WORKDIR}/git"

inherit allarch

CLEANBROKEN = "1"

do_unpack[postfuncs] += "rename_license"

rename_license() {
	mv ${S}/LICENSE ${S}/LICENSE.ti-bt
}

do_compile() {
	:
}

do_install() {
    oe_runmake 'DEST_DIR=${D}' 'BASE_LIB_DIR=${nonarch_base_libdir}'

    # Remove files we're not packaging...
    rm -f ${D}${nonarch_base_libdir}/firmware/ti-connectivity/TIInit_7.6.15.bts \
    ${D}${nonarch_base_libdir}/firmware/ti-connectivity/TIInit_10.6.15.bts \
    ${D}${nonarch_base_libdir}/firmware/ti-connectivity/TIInit_6.7.16_bt_spec_4.1.bts \
    ${D}${nonarch_base_libdir}/firmware/ti-connectivity/TIInit_12.8.32.bts \
    ${D}${nonarch_base_libdir}/firmware/ti-connectivity/TIInit_12.10.28.bts \
    ${D}${nonarch_base_libdir}/firmware/ti-connectivity/TIInit_6.7.16_avpr_add-on.bts \
    ${D}${nonarch_base_libdir}/firmware/ti-connectivity/TIInit_6.7.16_bt_spec_4.0.bts \
    ${D}${nonarch_base_libdir}/firmware/ti-connectivity/TIInit_6.7.16_ble_add-on.bts

    for each in ${D}${nonarch_base_libdir}/firmware/ti-connectivity/* ; do
        ln -s ti-connectivity/`basename $each` ${D}${nonarch_base_libdir}/firmware/`basename $each`
    done

    cp LICENSE.ti-bt ${D}${nonarch_base_libdir}/firmware/License.ti-bt
}

#  11.8.32   =   WL180x, WL183x, WL185x PG2.1 or PG2.2, 8.32 ROM Version
PACKAGES =+ "${PN}-wl180x ${PN}-license"

# Ensure if someone installs the main one, they get the specific named package
ALLOW_EMPTY:${PN} = "1"
RDEPENDS:${PN} += "${PN}-wl180x"

FILES:${PN}-license = "\
    ${nonarch_base_libdir}/firmware/License.ti-bt \
    "

FILES:${PN}-wl180x = "\
    ${nonarch_base_libdir}/firmware/TIInit_11.8.32.bts \
    ${nonarch_base_libdir}/firmware/ti-connectivity/TIInit_11.8.32.bts \
    "

RDEPENDS:${PN}-wl180x = "\
    ${PN}-license linux-firmware-wl18xx \
    "

LICENSE:${PN}-wl180x = "Firmware-ti-bt"

INSANE_SKIP = "arch"
