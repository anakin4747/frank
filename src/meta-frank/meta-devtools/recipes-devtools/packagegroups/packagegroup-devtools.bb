SUMMARY = "Devtools"
DESCRIPTION = "Devtools"
LICENSE = "MIT"
 
inherit packagegroup

PACKAGES = "\
    ${PN}-editor \
    ${PN}-shells \
    ${PN}-tools \
    ${PN}-termemu \
"

RDEPENDS:${PN}-editor = "\
    vim \
"

RDEPENDS:${PN}-shells = "\
    bash \
    zsh \
"

RDEPENDS:${PN}-tools = "\
    tmux \
"

# RDEPENDS:${PN}-termemu = "\
#     st \
# "
