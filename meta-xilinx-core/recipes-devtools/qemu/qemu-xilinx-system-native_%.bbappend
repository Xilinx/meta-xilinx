# Automatically enable pmu-rom-native for ZynqMP support
PMU_ROM_DEP[vardepsexclude] = "LICENSE_FLAGS_ACCEPTED"
PMU_ROM_DEP = "${@bb.utils.contains("LICENSE_FLAGS_ACCEPTED", "xilinx", " pmu-rom-native", "", d)}"

DEPENDS .= "${PMU_ROM_DEP}"
