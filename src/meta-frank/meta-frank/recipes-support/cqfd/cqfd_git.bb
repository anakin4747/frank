LICENSE = "GPL-3.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "git://github.com/savoirfairelinux/cqfd.git;protocol=https;branch=master"

PV = "5.7.0+git"
SRCREV = "48e871eaa90f85979b90806bc64f4da1bb2741ee"

S = "${WORKDIR}/git"

PACKAGECONFIG ??= "podman"
PACKAGECONFIG[docker] = ",,,docker,,podman"
PACKAGECONFIG[podman] = ",,,podman,,docker"

RDEPENDS:${PN} += "\
    git \
    gzip \
    tar \
    xz \
    zip \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

FILES:${PN}-bin += "${bindir}/cqfd"
FILES:${PN}-doc += "\
    ${docdir}/cqfd/CHANGELOG.md \
    ${docdir}/cqfd/README.md \
    ${docdir}/cqfd/AUTHORS \
    ${docdir}/cqfd/LICENSE \
    ${datadir}/cqfd/samples/Dockerfile.focalFossa.nodejs20x \
    ${datadir}/cqfd/samples/dot-cqfdrc \
    ${datadir}/cqfd/samples/Dockerfile.focalFossa.android34 \
"

do_install () {
    oe_runmake install 'DESTDIR=${D}' 'PREFIX=${prefix}'
}
