FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://defconfig"

SRC_URI:append:qemux86-64 = " file://qemu.cfg"

KCONFIG_MODE = "allnoconfig"
