LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=f70a3617a33510362b75a127f401e607"

SRC_URI = "\
    gitsm://github.com/anakin4747/xcb-util-xrm.git;protocol=https;branch=master \
"

PV = "1.3+git"
SRCREV = "41e4059cc6cfe37c4e6b831f2ed78caa7bb58ebf"

S = "${WORKDIR}/git"

DEPENDS = "xcb-util libx11"

# NOTE: if this software is not capable of being built in a separate build directory
# from the source, you should replace autotools with autotools-brokensep in the
# inherit line
inherit pkgconfig autotools
