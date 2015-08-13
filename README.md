# Neo

**Neo** is a smart, interactive LED matrix that serves as a personal assistant. While his main purpose is to display the current time digitally, Neo can also display any other text sent to him via an Android device connected to Neo through Bluetooth. Furthermore, Neo can answer any question (preferably closed questions) that you ask him.

## Detailed Description

Neo is composed of two parts: a **24x8 LED matrix** and an **Android application**.

### 24x8 LED Matrix

The LED matrix is composed of 192 blue diffused LEDs that are controlled by shift registers. These shift registers receive data to display from an Arduino Uno. The core of the Arduino code is the displayText function, that takes in a string to display and a boolean that decides whether the text should scroll across the matrix from left to right. The Arduino receives strings that will be displayed serially via a Bluetooth connection with an Android application.

### Android Application

The Android application builds a minimalist user interface that a user can operate to send data to the LED matrix. It consists of three tabs: Time, Echo and Query.

#### Time

By pressing the Time tab, the user lets Neo display the current time onto his matrix. This time is updated in sync with the phone's current time.

#### Echo

In the echo tab, the user can input text into the text field, either through a keyboard or by voice. Upon pressing the send button, the text is sent to the LED matrix and displayed.

#### Query

In this tab, the user can input any queries into the text field, either through a keyboard or by voice. After pressing the question mark, the Android application computes the answer to this query and sends its answer to the LED matrix to be displayed.

## Installation

1. Acquire a 24x8 LED matrix. More details on this are available at http://github.com/edhyah/LED_experiments/tree/master/finalMatrix
2. Connect the LED matrix to an Arduino Uno.
3. Acquire an HC-06 Bluetooth module and connect it to the Arduino.
4. Load the Arduino code available in this repository onto the Arduino.
5. Load the apk available in this repository onto an Android device. Kitkat is the minimum OS requirement.

## Resources Used
- Voice-Actions API to compute queries
- OpenNLP API to process/simplify answers from the QA service