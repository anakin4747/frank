# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
#
# NOTE: multiple licenses have been detected; they have been separated with &
# in the LICENSE value for now since it is a reasonable assumption that all
# of the licenses apply. If instead there is a choice between the multiple
# licenses then you should change the value to separate the licenses with |
# instead of &. If there is any doubt, check the accompanying documentation
# to determine which situation is applicable.
LICENSE = "GPL-3.0-only & LGPL-3.0-only"
LIC_FILES_CHKSUM = "file://GPLv3;md5=d32239bcb673463ab874e80d47fae504 \
                    file://LGPLv3;md5=6a6a8e020838b23406c81b19c1d46df6 \
                    file://LICENSE;md5=e0ddf7854ece91b3b6c9c861bb35ac31"

SRC_URI = "git://github.com/mauke/unibilium.git;protocol=https;branch=master"

# Modify these as desired
PV = "2.0.0+git"
SRCREV = "e3b16d6219ca1cb92d98b1d9cc416b49a3ac468e"

S = "${WORKDIR}/git"

do_install () {
	oe_runmake install 'DESTDIR=${D}' 'PREFIX=${prefix}'
}

BBCLASSEXTEND = "native"
