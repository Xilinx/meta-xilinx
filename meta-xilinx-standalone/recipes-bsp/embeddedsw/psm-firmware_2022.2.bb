require psm-firmware.inc
require ${@'psm-firmware_generic.inc' if d.getVar('XILINX_WITH_ESW') == 'generic' else ''}
require ${@'psm-firmware_${PV}-generic.inc' if d.getVar('XILINX_WITH_ESW') == 'generic' else ''}
