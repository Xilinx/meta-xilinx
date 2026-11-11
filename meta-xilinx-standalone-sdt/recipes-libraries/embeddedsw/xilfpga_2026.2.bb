SUMMARY = "AMD Xilinx FPGA programming library (xilfpga)."
DESCRIPTION = "Library from the AMD Xilinx embeddedsw stack that \
performs PL programming and bitstream management on Zynq UltraScale+ \
MPSoC and Versal devices."
inherit esw

ESW_COMPONENT_SRC = "/lib/sw_services/xilfpga/src/"
ESW_COMPONENT_NAME = "libxilfpga.a"

DEPENDS += "libxil xilsecure"
DEPENDS += "libxil ${@'xilmailbox' if d.getVar('MACHINE') == 'versal-generic' else ''}"

