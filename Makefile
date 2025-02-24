# The goal of this Makefile is to remove the need to add configurations files
# in the ephemerial ./build directory
#
# This is done by generating a build/conf/bblayers.conf and setting variables in
# build/conf/local.conf all from this Makefile

MACHINE ?= t490
DISTRO ?= frank
IMAGE ?= core-image-minimal

SUBMODULES := $(shell ./scripts/gitmodules)

# Misc targets

.PHONY: help
help:
	@./scripts/list-make-targets $(MAKEFILE_LIST)

.PHONY: clean
clean: # Remove all but conf from build
	rm -rf build/{cache,downloads,tmp,sstate-cache}

.PHONY: distclean
distclean: # Remove build dir and submodules
	rm -rf build $(SUBMODULES)

.PHONY: menuconfig
menuconfig: # Kernel make menuconfig
	. ./src/poky/oe-init-build-env > /dev/null; bitbake -c menuconfig virtual/kernel

# Build targets

.PHONY: submodules
submodules: $(SUBMODULES) # Clone git submodules
$(SUBMODULES):
	git submodule update --init --recursive --force

build/conf/local.conf build/conf/bblayers.conf: submodules
	@. ./src/poky/oe-init-build-env > /dev/null

.PHONY: layers
layers: build/conf/bblayers.conf submodules # Find layers in ./src and save them to build/conf/bblayers.conf
	@./scripts/find-layers

.PHONY: machine
machine: build/conf/local.conf # Set MACHINE variable in build/conf/local.conf
	@./scripts/setvar MACHINE $(MACHINE)

.PHONY: distro
distro: build/conf/local.conf # Set DISTRO variable in build/conf/local.conf
	@./scripts/setvar DISTRO $(DISTRO)

.PHONY: build
build: layers machine distro submodules # Build yocto
	. ./src/poky/oe-init-build-env > /dev/null; bitbake -k $(IMAGE)
