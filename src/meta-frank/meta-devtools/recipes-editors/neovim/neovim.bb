LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=9366235cf648a9664dae99bd4f957a97 \
                    file://src/mpack/LICENSE-MIT;md5=c4c3fb7d09e6d48eeaa6ce6e6398a876 \
                    file://src/vterm/LICENSE;md5=be5681ffe0dc58ccc9756bc6260fe0cd \
                    file://src/xdiff/COPYING;md5=278f2557e3b277b94e9a8430f6a6d0a9"

SRC_URI = "git://github.com/neovim/neovim.git;protocol=https;branch=master"

PV = "0.10.2+git"
SRCREV = "8f84167c30692555d3332565605e8a625aebc43c"

S = "${WORKDIR}/git"

# nvim executable contains the path to the compiler used and other compiler
# related info which contains the TMPDIR path, this causes it to fail the
# buildpaths qa check
INSANE_SKIP:${PN} += "buildpaths"

inherit cmake gettext mime-xdg

PREFERRED_VERSION_lua = "5.1.5"
PREFERRED_VERSION_lua-native = "5.1.5"

FILES:${PN} += "\
    ${libdir} \
    ${datadir} \
"

RDEPENDS:${PN} += "\
    lua \
    luajit \
    luv \
    treesitter \
    utf8proc \
"

DEPENDS += "\
    libuv \
    lpeg-native \
    luajit \
    lua-native \
    luv \
    treesitter \
    unibilium \
    utf8proc \
"

do_compile() {
    oe_runmake \
        -f ${S}/Makefile \
        'CMAKE_BUILD_TYPE=Debug' \
        'CMAKE_INSTALL_PREFIX=${prefix}' \
        'USE_BUNDLED=false' \
        'DESTDIR=${D}'
}

do_install() {
    oe_runmake \
        -f ${S}/Makefile \
        'CMAKE_BUILD_TYPE=Debug' \
        'CMAKE_INSTALL_PREFIX=${prefix}' \
        'USE_BUNDLED=false' \
        'DESTDIR=${D}' \
        install 
}
