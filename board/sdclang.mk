ifneq ($(HOST_OS),darwin)

SDCLANG := true

SDCLANG_PATH := prebuilts/snapdragon/llvm-3.8/bin

SDCLANG_LTO_DEFS := device/qcom/common/sdllvm-lto-defs.mk

endif
