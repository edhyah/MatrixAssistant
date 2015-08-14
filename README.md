# Neo

Neo is a **smart, interactive, Arduino-powered LED matrix** that serves as a **visual personal assistant** for anyone who uses a modern **Android phone**. Through his Bluetooth connection with an Android application, he can display the current time digitally or echo any text sent to him through the app. Neo's best feature, however, is his ability to answer any question that you ask him, just like Apple's Siri or Microsoft's Cortana.

## Detailed Description

After connecting Neo with the Android application available inside the Assistant folder of this repository, Neo can:

##### Display Current Time

Neo will display the current local time digitally if the user is on the Time tab. The current time will also be displayed if the user is not currently on the app, as long as the app is running in the background of the Android device with a maintained Bluetooth connection to Neo.

##### Echo Text

In the echo tab, the user can input text into the text field, either through a keyboard or by voice. Upon pressing the send button, the text is sent to Neo and is displayed visually.

##### Answer Queries

The user can input any queries into the text field of the query tab, either through a keyboard or by voice. Neo will answer this query visually after the question mark button is pressed.

## How does Neo work?

Neo works from the interaction of two components: a **24x8 LED matrix** and an **Android application**. In general, the Android application sends strings for the LED matrix to display.

##### 24x8 LED Matrix

The LED matrix is composed of 192 blue diffused LEDs that are controlled by shift registers. These shift registers receive data to display from an Arduino Uno. The core of the Arduino code is the displayText function, that takes in a string to display and a boolean that decides whether the text should remain static or scroll across the matrix from left to right. Strings are received serially via a Bluetooth connection with the Android application.

##### Android Application

The Android application builds a minimalist user interface that a user can operate to send data to the LED matrix. It consists of three tabs: Time, Echo and Query.

###### Time

By pressing the Time tab, the user lets the matrix display the current time. The time is retrieved and updated in sync with the phone's current time.

###### Echo

In the echo tab, the user inputs text through a keyboard or by voice. Google's voice recognition service is used to convert sound into text. Upon pressing the send button, the text is sent to the LED matrix and displayed.

###### Query

In this tab, the user can input any queries into the text field, either through a keyboard or by voice. Like the Echo tab, Google's speech recognition service is used to convert voice into text. After pressing the question mark, the Android application computes the answer to this query. This is done in two steps: (1) obtain an answer through a question-answering API and (2) simplify the answer through a natural language processing API. The result is sent to the LED matrix to be displayed.

##### External APIs Used
- Voice-Actions API to compute queries
- OpenNLP API to process/simplify answers from the QA service
- Google SpeechRecognizer API to convert voice into text

## Resources Needed
- 24x8 LED Matrix (more details available at http://github.com/edhyah/LED_experiments/tree/master/finalMatrix)
- Android phone running a minimum OS version of 4.0.3 (Kitkat) with an Internet connection and Bluetooth capabilities
- Either a 9V - 12V power source or a 9V battery
- HC-06 Bluetooth slave module

## Installation

1. Connect the LED matrix to an Arduino Uno. Do this by connecting the power and ground of the matrix to the 5V and ground pins of the Arduino, and by connecting the SER, LATCH, and CLK lines (data lines for the daisy-chained shift registers) to pins 8, 9 and 10 on the Arduino, respectively.
2. Acquire an HC-06 Bluetooth module and connect it to the Arduino. Do this by connecting the power and ground of the HC-06 to the 3.3V and ground pins of the Arduino, and by connecting the data lines to the TX and RX pins on the Arduino.
3. Load the Arduino code available in this repository (inside the Matrix folder) onto the Arduino. Make sure to disconnect the TX and RX pins on the Arduino before loading the code via a serial cable to avoid loading problems.
4. Load the apk available in this repository onto the Android device.

## Executing Neo

1. Turn the LED matrix on by powering the Arduino. Make sure the HC-06 is blinking to ensure that it is being powered.
2. Open the android application provided from this repository. Turn on Bluetooth when requested.
3. Press *Connect*. It can be located by pressing the action overflow button on the action bar of the app.
4. Select HC-06 among the list of available devices.
5. Confirm that the Bluetooth connection is secured by waiting for the current time to be displayed on Neo.