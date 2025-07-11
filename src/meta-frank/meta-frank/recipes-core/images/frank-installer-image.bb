SUMMARY = "Installer image for Frank"
LICENSE = "MIT"

require recipes-core/images/core-image-minimal.bb

RDEPENDS:${PN} = "usb-installer"
DEPENDS = "frank-image"

WIC = "${DEPLOY_DIR_IMAGE}/frank-image-${MACHINE}.rootfs.wic"

do_install() {
    cp --no-preserve=ownership ${WIC} ${WIC}.bmap ${D}
}
