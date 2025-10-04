#!/bin/bash

# augroup BufLocalScp
#   autocmd!
#   autocmd BufWritePost <buffer> execute 'silent !scp * root@192.168.88.253:/root/ &'
# augroup END

source vars.sh

cleanup() {
    if swapon --show | grep -q /dev; then
        swapoff /dev/mapper/${VG}-swap || {
            fatal "failed to swapoff"
        }
        log "swapoffed /dev/mapper/${VG}-swap"
    fi

    if mount | grep -q /mnt; then
        umount -R /mnt || {
            fatal "failed to unmount /mnt"
        }
        log "recursively unmounted /mnt"
    fi

    if lsblk | grep ${LV} | grep -q crypt; then
        cryptsetup close ${LV} || {
            fatal "failed to close old encrypted partition"
        }
        log "closed old ${LV}"
    fi

    if lvs | grep -q ${VG}; then
        lvremove -ff ${VG} > /dev/null || {
            fatal "failed to remove logical volume"
        }
        log "removed old logical volume"
    fi

    if vgs | grep -q ${VG}; then
        vgremove -ff ${VG} > /dev/null || {
            fatal "failed to remove ${VG}"
        }
        log "removed old volume group"
    fi

    OLD_PVS=$(pvs --noheadings -o pv_name)
    if [ -n "$OLD_PVS" ]; then
        for pv in $OLD_PVS; do
            pvremove -ff $pv > /dev/null || {
                fatal "failed to remove physical volume: $pv"
            }
            log "removed old physical volume: $pv"
        done
    fi
}

cleanup

ARCHISO="/dev/$(lsblk -no PKNAME $(blkid -L ARCHISO_EFI))"

[ -b "$ARCHISO" ] || fatal "archiso not found"

DRIVES=$(lsblk -ndo NAME,TYPE \
            | awk '$2 == "disk" {print "/dev/" $1}' \
            | grep -v "$ARCHISO")

[ -z "$DRIVES" ] && fatal "no DRIVES"

timedatectl set-ntp true || warning "failed to set-ntp"
log "set up ntp"

LARGEST_DRIVE=$(lsblk -bdno NAME,SIZE \
                    | sort -rhk2 \
                    | sed -nE '1s|(\w*) .*|/dev/\1|p;q')

[ -b "$LARGEST_DRIVE" ] || fatal "failed to find largest drive"

parted --script "$LARGEST_DRIVE" \
    mklabel gpt \
    mkpart ${ESP_PART} fat32 1MiB 101MiB \
    set 1 esp on \
    mkpart ${BOOT_PART} ext4 101MiB 613MiB \
    set 2 boot on \
    mkpart ${PV}0 ext4 613MiB 100% || {
        fatal "failed to create partitions"
}
log "created gpt label, ${ESP_PART}, ${BOOT_PART}, and ${PV}0 on $LARGEST_DRIVE"

i=1
for drive in $DRIVES; do
    if [ "$drive" = "$LARGEST_DRIVE" ]; then
        break
    fi

    parted --script "$drive" \
        mklabel gpt \
        mkpart ${PV}$i 1MiB 100% || {
            fatal "failed to create partition on $drive"
    }
    log "created gpt label and ${PV}$i partition on $drive"

    i=$((i + 1))
done

# wait for files to appear
log "waiting for partitions for physical volumes to appear"
while [ ! -b /dev/disk/by-partlabel/${PV}$((i - 1)) ]; do
    sleep 0.1
done

for pv in /dev/disk/by-partlabel/${PV}*; do
    pvcreate $pv > /dev/null || fatal "failed to create physical volume: $pv"
    log "created physical volume $pv"
done

vgcreate ${VG} $(pvs --noheadings -o pv_name) > /dev/null || {
    fatal "failed to create volume group ${VG}"
}

lvcreate -L 4G -n swap ${VG} > /dev/null || {
    fatal "failed to create swap logical volume"
}
log "created logical volume swap"

lvcreate -l 100%FREE -n ${LV} ${VG} > /dev/null || {
    fatal "failed to create ${LV} logical volume"
}
log "created logical volume ${LV}"

log "create luks passphrase"
cryptsetup luksFormat /dev/${VG}/${LV} --batch-mode || {
    fatal "failed to cryptsetup ${LV} logical volume"
}
log "encrypted luks partition"

log "enter luks passphrase"
cryptsetup open /dev/${VG}/${LV} ${LV} || {
    fatal "failed to open luks partition"
}
log "decrypted luks partition"

mkfs.ext4 /dev/mapper/${LV} > /dev/null || {
    fatal "failed to create ext4 filesystem on ${LV} logical volume"
}
log "created ext4 filesystem on ${LV} logical volume"

mkfs.ext4 /dev/disk/by-partlabel/${BOOT_PART} > /dev/null || {
    fatal "failed to create ext4 filesystem on ${BOOT_PART} partition"
}
log "created ext4 filesystem on ${BOOT_PART} partition"

mkfs.fat -F 32 /dev/disk/by-partlabel/${ESP_PART} > /dev/null || {
    fatal "failed to create fat filesystem on ${ESP_PART} parition"
}
log "created fat filesystem on ${ESP_PART} parition"

mkswap /dev/${VG}/swap > /dev/null || {
    fatal "failed to create swap"
}
log "created swap"

mount /dev/mapper/${LV} /mnt || {
    fatal "failed to mount ${LV} logical volume"
}
log "mounted ${LV} on /mnt"

mount --mkdir /dev/disk/by-partlabel/${BOOT_PART} /mnt/${BOOT_PART} || {
    fatal "failed to mount ${LV} logical volume"
}
log "mounted ${BOOT_PART} on /mnt/${BOOT_PART}"

mount --mkdir /dev/disk/by-partlabel/${ESP_PART} /mnt/efi || {
    fatal "failed to mount ${LV} logical volume"
}
log "mounted ${ESP_PART} on /mnt/efi"

swapon /dev/${VG}/swap || fatal "failed swapon"
log "swapon"

pacstrap -K /mnt ${BASE_PACKAGES} || fatal "failed to pacstrap"
log "successfully pacstrapped"

genfstab -U /mnt > /mnt/etc/fstab || fatal "failed to genfstab"
log "generated fstab"

cp chroot-install.sh vars.sh /mnt/

log "entering chroot"
arch-chroot /mnt /chroot-install.sh || fatal "failed to chroot"
log "exited chroot"

umount -R /mnt || warning "failed to recursively unmount /mnt"
log "unmounted mounts"

swapoff -a || warning "failed to swapoff"
log "swapoffed"

log "install finished, you can reboot now"
