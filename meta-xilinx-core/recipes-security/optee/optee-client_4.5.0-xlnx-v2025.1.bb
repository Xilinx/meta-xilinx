require recipes-security/optee/optee-client.inc

SRCREV = "6486773583b5983af8250a47cf07eca938e0e422"

inherit pkgconfig
DEPENDS += "util-linux"
EXTRA_OEMAKE += "PKG_CONFIG=pkg-config"
