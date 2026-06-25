SUMMARY = "Aggregates the AMD FPGA boot artifacts (boot.bin and the \
associated QEMU support files) into a deploy-time packagegroup."
DESCRIPTION = "Boot artifacts for AMD FPGAs. This includes the \
boot.bin, and associated qemu artifacts."

PACKAGE_ARCH = "${MACHINE_ARCH}"

QEMU_FLASH_TYPE ??= "undefined"

# Base dependency on EXTRA_IMAGEDEPENDS
DEPLOY_DEPENDS = "${EXTRA_IMAGEDEPENDS}"

inherit packagegroup deploy

# No packages will be generated
PACKAGES = ""

do_deploy() {
    :
}

addtask do_deploy before do_build after do_populate_sysroot

python() {
    def extraimage_getdepends(task):
        deps = ""
        for dep in (d.getVar('DEPLOY_DEPENDS') or "").split():
            if ":" in dep:
                deps += " %s " % (dep)
            else:
                deps += " %s:%s" % (dep, task)
        return deps

    deps = " " + extraimage_getdepends('do_build')
    d.appendVarFlag('do_deploy', 'depends', deps)
}
