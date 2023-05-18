# Automatically enable pmu-rom-native for ZynqMP support, or warn the user
DEPENDS .= "${@' pmu-rom-native' if 'xilinx' in (d.getVar('LICENSE_FLAGS_ACCEPTED') or '').split() else ''}"
