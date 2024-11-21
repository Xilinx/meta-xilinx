SUMMARY = "Generates boot.mcs using vivado"
DESCRIPTION = "Manages task dependencies and creation of boot.mcs for microblaze"

LICENSE = "BSD"

PROVIDES = "virtual/boot-bin"

DEPENDS = "bitstream-microblaze"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE ?= "^$"
COMPATIBLE_MACHINE:microblaze = ".*"

inherit deploy image-artifact-names

do_fetch[noexec] = "1"
do_unpack[noexec] = "1"
do_patch[noexec] = "1"

PROC ??= "kc705_i/microblaze_0"

FLASH_SIZE ??= "0x80"
FLASH_INTERFACE ??= "BPIx16"
MB_OUT_FORMAT  ??= "mcs"
BOOT_EXT = "${@d.getVar('MB_OUT_FORMAT').lower()}"

BITSTREAM_FILE ?= "${RECIPE_SYSROOT}/boot/bitstream/download.bit"
B = "${WORKDIR}/build"
WR_CFGMEM_MISC ?= "-loadbit \" up 0 ${BITSTREAM_FILE}\""

do_check_for_vivado() {
	bbnote "Checking Vivado install path"
	which "vivado" 2>/dev/null || {
		bbfatal "Vivado not found! Please add \"INHERIT += \"vivado\"\" to your local.conf"
  }
}

addtask do_check_for_vivado before do_configure

do_configure() {
    echo " write_cfgmem -force -format ${MB_OUT_FORMAT} -size ${FLASH_SIZE} -interface ${FLASH_INTERFACE} ${WR_CFGMEM_MISC} ${B}/BOOT.${BOOT_EXT} " > ${B}/write_cfgmem_boot_mcs.tcl
    if [ ! -e ${B}/write_cfgmem_boot_mcs.tcl ]; then
        bbfatal "write_cfgmem_boot_mcs.tcl creation failed. See log for details"
    fi
}


do_compile() {
    vivado -log "${B}/cfgmem_mcs.log" -jou "${B}/cfgmem_mcs.jou" -mode batch -s ${B}/write_cfgmem_boot_mcs.tcl
    if [ ! -e ${B}/BOOT.${BOOT_EXT} ]; then
        bbfatal "BOOT.${BOOT_EXT} failed. See log"
    fi
}

do_install() {
	:
}

BOOT_BASE_NAME ?= "BOOT-${MACHINE}${IMAGE_VERSION_SUFFIX}"

do_deploy() {
    #install BOOT.mcs
    if [ -e ${B}/BOOT.${BOOT_EXT} ]; then
        install -Dm 0644 ${B}/BOOT.${BOOT_EXT} ${DEPLOYDIR}/${BOOT_BASE_NAME}.${BOOT_EXT}
        ln -sf ${BOOT_BASE_NAME}.${BOOT_EXT} ${DEPLOYDIR}/BOOT-${MACHINE}.${BOOT_EXT}
    fi
}
addtask do_deploy before do_build after do_compile
