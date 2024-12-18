
MACHINE ?= "qemux86-64"
DISTRO ?= "frank"
IMAGE ?= "core-image-minimal"

all: help

.PHONY: find-layers
find-layers: ## Find layers in ./src and save them to bblayers.conf
	@$(shell find $(PWD)/src -path '*meta*conf/layer.conf*' \
	| xargs dirname \
	| xargs dirname \
	| awk 'BEGIN { print "BBPATH = \"$${TOPDIR}\"\nBBFILES ?= \"\"\nBBLAYERS ?= \"\\" } \
		{ print "\t", $$0, "\\" } \
		END { print "\"" }' > build/conf/bblayers.conf)

.PHONY: build
build: find-layers ## Build yocto
	. ./src/poky/oe-init-build-env > /dev/null; MACHINE=$(MACHINE) DISTRO=$(DISTRO) bitbake -k $(IMAGE)

.PHONY: help
help: ## List all make targets
	@echo "Makefile targets:"
	@grep -E '^[ a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | \
		awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-25s\033[0m %s\n", $$1, $$2}'
