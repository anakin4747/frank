SUMMARY = "Installer image for Frank"
LICENSE = "MIT"

require recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL += "usb-installer"

WIC = "${DEPLOY_DIR_IMAGE}/frank-image-${MACHINE}.rootfs.wic"

do_install[depends] += "frank-image:do_image_wic"
do_install() {
    cp --no-preserve=ownership ${WIC} ${WIC}.bmap ${IMAGE_ROOTFS}
}
