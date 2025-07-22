# OSTree deployment
inherit features_check

REQUIRED_DISTRO_FEATURES = "usrmerge"

OSTREE_ROOTFS ??= "${WORKDIR}/ostree-rootfs"
OSTREE_COMMIT_SUBJECT ??= "Commit-id: ${IMAGE_NAME}"
OSTREE_COMMIT_BODY ??= ""
OSTREE_COMMIT_VERSION ??= "${DISTRO_VERSION}"
OSTREE_UPDATE_SUMMARY ??= "0"

OSTREE_REPO ?= "${DEPLOY_DIR_IMAGE}/ostree_repo"
OSTREE_BRANCHNAME ?= "${SOTA_HARDWARE_ID}"
OSTREE_OSNAME ?= "poky"
OSTREE_BOOTLOADER ??= 'u-boot'
OSTREE_BOOT_PARTITION ??= "/boot"
OSTREE_KERNEL ??= "${KERNEL_IMAGETYPE}"
OSTREE_DEPLOY_DEVICETREE ??= "0"
OSTREE_DEVICETREE ??= "${KERNEL_DEVICETREE}"
OSTREE_MULTI_DEVICETREE_SUPPORT ??= "0"
OSTREE_SYSROOT_READONLY ??= "0"
OSTREE_REPO_CONFIG ?= ""
OSTREE_OTA_REPO_CONFIG ?= ""

IMAGE_INSTALL:append = " \
    os-release \
    ostree \
    ostree-initramfs \
    ostree-kernel \
    ${@'ostree-devicetrees' if oe.types.boolean('${OSTREE_DEPLOY_DEVICETREE}') else ''} \
"

IMAGE_FSTYPES += "${@bb.utils.contains('BUILD_OSTREE_TARBALL', '1', 'ostree.tar.bz2', ' ', d)}"

INITRAMFS_IMAGE ?= "initramfs-ostree-image"
INITRAMFS_FSTYPES ?= "${@oe.utils.ifelse(d.getVar('OSTREE_BOOTLOADER') == 'u-boot', 'cpio.gz.u-boot', 'cpio.gz')}"

SYSTEMD_USED = "${@oe.utils.ifelse(d.getVar('VIRTUAL-RUNTIME_init_manager') == 'systemd', 'true', '')}"

IMAGE_CMD_TAR = "tar ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', '--selinux', '', d)} --xattrs --xattrs-include=*"
CONVERSION_CMD:tar = "touch ${IMGDEPLOYDIR}/${IMAGE_NAME}.${type}; ${IMAGE_CMD_TAR} --numeric-owner -cf ${IMGDEPLOYDIR}/${IMAGE_NAME}.${type}.tar -C ${TAR_IMAGE_ROOTFS} . || [ $? -eq 1 ]"
CONVERSIONTYPES:append = " tar"

TAR_IMAGE_ROOTFS:task-image-ostree = "${OSTREE_ROOTFS}"

ostree_rmdir_helper(){
    if [ -d ${1} ] && [ ! -L ${1} ]; then
        if ! rmdir ${1}; then
            bbwarn "Data in '${1}' directory is not preserved by OSTree. Consider moving it under '/usr'\n$(find ${1} | tail -n +2)"
            rm -vrf ${1}
        fi
    fi
}

do_image_ostree[dirs] = "${OSTREE_ROOTFS}"
do_image_ostree[cleandirs] = "${OSTREE_ROOTFS}"
do_image_ostree[depends] = "coreutils-native:do_populate_sysroot virtual/kernel:do_deploy ${INITRAMFS_IMAGE}:do_image_complete"
IMAGE_CMD:ostree () {
    # Copy required as we change permissions on some files.
    ${IMAGE_CMD_TAR} -cf - -S -C ${IMAGE_ROOTFS} -p . | ${IMAGE_CMD_TAR} -xf - -C ${OSTREE_ROOTFS}

    # Just preserve var/local
    if [ -d var/local ]; then
        mv var/local var-local
    fi
    # var/lib and var/cache requires special handling as they are needed by do_rootfs
    ostree_rmdir_helper var/lib
    ostree_rmdir_helper var/cache
    ostree_rmdir_helper var
    mkdir var
    if [ -d var-local ]; then
        mv var-local var/local
    fi

    # Create sysroot directory to which physical sysroot will be mounted
    mkdir sysroot
    ln -sf sysroot/ostree ostree

    mkdir -p usr/rootdirs

    mv etc usr/

    if [ -n "${SYSTEMD_USED}" ]; then
        mkdir -p usr/etc/tmpfiles.d
        tmpfiles_conf=usr/etc/tmpfiles.d/00ostree-tmpfiles.conf
        echo "d /var/rootdirs 0755 root root -" >>${tmpfiles_conf}
    else
        mkdir -p usr/etc/init.d
        tmpfiles_conf=usr/etc/init.d/tmpfiles.sh
        echo '#!/bin/sh' > ${tmpfiles_conf}
        echo "mkdir -p /var/rootdirs; chmod 755 /var/rootdirs" >> ${tmpfiles_conf}

        ln -s ../init.d/tmpfiles.sh usr/etc/rcS.d/S20tmpfiles.sh
    fi

    # Preserve OSTREE_BRANCHNAME for future information
    mkdir -p usr/share/frank/
    echo -n "${OSTREE_BRANCHNAME}" > usr/share/frank/branchname

    # home directories get copied from the OE root later to the final sysroot
    # Create a symlink to var/rootdirs/home to make sure the OSTree deployment
    # redirects /home to /var/rootdirs/home.
    ostree_rmdir_helper home
    ln -sf var/rootdirs/home home

    # Move persistent directories to /var
    dirs="opt mnt media srv"

    for dir in ${dirs}; do
        ostree_rmdir_helper ${dir}

        if [ -n "${SYSTEMD_USED}" ]; then
            echo "d /var/rootdirs/${dir} 0755 root root -" >>${tmpfiles_conf}
        else
            echo "mkdir -p /var/rootdirs/${dir}; chmod 755 /var/rootdirs/${dir}" >>${tmpfiles_conf}
        fi
        ln -sf var/rootdirs/${dir} ${dir}
    done

    ostree_rmdir_helper root
    ln -sf var/roothome root

    if [ -n "${SYSTEMD_USED}" ]; then
        echo "d /var/roothome 0700 root root -" >>${tmpfiles_conf}
    else
        echo "mkdir -p /var/roothome; chmod 700 /var/roothome" >>${tmpfiles_conf}
    fi

    ostree_rmdir_helper usr/local

    if [ -n "${SYSTEMD_USED}" ]; then
        echo "d /var/usrlocal 0755 root root -" >>${tmpfiles_conf}
    else
        echo "mkdir -p /var/usrlocal; chmod 755 /var/usrlocal" >>${tmpfiles_conf}
    fi

    dirs="bin etc games include lib man sbin share src"

    for dir in ${dirs}; do
        if [ -n "${SYSTEMD_USED}" ]; then
            echo "d /var/usrlocal/${dir} 0755 root root -" >>${tmpfiles_conf}
        else
            echo "mkdir -p /var/usrlocal/${dir}; chmod 755 /var/usrlocal/${dir}" >>${tmpfiles_conf}
        fi
    done

    ln -sf ../var/usrlocal usr/local

    # Copy image manifest
    cat ${IMAGE_MANIFEST} | cut -d " " -f1,3 > usr/package.manifest
}

IMAGE_TYPEDEP:ostreecommit = "ostree"
do_image_ostreecommit[depends] += "ostree-native:do_populate_sysroot"
do_image_ostreecommit[lockfiles] += "${OSTREE_REPO}/ostree.lock"
IMAGE_CMD:ostreecommit () {
    if ! ostree --repo=${OSTREE_REPO} refs 2>&1 > /dev/null; then
        ostree --repo=${OSTREE_REPO} init --mode=archive-z2
    fi

    # Apply generic configurations to the main ostree repository; they are
    # specified as a series of "key:value ..." pairs.
    for cfg in ${OSTREE_REPO_CONFIG}; do
        ostree config --repo=${OSTREE_REPO} set \
               "$(echo "${cfg}" | cut -d ":" -f1)" \
               "$(echo "${cfg}" | cut -d ":" -f2-)"
    done

    # Commit the result
    ostree_target_hash=$(ostree --repo=${OSTREE_REPO} commit \
           --tree=dir=${OSTREE_ROOTFS} \
           --skip-if-unchanged \
           --branch=${OSTREE_BRANCHNAME} \
           --subject="${OSTREE_COMMIT_SUBJECT}" \
           --body="${OSTREE_COMMIT_BODY}" \
           --add-metadata-string=version="${OSTREE_COMMIT_VERSION}" \
           ${EXTRA_OSTREE_COMMIT})

    echo $ostree_target_hash > ${WORKDIR}/ostree_manifest

    if [ ${@ oe.types.boolean('${OSTREE_UPDATE_SUMMARY}')} = True ]; then
        ostree --repo=${OSTREE_REPO} summary -u
    fi
}

# required by ostree-kernel-initramfs
kernel_do_deploy:append() {
    install -m 0644 ${STAGING_KERNEL_BUILDDIR}/kernel-abiversion $deployDir
}
# vim:set ts=4 sw=4 sts=4 expandtab:
