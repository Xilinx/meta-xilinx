# We use libgloss as if it was libxil, to avoid linking issues
do_install_append_zynqmp-pmu(){
  cp ${D}/${libdir}/libgloss.a ${D}/${libdir}/libxil.a
}
