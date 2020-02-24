DEPENDS += "${MLPREFIX}newlib"

# The following isnt necessary on decoupling, since libxil is actually built
# We use libgloss as if it was libxil, to avoid linking issues
# do_install_append_microblaze-pmu(){
#   cp ${D}/${libdir}/libgloss.a ${D}/${libdir}/libxil.a
# }
