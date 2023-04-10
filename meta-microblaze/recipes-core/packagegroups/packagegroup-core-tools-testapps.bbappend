GOTOOLS:microblaze ?= ""
RUSTTOOLS:microblaze ?= ""
GSTEXAMPLES:microblaze ?= ""
X11GLTOOLS:microblaze ?= ""
3GTOOLS:microblaze ?= ""

RDEPENDS:${PN}:remove:microblaze = "\
connman-tools \
connman-tests \
connman-client \
"
