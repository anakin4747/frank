# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   lib/src/unicode/LICENSE
#
# NOTE: multiple licenses have been detected; they have been separated with &
# in the LICENSE value for now since it is a reasonable assumption that all
# of the licenses apply. If instead there is a choice between the multiple
# licenses then you should change the value to separate the licenses with |
# instead of &. If there is any doubt, check the accompanying documentation
# to determine which situation is applicable.
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=1a8b005a59bb827a79b65bcd4c01d9ec \
                    file://lib/src/unicode/LICENSE;md5=8bc5d32052a96f214cbdd1e53dfc935d"

SRC_URI = "git://github.com/tree-sitter/tree-sitter.git;protocol=https;branch=release-0.24"

PV = "0.24.4+git"
SRCREV = "e3c82633389256ccc2c5ab2e509046cbf20453d3"

S = "${WORKDIR}/git"

EXTRA_OEMAKE += "STRIP="

do_install () {
	oe_runmake install 'DESTDIR=${D}' 'PREFIX=${prefix}'
}

BBCLASSEXTEND = "native"
