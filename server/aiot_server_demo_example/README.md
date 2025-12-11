# Warning
This service project is only for developer quick experience and demonstration purposes. 
Do not use in production environment. Production environment services need to be developed by developers.

# IoT Conversational AI Server 🚀

[![Python 3.7+](https://img.shields.io/badge/python-3.7+-blue.svg)](https://www.python.org/downloads/)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

This server provides APIs for managing IoT devices and conversational AI agents, built with Python and supporting real-time communication via Agora RTC.

## Features
- 🎙️ Real-time voice communication
- 🤖 Conversational AI integration
- 🔐 Secure token generation
- 📦 Easy deployment options
- 📊 Comprehensive logging

## Requirements

- Python 3.7+ 
- Required packages:
  ```bash
  requests
  flask
  pyjwt
  ```

## Installation
1. Clone this repository
2. Install dependencies:
```bash
pip install -r requirements.txt
```

## Configuration

### Configuration File
Create a `config.json` file with the following structure and explanations:

```json
{
  // Agora related configuration
  "app_id": "YOUR_AGORA_APP_ID",  // Agora App ID
  "app_certificate": "YOUR_AGORA_APP_CERTIFICATE",  // Agora App Certificate
  
  // Customer authentication information
  "customer_key": "YOUR_CUSTOMER_KEY",  // Customer Key
  "customer_secret": "YOUR_CUSTOMER_SECRET",  // Customer Secret
  
  // Automatic Speech Recognition (ASR) configuration
  "asr": {
    "language": "zh-CN"  // Recognition language, default Chinese (supports Chinese-English mixed)
  },
  
  // System parameters configuration
  "parameters": {
    "output_audio_codec": "PCMA"  // RTC streaming audio codec format, supported formats: "PCMU" "PCMA" "G722" "OPUS" "OPUSFB"
    "transcript": {               // Subtitle feature parameter configuration
      "enable": false             // Disable subtitle feature
    }
  },
  
  // Text-to-Speech (TTS) configuration
  "tts": {
    "vendor": "YOUR_TTS_VENDOR",  // TTS service provider
    "params": {
      
    }
  },
  
  // Session timeout configuration
  "idle_timeout": 30,  // Session timeout (seconds)
  
  // Large Language Model (LLM) configuration
  "llm": {
    "url": "YOUR_LLM_API_URL",  // LLM service URL
    "params": {
      "model": "YOUR_LLM_MODEL"  // Model used
    },
    "api_key": "YOUR_LLM_API_KEY",  // LLM service API key
    "system_messages": [  // System preset messages
      {
        "role": "system",
        "content": "You are a helpful chatbot."
      }
    ],
    "max_history": 10,  // Maximum history records
    "greeting_message": "Hello, I'm your AI assistant. How can I help you?",  // Greeting message
    "failure_message": "Sorry, I am unable to answer your question..."  // Failure message
  }
}
```
For more detailed parameter configuration, see: ```https://doc.shengwang.cn/doc/convoai/restful/convoai/operations/start-agent```

## Running the Server
Start the server with:
```bash
python3 main.py
```

The server will run on port 5001 by default.

## API Documentation

### Base URL
`https://your-domain.com/api/v1`

### Authentication
All requests require an Authorization header:
```http
Authorization: Bearer <access_token>
```

### API Endpoints

### POST /device
Register a new device and generate RTC token

Request body:
```json
{
  "channel_name": "DEVICE_ID",
  "uid": DEVICE_USER_ID
}
```

### POST /agent/start 
Start a conversational AI agent

Request body:
```json
{
  "channel_name": "DEVICE_ID",
  "uid": DEVICE_USER_ID,
  "agent_uid": AGENT_USER_ID
}
```

### POST /agent/stop
Stop a conversational AI agent

Request body:
```json
{
  "agent_id": "AGENT_ID"
}
```

## Logging

Logs are written to stdout with the following format:
```
[timestamp] [level] - [message]
```

Log levels:
- DEBUG: Detailed debug information
- INFO: General operational messages
- WARNING: Indicates potential issues
- ERROR: Errors that need attention
- CRITICAL: Critical system failures

## Security Considerations

- Always keep your Agora credentials secure
- Use HTTPS in production environments
- Regularly rotate your access tokens
- Implement rate limiting for API endpoints

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Example Usage

```python
import requests

# Generate RTC token
response = requests.post(
    "https://your-domain.com/device",
    json={"channel_name": "12345", "uid": 1}
)
print(response.json())
```
