
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Apply patch to add "microblazeel" support to gnu-config
SRC_URI += "file://config-microblazeel.patch"

