# MicroBlaze does not support LTTng UST
LTTNGUST:microblaze = ""

# Add MicroBlaze patches
FILESEXTRAPATHS:append := ":${THISDIR}/gdb"

SRC_URI:append:microblaze = " \
     file://0001-Patch-MicroBlaze.patch \
     file://0002-Initial-port-of-core-reading-support.patch \
     file://0003-Fix-debug-message-when-register-is-unavailable.patch \
     file://0004-microblaze-Add-build_gdbserver-yes-to-top-level-conf.patch \
     file://0005-Fixing-the-issues-related-to-GDB-7.12.patch \
     file://0006-Patch-MicroBlaze-MicroBlaze-native-gdb-port.patch \
     file://0007-Patch-microblaze-Adding-64-bit-MB-support.patch \
     file://0008-Patch-MicroBlaze-these-changes-will-make-64-bit-vect.patch \
     file://0009-Patch-MicroBlaze-Added-m64-abi-for-64-bit-target-des.patch \
     file://0010-Patch-MicroBlaze-Code-changes-for-gdbserver.patch \
     file://0011-Patch-Microblaze-Depth-Total-number.patch \
     "
