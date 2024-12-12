LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=703e9835709f45ee7b81082277f1daec"

SRC_URI = "git://git.suckless.org/st;branch=master"

# Modify these as desired
PV = "0.9.2+git"
SRCREV = "a0274bc20e11d8672bb2953fdd1d3010c0e708c5"

S = "${WORKDIR}/git"

DEPENDS = "\
    libx11 \
    libxft \
    fontconfig \
    pkgconfig-native \
"
# ncurses-native provides `tic` for generating terminal info
DEPENDS += " ncurses-native"

do_install () {
	oe_runmake install 'DESTDIR=${D}' 'PREFIX=/usr'
}
