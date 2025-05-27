SUMMARY = "A Mali G78AE Linux Kernel modules"
SECTION = "kernel/modules"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = " \
	file://${WORKDIR}/driver/product/kernel/license.txt;md5=13e14ae1bd7ad5bff731bba4a31bb510 \
	"
# Arbiter license file
# driver/product/kernel/drivers/gpu/arm/arbitration/license_gplv2.txt
# md5sum: 13e14ae1bd7ad5bff731bba4a31bb510

SRC_URI = " \
	https://developer.arm.com/-/media/Files/downloads/mali-drivers/kernel/mali-valhall-gpu/VX504X08X-SW-99002-${PV}.tar;name=kernel \
	https://developer.arm.com/-/media/Files/downloads/mali-drivers/arbitration/ArbitrationReferenceCodeValhall/VX501X08X-SW-99007-${PV}.tar;name=arbitration \
	file://compiler.py \
	file://0001-Fix-tab-alignment-in-mali-ptm.txt.patch \
	file://0002-Making-arbiter-driver-compatible-with-6.12-kernel.patch \
	file://0003-Making-gpu-driver-compatible-with-6.12-kernel.patch \
        file://load-mali-modules.sh \
        file://99-mali-modules.rules \
	"

SRC_URI[kernel.sha256sum] = "614818481f2d0335e48f9b2bca0714e43555b0c6e3b02f8f111233ec1ef8cf37"
SRC_URI[arbitration.sha256sum] = "9304774e8b64cda9bd2f76a388912286567ee54f2a2e15669b0f32933f5b9b80"

inherit features_check module python3native

S = "${WORKDIR}/driver/product/kernel"

REQUIRED_MACHINE_FEATURES = "malig78ae"

BUILD_SCRIPT = "${WORKDIR}/compiler.py"
BUILD_CMD = "${STAGING_BINDIR_NATIVE}/python3-native/python3 ${BUILD_SCRIPT} --driver ${S} --debug"

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
