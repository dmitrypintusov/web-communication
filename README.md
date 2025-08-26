# From request to response: ​Improving web communication​

---

This repository contains materials related to the presentation "From request to response: improving web communication" held on June 9, 2025.

## Presenter
**Dzmitry Pintusau**  
Solution Architect, Andersen

### Contact

* **Email**: dzmitrypintusov@gmail.com
* **LinkedIn**: [www.linkedin.com/in/dmitry-pintusov](https://www.linkedin.com/in/dmitry-pintusov)

## Watch the meetup recording on YouTube
[Watch the video](https://www.youtube.com/watch?v=MXrfU2oV3y4&list=PLdyUrdn1emW0TzXrT7QUBybOp_7VrieO2&index=87)

## Project details

This project demonstrates the concepts discussed in the presentation and is built using the following technologies:

* **Java 24**
* **Spring Boot 3.5**
* **Gemini AI Integration**
* **Dependencies:**
    * `spring-boot-starter-webflux`
    * `spring-ai-starter-model-vertex-ai-gemini`
    * `spring-grpc-spring-boot-starter`
    * `spring-boot-starter-websocket`
    * `netty-incubator-codec-http3`

## Branch Overview

* `origin/web-communication-part-1`: This branch illustrates **REST**, **gRPC**, **WebSocket**, and **Server-Sent Events (SSE)** approaches to web communication.
* `origin/web-communication-part-2`: This branch demonstrates the implementation of the **QUIC protocol** for web communication.
 
## Prerequisites for running the project

To successfully run this project, ensure you have the following configured:

1.  **Java 24 SDK**:
    * Download and install the Java 24 Development Kit (SDK) from OpenJDK distribution (e.g., [jdk.java.net](https://jdk.java.net/24/)) or any of your choice.
    * Verify your Java installation by running `java -version` in your terminal.

2.  **Gemini API project configuration**:
    * Configure your Google Cloud project to enable the Gemini API. Follow the official documentation on how to set up your project for Generative AI inference: [https://cloud.google.com/vertex-ai/generative-ai/docs/model-reference/inference](https://cloud.google.com/vertex-ai/generative-ai/docs/model-reference/inference)
    * Ensure you have the necessary service account keys or proper authentication configured for your Spring AI application to access the Gemini API.

3.  **Self-signed certificate for TLS/SSL (keystore.p12)**:
    * Generate a self-signed PKCS#12 keystore file named `keystore.p12` and place it in the `src/main/resources` folder of the project. This is required for certain secure communication protocols (e.g., QUIC, gRPC with TLS).
    * You can generate this file using `keytool` (which comes with the Java SDK) or `openssl`. Here's a common `keytool` command:

        ```bash
        keytool -genkeypair -alias myalias -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore src/main/resources/keystore.p12 -validity 365 -dname "CN=localhost, OU=YourOrgUnit, O=YourOrg, L=YourCity, ST=YourState, C=YourCountry"
        ```
        * Replace `myalias`, `YourOrgUnit`, `YourOrg`, `YourCity`, `YourState`, and `YourCountry` with your desired values.
        * You will be prompted to enter a password for the keystore. **Remember this password** as you'll need it for the next step.

4.  **Environment variables**
    * Set an environment variable named `JKS_PASSWORD` that holds the password you set for the `keystore.p12` file. The application will use this to access the keystore.
    * Set an environment variable named `GOOGLE_PROJECT_ID`specifying the Google Cloud Project ID with enabled Gemini API.

    * **For Linux/macOS:**
        ```bash
        export JKS_PASSWORD="your_keystore_password"
        export GOOGLE_PROJECT_ID="your_google_project_id"
        ```
    * **For Windows (Command Prompt):**
        ```cmd
        set JKS_PASSWORD="your_keystore_password"
        set GOOGLE_PROJECT_ID="your_google_project_id"
        ```
    * **For Windows (PowerShell):**
        ```powershell
        $env:JKS_PASSWORD="your_keystore_password"
        $env:GOOGLE_PROJECT_ID="your_google_project_id"
        ```
    * **Note**: For production environments, it's highly recommended to use more secure methods for managing sensitive information like passwords (e.g., secret management services).

6.  **Install `grpcurl` and `Cloudflare curl`**:
    * **`grpcurl`**: A command-line tool for interacting with gRPC servers.
    * **Installation instructions**: Refer to the official `grpcurl` GitHub repository for binaries or your package manager: [https://github.com/fullstorydev/grpcurl](https://github.com/fullstorydev/grpcurl)
    * **Example (Homebrew for macOS):** `brew install grpcurl`

* **HTTP/3-enabled `curl`**: To test HTTP/3 (QUIC) endpoints, you need a `curl` version compiled with HTTP/3 support.
    * **Check existing `curl`**: Run `curl --version | grep Features | grep H3`. If `H3` is listed, your `curl` supports HTTP/3.
    * **Installation on macOS (Homebrew)**:
      ```bash
      brew remove -f curl # Remove existing curl if installed via homebrew
      brew install cloudflare/cloudflare/curl
      # Add curl to your PATH. Adjust if you use .bashrc or another shell config file.
      echo 'export PATH="/opt/homebrew/opt/curl/bin:$PATH"' >> ~/.zshrc
      source ~/.zshrc
      ```

* **`websocat`**: A command-line client for WebSockets, useful for sending and receiving messages.
  * **Installation instructions**: Refer to the `websocat` GitHub repository for various installation methods (package managers, cargo, pre-built binaries): [https://github.com/vi/websocat](https://github.com/vi/websocat)
  * **Example (Homebrew for macOS):** `brew install websocat`

## Usage examples

Once the project is running, you can use `curl` and `grpcurl` to interact with the endpoints:

### REST

```bash
curl "http://localhost:8080/api/rest?question=REST" --verbose
```

### SSE

```bash
curl -N "http://localhost:8080/api/sse?question=SSE"
```

### WebSocket

```bash
websocat ws://localhost:8080/ws/message
```
Print question and submit to receive the response.

### gRPC Unary

```bash
grpcurl -v -insecure -d '{"question": "What is Unary Grpc"}' \
  localhost:9443 WebCommunicationGrpcService/UnaryGrpc
```

### gRPC Server Stream

```bash
grpcurl -v -insecure -d '{"question": "What is server stream Grpc"}' \
  localhost:9443 WebCommunicationGrpcService/ServerStreamGrpc
```

### gRPC Client Stream

```bash
grpcurl -v -insecure -d '{"question": "What is client stream Grpc"}' \
  localhost:9443 WebCommunicationGrpcService/ClientStreamGrpc
```

### gRPC Bidirectional Stream

```bash
grpcurl -v -insecure -d '{"question": "What is bidirectional stream Grpc"}' \
  localhost:9443 WebCommunicationGrpcService/BidirectionalStreamGrpc
```

### QUIC (HTTP/3)

```bash
curl --http3 -k "https://localhost:8443/api/rest?question=HTTP3" --verbose
```
---

