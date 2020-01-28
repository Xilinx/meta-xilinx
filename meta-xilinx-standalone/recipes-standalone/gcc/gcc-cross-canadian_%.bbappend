require gcc-xilinx-standalone.inc

# Temporary hack to build gcc cross canadian for tclibc-newlib as --with-sysroot=/not/exist
# has been removed from TARGET_OS for elf and eabi in gcc-cross-canadian.inc

python() {
    extraoeconfgcc = d.getVar('EXTRA_OECONF')
    extraoeconfgcc += " --with-sysroot=/not/exist"
    d.delVar('EXTRA_OECONF')
    d.setVar('EXTRA_OECONF', extraoeconfgcc)
}
