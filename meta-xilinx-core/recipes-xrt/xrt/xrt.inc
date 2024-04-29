REPO ?= "git://github.com/Xilinx/XRT.git;protocol=https"
BRANCHARG = "${@['nobranch=1', 'branch=${BRANCH}'][d.getVar('BRANCH', True) != '']}"
SRC_URI = "${REPO};${BRANCHARG};name=xrt"

BRANCH= "2024.1"
SRCREV_xrt = "11f9d2bc6fc61862105093e42cb62c04cf506e51"
PV = "202410.2.17.0"

SRC_URI += "git://github.com/Xilinx/dma_ip_drivers.git;branch=master;name=dma_ip_drivers;destsuffix=git/src/runtime_src/core/pcie/driver/linux/xocl/lib/libqdma;protocol=https"
SRCREV_dma_ip_drivers = "9f02769a2eddde008158c96efa39d7edb6512578"

SRC_URI += "git://github.com/serge1/ELFIO.git;branch=main;name=ELFIO;destsuffix=git/src/runtime_src/core/common/elf;protocol=https"
SRCREV_ELFIO = "a04810f12625207cce72665d783babb80f0175a8"
SRCREV_FORMAT = "xrt"
