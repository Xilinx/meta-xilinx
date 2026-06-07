# Fix EGL_PLATFORM_SURFACELESS_MESA undeclared error when building with
# non-Mesa EGL implementations (e.g. libmali-xlnx) that do not install
# <EGL/eglmesaext.h>.  The symbol is already present in the upstream
# source alongside all other platform guards; this patch simply adds the
# missing #ifndef guard for EGL_PLATFORM_SURFACELESS_MESA.

FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://0001-gstgldisplay_egl-define-EGL_PLATFORM_SURFACELESS_MES.patch"
