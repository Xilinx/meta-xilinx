# Meson will try to execute tests with qemu, this doesn't work for mingw
EXEWRAPPER_ENABLED:mingw32 = "False"
