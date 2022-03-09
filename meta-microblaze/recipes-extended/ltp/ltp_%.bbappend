# gdb on-target is not supported on Microblaze
RDEPENDS:${PN}:remove:microblaze = "gdb"
