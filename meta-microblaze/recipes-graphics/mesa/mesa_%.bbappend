# mesa doesn't compile with microblaze compiler

#
# When building POKY, it's advised that the user set the following:
#
# POKY_DEFAULT_DISTRO_FEATURES:microblaze = "ptest"
#
# The original version is: POKY_DEFAULT_DISTRO_FEATURES = "opengl ptest multiarch wayland vulkan"
#
# opengl, wayland and vulkan are not supported, primarily due to mesa not compiling.
#
# multiarch is not something we have on microblaze, so can be left or removed
#
COMPATIBLE_MACHINE:microblaze = "none"
