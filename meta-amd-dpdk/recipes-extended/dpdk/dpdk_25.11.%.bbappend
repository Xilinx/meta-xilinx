FILESEXTRAPATHS:prepend := "${THISDIR}/dpdk:"

# XXV PMD needs the driver-SDK headers and the bus/platform compatible
# field; carried here so meta-dpdk stays a pristine upstream fork.
SRC_URI += "file://0002-bus-platform-add-compatible-string-matching.patch"

PACKAGECONFIG:append = " driver-sdk"
PACKAGECONFIG[driver-sdk] = "-Denable_driver_sdk=true,-Denable_driver_sdk=false,"

# Core patch 0001 (libatomic link): it edits config/arm/arm64_armv8_linux_gcc,
# which is unused here. LDFLAGS reaches meson as c_link_args, so add it there.
LDFLAGS:append:aarch64 = " -l:libatomic.a"
