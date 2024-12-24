SUMMARY = "Lua is a powerful light-weight programming language designed \
for extending applications."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=59bdd99bb82238f238cf5c65c21604fd"
HOMEPAGE = "http://www.lua.org/"

SRC_URI = "http://www.lua.org/ftp/lua-${PV}.tar.gz;name=tarballsrc \
           file://lua.pc.in \
           ${@bb.utils.contains('DISTRO_FEATURES', 'ptest', 'http://www.lua.org/tests/lua-${PV_testsuites}-tests.tar.gz;name=tarballtest file://run-ptest ', '', d)} \
           "

S = "${WORKDIR}/lua-${PV}"

# if no test suite matches PV release of Lua exactly, download the suite for the closest Lua release.
PV_testsuites = "${PV}"

SRC_URI[tarballsrc.sha256sum] = "2640fc56a795f29d28ef15e13c34a47e223960b0240e8cb0a82d9b0738695333"
SRC_URI[tarballtest.sha256sum] = "8a4898ffe4c7613c8009327a0722db7a41ef861d526c77c5b46114e59ebf811e"

inherit pkgconfig binconfig ptest

DEPENDS = "readline ncurses-native"

RDEPENDS:${PN} += "ncurses"

FILES:${PN} += "${libdir}/lua"

TARGET_CC_ARCH += " -fPIC ${LDFLAGS}"
EXTRA_OEMAKE = "'CC=${CC} -fPIC' 'MYCFLAGS=${CFLAGS} -fPIC' MYLDFLAGS='${LDFLAGS}' 'AR=ar rcD' 'RANLIB=ranlib -D'"

do_configure:prepend () {
    sed -i -e s:/usr/local:${prefix}:g src/luaconf.h
    sed -i -e s:lib/lua/:${baselib}/lua/:g src/luaconf.h
}

do_compile () {
    oe_runmake linux 
}

do_install () {
    oe_runmake \
        'INSTALL_TOP=${D}${prefix}' \
        'INSTALL_BIN=${D}${bindir}' \
        'INSTALL_INC=${D}${includedir}/' \
        'INSTALL_MAN=${D}${mandir}/man1' \
        'INSTALL_SHARE=${D}${datadir}/lua' \
        'INSTALL_LIB=${D}${libdir}' \
        'INSTALL_CMOD=${D}${libdir}/lua/5.1' \
        install
    install -d ${D}${libdir}/pkgconfig

    sed -e s/@VERSION@/${PV}/ -e s#@LIBDIR@#${libdir}# -e s#@INCLUDEDIR@#${includedir}# ${UNPACKDIR}/lua.pc.in > ${S}/lua.pc
    install -m 0644 ${S}/lua.pc ${D}${libdir}/pkgconfig/
    rmdir ${D}${datadir}/lua/5.1
    rmdir ${D}${datadir}/lua
}

do_install_ptest () {
    cp -R --no-dereference --preserve=mode,links -v ${UNPACKDIR}/lua-${PV_testsuites}-tests ${D}${PTEST_PATH}/test
}

do_install_ptest:append:libc-musl () {
    # locale tests does not work on musl, due to limited locale implementation
    # https://wiki.musl-libc.org/open-issues.html#Locale-limitations
    sed -i -e 's|os.setlocale("pt_BR") or os.setlocale("ptb")|false|g' ${D}${PTEST_PATH}/test/literals.lua
}

BBCLASSEXTEND = "native nativesdk"

inherit multilib_script
MULTILIB_SCRIPTS = "${PN}-dev:${includedir}/luaconf.h"

