# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# Unable to find any files that looked like license statements. Check the accompanying
# documentation and source headers and set LICENSE and LIC_FILES_CHKSUM accordingly.
#
# NOTE: LICENSE is being set to "CLOSED" to allow you to at least start building - if
# this is not accurate with respect to the licensing of the software being built (it
# will not be in most cases) you must specify the correct value before using this
# recipe for anything other than initial testing/development!
LICENSE = "CLOSED"
LIC_FILES_CHKSUM = ""

SRC_URI = "https://github.com/neovim/deps/raw/d495ee6f79e7962a53ad79670cb92488abe0b9b4/opt/lpeg-${PV}.tar.gz \
           file://0001-build-allow-for-cross-compilation.patch \
           "
SRC_URI[sha256sum] = "4b155d67d2246c1ffa7ad7bc466c1ea899bbc40fef0257cc9c03cecbaed4352a"

FILES:${PN} += "${libdir}/lpeg.so"

PREFERRED_VERSION_lua = "5.1.5"
PREFERRED_VERSION_lua-native = "5.1.5"

DEPENDS += "\
    lua \
    lua-native \
"

do_install () {
    install -Dm 0644 ${S}/lpeg.so ${D}${libdir}/lpeg.so
}

BBCLASSEXTEND = "native"
