# The goal of this Makefile is to remove the need to add configurations files
# in the ephemerial ./build directory
#
# This is done by generating a build/conf/bblayers.conf and setting variables in
# build/conf/local.conf all from this Makefile

MACHINE ?= "qemux86-64"
DISTRO ?= "frank"
IMAGE ?= "core-image-minimal"

# This help target lists all targets that are commented with a double ##
.PHONY: help
help:
	@echo "Makefile targets:"
	@grep -E '^[ a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-25s\033[0m %s\n", $$1, $$2}'

.PHONY: layers
layers: ## Find layers in ./src and save them to build/conf/bblayers.conf
	@find $(PWD)/src -path '*meta*conf/layer.conf*' \
		| xargs dirname \
		| xargs dirname \
		| awk '\
			BEGIN { print("BBPATH = \"$${TOPDIR}\"\nBBFILES ?= \"\"\nBBLAYERS ?= \"\\") } \
			{ print("   ", $$0, "\\") } \
			END { print("\"") }' > build/conf/bblayers.conf

.PHONY: machine
machine: ## Set MACHINE variable in build/conf/local.conf
	@grep -qE '^MACHINE' build/conf/local.conf
	sed -i 's/^MACHINE.*/MACHINE = $(MACHINE)/' build/conf/local.conf

.PHONY: distro
distro: ## Set DISTRO variable in build/conf/local.conf
	@grep -qE '^DISTRO' build/conf/local.conf
	sed -i 's/^DISTRO.*/DISTRO = $(DISTRO)/' build/conf/local.conf

.PHONY: build
build: layers machine distro ## Build yocto
	. ./src/poky/oe-init-build-env > /dev/null; bitbake -k $(IMAGE)
