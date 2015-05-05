#
# Configuration to use external Xilinx toolchain
#

TOOLCHAIN_PATH_ADD = "${EXTERNAL_TOOLCHAIN}/bin:"
PATH =. "${TOOLCHAIN_PATH_ADD}"

CSL_TARGET_SYS_armv7a ?= "arm-xilinx-linux-gnueabi"
CSL_TARGET_SYS_microblazeel ?= "microblazeel-xilinx-linux-gnu"
CSL_TARGET_SYS = "${TARGET_SYS}"

TARGET_PREFIX = "${CSL_TARGET_SYS}-"

PREFERRED_PROVIDER_linux-libc-headers = "external-xilinx-toolchain"
PREFERRED_PROVIDER_linux-libc-headers-dev = "external-xilinx-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc = "external-xilinx-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-initial = "external-xilinx-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}gcc-intermediate = "external-xilinx-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}g++ = "external-xilinx-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}binutils = "external-xilinx-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-for-gcc = "external-xilinx-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}libc-initial = "external-xilinx-toolchain"
PREFERRED_PROVIDER_virtual/${TARGET_PREFIX}compilerlibs = "external-xilinx-toolchain"
PREFERRED_PROVIDER_libgcc = "external-xilinx-toolchain"
PREFERRED_PROVIDER_glibc = "external-xilinx-toolchain"
PREFERRED_PROVIDER_virtual/libc = "external-xilinx-toolchain"
PREFERRED_PROVIDER_virtual/libintl = "external-xilinx-toolchain"
PREFERRED_PROVIDER_virtual/libiconv = "external-xilinx-toolchain"
PREFERRED_PROVIDER_glibc-thread-db = "external-xilinx-toolchain"
PREFERRED_PROVIDER_virtual/linux-libc-headers = "external-xilinx-toolchain"
PREFERRED_PROVIDER_virtual/linux-libc-headers-dev = "external-xilinx-toolchain"
PREFERRED_PROVIDER_gdbserver ??= "external-xilinx-toolchain"

# No need to re-compile the locale files
GLIBC_INTERNAL_USE_BINARY_LOCALE = "precompiled"
ENABLE_BINARY_LOCALE_GENERATION = ""

TOOLCHAIN_OPTIONS = " --sysroot=${STAGING_DIR_HOST}"
# Fix up default hash, enforce it in for CC_ARCH for packages that ignore LDFLAGS.
TARGET_CC_ARCH_append += "${TARGET_LINK_HASH_STYLE}"

# Default sysroot is inside the Xilinx ARM v7-A toolchain in the '<CSL_TARGET_SYS>/libc' (no debug-root)
EXTERNAL_TOOLCHAIN_SYSROOT_armv7a ??= "${EXTERNAL_TOOLCHAIN}/${CSL_TARGET_SYS}/libc"
EXTERNAL_TOOLCHAIN_DBGROOT_armv7a ??= ""

# Default sysroot is inside the Xilinx MicroBlaze toolchain in the '<CSL_TARGET_SYS>/sys-root'
EXTERNAL_TOOLCHAIN_SYSROOT_microblazeel ??= "${EXTERNAL_TOOLCHAIN}/${CSL_TARGET_SYS}/sys-root"
EXTERNAL_TOOLCHAIN_DBGROOT_microblazeel ??= "${EXTERNAL_TOOLCHAIN}/${CSL_TARGET_SYS}/debug-root"

addhandler toolchain_setup
toolchain_setup[eventmask] = "bb.event.BuildStarted"
python toolchain_setup () {
    import errno

    d = e.data
    d = d.createCopy()
    d.finalize()

    tarprefix = d.expand('${TARGET_PREFIX}')
    files = []
    if tarprefix == "arm-xilinx-linux-gnueabi-":
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

require conf/distro/include/xilinx-versions.inc

# Setup suitable toolchain flags
require conf/distro/include/as-needed.inc

