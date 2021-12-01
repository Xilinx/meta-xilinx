# OpenGL comes from libmali, adjust parameters
DEPENDS:append:mali400 =  " virtual/libgles2"
PACKAGE_ARCH:mali400 = "${SOC_VARIANT_ARCH}"
