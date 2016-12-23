TARGET_BOOTLOADER_BOARD_NAME := libra

TARGET_NO_BOOTLOADER := true
TARGET_NO_RADIOIMAGE := true

TARGET_BOARD_INFO_FILE := $(DEVICE_PATH)/board-info.txt
TARGET_RELEASETOOLS_EXTENSIONS := $(DEVICE_PATH)
TARGET_RECOVERY_UI_LIB := librecovery_ui_nanohub

TARGET_USES_AOSP := true
TARGET_USES_INTERACTION_BOOST := true
