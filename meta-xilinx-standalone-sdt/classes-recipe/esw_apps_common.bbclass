#
# Copyright (C) 2024, Advanced Micro Devices, Inc.  All rights reserved.
#
# SPDX-License-Identifier: MIT
#
# This bbclass is inherited by esw application recipes. This class provides
# common code for elf name, bitbake install and deploy task functionality for
# multiconfig target images.

inherit deploy image-artifact-names

APP_IMAGE_NAME ?= "${BPN}-${PKGE}-${PKGV}-${PKGR}-${MACHINE}-${BB_CURRENT_MC}${IMAGE_VERSION_SUFFIX}"

ESW_EXECUTABLE_NAME ?= ""

do_install() {
    install -d ${D}/${base_libdir}/firmware
    # Note that we have to make the ELF executable for it to be stripped
    install -m 0755  ${B}/${ESW_EXECUTABLE_NAME}* ${D}/${base_libdir}/firmware
}

do_deploy() {
    # We need to deploy the stripped elf, hence why not doing it from ${D}
    install -Dm 0644 ${WORKDIR}/package/${base_libdir}/firmware/${ESW_EXECUTABLE_NAME}.elf ${DEPLOYDIR}/${APP_IMAGE_NAME}.elf
    ln -sf ${APP_IMAGE_NAME}.elf ${DEPLOYDIR}/${BPN}-${MACHINE}-${BB_CURRENT_MC}.elf
    ${OBJCOPY} -O binary ${WORKDIR}/package/${base_libdir}/firmware/${ESW_EXECUTABLE_NAME}.elf ${WORKDIR}/package/${base_libdir}/firmware/${ESW_EXECUTABLE_NAME}.bin
    install -m 0644 ${WORKDIR}/package/${base_libdir}/firmware/${ESW_EXECUTABLE_NAME}.bin ${DEPLOYDIR}/${APP_IMAGE_NAME}.bin
    ln -sf ${APP_IMAGE_NAME}.bin ${DEPLOYDIR}/${BPN}-${MACHINE}-${BB_CURRENT_MC}.bin
}

addtask deploy before do_build after do_package
