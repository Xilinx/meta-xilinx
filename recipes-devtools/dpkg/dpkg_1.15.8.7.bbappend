PRINC = "1"

FILESEXTRAPATHS_prepend := "${THISDIR}/files"

SRC_URI += "file://no-vla-warning.patch \
	    file://glibc2.5-sync_file_range.patch"
