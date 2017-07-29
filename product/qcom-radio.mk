PRODUCT_COPY_FILES += \
    frameworks/native/data/etc/android.hardware.telephony.cdma.xml:system/etc/permissions/android.hardware.telephony.cdma.xml \
    frameworks/native/data/etc/android.hardware.telephony.gsm.xml:system/etc/permissions/android.hardware.telephony.gsm.xml

# Telephony packages
PRODUCT_PACKAGES += \
    Stk \
    CellBroadcastReceiver \
    telephony-ext

PRODUCT_BOOT_JARS += \
    telephony-ext
