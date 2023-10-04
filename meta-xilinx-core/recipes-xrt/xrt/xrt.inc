REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG};name=xrt"

BRANCH= "2023.2"
SRCREV_xrt = "d69410b4338f4941df280d769615238f25cd5094"
PV = "202320.2.16.0"

SRC_URI += "git://github.com/Xilinx/dma_ip_drivers.git;branch=master;name=dma_ip_drivers;destsuffix=git/src/runtime_src/core/pcie/driver/linux/xocl/lib/libqdma;protocol=https"
SRCREV_dma_ip_drivers = "9f02769a2eddde008158c96efa39d7edb6512578"

SRC_URI += "git://github.com/serge1/ELFIO.git;branch=main;name=ELFIO;destsuffix=git/src/runtime_src/core/common/elf;protocol=https"
SRCREV_ELFIO = "a04810f12625207cce72665d783babb80f0175a8"
SRCREV_FORMAT = "xrt"
