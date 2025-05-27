# override packageconfig
PACKAGECONFIG[plugin_uefi_capsule] = "-Dplugin_uefi_capsule=enabled -Dplugin_uefi_capsule_splash=false -Defi_binary=false,-Dplugin_uefi_capsule=disabled,,fwupd-efi"
