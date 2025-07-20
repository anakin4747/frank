#!/bin/bash

die() {
    echo -e "\033[31m[install.sh] DIED: $*\033[0m"
    exit 1
}

err() {
    echo -e "\033[31m[install.sh] ERR: $*\033[0m"
}

log() {
    echo -e "\033[32m[install.sh] LOG: $*\033[0m"
}

chvt 3

installer_boot_drive="$(lsblk -pno PKNAME,PARTLABEL,LABEL | awk '/boot-usb/ { print $1 }')"

test -b "$installer_boot_drive" || {
    die "failed to find block device for installer boot drive: '$installer_boot_drive'"
}

lsblk

log "found installer boot drive: '$installer_boot_drive'"

disks="$(lsblk -dno PATH | grep -v "$installer_boot_drive")"

set -- $disks
if [ $# -eq 1 ]; then
    installation_destination=$1
else
    PS3="select installation destination: "
    select disk in $*; do
        if [ -n "$disk" ]; then
            installation_destination="$disk"
            break
        fi
    done
fi

test -b "$installation_destination" || {
    die "failed to find block device for installation destination: '$installation_destination'"
}

log "found installation destination: '$installation_destination'"

bmap="$(ls /*.wic.bmap)"
test -f "$bmap" || die "failed to find bmap file: '$bmap'"
log "found bmap file: '$bmap'"

wic="$(ls /*.wic)"
test -f "$wic" || die "failed to find wic file: '$wic'"
log "found wic file: '$wic'"

flash() {
    bmaptool copy \
        --no-verify \
        --bmap "$bmap" \
        "$wic" \
        "$installation_destination"
    if [ $? -ne 0 ]; then
        err "failed bmaptool copy"
    else
        log "successfully installed '$wic' on '$installation_destination'"
    fi
}

PS3=""
select option in flash reboot poweroff bash; do
    case $option in
        flash)
            flash
            ;;
        reboot)
            log "rebooting system"
            reboot
            ;;
        poweroff)
            log "powering off system"
            poweroff
            ;;
        bash)
            bash
            ;;
    esac
done
