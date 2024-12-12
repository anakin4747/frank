LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a77b7838f84aa753d37f88fd9c9ccf7d"

SRC_URI = "git://github.com/lgi-devs/lgi.git;protocol=https;branch=master"
SRCREV = "d7666f77e7ee33907c84f5efdef32aef2e1cc196"

PV = "0.9.2+git"

S = "${WORKDIR}/git"

inherit pkgconfig

# NOTE: the following library dependencies are unknown, ignoring: regress lua
#       (this is based on recipes that have previously been built and packaged)

# NOTE: this is a Makefile-only piece of software, so we cannot generate much of the
# recipe automatically - you will need to examine the Makefile yourself and ensure
# that the appropriate arguments are passed in.

DEPENDS += "lua gobject-introspection glib-2.0"

FILES:${PN} = "${datadir} ${libdir}"

do_configure () {
	# Specify any needed configure commands here
	:
}

do_compile () {
	# You will almost certainly need to add additional arguments here
	oe_runmake
}

do_install () {
	oe_runmake install 'DESTDIR=${D}' 'PREFIX=/usr'
}

BBCLASSEXTEND = "native nativesdk"
