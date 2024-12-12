SUMMARY = "Devtools"
DESCRIPTION = "Devtools"
LICENSE = "MIT"
 
inherit packagegroup

PACKAGES = "\
    ${PN}-editors \
    ${PN}-shells \
    ${PN}-tools \
    ${PN}-termemu \
"

RDEPENDS:${PN}-editors = "\
    vim \
"

RDEPENDS:${PN}-shells = "\
    bash \
    zsh \
"

RDEPENDS:${PN}-tools = "\
    tmux \
"

RDEPENDS:${PN}-termemu = "\
    simple-terminal \
"
