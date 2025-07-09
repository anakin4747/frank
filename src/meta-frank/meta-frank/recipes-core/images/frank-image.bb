
IMAGE_FEATURES = "\
    empty-root-password \
    allow-empty-password \
    allow-root-login \
"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
"

# ensure TMPDIR doesn't get appended with libc
TCLIBCAPPEND = ""

inherit image
