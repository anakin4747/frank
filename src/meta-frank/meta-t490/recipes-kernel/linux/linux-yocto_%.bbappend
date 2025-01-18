FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
    file://defconfig \
"

COMPATIBLE_MACHINE = "^(qemux86-64|genericx86-64|t490)$"
