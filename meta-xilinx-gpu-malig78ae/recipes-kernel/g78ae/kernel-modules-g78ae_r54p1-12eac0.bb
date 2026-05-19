SUMMARY = "A Mali G78AE Linux Kernel modules"
DESCRIPTION = "Out-of-tree Linux kernel module for the Arm Mali-G78AE \
GPU as found on AMD Versal Series Gen 2 devices."
SECTION = "kernel/modules"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = " \
	file://${S}/kbase/license.txt;md5=13e14ae1bd7ad5bff731bba4a31bb510 \
	"
# Arbiter license file
# driver/product/kernel/drivers/gpu/arm/arbitration/license_gplv2.txt
# md5sum: 13e14ae1bd7ad5bff731bba4a31bb510

BRANCH = "r54p1-12eac0"
SRCREV_kernel = "379addce312d566720edc878ebf7292774a6e27f"
SRCREV_arbitration = "8c5448552b84e5a7ecb13046ad02a31cb673aeeb"
SRCREV_FORMAT = "kernel_arbitration"

SRC_URI = " \
        git://github.com/Xilinx/malig78ae-kbase.git;protocol=https;branch=${BRANCH};name=kernel;destsuffix=git/kbase \
        git://github.com/Xilinx/malig78ae-arbitration.git;protocol=https;branch=${BRANCH};name=arbitration;destsuffix=git/arbiter \
        file://compiler.py \
        file://load-mali-modules.sh \
        file://99-mali-modules.rules \
        file://mali_kbase.conf \
        "

inherit features_check module python3native

S = "${WORKDIR}/git"

REQUIRED_MACHINE_FEATURES = "malig78ae"

BUILD_SCRIPT = "${WORKDIR}/compiler.py"
BUILD_CMD = "${STAGING_BINDIR_NATIVE}/python3-native/python3 ${BUILD_SCRIPT} --driver ${S}/kbase --debug"

INSTALL_DIR = "${nonarch_base_libdir}/modules/${KERNEL_VERSION}"

do_compile() {
	${BUILD_CMD} \
		--make "${MAKE} ${EXTRA_OEMAKE}" \
		--kernel "${STAGING_KERNEL_BUILDDIR}" \
		--arch ${ARCH} \
		--cross-compile "${TARGET_PREFIX}" \
		--target-prefix "${TARGET_DBGSRC_DIR}"
}

do_install() {
	${BUILD_CMD} \
		--install "${D}${INSTALL_DIR}"

        # Install the script to load modules and configure settings
        install -d ${D}/usr/bin
        install -m 0755 ${WORKDIR}/load-mali-modules.sh ${D}/usr/bin

        # Install the udev rules file to /etc/udev/rules.d/
        install -d ${D}/etc/udev/rules.d
        install -m 0644 ${WORKDIR}/99-mali-modules.rules ${D}/etc/udev/rules.d

        # Install the mali_kbase conf
        install -d ${D}/etc/modprobe.d/
        install -m 0644 ${WORKDIR}/mali_kbase.conf ${D}/etc/modprobe.d/
}


FILES:${PN} = "\
	${INSTALL_DIR}/kernel-module-dma-buf-test-exporter.ko \
	${INSTALL_DIR}/kernel-module-kutf.ko \
	${INSTALL_DIR}/kernel-module-mali-arbiter.ko \
	${INSTALL_DIR}/kernel-module-mali-emu-kbase.ko \
	${INSTALL_DIR}/kernel-module-mali-gpu-assign.ko \
	${INSTALL_DIR}/kernel-module-mali-gpu-aw.ko \
	${INSTALL_DIR}/kernel-module-mali-gpu-partition-config.ko \
	${INSTALL_DIR}/kernel-module-mali-gpu-partition-control.ko \
	${INSTALL_DIR}/kernel-module-mali-gpu-power.ko \
	${INSTALL_DIR}/kernel-module-mali-gpu-resource-group.ko \
	${INSTALL_DIR}/kernel-module-mali-gpu-system.ko \
	${INSTALL_DIR}/kernel-module-mali-kbase.ko \
	${INSTALL_DIR}/kernel-module-mali-kutf-clk-rate-trace-test-portal.ko \
	${INSTALL_DIR}/kernel-module-mali-kutf-irq-test.ko \
	${INSTALL_DIR}/kernel-module-mali-kutf-mgm-integration-test.ko \
	${INSTALL_DIR}/kernel-module-memory-group-manager.ko \
        /etc/udev/rules.d/99-mali-modules.rules \
        /usr/bin/load-mali-modules.sh \
        /etc/modprobe.d/mali_kbase.conf \
"
RPROVIDES:${PN} = "\
	kernel-module-dma-buf-test-exporter \
	kernel-module-kutf \
	kernel-module-mali-arbiter \
	kernel-module-mali-emu-kbase \
	kernel-module-mali-gpu-assign \
	kernel-module-mali-gpu-aw \
	kernel-module-mali-gpu-partition-config \
	kernel-module-mali-gpu-partition-control \
	kernel-module-mali-gpu-power \
	kernel-module-mali-gpu-resource-group \
	kernel-module-mali-gpu-system \
	kernel-module-mali-kbase \
	kernel-module-mali-kutf-clk-rate-trace-test-portal \
	kernel-module-mali-kutf-irq-test \
	kernel-module-mali-kutf-mgm-integration-test \
	kernel-module-memory-group-manager \
"
