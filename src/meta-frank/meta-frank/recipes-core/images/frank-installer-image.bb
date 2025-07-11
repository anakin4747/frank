SUMMARY = "Installer image for Frank"
LICENSE = "MIT"

require recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL += "usb-installer"

PAYLOAD_IMG = "frank-image"

WIC = "${PAYLOAD_IMG}-${MACHINE}.rootfs.wic"
ABS_WIC = "${DEPLOY_DIR_IMAGE}/${WIC}"

FILES:${PN} = "/${WIC} /${WIC}.bmap"

do_install[depends] += "${PAYLOAD_IMG}:do_image_wic"
do_install[noexec] = "0"
do_install() {
    cp --no-preserve=ownership ${ABS_WIC} ${ABS_WIC}.bmap ${D}
}
