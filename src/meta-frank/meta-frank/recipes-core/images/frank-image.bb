
IMAGE_FEATURES = "\
    empty-root-password \
    allow-empty-password \
    allow-root-login \
"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
"

inherit image

def image_wic_stamp_extra_info():
    import os
    deploy_dir = d.getVar('DEPLOY_DIR_IMAGE')
    image_name = d.getVar('IMAGE_NAME')
    machine = d.getVar('MACHINE')
    wic_path = os.path.join(deploy_dir, "%s-%s.rootfs.wic" % (image_name, machine))
    if not os.path.exists(wic_path):
        return "missing"
    return ""

do_image_wic[stamp-extra-info] = "${@image_wic_stamp_extra_info(d)}"
