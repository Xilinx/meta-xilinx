# meta-amd-dpdk

This layer carries the AMD additions to DPDK: the core patches and build
options required by the AMD poll mode drivers, kept here so that the
meta-dpdk layer remains a pristine fork of upstream.

The poll mode drivers themselves are built from their own repositories and
are packaged by recipes in this layer.

> **Note:** Additional information on AMD Adaptive SoC's and FPGA's can be found at:
	https://www.amd.com/en/products/adaptive-socs-and-fpgas.html

## Maintainers, Mailing list, Patches

Please send any patches, pull requests, comments or questions for this layer to
the [meta-xilinx mailing list](https://lists.yoctoproject.org/g/meta-xilinx)
with ['meta-amd-dpdk'] in the subject:

	meta-xilinx@lists.yoctoproject.org

When sending patches, please make sure the email subject line includes
`[meta-amd-dpdk][<BRANCH_NAME>][PATCH]` and cc'ing the maintainers.

For more details follow the Yocto Project community patch submission guidelines,
as described in:

https://docs.yoctoproject.org/dev/contributor-guide/submit-changes.html#

`git send-email --to meta-xilinx@lists.yoctoproject.org *.patch`

**Syntax:**
`git format-patch -s --subject-prefix="meta-amd-dpdk][<BRANCH_NAME>][PATCH" -1`

**Example:**
`git format-patch -s --subject-prefix="meta-amd-dpdk][wrynose][PATCH" -1`

**Maintainers:**

	Mark Hatle <mark.hatle@amd.com>
	Sandeep Gundlupet Raju <sandeep.gundlupet-raju@amd.com>
	John Toomey <john.toomey@amd.com>
	Trevor Woerner <trevor.woerner@amd.com>
---

## Dependencies

This layer depends on:

	URI: https://git.yoctoproject.org/poky
	layers: meta
	branch: wrynose

	URI:
        https://git.yoctoproject.org/meta-dpdk (official version)
        https://github.com/AMD-AECG-SSW-PUBLIC/meta-dpdk (development and AMD release)
	layers: meta-dpdk
	branch: AMD release version (e.g. rel-v2026.2)
