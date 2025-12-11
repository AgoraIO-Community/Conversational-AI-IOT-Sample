#ifndef __AGORA_LED_BLINK_H__
#define __AGORA_LED_BLINK_H__

#ifdef __cplusplus
extern "C" {
#endif


#include <stdint.h>
#include <os/os.h>

#define MAX_LED_NUM  4
#define RED_LED      40
#define GREEN_LED      41
#define LED_FAST_BLINK_TIME   200    //ms
#define LED_SLOW_BLINK_TIME   1000
#define LED_LAST_FOREVER      0xFFFFFFFF
typedef enum {
    LED_OFF = 0, //off 
    LED_ON,      //on
    LED_FAST_BLINK,   //fast blinking
    LED_SLOW_BLINK,   // slow blinking
    LED_ALTERNATE
} LedState;

// Error type definition (sorted by priority from high to low)
typedef enum {
    ERROR_CRITICAL = 0, // Highest priority, fast blink (e.g., critical fault)
    ERROR_WARNING,      // Secondary priority, slow blink (e.g., normal warning)
    ERROR_TYPE_COUNT    // No error marker
} ErrorType;


typedef struct {
    uint8_t gpio_num;       // GPIO number
    LedState state;         // Current state
    beken_timer_t timer;    // Timer handle
    uint32_t interval;      // Current blink interval
    bool led_status;        // Current physical state
    int alt_partner;        // Binding group
    uint32_t blink_duration; // Blink duration
    uint32_t blink_start;   // Start time

    int error_counts[ERROR_TYPE_COUNT]; // Error counter for each type
    ErrorType active_error;    // Currently active error type
} LedControlBlock;

typedef enum
{
    LED_OFF_GREEN,
    LED_ON_GREEN,
    LED_FAST_BLINK_GREEN,
    LED_SLOW_BLINK_GREEN,

    LED_OFF_RED,
    LED_ON_RED,
    LED_FAST_BLINK_RED,
    LED_SLOW_BLINK_RED,

    LED_REG_GREEN_ALTERNATE,
    LED_REG_GREEN_ALTERNATE_OFF,

    LED_ERROR_CRITICAL,
    LED_ERROR_WARNING,
    LED_ERROR_CRITICAL_CLOSE,
    LED_ERROR_WARNING_CLOSE,
}led_operate_t;

/// @brief led init
void led_driver_init();


/// @brief control led
/// @param oper the type of led event
/// @param time the duration of LED's luminescence
void led_app_set(led_operate_t oper, uint32_t time);

#ifdef __cplusplus
}
#endif
#endif