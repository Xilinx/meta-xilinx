require qemu-10.2.inc

inherit_defer native

EXTRA_OEMAKE:append = " LD='${LD}' AR='${AR}' OBJCOPY='${OBJCOPY}' LDFLAGS='${LDFLAGS}'"

LDFLAGS += "-fuse-ld=bfd"
