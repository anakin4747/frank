SUMMARY = "Installer image for Frank"
LICENSE = "MIT"

require recipes-core/images/core-image-minimal.bb

WKS_FILE = "x86-64-usb-installer.wks"

# Prevent /boot from getting mounted
WIC_CREATE_EXTRA_ARGS = "--no-fstab-update"

IMAGE_INSTALL += "usb-installer"

do_rootfs[depends] += "frank-image:do_image_ostreecommit"

load_payload_image() {
    WIC="$(echo "${IMGDEPLOYDIR}/${IMAGE_LINK_NAME}.wic" | sed 's/frank-installer-image/frank-image/g')"
    cp --no-preserve=ownership "${WIC}" "${WIC}.bmap" "${IMAGE_ROOTFS}"
}

ROOTFS_POSTPROCESS_COMMAND += "load_payload_image"

do_image_garagecheck[noexec] = "1"
do_image_garagesign[noexec] = "1"
do_image_ostree[noexec] = "1"
do_image_ostreecommit[noexec] = "1"
do_image_ostreepush[noexec] = "1"
do_image_ota[noexec] = "1"
do_image_ota_ext4[noexec] = "1"
