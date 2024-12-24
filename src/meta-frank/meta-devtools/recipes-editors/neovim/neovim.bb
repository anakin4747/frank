# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE.txt
#   src/xdiff/COPYING
#
# NOTE: multiple licenses have been detected; they have been separated with &
# in the LICENSE value for now since it is a reasonable assumption that all
# of the licenses apply. If instead there is a choice between the multiple
# licenses then you should change the value to separate the licenses with |
# instead of &. If there is any doubt, check the accompanying documentation
# to determine which situation is applicable.
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9366235cf648a9664dae99bd4f957a97 \
                    file://src/mpack/LICENSE-MIT;md5=c4c3fb7d09e6d48eeaa6ce6e6398a876 \
                    file://src/vterm/LICENSE;md5=be5681ffe0dc58ccc9756bc6260fe0cd \
                    file://src/xdiff/COPYING;md5=278f2557e3b277b94e9a8430f6a6d0a9"

SRC_URI = "git://github.com/neovim/neovim.git;protocol=https;branch=master"

PV = "0.10.2+git"
SRCREV = "8f84167c30692555d3332565605e8a625aebc43c"

S = "${WORKDIR}/git"

# NOTE: unable to map the following CMake package dependencies: Luajit Wasmtime Unibilium Iconv Libintl Treesitter Libuv Lpeg Luv Lua UTF8proc
inherit cmake gettext

PREFERRED_VERSION_lua = "5.1.5"
PREFERRED_VERSION_lua-native = "5.1.5"

DEPENDS += "\
    lua-native \
    luajit \
    luv \
    libuv \
    lpeg \
    treesitter \
    unibilium \
    utf8proc \
"
