
require recipes-core/images/core-image-minimal.bb

IMAGE_FSTYPES += "iso"

IMAGE_FEATURES = " \
    doc-pkgs \
"

IMAGE_FEATURES:append:frank-dbg = " \
    empty-root-password \
    serial-autologin-root \
"

IMAGE_INSTALL:append = " \
    bash \
    cqfd \
    man-db \
    man-pages \
    vim \
    strace \
"

BAD_RECOMMENDATIONS += "less"
