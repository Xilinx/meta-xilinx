#
# This include file implements the MACHINE_DEVICETREE and MACHINE_KCONFIG
# variable handling for the Kernel.
#
# e.g. (set in the associated <machine>.conf)
# MACHINE_DEVICETREE := "zc702/zc702-zynq7.dts"
# MACHINE_KCONFIG := "common/rtc.cfg"
#
# This will expand out to:
# SRC_URI_append += "file://zc702/zc702-zynq7.dts file://common/rtc.cfg"
# KERNEL_DEVICETREE ?= "${WORKDIR}/devicetree/zc702-zynq7.dts"
#
# This include also adds all the 'conf/machine/boards' for all layers (that are
# available) to the FILESEXTRAPATHS.
#

# Returns a space seperated list of all '.dts' files which 
def get_all_devicetrees(d):
    workdir = d.getVar("WORKDIR", True)
    machine_devicetree = d.getVar("MACHINE_DEVICETREE", True) or ''
    if machine_devicetree:
        files=set()
        for path in machine_devicetree.split():
            if os.path.splitext(path)[1] == '.dts':
                files.add(os.path.join(workdir, "devicetree", os.path.basename(path)))
        if len(files) != 0:
            return ' '.join(files)
    return ''

# Retuns a ':' seperated list of expanded '${BBPATH}/$path'
def get_additional_bbpath_filespath(path, d):
    board_extrapaths = []
    bbpath = d.getVar("BBPATH", True) or ""
    for i in bbpath.split(":"):
        board_extrapaths.append(os.path.join(i, path))
    if len(board_extrapaths):
        return ":".join(board_extrapaths) + ":"
    return ""

# If KERNEL_DEVICETREE is not set, default to the device tree's provided by
# MACHINE_DEVICETREE
KERNEL_DEVICETREE ?= "${@get_all_devicetrees(d)}"

# Appends the '<layer>/conf/machine/boards' path to FILESEXTRAPATHS for all
# layers (using the ${BBPATH})
FILESEXTRAPATHS_append := "${@get_additional_bbpath_filespath('conf/machine/boards', d)}"

# Using the MACHINE_DEVICETREE and MACHINE_KCONFIG vars, append them to SRC_URI
python () {
    machine_devicetree = d.getVar("MACHINE_DEVICETREE", True) or ''
    machine_kconfigs = d.getVar("MACHINE_KCONFIG", True) or ''
    sources = set()
    for path in (machine_devicetree.split() + machine_kconfigs.split()):
        sources.add("file://" + path)
    if len(sources) != 0:
        d.setVar("SRC_URI_append", " %s " % " ".join(sources))
}

# Copy all device tree's into the same directory. This is due to compatibility
# with dtc and the use of DTSI (Device Tree Includes), the version of DTC in
# Yocto does not provide include path support.
do_install_prepend() {
	if test -n "${MACHINE_DEVICETREE}"; then
		mkdir -p ${WORKDIR}/devicetree
		for i in ${MACHINE_DEVICETREE}; do
			if test -e ${WORKDIR}/$i; then
				cp ${WORKDIR}/$i ${WORKDIR}/devicetree
			fi
		done
	fi
}
