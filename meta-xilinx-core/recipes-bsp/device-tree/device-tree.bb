SUMMARY = "Xilinx BSP device trees"
DESCRIPTION = "Xilinx BSP device trees from within layer."
SECTION = "bsp"

# the device trees from within the layer are licensed as MIT, kernel includes are GPL
LICENSE = "MIT & GPL-2.0-or-later"
LIC_FILES_CHKSUM = " \
		file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302 \
		file://${COMMON_LICENSE_DIR}/GPL-2.0-or-later;md5=fed54355545ffd980b814dab4a3b312c \
		"

# Since we're not based on a kernel default to nothing.
# This needs to be before the devicetree inherit, as it configured for use
# with a linux kernel by default.
KERNEL_INCLUDE ?= ""

inherit devicetree image-artifact-names

# Fall back to SYSTEM_DTFILE if specified...
# CONFIG_DTFILE is intended to hold a specific configuration's (multiconfig)
# device tree, while SYSTEM_DTFILE is used for the full heterogenous
# system.
SYSTEM_DTFILE ??= ""
CONFIG_DTFILE ??= "${SYSTEM_DTFILE}"

SYSTEM_DTFILE_DEPENDS ??= ""
DEPENDS += "${SYSTEM_DTFILE_DEPENDS}"

BASE_DTS ?= "${@os.path.splitext(os.path.basename(d.getVar('CONFIG_DTFILE') or ''))[0] or 'system-top'}"

EXTRA_DT_FILES ?= ""
EXTRA_DTFILE_PREFIX ?= "system-top"
EXTRA_DTFILES_BUNDLE ?= ""
UBOOT_DT_FILES ?= ""
UBOOT_DTFILE_PREFIX ?= "system-top"
UBOOT_DTFILES_BUNDLE ?= ""
EXTRA_DT_INCLUDE_FILES ?= ""

SYSTEM_DTFILE[doc] = "System Device Tree which accepts at 0...1 dts file"
CONFIG_DTFILE[doc] = "Domain Specific Device Tree which accepts 0...1 dts file"
EXTRA_DT_FILES[doc] = "Add extra files to DT_FILES_PATH, it accepts 1...n dtsi files and adds to SRC_URI"
EXTRA_DT_INCLUDE_FILES[doc] = "Add extra files to DT_FILES_PATH and adds a #include for each to the BASE_DTS, it access 1..n dtsi files and adds to SRC_URI"

# There should only be ONE CONFIG_DTFILE listed
# These need to be passed in from global, not from a bbappend
FILESEXTRAPATHS:prepend := "${@'%s:' % os.path.dirname(d.getVar('CONFIG_DTFILE') or '') if (d.getVar('CONFIG_DTFILE')) else ''}"
SRC_URI:append := " ${@'file://%s' % os.path.basename(d.getVar('CONFIG_DTFILE') or '') if (d.getVar('CONFIG_DTFILE')) else ''}"

SRC_URI:append = " ${@" ".join(["file://%s" % f for f in (d.getVar('EXTRA_DT_FILES') or "").split()])}"
SRC_URI:append = " ${@" ".join(["file://%s" % f for f in (d.getVar('EXTRA_DT_INCLUDE_FILES') or "").split()])}"

COMPATIBLE_MACHINE:zynq   = ".*"
COMPATIBLE_MACHINE:zynqmp = ".*"
COMPATIBLE_MACHINE:versal = ".*"
COMPATIBLE_MACHINE:versal-net = ".*"

DEPENDS += "python3-dtc-native"

PROVIDES = "virtual/dtb"

# common zynq include
SRC_URI:append:zynq = " file://zynq-7000-qspi-dummy.dtsi"

DTB_FILE_NAME ?= "${BASE_DTS}.dtb"

DTB_BASE_NAME ?= "${MACHINE}-system${IMAGE_VERSION_SUFFIX}"

# Copy the EXTRA_DT_FILES and EXTRA_DT_INCLUDE_FILES files in prepend operation so that
# it can be preprocessed.
do_configure:prepend () {
    # Create DT_FILES_PATH directory if doesn't exist during prepend operation.
    if [ ! -d ${DT_FILES_PATH} ]; then
        mkdir -p ${DT_FILES_PATH}
    fi

    for f in ${EXTRA_DT_FILES} ${EXTRA_DT_INCLUDE_FILES}; do
        if [ "$(realpath ${WORKDIR}/${f})" != "$(realpath ${DT_FILES_PATH}/`basename ${f}`)" ]; then
            cp ${WORKDIR}/${f} ${DT_FILES_PATH}/
        fi
    done
}

do_configure:append () {
    for f in ${EXTRA_DT_INCLUDE_FILES}; do
        if [ ! -e ${DT_FILES_PATH}/${BASE_DTS}.dts ]; then
            if [ -e ${DT_FILES_PATH}/${BASE_DTS}.dtb ]; then
                bberror "Unable to find ${BASE_DTS}.dts, to use EXTRA_DT_INCLUDE_FILES you must use a 'dts' and not 'dtb' in CONFIG_DTFILE"
            else
                bberror "Unable to find ${BASE_DTS}.dts, to use EXTRA_DT_INCLUDE_FILES you must set a valid CONFIG_DTFILE or use system-top.dts"
            fi
            exit 1
        fi
        echo "/include/ \"$f\"" >> ${DT_FILES_PATH}/${BASE_DTS}.dts
    done
}

devicetree_do_compile:append() {
    import subprocess

    dtb_file = d.getVar('DTB_FILE_NAME') or ''
    if not dtb_file or not os.path.isfile(dtb_file):
        bb.error("Expected file ${DTB_FILE_NAME} doesn't exist")

    if d.getVar('EXTRA_DTFILES_BUNDLE'):
        ccdtb_prefix = d.getVar('EXTRA_DTFILE_PREFIX')
        extra_dt_files = d.getVar('EXTRA_DT_FILES').split() or []

        for dtsfile in extra_dt_files:
            dtname = os.path.splitext(os.path.basename(dtsfile))[0]
            if os.path.isfile(f"{dtname}.dtbo"):
                fdtargs = ["fdtoverlay", "-o", f"{ccdtb_prefix}-{dtname}.dtb", "-i", dtb_file, f"{dtname}.dtbo"]
                bb.note("Running {0}".format(" ".join(fdtargs)))
                subprocess.run(fdtargs, check = True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)

    if d.getVar('UBOOT_DTFILES_BUNDLE'):
        uboot_ccdtb_prefix = d.getVar('UBOOT_DTFILE_PREFIX')
        uboot_dt_files = d.getVar('UBOOT_DT_FILES').split() or []

        for dtsfile in uboot_dt_files:
            dtname = os.path.splitext(os.path.basename(dtsfile))[0]
            if os.path.isfile(f"{dtname}.dtbo"):
                fdtargs = ["fdtoverlay", "-o", f"{uboot_ccdtb_prefix}-{dtname}.dtb", "-i", dtb_file, f"{dtname}.dtbo"]
                bb.note("Running {0}".format(" ".join(fdtargs)))
                subprocess.run(fdtargs, check = True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
}

FILES:${PN} += "/boot/system.dtb"

devicetree_do_install:append() {
    if [ -n "${DTB_FILE_NAME}" ]; then
        # If it's already a dtb, we have to copy from the original location
        if [ -e "${DT_FILES_PATH}/${DTB_FILE_NAME}" ]; then
            install -Dm 0644 ${DT_FILES_PATH}/${DTB_FILE_NAME} ${D}/boot/devicetree/${DTB_FILE_NAME}
        fi
        if [ -e "${D}/boot/devicetree/${DTB_FILE_NAME}" ]; then
            # We need the output to be system.dtb for WIC setup to match XSCT flow
            cp ${D}/boot/devicetree/${DTB_FILE_NAME} ${D}/boot/system.dtb
        else
            bberror "Expected filename ${DTB_FILE_NAME} doesn't exist in ${DEPLOYDIR}/devicetree"
        fi
    fi
}

devicetree_do_deploy:append() {
    if [ -n "${DTB_FILE_NAME}" ]; then
        # If it's already a dtb, we have to copy from the original location
        if [ -e "${DT_FILES_PATH}/${DTB_FILE_NAME}" ]; then
            install -Dm 0644 ${DT_FILES_PATH}/${DTB_FILE_NAME} ${DEPLOYDIR}/devicetree/${DTB_FILE_NAME}
        fi
        if [ -e "${DEPLOYDIR}/devicetree/${DTB_FILE_NAME}" ]; then
            # We need the output to be system.dtb for WIC setup to match XSCT flow
            ln -sf devicetree/${DTB_FILE_NAME} ${DEPLOYDIR}/${DTB_BASE_NAME}.dtb
            ln -sf devicetree/${DTB_FILE_NAME} ${DEPLOYDIR}/${MACHINE}-system.dtb
            ln -sf devicetree/${DTB_FILE_NAME} ${DEPLOYDIR}/system.dtb
        else
            bberror "Expected filename ${DTB_FILE_NAME} doesn't exist in ${DEPLOYDIR}/devicetree"
        fi
    fi
}

def check_devicetree_variables(d):
    # Don't cache this, as the items on disk can change!
    d.setVar('BB_DONT_CACHE', '1')

    if not d.getVar('CONFIG_DTFILE'):
        raise bb.parse.SkipRecipe("CONFIG_DTFILE or SYSTEM_DTFILE is not defined.")

python() {
    # Need to allow bbappends to change the check
    check_devicetree_variables(d)
}
