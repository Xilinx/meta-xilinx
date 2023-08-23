GOTOOLS:microblaze ?= ""
RUSTTOOLS:microblaze ?= ""
GSTEXAMPLES:microblaze ?= ""
X11GLTOOLS:microblaze ?= ""
3GTOOLS:microblaze ?= ""
KEXECTOOLS:microblaze = ""

RDEPENDS:${PN}:remove:microblaze = "\
ltp \
connman-tools \
connman-tests \
connman-client \
"
