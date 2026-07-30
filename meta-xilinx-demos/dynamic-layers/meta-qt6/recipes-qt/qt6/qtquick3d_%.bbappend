# The OpenXR backend does not build on the opengl+x11 Mali targets. No XR
# hardware ships, so disable it.
EXTRA_OECMAKE:append = " -DINPUT_openxr=no"
