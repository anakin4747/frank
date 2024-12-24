# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE.md
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=96d5a3ba306e0f24fb289427af484408"

SRC_URI = "git://github.com/JuliaStrings/utf8proc.git;protocol=https;branch=master"

# Modify these as desired
PV = "1.0+git"
SRCREV = "3de4596fbe28956855df2ecb3c11c0bbc3535838"

S = "${WORKDIR}/git"

inherit cmake

# Specify any options you want to pass to cmake using EXTRA_OECMAKE:
EXTRA_OECMAKE = ""

