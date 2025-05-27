require u-boot-tools-xlnx.inc
require u-boot-xlnx-2025.1.inc

PROVIDES:append = " ${PN}-mkfwumdata"
PROVIDES:class-native += "u-boot-mkfwumdata-native"

# MUST clear CONFIG_VIDEO to avoid a compilation failure trying to construct
# bmp_logo.h
SED_CONFIG_EFI:append = ' -e "s/CONFIG_VIDEO=.*/# CONFIG_VIDEO is not set/"'

# Default do_compile fails with:
# | error: object directory ../downloads/git2/github.com.Xilinx.u-boot-xlnx.git/objects does not exist; check .git/objects/info/alternates.
# The regular workaround of calling 'git diff' seems to be problematic.
do_compile () {
        oe_runmake -C ${S} tools-only_defconfig O=${B}

        # Disable CONFIG_CMD_LICENSE, license.h is not used by tools and
        # generating it requires bin2header tool, which for target build
        # is built with target tools and thus cannot be executed on host.
        sed -i -e "s/CONFIG_CMD_LICENSE=.*/# CONFIG_CMD_LICENSE is not set/" ${SED_CONFIG_EFI} ${B}/.config

        # Add mkfwumdata to config - required for System Ready IR
	    sed -i "$ a CONFIG_TOOLS_MKFWUMDATA=y" ${B}/.config

        oe_runmake -C ${S} cross_tools NO_SDL=1 O=${B}
}

PACKAGES += "${PN}-mkfwumdata"

do_install:append() {
        install -m 0755 tools/mkfwumdata ${D}${bindir}/uboot-mkfwumdata
        ln -sf uboot-mkfwumdata ${D}${bindir}/mkfwumdata
}

FILES:${PN}-mkfwumdata = "${bindir}/uboot-mkfwumdata ${bindir}/mkfwumdata"
