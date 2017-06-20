BOARD_USES_QCOM_HARDWARE := true

BOARD_USES_QC_TIME_SERVICES := true

# Power
TARGET_POWERHAL_VARIANT := none

# Enable peripheral manager
TARGET_PER_MGR_ENABLED := true

TARGET_HW_DISK_ENCRYPTION := true
TARGET_KEYMASTER_WAIT_FOR_QSEE := true

#TARGET_USE_SDCLANG := true
ifneq ($(HOST_OS),darwin)

SDCLANG := true

SDCLANG_PATH := prebuilts/snapdragon/llvm-3.8/bin

SDCLANG_LTO_DEFS := device/qcom/common/sdllvm-lto-defs.mk

endif
