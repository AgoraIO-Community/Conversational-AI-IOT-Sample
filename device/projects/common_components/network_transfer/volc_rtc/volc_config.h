// Copyright (2025) Beijing Volcano Engine Technology Ltd.
// SPDX-License-Identifier: MIT

#if CONFIG_VOLC_HTTP_STARTUP_AGENT
// RTC APP ID
#define DEFAULT_RTC_APP_ID    "xxx"
// Server address
#define DEFAULT_SERVER_HOST   "xxx"
// Default agent ID
#define DEFAULT_END_POINT_ID  "xxx"
// Default voice ID
#define DEFAULT_VOICE_TYPE    "BV007_streaming"
#else
// RTC APP ID
#define DEFAULT_RTC_APP_ID  "zzzz"
// Server address
#define DEFAULT_ROOM_ID     "zzzz"
// Default agent ID
#define DEFAULT_USER_ID     "zzzz"
// Default voice ID
#define DEFAULT_TOKEN       "zzzz"
#endif


// (CONFIG_PCM_FRAME_LEN * 1000 / CONFIG_PCM_SAMPLE_RATE / CONFIG_PCM_CHANNEL_NUM /sizeof(int16_t))

#define DEFAULT_SDK_LOG_PATH "io.volc.rtc_sdk"