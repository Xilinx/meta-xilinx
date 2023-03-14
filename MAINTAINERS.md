# Maintainers, Mailing list, Patches

Please send any patches, pull requests, comments or questions for this layer to
the [meta-xilinx mailing list](https://lists.yoctoproject.org/g/meta-xilinx):

	meta-xilinx@lists.yoctoproject.org

When sending patches, please make sure the email subject line includes
`[meta-xilinx][<BRANCH_NAME>][PATCH]` and cc'ing the maintainers.

For more details follow the OE community patch submission guidelines, as described in:

https://www.openembedded.org/wiki/Commit_Patch_Message_Guidelines
https://www.openembedded.org/wiki/How_to_submit_a_patch_to_OpenEmbedded

`git send-email --to meta-xilinx@lists.yoctoproject.org *.patch`

> **Note:** When creating patches, please use below format. To follow best practice,
> if you have more than one patch use `--cover-letter` option while generating the
> patches. Edit the 0000-cover-letter.patch and change the title and top of the
> body as appropriate.

**Syntax:**
`git format-patch -s --subject-prefix="meta-xilinx][<BRANCH_NAME>][PATCH" -1`

**Example:**
`git format-patch -s --subject-prefix="meta-xilinx][langdale][PATCH" -1`

**Maintainers:**

	Mark Hatle <mark.hatle@amd.com>
	Sandeep Gundlupet Raju <sandeep.gundlupet-raju@amd.com>
	John Toomey <john.toomey@amd.com>

> **Note:**

* meta-xilinx-contrib layer:
  * We don't have any maintainers when user submit a patch to this layer
    email meta-xilinx@yoctoproject.org and cc'ing below reviewers.

**Reviewers:**

	Mark Hatle <mark.hatle@xilinx.com>
	Sandeep Gundlupet Raju <sandeep.gundlupet-raju@xilinx.com>
	John Toomey <john.toomey@xilinx.com>
