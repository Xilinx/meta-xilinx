include dpdk.inc

SRC_URI = "git://github.com/Xilinx-CNS/cns-dpdk-next-sfc.git;branch=${BRANCH};protocol=https"

BRANCH = "cdx_22.11"
SRCREV = "92339d519b50996915e7dbb5b8246b34febcba93"
S = "${WORKDIR}/git"

# kernel module is provide by dpdk-module recipe, so disable here
EXTRA_OEMESON = " \
                -Denable_kmods=false \
                -Dexamples=cdma_demo,cdx_test \
"

COMPATIBLE_MACHINE = "null"
COMPATIBLE_MACHINE:versal-net = "${MACHINE}"
COMPATIBLE_HOST:libc-musl:class-target = "null"
COMPATIBLE_HOST:linux-gnux32 = "null"

PACKAGECONFIG ??= " "
PACKAGECONFIG[afxdp] = ",,libbpf xdp-tools"
PACKAGECONFIG[libvirt] = ",,libvirt"

RDEPENDS:${PN} += "pciutils python3-core"
RDEPENDS:${PN}-examples += "bash"
DEPENDS = "numactl python3-pyelftools-native"

inherit meson pkgconfig

INSTALL_PATH = "${prefix}/share/dpdk"

do_write_config:append(){
    sed -i "/\[properties\]/a platform = \'cdx\'" ${WORKDIR}/meson.cross
}

do_install:append(){
    # remove  source files
    rm -rf ${D}/${INSTALL_PATH}/examples/*

    # Install examples
    install -m 0755 -d ${D}/${INSTALL_PATH}/examples/
    for dirname in ${B}/examples/dpdk-*
    do
        if [ ! -d ${dirname} ] && [ -x ${dirname} ]; then
            install -m 0755 ${dirname} ${D}/${INSTALL_PATH}/examples/
        fi
    done

}

PACKAGES =+ "${PN}-examples ${PN}-tools"

FILES:${PN} += " ${bindir}/dpdk-testpmd \
		 ${bindir}/dpdk-proc-info \
		 ${libdir}/*.so* \
		 ${libdir}/dpdk/pmds-22.0/*.so* \
		 "
FILES:${PN}-examples = " \
	${INSTALL_PATH}/examples/* \
	"

FILES:${PN}-tools = " \
    ${bindir}/dpdk-pdump \
    ${bindir}/dpdk-test \
    ${bindir}/dpdk-test-* \
    ${bindir}/dpdk-*.py \
    "

CVE_PRODUCT = "data_plane_development_kit"

INSANE_SKIP:${PN} = "dev-so"
