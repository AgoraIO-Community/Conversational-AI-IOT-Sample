#pragma once

#include "agora_rtc_api.h"
#include "audio_config.h"

/* Just for debug mode, remove WIFI_SSID and WIFI_PWD if you want to connect your device to the network using the APP */
#define WIFI_SSID                           "NXIOT"
#define WIFI_PWD                            "88888888"

#define AGORA_CONVOAI_APP_VERSION           "1.0.3"

#define CONFIG_AGENT_SERVER_URL             "http://23.251.122.104:8888"

#define AGORA_CONVOAI_LOCAL_UID             1
#define AGORA_CONVOAI_AGENT_UID             11

#define DEFAULT_SDK_LOG_PATH                "io.agora.rtc_sdk"

#define GRAPH_NAME                          "va_openai_azure"
#define TENAI_LLM_MODEL                     "gpt-4o"
#define TENAI_RTC_PARAMES                   "{\\\"che.audio.custom_payload_type\\\":9}"
#define GREETING                            "Hello"
#define AGORA_CONVOAI_PROMPT                "You are a helpful assistant."
#define LANGUAGE                            "en-US"
#define VOICE_TYPE                          "male"