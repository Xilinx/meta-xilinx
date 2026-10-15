SUMMARY = "Firmware for ISP"
DESCRIPTION = "Firmware binaries provider for ISP"
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=cd33347a71202f7531fd3f82892a4a74"
COMPATIBLE_HOST = ".*-linux"

S = "${UNPACKDIR}"

SRC_URI = "https://edf.amd.com/sswreleases/isp-fw/2026.2/10132026/isp_fw_10132026.tar.gz"
SRC_URI[sha256sum] = "ff865fa34febaa469d24b88ab47869b9fe6c5e0b37f39d6787f4a9fdaf52cad0"

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
