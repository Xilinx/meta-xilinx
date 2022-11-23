FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:kc705-microblazeel = " \
    file://kc705-microblazeel.cfg \
    "