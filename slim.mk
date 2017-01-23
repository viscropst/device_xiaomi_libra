# Boot animation
TARGET_SCREEN_HEIGHT := 1280
TARGET_SCREEN_WIDTH := 800

# Inherit some common CM stuff.
$(call inherit-product, vendor/slim/config/common_full_phone.mk)

# Inherit device configuration
$(call inherit-product, device/xiaomi/libra/aosp_libra.mk)

## Device identifier. This must come after all inclusions
PRODUCT_NAME := slim_libra
PRODUCT_BRAND := Xiaomi
PRODUCT_MODEL := Mi-4c
TARGET_MANUFACTURER := Xiaomi
PRODUCT_RESTRICT_VENDOR_FILES := false

PRODUCT_BUILD_PROP_OVERRIDES += \
    PRODUCT_NAME=libra

# Assert
TARGET_OTA_ASSERT_DEVICE := libra,aqua

PRODUCT_BUILD_PROP_OVERRIDES += \
PRODUCT_NAME=libra \
PRIVATE_BUILD_DESC="libra-user 6.0.1 LMY47V V7.2.3.0.LXKCNDA release-keys" \
BUILD_FINGERPRINT=Xiaomi/libra/libra:6.0.1/LMY47V/V7.2.3.0.LXKCNDA:user/release-keys
