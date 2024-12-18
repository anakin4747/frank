
MACHINE ?= "qemux86-64"
DISTRO ?= "frank"
IMAGE ?= "core-image-minimal"

all: help

.PHONY: find-layers
find-layers: ## Find layers in ./src and save them to build/conf/bblayers.conf
	@$(shell find $(PWD)/src -path '*meta*conf/layer.conf*' \
	| xargs dirname \
	| xargs dirname \
	| awk '\
		BEGIN { print "BBPATH = \"$${TOPDIR}\"\nBBFILES ?= \"\"\nBBLAYERS ?= \"\\" } \
		{ print "\t", $$0, "\\" } \
		END { print "\"" }' > build/conf/bblayers.conf)

.PHONY: set-machine
set-machine: ## Set MACHINE variable in build/conf/local.conf
	@sed -i 's/^MACHINE.*/MACHINE = $(MACHINE)/' build/conf/local.conf

.PHONY: set-distro
set-distro: ## Set DISTRO variable in build/conf/local.conf
	@sed -i 's/^DISTRO.*/DISTRO = $(DISTRO)/' build/conf/local.conf


.PHONY: build
build: find-layers set-machine set-distro ## Build yocto
	. ./src/poky/oe-init-build-env > /dev/null; bitbake -k $(IMAGE)

.PHONY: help
help: ## List all make targets
	@echo "Makefile targets:"
	@grep -E '^[ a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-25s\033[0m %s\n", $$1, $$2}'
