FILESEXTRAPATHS_prepend_zynqmp := "${THISDIR}/files:"

SRC_URI_append = "file://0001-Synchronize-with-the-Kernel-headers-for-routing-oper.patch \
		  file://0002-v4l2-ctl-add-ROUTING-get-and-set-options.patch \
		  file://0003-Synchronise-with-linux-xlnx-kernel-v2020.1-for-routi.patch \
		  "
