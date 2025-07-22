
IMAGE_FEATURES = "\
    empty-root-password \
    allow-empty-password \
    allow-root-login \
"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-base \
    weston \
    wayland \
    podman \
    cqfd \
    bash \
    vim \
    strace \
"

IMAGE_FSTYPES:remove = "wic"

inherit image
