
IMAGE_FEATURES = "\
    empty-root-password \
    allow-empty-password \
    allow-root-login \
"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    podman \
    cqfd \
    bash \
    vim \
"

WKS_FILE:sota = "efiimage-sota-persistent-build.wks"

inherit image
