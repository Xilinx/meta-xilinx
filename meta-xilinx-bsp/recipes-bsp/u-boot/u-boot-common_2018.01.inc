HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://README;beginline=1;endline=6;md5=157ab8408beab40cd8ce1dc69f702a6c"
PE = "1"

S = "${WORKDIR}/git"

UBOOTURI ?= "git://github.com/Xilinx/u-boot-xlnx.git;protocol=https"
UBRANCH ?= ""
UBRANCHARG = "${@['nobranch=1', 'branch=${UBRANCH}'][d.getVar('UBRANCH', True) != '']}"

SRCREV = "21812b5fd359d8756d619a15b49b6079ae3f9f36"
SRC_URI = "${UBOOTURI};${UBRANCHARG} \
    file://MPC8315ERDB-enable-DHCP.patch \
"

