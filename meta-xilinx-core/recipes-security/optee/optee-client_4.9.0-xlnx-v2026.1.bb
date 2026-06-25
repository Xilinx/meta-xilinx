require recipes-security/optee/optee-client.inc

SRCREV = "9f5e90918093c1d1cd264d8149081b64ab7ba672"

inherit pkgconfig
DEPENDS += "util-linux"
EXTRA_OEMAKE += "PKG_CONFIG=pkg-config"
