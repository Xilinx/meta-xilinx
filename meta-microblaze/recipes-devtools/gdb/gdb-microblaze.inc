# MicroBlaze does not support LTTng UST
LTTNGUST:microblaze = ""

# Add MicroBlaze patches
FILESEXTRAPATHS:append := ":${THISDIR}/gdb"

SRC_URI:append:microblaze = " \
     file://0001-Add-initial-port-of-linux-gdbserver.patch \
     file://0002-Patch-MicroBlaze-Initial-port-of-core-reading-suppor.patch \
     file://0003-Fix-debug-message-when-register-is-unavailable.patch \
     file://0004-Patch-MicroBlaze-MicroBlaze-native-gdb-port.patch \
     file://0005-Patch-microblaze-Adding-64-bit-MB-support.patch \
     file://0006-Patch-MicroBlaze-these-changes-will-make-64-bit-vect.patch \
     file://0007-Patch-MicroBlaze-Added-m64-abi-for-64-bit-target-des.patch \
     file://0008-Patch-MicroBlaze.patch \
     "
