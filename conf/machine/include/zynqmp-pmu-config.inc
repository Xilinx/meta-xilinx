
# Enable the use of zynqmp-pmu for specific recipes only
def zynqmp_pmu_enable_for_bpns(d):
    bpns = [
        "binutils",
        "gcc", "libgcc",
        "newlib", "libgloss",
        "pmu-firmware",
        ]
    return " zynqmp-pmu" if (d.getVar("BPN") in bpns) else ""

BBCLASSEXTEND_append = "${@zynqmp_pmu_enable_for_bpns(d)}"

# Add the pkgarch to the extra list, this is done to avoid warnings about
# missing manifests due to implicit dependency chains on populate_sysroot when
# a recipe/task depends on a IMAGE_EXTRADEPENDS/do_deploy task which is
# provided by a zynqmp-pmu-* target.
PACKAGE_EXTRA_ARCHS_append = " microblazeel-v9.2-bs-cmp"

