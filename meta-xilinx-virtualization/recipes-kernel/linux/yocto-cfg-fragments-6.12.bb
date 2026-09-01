HOMEPAGE = "https://git.yoctoproject.org/cgit/cgit.cgi/yocto-kernel-cache/"
SUMMARY = "Kernel configuration fragments"
DESCRIPTION = "Typically used as part of a kernel clone, this is the standalone \
fragment repository. Making it available to other fragment management schemes \
"
SECTION = "devel"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
INHIBIT_DEFAULT_DEPS = "1"

LINUX_VERSION ?= "6.12"
PV = "v${LINUX_VERSION}+git${SRCREV}"

SRCREV = "5d9c6c5b0531161f9e8e9d108740ebcec9177398"
SRC_URI = "\
        git://git.yoctoproject.org/yocto-kernel-cache;branch=yocto-${LINUX_VERSION} \
        "

do_install() {
    install -d ${D}${base_prefix}/kcfg

    # copy the configuration fragments over to the native deploy
    cp -r ${S}/* ${D}${base_prefix}/kcfg
    # scripts bring in a bash dependency we don't want
    rm -rf ${D}${base_prefix}/kcfg/scripts
}

FILES:${PN} += "kcfg/"
SYSROOT_DIRS += "${base_prefix}/kcfg"
BBCLASSEXTEND = "native nativesdk"

