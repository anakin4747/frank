#!/bin/bash

source ./src/poky/oe-init-build-env

bitbake -k core-image-minimal
