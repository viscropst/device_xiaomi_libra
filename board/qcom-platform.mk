# RIL
TARGET_RIL_VARIANT := caf

# Power
TARGET_POWERHAL_VARIANT := none

# Enable peripheral manager
TARGET_PER_MGR_ENABLED := true
TARGET_PROVIDES_KEYMASTER := true
TARGET_HW_DISK_ENCRYPTION := true
TARGET_KEYMASTER_WAIT_FOR_QSEE := true

TARGET_INIT_VENDOR_LIB := libinit_msm
TARGET_RECOVERY_DEVICE_MODULES := libinit_msm
