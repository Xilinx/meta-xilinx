
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Apply patch to add "microblazeel" support to ncurses
SRC_URI += "file://config-microblazeel.patch"

