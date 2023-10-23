FILESEXTRAPATHS:append := ":${THISDIR}/files"

SRC_URI += "file://zcu106-xlnx-firmware-detect"

PACKAGE_ARCH:zcu106-zynqmp = "${MACHINE_ARCH}"

# ZCU106 eval board firmware detection script.
do_install:append:zcu106-zynqmp () {
	install -m 0755 ${WORKDIR}/zcu106-xlnx-firmware-detect ${D}${bindir}/xlnx-firmware-detect
}
