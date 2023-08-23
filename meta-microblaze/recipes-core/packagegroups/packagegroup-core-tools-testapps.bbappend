GOTOOLS:microblaze ?= ""
RUSTTOOLS:microblaze ?= ""
GSTEXAMPLES:microblaze ?= ""
X11GLTOOLS:microblaze ?= ""
3GTOOLS:microblaze ?= ""
KEXECTOOLS:microblaze = ""

RDEPENDS:${PN}:remove:microblaze = "\
alsa-utils-amixer \
alsa-utils-aplay \
ltp \
connman-tools \
connman-tests \
connman-client \
"
