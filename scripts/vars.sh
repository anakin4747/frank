
HOSTNAME="vader"
USER="kin"
LOCALE="en_CA.UTF-8"

VG="vg0"
LV="root"
BOOT_PART="boot"
ESP_PART="ESP"
PV="pv"

BASE_PACKAGES=" \
    base \
    efibootmgr \
    git \
    grml-zsh-config \
    grub \
    linux \
    linux-firmware \
    lvm2 \
    neovim \
    networkmanager \
    openssh \
    sudo \
    vim \
    zsh \
"

GREEN='\033[0;32m'
YELLOW='\033[0;33m'
RED='\033[0;31m'
NC='\033[0m'

log() { echo -e "${GREEN}$*${NC}"; }
warning() { echo -e "${YELLOW}$*${NC}"; }
fatal() { echo -e "${RED}$*${NC}"; exit 1; }
