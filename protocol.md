# IoT Device Protocol

This document describes the HTTP protocol an IoT device should use to connect to
the Python server in this repository while matching the current web client flow.

The IoT device is expected to handle Agora RTC itself. The Python server does not
carry microphone audio over HTTP; it only creates connection config and starts or
stops the Conversational AI agent.

## Base URL

When calling the Python server directly:

```text
http://<python-server-host>:8000
```

For local development on the same machine:

```text
http://localhost:8000
```

The web app calls the same contract through Next.js at `/api/*`, but an IoT
device that talks directly to the Python service should use the paths documented
below without the `/api` prefix.

## Session Flow

1. Call `GET /get_config`.
2. Read `data.app_id`, `data.token`, `data.channel_name`, `data.uid`, and
   `data.agent_uid`.
3. Join Agora RTC with:
   - App ID: `data.app_id`
   - Channel: `data.channel_name`
   - Token: `data.token`
   - Local RTC UID: numeric value of `data.uid`
4. Start the AI agent with `POST /v2/startAgent`.
   - `channelName` must equal `data.channel_name`.
   - `rtcUid` must be the numeric value of `data.agent_uid`.
   - `userUid` must be the numeric value of `data.uid`.
5. Send microphone audio through Agora RTC. The agent publishes its audio back
   into the same RTC channel.
6. Stop the agent with `POST /v2/stopAgent` using the returned `agent_id`.
7. Leave RTC and release device audio resources.

The server scopes the agent session to `userUid`, so the UID used in
`/v2/startAgent` must be the same UID the device used to join RTC.

## Endpoint: Get Config

Generate an Agora connection bundle for one conversation.

```http
GET /get_config
```

Optional query parameters:

| Name | Type | Description |
| --- | --- | --- |
| `channel` | string | Reuse an existing channel name. Omit this for a new generated channel. |
| `uid` | integer | Reuse a specific user RTC UID. Omit this for a generated user UID. |

Example:

```bash
curl "http://localhost:8000/get_config"
```

Example with explicit channel and UID:

```bash
curl "http://localhost:8000/get_config?channel=device-001-session&uid=4321"
```

Success response:

```json
{
  "code": 0,
  "data": {
    "app_id": "your_agora_app_id",
    "token": "007...",
    "uid": "4321",
    "channel_name": "device-001-session",
    "agent_uid": "58888506"
  },
  "msg": "success"
}
```

Field meanings:

| Field | Type | Description |
| --- | --- | --- |
| `app_id` | string | Agora App ID. |
| `token` | string | One-hour Agora RTC plus RTM token scoped to the returned channel and user UID. |
| `uid` | string | User RTC UID. Convert to a number for RTC APIs that require numeric UIDs. |
| `channel_name` | string | Agora RTC channel name. |
| `agent_uid` | string | RTC UID reserved for the AI agent. Convert to a number when starting the agent. |

## Endpoint: Start Agent

Start the Conversational AI agent in the RTC channel.

```http
POST /v2/startAgent
Content-Type: application/json
```

Request body:

```json
{
  "channelName": "device-001-session",
  "rtcUid": 58888506,
  "userUid": 4321
}
```

Fields:

| Field | Type | Required | Description |
| --- | --- | --- | --- |
| `channelName` | string | yes | The `channel_name` returned by `/get_config`. |
| `rtcUid` | integer | yes | The numeric `agent_uid` returned by `/get_config`. This is the agent's RTC UID. |
| `userUid` | integer | yes | The numeric `uid` returned by `/get_config`. This is the IoT device's RTC UID. |

Example:

```bash
curl -X POST "http://localhost:8000/v2/startAgent" \
  -H "Content-Type: application/json" \
  -d '{"channelName":"device-001-session","rtcUid":58888506,"userUid":4321}'
```

Success response:

```json
{
  "code": 0,
  "msg": "success",
  "data": {
    "agent_id": "agent-runtime-id",
    "channel_name": "device-001-session",
    "status": "started"
  }
}
```

Save `data.agent_id`. It is required to stop the agent.

Starting the agent is asynchronous at the Agora media layer. A successful HTTP
response means the start request was accepted and the runtime agent ID was
created. The device should still wait for the agent to join or publish audio in
the RTC channel before assuming the agent is ready to speak.

## Endpoint: Stop Agent

Stop a running Conversational AI agent.

```http
POST /v2/stopAgent
Content-Type: application/json
```

Request body:

```json
{
  "agentId": "agent-runtime-id"
}
```

Example:

```bash
curl -X POST "http://localhost:8000/v2/stopAgent" \
  -H "Content-Type: application/json" \
  -d '{"agentId":"agent-runtime-id"}'
```

Success response:

```json
{
  "code": 0,
  "msg": "success"
}
```

Call this before the device leaves RTC when possible. If the device loses
network connectivity, the agent also has an idle timeout configured by the
server.

## Token Renewal

Tokens expire after 3600 seconds. The device should renew before expiry while a
conversation is active.

To renew the RTC token, call `/get_config` with the same channel and UID:

```http
GET /get_config?channel=<current-channel-name>&uid=<current-user-uid>
```

Then pass the returned `data.token` to the device's Agora RTC token-renewal API.

Example:

```bash
curl "http://localhost:8000/get_config?channel=device-001-session&uid=4321"
```

Do not change channel or user UID during an active conversation. If either
changes, treat it as a new session and start over from `GET /get_config`.

## RTM and Transcript Events

The current web client also logs into Agora RTM with `String(uid)` and subscribes
to the same channel name to receive agent transcript and state events. If the IoT
device only needs audio, RTM is optional.

If the IoT device needs transcripts or agent state:

1. Log in to Agora RTM using the returned `app_id`, `token`, and user ID
   `String(uid)`.
2. Subscribe to the RTM channel named `channel_name`.
3. Start the agent after RTM subscription is ready.

The Python server starts the agent with RTM enabled and uses the same channel for
RTC and RTM events.

## Error Handling

HTTP errors use JSON.

Application errors usually have this shape:

```json
{
  "detail": "error message"
}
```

FastAPI request validation errors may return a structured `422` response:

```json
{
  "detail": [
    {
      "loc": ["body", "userUid"],
      "msg": "field required",
      "type": "value_error.missing"
    }
  ]
}
```

Client behavior:

- Treat non-2xx HTTP responses as failed requests.
- Treat successful HTTP responses with `code` other than `0` as failed requests.
- Do not start RTC without a successful `/get_config` response.
- Do not call `/v2/startAgent` with UIDs that differ from the active RTC session.
- Retry token renewal before expiry; if renewal fails and the token expires,
  stop the current session and reconnect from the beginning.

## Minimal Device State

The IoT device should keep these values for each active conversation:

```json
{
  "appId": "your_agora_app_id",
  "token": "007...",
  "channelName": "device-001-session",
  "userUid": 4321,
  "agentUid": 58888506,
  "agentId": "agent-runtime-id"
}
```

`agentId` is not known until `/v2/startAgent` succeeds.
