inherit esw

# Not compatible with Zynq
COMPATIBLE_MACHINE:microblaze = "none"

ESW_COMPONENT_SRC = "/lib/sw_apps/undefined/src"
ESW_COMPONENT_SRC:zynq = "/lib/sw_apps/zynq_fsbl/src"
ESW_COMPONENT_SRC:zynqmp = "/lib/sw_apps/zynqmp_fsbl/src"

ESW_FSBL_DEPENDS = "xilstandalone xiltimer xilffs"
ESW_FSBL_DEPENDS:append:zynq = " xilrsa"
ESW_FSBL_DEPENDS:append:zynqmp = " xilsecure xilpm"

DEPENDS += "${PSU_INIT_DEPENDS} ${ESW_FSBL_DEPENDS}"

CFLAGS:append:aarch64 = " -DARMA53_64"
CFLAGS:append:armv7r = " -DARMR5"



# These files usually come in from sdt-artifacts (SYSTEM_DTFILE_DEPENDS)
FSBL_PS_INIT_C:zynq = "ps7_init.c"
FSBL_PS_INIT_C:zynqmp = "psu_init.c"
FSBL_PS_INIT_H:zynq = "ps7_init.h"
FSBL_PS_INIT_H:zynqmp = "psu_init.h"

SYSTEM_DTFILE_DEPENDS ??= ""
PSU_INIT_DEPENDS ?= "${SYSTEM_DTFILE_DEPENDS}"
PSU_INIT_PATH ?= "${SYSTEM_DTFILE_DIR}"
PSU_INIT_PATH[doc] = "Path to psu_init or ps7_init files, defaults to system device tree output directory"

do_configure:prepend() {
    if [ -e ${PSU_INIT_PATH}/${FSBL_PS_INIT_C} ]; then
        install -m 0644 ${PSU_INIT_PATH}/${FSBL_PS_INIT_C} ${S}/${ESW_COMPONENT_SRC}
    else
        bbfatal "Using the default ${FSBL_PS_INIT_C}, this may not work correctly.  Looking for ${PSU_INIT_PATH}/${FSBL_PS_INIT_C}"
    fi

    if [ -e ${PSU_INIT_PATH}/${FSBL_PS_INIT_H} ]; then
        install -m 0644 ${PSU_INIT_PATH}/${FSBL_PS_INIT_H} ${S}/${ESW_COMPONENT_SRC}
    else
        bbfatal "Using the default ${FSBL_PS_INIT_H}, this may not work correctly.  Looking for ${PSU_INIT_PATH}/${FSBL_PS_INIT_H}"
    fi
    install -m 0644 ${S}/cmake/UserConfig.cmake ${S}/${ESW_COMPONENT_SRC}

    # binutils 2.46 promotes "PHDR segment not covered by LOAD segment" from
    # a warning to a fatal error during LTO link. The linker auto-generates a
    # PT_PHDR entry that cannot be covered by any LOAD segment (the binary
    # lives entirely in OCM at 0xFFFC0000). The --no-check-sections flag is
    # not forwarded through lto-wrapper's internal ld invocation.
    # Fix: inject an explicit PHDRS block (without PT_PHDR) into the linker
    # script so the linker never generates the uncoverable PT_PHDR entry.
    for _ld in ${S}/${ESW_COMPONENT_SRC}/lscript_a53.ld ${S}/${ESW_COMPONENT_SRC}/lscript.ld; do
        if [ -f "${_ld}" ] && ! grep -q "^PHDRS" "${_ld}"; then
            # Insert PHDRS block (single load segment) before SECTIONS
            sed -i 's/^SECTIONS$/PHDRS\n{\n  load PT_LOAD ;\n}\n\nSECTIONS/' "${_ld}"
            # Annotate the first section's memory assignment with :load so
            # that PT_LOAD covers it; subsequent sections inherit this PHDR.
            sed -i '0,/^} > [^ ]* *$/{s/^\(} > [^ ]*\) *$/\1 :load/}' "${_ld}"
        fi
    done
}

do_install() {
    :
}

PACKAGES = ""

ESW_COMPONENT = "undefined"
ESW_COMPONENT:zynq = "zynq_fsbl.elf"
ESW_COMPONENT:zynqmp = "zynqmp_fsbl.elf"

do_deploy() {
    install -Dm 0644 ${B}/${ESW_COMPONENT} ${DEPLOYDIR}/${FSBL_BASE_NAME}.elf
    ln -sf ${FSBL_BASE_NAME}.elf ${DEPLOYDIR}/${FSBL_IMAGE_NAME}.elf
}

addtask deploy before do_build after do_package
