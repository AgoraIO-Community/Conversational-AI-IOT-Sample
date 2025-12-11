#pragma once

#ifdef __cplusplus
extern "C" {
#endif

#include <stdint.h>

#define COUNTDOWN_INFINITE 0xFFFFFFFF
/* Countdown ticket source types, sorted by priority */
typedef enum {
    COUNTDOWN_TICKET_PROVISIONING,   // Network provisioning countdown (5 minutes), highest priority
    COUNTDOWN_TICKET_NETWORK_ERROR,  // Network error countdown (5 minutes), medium priority
    COUNTDOWN_TICKET_STANDBY,        // Standby countdown (3 minutes), low priority
    COUNTDOWN_TICKET_OTA,      // OTA event, special logic, not involved in comparison
    COUNTDOWN_TICKET_MAX
} countdown_ticket_t;


void update_countdown(uint32_t s_active_tickets);

#ifdef __cplusplus
}
#endif