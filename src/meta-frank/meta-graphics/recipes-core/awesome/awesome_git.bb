LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = "git://github.com/awesomeWM/awesome.git;protocol=https;branch=master \
           file://0001-remove-theme-icons-since-I-don-t-use-them.patch \
           "

PV = "4.3+git"
SRCREV = "0f950cbb625175134b45ea65acdf29b2cbe8c456"

S = "${WORKDIR}/git"

inherit cmake pkgconfig

PREFERRED_VERSION_lua = "5.4"
PREFERRED_VERSION_lua-native = "5.4"

DEPENDS += "\
    lua-native \
    asciidoc-native \
    cmake-native \
    dbus \
    lgi-native \
    libxdg-basedir \
    gdk-pixbuf \
    cairo \
    pango \
    startup-notification \
    xcb-util \
    xcb-util-cursor \
    xcb-util-keysyms \
    xcb-util-wm \
    xcb-util-xrm \
    xdg-user-dirs \
"

RDEPENDS:${PN} += "\
    lgi \
"

FILES:${PN} += " ${datadir}/xsessions"

EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=RelWithDebInfo -DGENERATE_DOC=OFF -DGENERATE_MANPAGES=OFF -DSYSCONFDIR=${sysconfdir}"

INSANE_SKIP:${PN} += "buildpaths"
INSANE_SKIP:${PN}-src += "buildpaths"
# something doesn't smell right here. why would a generated file be in FILES:${PN}-src?
INSANE_SKIP:${PN}-dbg += "buildpaths"
# Messy, lua paths end up compiled in the references to TMPDIR, hence the INSANE_SKIP
do_compile:prepend() {
    export PATH="${B}:$PATH"
    export LUA_PATH="${S}/?.lua;$LUA_PATH"
    export LUA_PATH="${STAGING_DATADIR_NATIVE}/lua/5.1/lgi/?.lua;$LUA_PATH"
    export LUA_PATH="${STAGING_DATADIR_NATIVE}/lua/5.1/?.lua;$LUA_PATH"
    export LUA_CPATH="${STAGING_LIBDIR_NATIVE}/lua/5.1/?.so;$LUA_CPATH"
    export GI_TYPELIB_PATH="${STAGING_LIBDIR}/girepository-1.0:$GI_TYPELIB_PATH"
}
