require qemu-8.2.inc

inherit_defer native

EXTRA_OEMAKE:append = " LD='${LD}' AR='${AR}' OBJCOPY='${OBJCOPY}' LDFLAGS='${LDFLAGS}'"

LDFLAGS:append = " -fuse-ld=bfd"
