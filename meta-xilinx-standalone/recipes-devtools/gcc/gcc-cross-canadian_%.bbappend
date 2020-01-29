require gcc-xilinx-standalone.inc

# We want to use the stock multilib configs, when available
EXTRACONFFUNCS_xilinx-standalone = ""

EXTRA_OECONF_append_xilinx-standalone = " \
        --enable-multilib \
"

# Temporary hack to build gcc cross canadian for tclibc-newlib as --with-sysroot=/not/exist
# has been removed from TARGET_OS for elf and eabi in gcc-cross-canadian.inc

python() {
    extraoeconfgcc = d.getVar('EXTRA_OECONF')
    extraoeconfgcc += " --with-sysroot=/not/exist"
    d.delVar('EXTRA_OECONF')
    d.setVar('EXTRA_OECONF', extraoeconfgcc)
}
