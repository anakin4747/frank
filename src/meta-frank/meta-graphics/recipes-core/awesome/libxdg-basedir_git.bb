# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=3d22fbc68acb5ab83faba84c33e07d7e"

SRC_URI = "git://github.com/devnev/libxdg-basedir.git;protocol=https;branch=master"

# Modify these as desired
PV = "1.2.3+git"
SRCREV = "b978568d1b3ee04e8197f23ca4e3abdd372f85e1"

S = "${WORKDIR}/git"


# NOTE: if this software is not capable of being built in a separate build directory
# from the source, you should replace autotools with autotools-brokensep in the
# inherit line
inherit autotools

# Specify any options you want to pass to the configure script using EXTRA_OECONF:
EXTRA_OECONF = ""

