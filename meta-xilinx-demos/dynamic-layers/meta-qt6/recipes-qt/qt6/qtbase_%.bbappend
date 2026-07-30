# Build the examples to populate the qtbase-examples package.
PACKAGECONFIG:append = " examples"

RRECOMMENDS:${PN}-plugins:append = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'qtwayland', '', d)} \
"
