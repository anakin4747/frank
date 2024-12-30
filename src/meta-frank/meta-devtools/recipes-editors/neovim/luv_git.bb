# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   deps/libuv/LICENSE-docs
#   deps/libuv/LICENSE-extra
#   deps/luajit/COPYRIGHT
#
# NOTE: multiple licenses have been detected; they have been separated with &
# in the LICENSE value for now since it is a reasonable assumption that all
# of the licenses apply. If instead there is a choice between the multiple
# licenses then you should change the value to separate the licenses with |
# instead of &. If there is any doubt, check the accompanying documentation
# to determine which situation is applicable.
LICENSE = "Apache-2.0 & MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=3b83ef96387f14655fc854ddc3c6bd57 \
                    file://deps/libuv/LICENSE;md5=74b6f2f7818a4e3a80d03556f71b129b \
                    file://deps/libuv/LICENSE-docs;md5=eacc0b19e3fb8dd12d2e110b24be0452 \
                    file://deps/libuv/LICENSE-extra;md5=f9307417749e19bd1d6d68a394b49324 \
                    file://deps/lua-compat-5.3/LICENSE;md5=b863a95a5f6ff64e40a0bb54501225d0 \
                    file://deps/luajit/COPYRIGHT;md5=076b97f5c7e61532f7f6f3865f04da57"

SRC_URI = "gitsm://github.com/luvit/luv.git;protocol=https;branch=master"

# Modify these as desired
PV = "1.48.0-2+git"
SRCREV = "3ff02c8c2beaaf125f3ee5db950138913ffcf978"

S = "${WORKDIR}/git"

inherit cmake

FILES:${PN} += "\
    ${libdir}/lua \
    ${includedir}/luv/luv.h \
"

BBCLASSEXTEND = "native nativesdk"

# compilation fails to build if ${B} is not in PATH since it builds
# ${B}/minilua and cannot find it in the sysroot
# TODO: clean up since I bet there is a better way to do this
do_compile:prepend() {
    export PATH="${B}:$PATH"
}

do_install:append() {
    install -Dm 0644 ${S}/src/luv.h ${D}${includedir}/luv/luv.h
    install -Dm 0644 ${B}/luv.so ${D}${libdir}/lua/5.1/libluv.so.1
}
