SUMMARY = "Device tree lops"
SECTION = "bootloader"
LICENSE = "BSD-3-Clause"
DEPENDS += "lopper-native"

INHIBIT_DEFAULT_DEPS = '1'

inherit deploy
inherit python3native

ESW_MACHINE ?= "${MACHINE}"

DTB_FILE = "${ESW_MACHINE}-system.dtb"
DTS_FILE = "${ESW_MACHINE}-baremetal.dtb"

COMPATIBLE_HOST = "^.*"

LOPS_DIR = "${RECIPE_SYSROOT_NATIVE}/usr/share/lopper/lops"

# All microblaze
do_compile_microblaze_xilinx-standalone() {
    LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f -O ${WORKDIR} ${SYSTEM_DTFILE} ${B}/${DTS_FILE}
}

# All Cortex R5
do_compile_append_armrm_xilinx-standalone() {
    LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f --enhanced -i ${LOPS_DIR}/lop-r5-imux.dts -O ${WORKDIR} ${SYSTEM_DTFILE} ${B}/${DTS_FILE}
}

# Only ZynqMP & Cortex A53 (baremetal)
do_compile_append_zynqmp_aarch64_xilinx-standalone() {
    LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f --enhanced -i ${LOPS_DIR}/lop-a53-imux.dts -O ${WORKDIR} ${SYSTEM_DTFILE} ${B}/${DTS_FILE}
}

# Only ZynqMP & Cortex A53 (Linux)
do_compile_append_zynqmp_aarch64_linux() {
    LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f --enhanced -i ${LOPS_DIR}/lop-a53-imux.dts -i ${LOPS_DIR}/lop-domain-linux-a53.dts -O ${WORKDIR} ${SYSTEM_DTFILE} ${B}/${DTB_FILE}
}

# Only Versal and Cortex A72 (baremetal)
do_compile_append_versal_aarch64_xilinx-standalone() {
    LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f --enhanced -i ${LOPS_DIR}/lop-a72-imux.dts -O ${WORKDIR} ${SYSTEM_DTFILE} ${B}/${DTS_FILE}
}

do_compile_append_versal_aarch64_linux() {
    LOPPER_DTC_FLAGS="-b 0 -@" lopper.py -f --enhanced -i ${LOPS_DIR}/lop-a72-imux.dts -i ${LOPS_DIR}/lop-domain-a72.dts -O ${WORKDIR} ${SYSTEM_DTFILE} ${B}/${DTB_FILE}
}

do_install() {
    :
}

PACKAGES = ""

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"
do_configure[noexec] = "1"
do_install[noexec] = "1"
deltask do_populate_lic
deltask do_populate_sysroot
deltask do_pacakge
deltask do_package_qa
deltask do_packagedata
deltask do_package_write_ipk
deltask do_package_write_deb
deltask do_package_write_rpm

# Linux
do_deploy() {
    install -Dm 0644 ${B}/${DTB_FILE} ${DEPLOYDIR}/${DTB_FILE}
}

# Baremetal
do_deploy_xilinx-standalone() {
    install -Dm 0644 ${B}/${DTS_FILE} ${DEPLOYDIR}/${DTS_FILE}
}
addtask deploy before do_build after do_install
