# ST10166526_PROG1A
QuickChat Application

A Java Swing-based chat simulator that guides users through registration, login, and message sending with robust validation, user-friendly dialogs, JSON persistence, and automated testing.

Features

User Registration & Login

Username must include an underscore (_) and be no more than 5 characters.

Password must be at least 8 characters, include an uppercase letter, a digit, and a special character.

Cell phone number must start with +27 followed by 10 digits.

Clear error messages and examples on invalid input.

Messaging Flow

Only authenticated users can send messages.

Main menu: 1) Send Messages, 2) Show Recently Sent, 3) Quit.

Batch entry: specify the number of messages to send.

Per-message validation:

Recipient phone number format.

Message text length (1–250 characters).

Post-entry options:

Send: show Message details (ID, Hash, Recipient, Text).

Disregard: discard the message.

Store: append to messages.json in JSON format.

Message Hash format: <first2digitsOfID>:<sequenceNumber>:<firstWord><lastWord> (uppercase).

Summary of total messages sent.

Persistence

Stored messages are appended as JSON entries in messages.json.

Automated Testing & CI

JUnit 4 tests for registration, login, and messaging logic.

Ant build script (build.xml) supports clean, compile, test, and jar targets.

GitHub Actions workflow runs builds and tests on each push.

Prerequisites

Java 17 or later

Apache Ant

Project Structure

ChatApp/
├─ .github/workflows/ci.yml      # CI pipeline
├─ lib/                          # JUnit & Hamcrest jars
├─ src/com/mycompany/chatapp/    # Java source
│   ├─ ChatApp.java
│   ├─ Login.java
│   └─ Message.java
├─ test/com/mycompany/chatapp/   # JUnit tests
│   ├─ LoginTest.java
│   └─ MessageTest.java
├─ build.xml                     # Ant build script
├─ messages.json                 # Stored messages (created at runtime)
└─ README.md                     # This file

Build & Run

From the project root:

# Clean, compile, run tests, and package
ant clean test jar

# Run the application
java -jar dist/ChatApp.jar

Running Tests

ant test

All tests should pass (LoginTest + MessageTest).

Usage

Registration: follow prompts for username, password, and cell number.

Login: enter your registered credentials.

Messaging: choose "Send Messages", specify how many, and follow input prompts.

View Sent Messages: select "Show Recently Sent" from the menu.

Quit: choose "Quit" from the main menu.

Enjoy using QuickChat! Feel free to report issues or contribute on GitHub.
