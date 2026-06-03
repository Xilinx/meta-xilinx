# Mesa rusticl uses bindgen (via meson's rust.bindgen()) to generate Rust FFI
# bindings for C++ LLVM headers (e.g. rusticl_llvm_bindings.hpp).  The clang
# flags for these bindgen invocations come from [properties].bindgen_clang_arguments
# in the meson.cross cross-file (written by meson.bbclass as
# HOST_CC_ARCH + TOOLCHAIN_OPTIONS + --target=TARGET_SYS).
#
# Two problems arise when cross-compiling for big.LITTLE tunes (e.g. cortexa72-cortexa53):
#
#   1. HOST_CC_ARCH (via TARGET_CC_ARCH -> TUNE_CCARGS) contains the GCC dot-notation
#      -mcpu=cortex-a72.cortex-a53+crc which clang/bindgen rejects:
#        "unsupported argument 'cortex-a72.cortex-a53+crc' to option '-mcpu='"
#      Fix: strip the secondary-core suffix from TUNE_CCARGS at parse time.
#
#   2. clang/bindgen cannot find the cross-GCC C++ stdlib headers (cassert etc.)
#      when --sysroot is present.  clang's GCC installation auto-detection does
#      not reliably work alongside --sysroot; explicit -isystem paths are needed.
#      Fix: patch meson.cross after meson.bbclass writes it to append -isystem
#      flags pointing directly at the libstdc++ headers.

python __anonymous () {
    import re
    tune_ccargs = d.getVar('TUNE_CCARGS') or ''
    # Replace -mcpu=<primary>.<secondary><suffix> with -mcpu=<primary><suffix>
    # e.g. -mcpu=cortex-a72.cortex-a53+crc  ->  -mcpu=cortex-a72+crc
    fixed = re.sub(r'(-mcpu=[^.]+)\.[^ ]*', r'\1', tune_ccargs)
    if fixed != tune_ccargs:
        d.setVar('TUNE_CCARGS', fixed)
}


# Override the default bindgen_args with ones that will work.
#
# Append -isystem flags for the cross-GCC C++ standard library headers so
# clang/bindgen can find <cassert>, <cstdint> etc. when processing C++ headers
# like rusticl_llvm_bindings.hpp.  --gcc-toolchain alone is insufficient when
# --sysroot is also present; explicit -isystem paths bypass auto-detection.
def bindgen_args(d):
    args = '${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} --target=${TARGET_SYS}'
    # For SDK packages TOOLCHAIN_OPTIONS don't contain full sysroot path
    if bb.data.inherits_class("nativesdk", d):
        args += ' --sysroot=${STAGING_DIR_HOST}${SDKPATHNATIVE}${prefix_nativesdk}'
    else:
        args += ' --gcc-toolchain=${STAGING_DIR_HOST}/usr'
        args += ' -isystem ${STAGING_DIR_HOST}/usr/include/c++/15.3.0'
        args += ' -isystem ${STAGING_DIR_HOST}/usr/include/c++/15.3.0/${TARGET_SYS}'
    items = d.expand(args).split()
    return repr(items[0] if len(items) == 1 else items)

do_write_config[vardeps] += "TARGET_SYS STAGING_DIR_HOST"
