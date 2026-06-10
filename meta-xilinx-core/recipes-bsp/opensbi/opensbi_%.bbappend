# Make OpenSBI firmware files (fw_dynamic, fw_jump, fw_payload) available in
# the recipe sysroot under /share so that u-boot fitimage creation can
# consume them as a dependency.
SYSROOT_DIRS += "/share"
