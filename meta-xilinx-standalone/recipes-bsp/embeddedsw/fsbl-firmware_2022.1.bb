require fsbl-firmware.inc
require ${@'fsbl-firmware_generic.inc' if d.getVar('XILINX_WITH_ESW') == 'generic' else ''}
require ${@'fsbl-firmware_${PV}-generic.inc' if d.getVar('XILINX_WITH_ESW') == 'generic' else ''}
