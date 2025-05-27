# libmali is selected by DISTRO_FEATURE of libmali & MACHINE_FEATURES of mali400
# lima is selected by DISTRO_FEATURE != libmali & MACHINE_FEATURES of mali400
# default mesa because otherwise
def xlnx_is_libmalig78ae_enabled(d):
    if bb.utils.contains('MACHINE_FEATURES', 'malig78ae', '1', '', d) == '1':
        if bb.utils.contains('DISTRO_FEATURES', 'libmali', '1', '', d) == '1':
            return True
    return False

# libmali requires certain preferred providers to be selected 
PREFERRED_PROVIDER_virtual/egl = "${@'libmali-xlnx' if xlnx_is_libmalig78ae_enabled(d) else 'mesa'}"
PREFERRED_PROVIDER_virtual/libgl = "${@'mesa-gl' if xlnx_is_libmalig78ae_enabled(d) else 'mesa'}"
PREFERRED_PROVIDER_virtual/libgles1 = "${@'libmali-xlnx' if xlnx_is_libmalig78ae_enabled(d) else 'mesa'}"
PREFERRED_PROVIDER_virtual/libgles2 = "${@'libmali-xlnx' if xlnx_is_libmalig78ae_enabled(d) else 'mesa'}"
PREFERRED_PROVIDER_virtual/libgles3 = "${@'libmali-xlnx' if xlnx_is_libmalig78ae_enabled(d) else 'mesa'}"
PREFERRED_PROVIDER_virtual/libgbm = "${@'libmali-xlnx' if xlnx_is_libmalig78ae_enabled(d) else 'mesa'}"
PREFERRED_PROVIDER_virtual/mesa = "${@'mesa-gl' if xlnx_is_libmalig78ae_enabled(d) else 'mesa'}"
PREFERRED_PROVIDER_virtual/opencl-icd = "${@'libmali-xlnx' if xlnx_is_libmalig78ae_enabled(d) else 'opencl-icd-loader'}"
PREFERRED_RPROVIDER_virtual-opencl-icd = "${@'libmali-xlnx' if xlnx_is_libmalig78ae_enabled(d) else 'opencl-icd-loader'}"
