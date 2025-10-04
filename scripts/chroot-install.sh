#!/bin/bash

source vars.sh

cleanup() {
    if grep -q ${USER} /etc/group; then
         userdel -r kin > /dev/null || {
             fatal "failed to delete old user: ${USER}"
         }
         log "deleted old user: ${USER}"
    fi

    if [ -L /etc/localtime ]; then
        unlink /etc/localtime || {
            fatal "failed to unlink localtime"
        }
        log "unlinked /etc/localtime"
    fi
}

cleanup

ln -s /usr/share/zoneinfo/Canada/Eastern /etc/localtime || {
    fatal "failed to set timezone"
}
log "timezone set"

hwclock --systohc || {
    warning "failed to set hardware clock"
}
log "hardware clock set"

sed -i "/^#${LOCALE} UTF-8/s/^#//" /etc/locale.gen && \
    locale-gen && \
    echo "LANG=${LOCALE}" > /etc/locale.conf || {
        warning "failed to set locale"
}
log "set locale to ${LOCALE}"

echo ${HOSTNAME} > /etc/hostname || fatal "failed to set hostname"
log "set hostname to ${HOSTNAME}"

useradd -mG wheel -s /usr/bin/zsh ${USER} || {
    fatal "failed to add user ${USER}"
}
log "added user ${USER}"

passwd ${USER} || fatal "failed to set ${USER} password"
log "set ${USER} password"

if grep -q "^# %wheel ALL=(ALL:ALL) ALL$" /etc/sudoers; then
    # Only edit sudoers if its commented out so that you can control the
    # messages better
    sed -i "s/^# \(%wheel ALL=(ALL:ALL) ALL\)$/\1/" /etc/sudoers || {
        fatal "sed failed to edit /etc/sudoers"
    }
    log "sudoers file editted to give wheel group sudo powers"
fi

if ! grep "^HOOKS=.*encrypt.*lvm2.*filesystem" /etc/mkinitcpio.conf; then
    sed -i '/^HOOKS/ s/filesystem/encrypt lvm2 filesystem/' /etc/mkinitcpio.conf || {
        fatal "sed failed to add encrypt and lvm2 to HOOKS"
    }
    log "added encrypt and lvm2 to HOOKS"
fi

mkinitcpio -P || fatal "failed to make initramfs"
log "made initramfs"

grub-install \
    --target=x86_64-efi \
    --efi-directory=/efi \
    --bootloader-id=GRUB || {
        fatal "failed to install grub"
}
log "installed grub"

UUID=$(blkid -s UUID -o value /dev/${VG}/${LV})
CMDLINE="cryptdevice=UUID=${UUID}:${LV} root=/dev/mapper/${LV}"

sed -i "s|^\(GRUB_CMDLINE_LINUX\)=.*|\1=\"${CMDLINE}\"|" /etc/default/grub || {
    fatal "failed to set GRUB_CMDLINE_LINUX"
}
log "GRUB_CMDLINE_LINUX set in /etc/default/grub"

grub-mkconfig -o /boot/grub/grub.cfg || fatal "failed to make grub config"
log "made grub config"

systemctl enable NetworkManager || fatal "failed to enable network manager"
log "enabled network manager"

rm chroot-install.sh vars.sh
