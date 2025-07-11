#!/bin/bash

err() {
    echo -e "\033[31m[install.sh] FATAL ERROR: $*\033[0m"
    exit 1
}

log() {
    echo -e "\033[32m[install.sh] LOG: $*\033[0m"
}

chvt 3

local installer_boot_drive="$(lsblk -pno PKNAME,PARTLABEL,LABEL | awk '/installer-boot/ { print $1 }')"

test -b "$installer_boot_drive" || {
    err "failed to find block device for installer boot drive: '$installer_boot_drive'"
}

log "found installer boot drive: '$installer_boot_drive'"

local installation_destination="$(lsblk -dno PATH | grep -v "$installer_boot_drive")"

test -b "$installation_destination" || {
    err "failed to find block device for installation destination: '$installation_destination'"
}

log "found installation destination: '$installation_destination'"

local bmap="/root/*.wic.bmap"
local wic="/root/*.wic"

test -f "$bmap" || err "failed to find bmap file: '$bmap'"
test -f "$wic" || err "failed to find wic file: '$wic'"

PS3=""
select option in flash reboot poweroff bash; do
    case $option in
        flash)
            sudo bmaptool copy \
                --bmap "$bmap" \
                "$wic" \
                "$installation_destination" || {
                    err "failed bmaptool copy"
            }
            log "successfully installed '$wic' on '$installation_destination'"
            ;;
        reboot)
            log "rebooting system"
            sudo reboot
            ;;
        poweroff)
            log "powering off system"
            sudo poweroff
            ;;
        bash)
            bash
            ;;
    esac
done
