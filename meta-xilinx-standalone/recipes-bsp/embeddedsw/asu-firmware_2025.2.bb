require asu-firmware.inc
require ${@'asu-firmware_generic.inc' if d.getVar('XILINX_WITH_ESW') == 'generic' else ''}
require ${@'asu-firmware_${PV}-generic.inc' if d.getVar('XILINX_WITH_ESW') == 'generic' else ''}
