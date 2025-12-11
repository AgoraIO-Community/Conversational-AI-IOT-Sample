# Agora BK7258 Demo Project

*[English](README.md) | English*

## Project Introduction

This demo demonstrates how to integrate the Agora RTSA Lite SDK with the BK7258 AI Robotic Kid development board to implement audio and video calling functionality.

### File Structure
```
├── beken_genie
│   ├── CMakeLists.txt
│   ├── config
│   │   ├── bk7258
│   │   │   ├── bk7258_partitions.csv
│   │   │   ├── config
│   │   │   ├── configuration.json
│   │   │   ├── partitions.csv
│   │   │   └── usr_gpio_cfg.h
│   │   ├── bk7258_cp1
│   │   │   ├── config
│   │   │   └── usr_gpio_cfg.h
│   │   └── bk7258_cp2
│   │       └── config
│   ├── main
│   │   ├── app_main.c
│   │   ├── app_main.h
│   │   ├── audio_para.c
│   │   ├── CMakeLists.txt
│   │   ├── include
│   │   │   ├── FifoBuffer.h
│   │   │   ├── fpscc.h
│   │   │   └── ota_display.h
│   │   ├── Kconfig.projbuild
│   │   ├── vendor_flash.c
│   │   └── vendor_flash_partition.h
│   └── pj_config.mk
└── common_components
    ├── asr
    │   ├── armino_asr.c
    │   ├── armino_asr.h
    │   ├── CMakeLists.txt
    │   └── Kconfig
    ├── audio_engine
    │   ├── audio_config.h
    │   ├── audio_dump_data.c
    │   ├── audio_dump_data.h
    │   ├── audio_engine.c
    │   ├── audio_engine.h
    │   ├── audio_log.h
    │   ├── audio_transfer.c
    │   ├── audio_transfer.h
    │   ├── CMakeLists.txt
    │   └── Kconfig
    ├── bk_app_event
    │   ├── app_event.c
    │   ├── app_event.h
    │   ├── CMakeLists.txt
    │   ├── Kconfig
    │   └── prompt_tone.h
    ├── bk_boarding_service
    │   ├── bk_genie_comm.h
    │   ├── boarding_core.c
    │   ├── boarding_service.c
    │   ├── boarding_service.h
    │   ├── CMakeLists.txt
    │   ├── Kconfig
    │   ├── wifi_boarding_internal.h
    │   ├── wifi_boarding_utils.c
    │   └── wifi_boarding_utils.h
    ├── bk_bt
    │   ├── a2dp_sink
    │   │   ├── a2dp_sink_demo.c
    │   │   ├── a2dp_sink_demo.h
    │   │   ├── mpeg4_get_bits.h
    │   │   ├── mpeg4_latm_dec.c
    │   │   ├── mpeg4_latm_dec.h
    │   │   ├── ring_buffer_node.c
    │   │   └── ring_buffer_node.h
    │   ├── a2dp_sink_demo_cli.c
    │   ├── bt_manager.c
    │   ├── bt_manager.h
    │   ├── CMakeLists.txt
    │   ├── headset_user_config.h
    │   ├── hfp_hf
    │   │   ├── hfp_hf_demo.c
    │   │   ├── hfp_hf_demo.h
    │   │   ├── ring_buffer_particle.c
    │   │   └── ring_buffer_particle.h
    │   ├── Kconfig
    │   ├── pan
    │   │   ├── bt_comm_list.c
    │   │   ├── bt_comm_list.h
    │   │   ├── bt_manager.c
    │   │   ├── bt_manager.h
    │   │   ├── hidd_service.c
    │   │   ├── hidd_service.h
    │   │   ├── pan_demo_cli.c
    │   │   ├── pan_service.c
    │   │   ├── pan_service.h
    │   │   └── pan_user_config.h
    │   └── storage
    │       ├── bluetooth_storage.c
    │       └── bluetooth_storage.h
    ├── bk_countdown
    │   ├── CMakeLists.txt
    │   ├── countdown_app.c
    │   ├── countdown_app.h
    │   ├── countdown.c
    │   ├── countdown.h
    │   └── Kconfig
    ├── bk_key_app
    │   ├── CMakeLists.txt
    │   ├── Kconfig
    │   ├── key_app_config.h
    │   ├── key_app_service.c
    │   └── key_app_service.h
    ├── bk_led_blink
    │   ├── CMakeLists.txt
    │   ├── Kconfig
    │   ├── led_app.c
    │   ├── led_app.h
    │   ├── led_blink.c
    │   └── led_blink.h
    ├── bk_motor
    │   ├── CMakeLists.txt
    │   ├── Kconfig
    │   ├── motor.c
    │   └── motor.h
    ├── bk_smart_config
    │   ├── CMakeLists.txt
    │   ├── include
    │   │   ├── bk_smart_config_agora_adapter.h
    │   │   ├── bk_smart_config.h
    │   │   ├── bk_smart_config_lingxin_adapter.h
    │   │   ├── bk_smart_config_volc_adapter.h
    │   │   └── bk_smart_config_wss_adapter.h
    │   ├── Kconfig
    │   └── src
    │       ├── adapter
    │       │   ├── agora
    │       │   │   └── bk_smart_config_agora_adapter.c
    │       │   ├── lingxin
    │       │   │   └── bk_smart_config_lingxin_adapter.c
    │       │   ├── volc
    │       │   │   └── bk_smart_config_volc_adapter.c
    │       │   └── wss
    │       │       └── bk_smart_config_wss_adapter.c
    │       └── core
    │           └── bk_smart_config_core.c
    ├── dual_screen_avi_play
    │   ├── CMakeLists.txt
    │   ├── Kconfig
    │   ├── lv_font.c
    │   ├── lvgl_app.c
    │   ├── lvgl_ui.c
    │   ├── lvgl_ui.h
    │   └── ota_display.c
    ├── network_transfer
    │   ├── agora_rtc
    │   │   ├── agora_config.h
    │   │   ├── agora_convoai_iot.c
    │   │   ├── agora_convoai_iot.h
    │   │   ├── agora_debug.c
    │   │   ├── agora_rtc.c
    │   │   ├── agora_rtc.h
    │   │   └── agora_rtc_main.c
    │   ├── bk_wss
    │   │   ├── bk_wss.c
    │   │   ├── bk_wss_config.h
    │   │   ├── bk_wss_debug.c
    │   │   ├── bk_wss_debug.h
    │   │   ├── bk_wss.h
    │   │   ├── bk_wss_main.c
    │   │   └── bk_wss_private.h
    │   ├── CMakeLists.txt
    │   ├── Kconfig
    │   ├── lingxin_wss
    │   │   └── lingxin_wss_main.c
    │   ├── network_transfer.c
    │   ├── network_transfer.h
    │   └── volc_rtc
    │       ├── RtcBotUtils.c
    │       ├── RtcBotUtils.h
    │       ├── RtcHttpUtils.c
    │       ├── RtcHttpUtils.h
    │       ├── volc_config.h
    │       ├── volc_rtc.c
    │       ├── volc_rtc.h
    │       └── volc_rtc_main.c
    ├── resource
    │   ├── agent_joined_16k_mono_16bit_en.mp3
    │   ├── agent_joined_16k_mono_16bit_en.wav
    │   ├── agent_offline_16k_mono_16bit_en.mp3
    │   ├── agent_offline_16k_mono_16bit_en.wav
    │   ├── agent_start_fail_16k_mono_16bit_en.mp3
    │   ├── agent_start_fail_16k_mono_16bit_en.wav
    │   ├── angry.avi
    │   ├── asr_standby_16k_mono_16bit_en.mp3
    │   ├── asr_standby_16k_mono_16bit_en.wav
    │   ├── asr_wakeup_16k_mono_16bit_en.mp3
    │   ├── asr_wakeup_16k_mono_16bit_en.wav
    │   ├── curious.avi
    │   ├── genie_eye.avi
    │   ├── happy.avi
    │   ├── love.avi
    │   ├── low_voltage_16k_mono_16bit_en.mp3
    │   ├── low_voltage_16k_mono_16bit_en.wav
    │   ├── network_provision_16k_mono_16bit_en.mp3
    │   ├── network_provision_16k_mono_16bit_en.wav
    │   ├── network_provision_fail_16k_mono_16bit_en.mp3
    │   ├── network_provision_fail_16k_mono_16bit_en.wav
    │   ├── network_provision_success_16k_mono_16bit_en.mp3
    │   ├── network_provision_success_16k_mono_16bit_en.wav
    │   ├── neutral.avi
    │   ├── ota_image.jpg
    │   ├── ota_update_fail_16k_mono_16bit_en.mp3
    │   ├── ota_update_fail_16k_mono_16bit_en.wav
    │   ├── ota_update_start_16k_mono_16bit_en.wav
    │   ├── ota_update_success_16k_mono_16bit_en.mp3
    │   ├── ota_update_success_16k_mono_16bit_en.wav
    │   ├── reconnect_network_16k_mono_16bit_en.mp3
    │   ├── reconnect_network_16k_mono_16bit_en.wav
    │   ├── reconnect_network_fail_16k_mono_16bit_en.mp3
    │   ├── reconnect_network_fail_16k_mono_16bit_en.wav
    │   ├── reconnect_network_success_16k_mono_16bit_en.mp3
    │   ├── reconnect_network_success_16k_mono_16bit_en.wav
    │   ├── rtc_connection_lost_16k_mono_16bit_en.mp3
    │   ├── rtc_connection_lost_16k_mono_16bit_en.wav
    │   ├── sad.avi
    │   ├── sleepy.avi
    │   ├── surprise.avi
    │   └── thinking.avi
    ├── single_screen_avi_play
    │   ├── CMakeLists.txt
    │   ├── Kconfig
    │   ├── lvgl_app.c
    │   ├── lvgl_ui.c
    │   └── ota_display.c
    ├── single_screen_font_display
    │   ├── CMakeLists.txt
    │   ├── Kconfig
    │   ├── lv_comm_list.c
    │   ├── lv_comm_list.h
    │   ├── lvgl_app.c
    │   ├── lvgl_ui.c
    │   └── ota_display.c
    └── video_engine
        ├── CMakeLists.txt
        ├── Kconfig
        ├── video_config.h
        ├── video_dump_data.c
        ├── video_dump_data.h
        ├── video_engine.c
        ├── video_engine.h
        └── video_log.h

```

## Environment Setup

### Hardware Requirements

This demo currently only supports the `BK7258 AI Robotic Kid` development board.

## Build and Flash

### Linux Operating System

#### Get bk_aidk Framework Project

This demo supports bk_aidk branch ai_release/v[2.0.1] and later. The demo uses tag ai_release/v[2.0.1.8] by default (commit id: 14f49e17332828700ff51e95d89d3090f37e70f1).

For development environment setup, please refer to BK official documentation: https://docs.bekencorp.com/arminodoc/bk_idk/bk7258/zh_CN/v2.0.1/get-started/index.html

After confirming that you have obtained the relevant project download permission from BK official, get the `bk_aidk` project from `github` as follows:

```bash
$ git clone --recurse-submodules https://github.com/bekencorp/bk_aidk.git -b ai_release/v2.0.1
$ git checkout ai_release/v2.0.1.8
$ git submodule update --recursive
```

#### Modify bk_aidk Project

1. Copy the `projects` directory to the `bk_aidk` project, completely replacing the `projects` directory (it is recommended to delete the original BK official `projects` directory first before copying):
```bash
$ rm -rf ${bk_aidk_path}/projects
$ cp -r ./projects ${bk_aidk_path}/projects
```
2. Modify the `projects/common_components/network_transfer/agora_rtc/agora_config.h` configuration file to set the server URL to your own deployed server, for example:
```
#define CONFIG_AGENT_SERVER_URL         "http://192.168.1.100:5001"
```

#### Build Firmware

In the bk_aidk project directory, build the demo firmware:
```bash
$ cd ${bk_aidk_path}
$ make bk7258 PROJECT=beken_genie
```
Note: The generated bin file is stored at `build/beken_genie/bk7258/all-app.bin`, and the corresponding OTA upgrade file is stored at `build/beken_genie/bk7258/encrypt/app_pack.rbl`.

#### Flash Firmware
After building the firmware, refer to the BK official documentation and use the flash tool to download the firmware: https://docs.bekencorp.com/arminodoc/bk_idk/bk7258/zh_CN/v2.0.1/developer-guide/config_tools/bk_tool_bkfil.html

## How to Use the Demo

### Quick 5-Minute Experience

Notes:

1. Please use a Type-C data cable to connect to the development board's `USB TO UART` interface and connect to your computer.
2. This interface is also used for battery charging.
3. Please note the position of the `RST` button. When the flash tool cannot automatically restart the development board, you can manually restart to restore flashing capability.

### Register Your Own Agora Account

Next, we will guide you to create your own Agora account.
Reference documentation: https://doc.shengwang.cn/doc/convoai/restful/get-started/enable-service

#### Demo Operation

1. The development board will automatically start after inserting the battery or data cable.
2. If you haven't set the WiFi account and password for the development board, please long press `S1` for 5 seconds to enter network provisioning mode.
3. Use the companion APP (this demo only provides Android project, please build and install the APK yourself)
4. After successful network provisioning, you can say `hi, Armino` to wake up the device and start a conversation with the AI Agent.
5. After the conversation, you can say `bye bye, Armino` to exit the conversation with the AI Agent.
6. The development board will automatically enter deep sleep mode after 3 minutes of idle state. You can restart it using the `RST` button.
