require plm-firmware.inc
require ${@'plm-firmware_generic.inc' if d.getVar('XILINX_WITH_ESW') == 'generic' else ''}
require ${@'plm-firmware_${PV}-generic.inc' if d.getVar('XILINX_WITH_ESW') == 'generic' else ''}
