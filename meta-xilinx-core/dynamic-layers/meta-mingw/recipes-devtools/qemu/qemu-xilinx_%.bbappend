FILESEXTRAPATHS:prepend := "${THISDIR}/qemu-xilinx:"

# memory_region_init_ram_from_file() is only declared/implemented under
# #ifdef CONFIG_POSIX in QEMU's include/system/memory.h / system/memory.c,
# since it relies on mmap()-ing a backing file, which is not supported on
# the mingw32/Windows nativesdk target. Two Xilinx-added call sites need
# to be guarded so the build doesn't fail with implicit-function-declaration
# errors:
#
#   hw/misc/amd-ddr-memory.c: In function 'amd_ddr_memory_realize':
#   hw/misc/amd-ddr-memory.c:41:9: error: implicit declaration of
#   function 'memory_region_init_ram_from_file' [-Wimplicit-function-declaration]
#
#   hwdtb/factory.c: In function 'hwdtb_factory_memory_region':
#   hwdtb/factory.c:320:9: error: implicit declaration of function
#   'memory_region_init_ram_from_file'; did you mean
#   'memory_region_init_ram_nomigrate'? [-Wimplicit-function-declaration]
#
# Additionally, QAPI's UnixSocketAddress "abstract"/"tight" fields are
# only defined "#if defined(CONFIG_LINUX)" (abstract UNIX domain sockets
# are a Linux-specific extension), but one call site in
# hw/core/remote-port.c initializes them unconditionally:
#
#   hw/core/remote-port.c:497:18: error: 'UnixSocketAddress' has no
#   member named 'abstract'
#   hw/core/remote-port.c:498:18: error: 'UnixSocketAddress' has no
#   member named 'tight'
SRC_URI:append:mingw32 = "\
           file://0001-hw-misc-amd-ddr-memory-Guard-file-backed-shared-mem.patch \
           file://0002-hwdtb-factory-Guard-file-backed-RAM-region-with-CON.patch \
           file://0003-hw-core-remote-port-Guard-abstract-tight-UNIX-socke.patch \
           "
