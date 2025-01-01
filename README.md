
TODO:
 - xserver
 - awesomewm
    - add startx dependency for awesomewm
    - cleanup recipe for PATHS and optional dependencies
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
