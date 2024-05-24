require pmu-firmware.inc
require ${@'pmu-firmware_generic.inc' if d.getVar('XILINX_WITH_ESW') == 'generic' else ''}
require ${@'pmu-firmware_${PV}-generic.inc' if d.getVar('XILINX_WITH_ESW') == 'generic' else ''}
