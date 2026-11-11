SUMMARY = "Firmware for ISP"
DESCRIPTION = "Firmware binaries provider for ISP"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=cd33347a71202f7531fd3f82892a4a74"
COMPATIBLE_HOST = ".*-linux"

S = "${UNPACKDIR}"

SRC_URI = "https://edf.amd.com/sswreleases/isp-fw/2026.2/10252026/isp_fw_10252026.tar.gz"
SRC_URI[sha256sum] = "4624d0424016ed3664eaf79897021a2aaee98f46c0702e02decb3d55cd4eeb00"

PACKAGE_ARCH = "${MACHINE_ARCH}"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${base_libdir}/firmware
    install -Dm 0644 ${S}/isp-r52-6-firmware.elf ${D}${base_libdir}/firmware/isp-r52-6-firmware.elf
    install -Dm 0644 ${S}/isp-r52-7-firmware.elf ${D}${base_libdir}/firmware/isp-r52-7-firmware.elf
    install -Dm 0644 ${S}/isp-r52-8-firmware.elf ${D}${base_libdir}/firmware/isp-r52-8-firmware.elf
    install -Dm 0644 ${S}/isp-r52-9-firmware.elf ${D}${base_libdir}/firmware/isp-r52-9-firmware.elf
    install -Dm 0644 ${S}/Fmcconfig.bin ${D}${base_libdir}/firmware/Fmcconfig.bin
}

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal-2ve-2vm = "versal-2ve-2vm"

INSANE_SKIP:${PN} += "arch"

# Inhibit warnings about files being stripped
FILES:${PN} += "\
    ${base_libdir}/firmware/isp-r52-6-firmware.elf \
    ${base_libdir}/firmware/isp-r52-7-firmware.elf \
    ${base_libdir}/firmware/isp-r52-8-firmware.elf \
    ${base_libdir}/firmware/isp-r52-9-firmware.elf \
    ${base_libdir}/firmware/Fmcconfig.bin \
"
