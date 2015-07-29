Matrix Assistant

Description
The Matrix Assistant (name pending) is composed of two parts, which are both connected via Bluetooth. The first is the 24x6 LED Matrix, which can display text on its matrix. The code of this matrix can be found inside my github (inside the finalMatrix folder under the LED_experiments repository). The second is an Android application, which sends data for the LED Matrix to display. In particular, the application has three modes: (1) display time, (2) display anything that the user wants the matrix to show, and (3) the answer to any query that a client sends to the application.

Current Problems
LED displays up to a 51-character text. nothing longer
In more detail: after arduino resets, it correctly displays all of the text. upon the next entry (that is entered into the android application), the arduino shows a 51 character limit)
Possible causes: some data lingers and is not cleared after the first entry`
Answers need to be formatted (ex. delete parentheticals)
Text displayed on display gets progressively faster
Possible solutions: add spaces (padding) at the end
