
Next steps:
- just get it to boot on the new laptop and develop on the current one
- figure out what if you need an initramfs for the kernel to be able to access
  the device the rootfs is on
- figure out how to write it directly to the on board storage like how
  sd-installer does
- once you have it reliably booting on the new laptop then customize to the
  point you are productive and switch over the second laptop.

TODO:
 - add the build system as a recipe
   - Since I want to be able to use the same machine for building and testing I
     have to come up with a way to have the build be part of the OS some how.
     You could possibly have several boot entries which have different rootfs
     which allows you to build using one kernel and rootfs and then test on
     another. But then that makes it so you have to reboot to switch to between
     OS's which is pretty inconvienent. You could possibly use the same kernel
     for both and just chroot to the build rootfs when you need to build. Do I
     even need two rootfs's?
     Could devtool's deploy functionality help?
 - xserver
 - awesomewm
    - cleanup recipe for PATHS and optional dependencies
    - investigate why awesome crashes
 - neovim
    - fix install step for neovim to actually deploy nvim on the target
 - machine layer for systemd-boot
    - wks for partition layout
 - language servers
 <!-- - vim -->
 <!-- - vi -->
 <!-- - GNU core utils -->
 - tmux
    ```
    root@qemux86-64:~# tmux
    tmux: need UTF-8 locale (LC_CTYPE) but have ANSI_X3.4-1968
    ```
 - LVM partitioning
 - fix xcb-util-xrm to not look to your fork
 - st
 - default user
     - zsh - set as default shell
       maybe switch off of zsh it is buggy
 <!-- - have Capslock be Capslock and escape since esc esc would have no effect  -->
 <!--   but then you just always have to double escape! -->
 - have the sources be put in ~/src

Do security related stuff. Figure out SBOMs and get vulnscout working on it.
