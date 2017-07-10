#
# Copyright 2015 The CyanogenMod Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/full_base_telephony.mk)

# Inherit from libra device
$(call inherit-product, device/xiaomi/libra/device.mk)

# Inherit some common Carbon stuff.
$(call inherit-product, vendor/carbon/config/common.mk)

# Inherit telephony stuff
$(call inherit-product, vendor/carbon/config/gsm.mk)

# Set those variables here to overwrite the inherited values.
BOARD_VENDOR := Xiaomi
PRODUCT_BRAND := Xiaomi
PRODUCT_DEVICE := libra
PRODUCT_NAME := carbon_libra
PRODUCT_MANUFACTURER := Xiaomi
PRODUCT_MODEL := Mi-4c
TARGET_VENDOR := Xiaomi

PRODUCT_GMS_CLIENTID_BASE := android-xiaomi

# Boot animation
TARGET_SCREEN_HEIGHT := 1920
TARGET_SCREEN_WIDTH := 1080

# Fingerprint
PRODUCT_BUILD_PROP_OVERRIDES += \
     BUILD_FINGERPRINT="Xiaomi/libra/libra:7.0/NRD90M/V8.5.1.0.NXKCNED:user/release-keys" \
     PRIVATE_BUILD_DESC="libra-user 7.0 NRD90M V8.5.1.0.NXKCNED release-keys"


TARGET_OTA_ASSERT_DEVICE := libra
