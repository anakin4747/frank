SUMMARY = "Installer image for Frank"
LICENSE = "MIT"

require recipes-core/images/core-image-minimal.bb

RDEPENDS:${PN} = "usb-installer"

do_rootfs[depends] += "frank-image:do_image_wic"

WIC = "${DEPLOY_DIR_IMAGE}/frank-image-${MACHINE}.rootfs.wic"

load_payload_image() {
    cp --no-preserve=ownership ${WIC} ${WIC}.bmap ${IMAGE_ROOTFS}
}

ROOTFS_POSTPROCESS_COMMAND += "load_payload_image"
