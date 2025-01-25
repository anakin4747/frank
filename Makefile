# The goal of this Makefile is to remove the need to add configurations files
# in the ephemerial ./build directory
#
# This is done by generating a build/conf/bblayers.conf and setting variables in
# build/conf/local.conf all from this Makefile

MACHINE ?= qemux86-64
DISTRO ?= frank
IMAGE ?= core-image-minimal

# This help target lists all targets that are commented with a double ##
.PHONY: help
help:
	@./scripts/list-make-targets $(MAKEFILE_LIST)

build/conf/local.conf build/conf/bblayers.conf:
	@. ./src/poky/oe-init-build-env > /dev/null

.PHONY: layers
layers: build/conf/bblayers.conf # Find layers in ./src and save them to build/conf/bblayers.conf
	@./scripts/find-layers

.PHONY: machine
machine: build/conf/local.conf # Set MACHINE variable in build/conf/local.conf
	@./scripts/setvar MACHINE $(MACHINE)

.PHONY: distro
distro: build/conf/local.conf # Set DISTRO variable in build/conf/local.conf
	@./scripts/setvar DISTRO $(DISTRO)

.PHONY: build
build: layers machine distro # Build yocto
	. ./src/poky/oe-init-build-env > /dev/null; bitbake -k $(IMAGE)

.PHONY: distclean
distclean: # Remove all but conf from build
	rm -rf build/{cache,downloads,tmp,sstate-cache}

.PHONY: menuconfig
menuconfig: # Kernel make menuconfig
	. ./src/poky/oe-init-build-env > /dev/null; bitbake -c menuconfig virtual/kernel
