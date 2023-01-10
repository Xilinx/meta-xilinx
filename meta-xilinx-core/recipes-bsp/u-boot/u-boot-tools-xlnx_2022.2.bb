require u-boot-tools-xlnx.inc
require u-boot-xlnx-2022.2.inc

# MUST clear CONFIG_VIDEO to avoid a compilation failure trying to construct
# bmp_logo.h
SED_CONFIG_EFI:append = ' -e "s/CONFIG_VIDEO=.*/# CONFIG_VIDEO is not set/"'
