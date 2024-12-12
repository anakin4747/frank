LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE;md5=751419260aa954499f7abaabaa882bbe"

SRC_URI = "git://github.com/awesomeWM/awesome.git;protocol=https;branch=master \
           file://0001-remove-theme-icons-since-I-don-t-use-them.patch \
           "

PV = "4.3+git"
SRCREV = "0f950cbb625175134b45ea65acdf29b2cbe8c456"

S = "${WORKDIR}/git"

# NOTE: unable to map the following pkg-config dependencies: xcb-errors
#       (this is based on recipes that have previously been built and packaged)
# NOTE: the following library dependencies are unknown, ignoring: execinfo
#       (this is based on recipes that have previously been built and packaged)
DEPENDS = "dbus"

inherit cmake pkgconfig

DEPENDS += "\
    lua-native \
    lgi \
    lua-ldoc-native \
    libxdg-basedir \
    gdk-pixbuf \
    cairo \
    startup-notification \
    xcb-util \
    xcb-util-cursor \
    xcb-util-keysyms \
    xcb-util-wm \
    xcb-util-xrm \
    xdg-user-dirs \
"
RDEPENDS:${PN} = "\
    lua \
    lgi \
    libxdg-basedir \
    gdk-pixbuf \
    cairo \
    startup-notification \
    xcb-util \
    xcb-util-cursor \
    xcb-util-keysyms \
    xcb-util-wm \
    xcb-util-xrm \
    xdg-user-dirs \
"

# I think you will have to learn how to build awesome more and hand make this
# recipe

# Right now this is failing since LUA_PATH is not pointing to the correct
# directory to find lgi.lua
# This is where is it ${WORKDIR}/recipe-sysroot/usr/share/lua/5.1/lgi.lua

# This is what LUA_PATH is hardcoded to:
# /home/kin/proj/frank/build/tmp/work/core2-64-oe-linux/awesome/4.3+git/git/tests/examples/shims/?.lua
# /home/kin/proj/frank/build/tmp/work/core2-64-oe-linux/awesome/4.3+git/git/tests/examples/shims/?/init.lua
# /home/kin/proj/frank/build/tmp/work/core2-64-oe-linux/awesome/4.3+git/git/tests/examples/shims/?
# /home/kin/proj/frank/build/tmp/work/core2-64-oe-linux/awesome/4.3+git/git/lib/?.lua
# /home/kin/proj/frank/build/tmp/work/core2-64-oe-linux/awesome/4.3+git/git/lib/?/init.lua
# /home/kin/proj/frank/build/tmp/work/core2-64-oe-linux/awesome/4.3+git/git/lib/?
# /home/kin/proj/frank/build/tmp/work/core2-64-oe-linux/awesome/4.3+git/git/themes/?.lua
# /home/kin/proj/frank/build/tmp/work/core2-64-oe-linux/awesome/4.3+git/git/themes/?
# /home/kin/proj/frank/build/tmp/work/core2-64-oe-linux/awesome/4.3+git/git/tests/examples/?.lua
# /home/kin/proj/frank/build/tmp/work/x86_64-linux/lua-native/5.4.7/recipe-sysroot-native/usr/share/lua/5.4/?.lua
# /home/kin/proj/frank/build/tmp/work/x86_64-linux/lua-native/5.4.7/recipe-sysroot-native/usr/share/lua/5.4/?/init.lua
# /home/kin/proj/frank/build/tmp/work/x86_64-linux/lua-native/5.4.7/recipe-sysroot-native/usr/lib/lua/5.4/?.lua
# /home/kin/proj/frank/build/tmp/work/x86_64-linux/lua-native/5.4.7/recipe-sysroot-native/usr/lib/lua/5.4/?/init.lua
# ./?.lua
# ./?/init.lua"

# OECMAKE_GENERATOR_ARGS:prepend = "-DLUA_PATH=anakin "
LUA_PATH = "ANAKIN"

# Gets passed to ninja as argument. Ninja does not accept env vars are arguments
# EXTRA_OECMAKE_BUILD += "LUA_PATH=anakin"

# Failed, did not set LUA_PATH
# do_compile:prepend() {
#     export LUA_PATH=anakin
# }
