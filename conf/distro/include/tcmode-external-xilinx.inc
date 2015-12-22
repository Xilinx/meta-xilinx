#
# Configuration to use external Xilinx toolchain
#

TOOLCHAIN_PATH_ADD = "${EXTERNAL_TOOLCHAIN}/bin:"
PATH =. "${TOOLCHAIN_PATH_ADD}"

XILINX_TARGET_SYS_aarch64 ?= "aarch64-linux-gnu"
XILINX_TARGET_SYS_armv7a ?= "arm-linux-gnueabihf"
XILINX_TARGET_SYS_microblazeel ?= "microblazeel-xilinx-linux-gnu"
XILINX_TARGET_SYS = "${TARGET_SYS}"

TARGET_PREFIX = "${XILINX_TARGET_SYS}-"

PREFERRED_PROVIDER_linux-libc-headers = "linux-libc-headers-xilinx"
PREFERRED_PROVIDER_linux-libc-headers-dev = "linux-libc-headers-xilinx"
PREFERRED_PROVIDER_virtual/linux-libc-headers = "linux-libc-headers-xilinx"
PREFERRED_PROVIDER_libgcc = "libgcc-xilinx"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}compilerlibs = "gcc-runtime-xilinx"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}binutils = "binutils-cross-xilinx-${TARGET_ARCH}"

PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc = "gcc-cross-xilinx-${TARGET_ARCH}"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-initial = "gcc-cross-xilinx-${TARGET_ARCH}"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-intermediate = "gcc-cross-xilinx-${TARGET_ARCH}"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}g++ = "gcc-cross-xilinx-${TARGET_ARCH}"

PREFERRED_PROVIDER_gcc-sanitizers ?= "gcc-sanitizers-xilinx"

PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-for-gcc = "glibc-xilinx"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-initial = "glibc-xilinx"
PREFERRED_PROVIDER_glibc = "glibc-xilinx"
PREFERRED_PROVIDER_virtual/libc = "glibc-xilinx"
PREFERRED_PROVIDER_virtual/libintl = "glibc-xilinx"
PREFERRED_PROVIDER_virtual/libiconv = "glibc-xilinx"
PREFERRED_PROVIDER_glibc-thread-db = "glibc-xilinx"

WARN_QA_remove = "ldflags textrel"
ERROR_QA_remove = "ldflags textrel"

# No need to re-compile the locale files
GLIBC_INTERNAL_USE_BINARY_LOCALE = "precompiled"
ENABLE_BINARY_LOCALE_GENERATION = ""

TOOLCHAIN_OPTIONS = " --sysroot=${STAGING_DIR_HOST}"
# Fix up default hash, enforce it in for CC_ARCH for packages that ignore LDFLAGS.
TARGET_CC_ARCH_append = " ${TARGET_LINK_HASH_STYLE}"

# Default sysroot is inside the Xilinx ARM v7-A toolchain in the '<XILINX_TARGET_SYS>/libc' (no debug-root)
EXTERNAL_TOOLCHAIN_SYSROOT_armv7a ??= "${EXTERNAL_TOOLCHAIN}/${XILINX_TARGET_SYS}/libc"
EXTERNAL_TOOLCHAIN_DBGROOT_armv7a ??= ""

# Default sysroot is inside the Xilinx ARM v8-A toolchain in the '<XILINX_TARGET_SYS>/libc' (no debug-root)
EXTERNAL_TOOLCHAIN_SYSROOT_aarch64 ??= "${EXTERNAL_TOOLCHAIN}/${XILINX_TARGET_SYS}/libc"
EXTERNAL_TOOLCHAIN_DBGROOT_aarch64 ??= "${EXTERNAL_TOOLCHAIN}/${XILINX_TARGET_SYS}/debug-root"

# Default sysroot is inside the Xilinx MicroBlaze toolchain in the '<XILINX_TARGET_SYS>/sys-root'
EXTERNAL_TOOLCHAIN_SYSROOT_microblazeel ??= "${EXTERNAL_TOOLCHAIN}/${XILINX_TARGET_SYS}/sys-root"
EXTERNAL_TOOLCHAIN_DBGROOT_microblazeel ??= "${EXTERNAL_TOOLCHAIN}/${XILINX_TARGET_SYS}/debug-root"

addhandler toolchain_setup
toolchain_setup[eventmask] = "bb.event.BuildStarted"
python toolchain_setup () {
    import errno

    d = e.data
    d = d.createCopy()
    d.finalize()

    tarprefix = d.expand('${TARGET_PREFIX}')
    files = []
    if (tarprefix == "arm-linux-gnueabihf-") or (tarprefix == "arm-xilinx-linux-gnueabi-"):
        files.append((tarprefix + "ld", tarprefix + "ld.bfd"))

    if len(files) > 0:
        ext_bindir = d.expand('${EXTERNAL_TOOLCHAIN}/bin')
        loc_bindir = d.getVar('STAGING_BINDIR_TOOLCHAIN', True)
        bb.utils.mkdirhier(loc_bindir)
        for f in files:
            source = os.path.join(ext_bindir, f[0])
            dest = os.path.join(loc_bindir, f[1])
            try:
                os.symlink(source, dest)
            except OSError as exc:
                if exc.errno == errno.EEXIST:
                    break
                bb.fatal("Unable to populate toolchain binary symlink for %s: %s" % (f[0], f[1]))
}

XILINX_VER_MAIN ?= "2000.0"

require conf/distro/include/xilinx-versions.inc

# Setup suitable toolchain flags
require conf/distro/include/as-needed.inc

