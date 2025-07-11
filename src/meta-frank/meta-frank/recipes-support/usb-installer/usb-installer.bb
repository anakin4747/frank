SUMMARY = "USB installer recipe"
LICENSE = "MIT"

SRC_URI = "\
    file://install.sh \
    file://usb-install.service \
"

RDEPENDS:${PN} = "\
    bash \
    bmaptool \
    util-linux-lsblk \
"

inherit systemd

SYSTEMD_SERVICE:${PN} = "usb-install.service"

SYSTEMD_AUTO_ENABLE = "enable"

FILES:${PN} = "\
    ${bindir}/install.sh \
    ${systemd_system_unitdir}/usb-install.service \
"

do_install_append() {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/install.sh ${D}${bindir}/install.sh
    install -d ${D}${systemd_system_unitdir}
    install -m 0644 ${WORKDIR}/usb-install.service ${D}${systemd_system_unitdir}/usb-install.service
}
