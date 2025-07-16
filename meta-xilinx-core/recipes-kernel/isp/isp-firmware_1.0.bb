SUMMARY = "Firmware for ISP"
DESCRIPTION = "Firmware binaries provider for ISP"
LICENSE="CLOSED"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=ae254519d1cbab1bfd89ed89f6f6268d"
COMPATIBLE_HOST = ".*-linux"

S = "${WORKDIR}"

SRC_URI = "https://petalinux.xilinx.com/sswreleases/isp-firmware/2025.1/07112025/isp_fw.tar.gz"
SRC_URI[sha256sum]="0175329d221b385a200fbb32746dc28b7ea1fa26849737e5d68a199b96b20d43"

PACKAGE_ARCH = "${MACHINE_ARCH}"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${base_libdir}/firmware
    install -Dm 0644 ${S}/isp-r52-6-firmware.elf ${D}${base_libdir}/firmware/isp-r52-6-firmware.elf
}

COMPATIBLE_MACHINE = "^$"
COMPATIBLE_MACHINE:versal-2ve-2vm = "versal-2ve-2vm"

INSANE_SKIP:${PN} += "arch"

# Inhibit warnings about files being stripped
FILES:${PN} += "${base_libdir}/firmware/isp-r52-6-firmware.elf"
