PACKAGECONFIG:append = " \
  examples \
  accessibility \
  tools \
  libinput \
  fontconfig \
  "

RRECOMMENDS:${PN}-plugins:append = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'qtwayland', '', d)} \
"
