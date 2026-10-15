SUMMARY = "ISP Media Server and Libraries"
DESCRIPTION = "ISP Media Server application and required shared libraries."
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=fb857fc9e8eed46509f2cce8648a05c1"
COMPATIBLE_HOST = ".*-linux"

S = "${UNPACKDIR}"

SRC_URI = "https://edf.amd.com/sswreleases/isp-media-server/2026.2/08202026/isp-media-server_08202026.tar.gz"
SRC_URI[sha256sum] = "76ddd0fa222d51ba7060b2c5d886832fec2360b5eb10ac1a55373b3e2ccb0736"

RDEPENDS:${PN} += "kernel-module-isp libdrm"

do_install() {
    install -d ${D}${libdir}
    install -d ${D}${bindir}

    install -d ${D}${datadir}

    cp -r ${UNPACKDIR}/Tuning_files ${D}${datadir}

    # Make sure necessary symlinks are created
    for fullso in *.so.*.* *.so.* *.so ; do
        soname=$(objdump -p $fullso | grep SONAME | xargs echo | cut -d ' ' -f 2)
        if [ -z "$soname" ]; then
            # Fallback if blank/undefined
            soname=${fullso}
        fi
        if [ -e ${D}${libdir}/${soname} ]; then
            # Already present
            continue
        fi
        if [ ! -e ${D}${libdir}/$fullso ]; then
            # Copy and set soname symlink (if different)
            install -m 0755 $fullso ${D}${libdir}/.
            if [ ! -e ${D}${libdir}/$soname ]; then
                ln -s $fullso ${D}${libdir}/$soname
            fi
        fi
    done

    # Install binary
    install -m 0755 *isp_media_server ${D}${bindir}/

    if [ -e ${S}/isp_bin/video_test ]; then
        install -m 0755 ${S}/isp_bin/video_test ${D}${bindir}/
    else
        bbwarn "isp-media-server: isp_bin/video_test is missing from this drop; it is optional, continuing build without it"
    fi
    if [ -e ${S}/isp_bin/vsi-ctl ]; then
        install -m 0755 ${S}/isp_bin/vsi-ctl ${D}${bindir}/
    else
        bbwarn "isp-media-server: isp_bin/vsi-ctl is missing from this drop; it is optional, continuing build without it"
    fi
}

FILES:${PN} += "${libdir}/*.so* ${bindir}/*isp_media_server ${bindir}/video_test ${bindir}/vsi-ctl ${datadir}/Tuning_files/*"

# Disable all QA checks and stripping
INSANE_SKIP:${PN} += "already-stripped ldflags"
INHIBIT_PACKAGE_STRIP = "1"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
do_populate_sysroot[noexec] = "1"

# Optional: mark package as arch-independent if safe
PACKAGE_ARCH = "${MACHINE_ARCH}"


FILES:${PN}-dev = ""


