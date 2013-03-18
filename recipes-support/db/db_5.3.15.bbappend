
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Apply patch to add "microblazeel" support to db
SRC_URI += "file://config-microblazeel.patch"

