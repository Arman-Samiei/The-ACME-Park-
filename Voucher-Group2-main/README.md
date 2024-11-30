[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/j07qAgWR)


To run the client application (transponder.py), you need to pass it the transponder id and the target message exchange (like localhost)

To register or unregister a customer you need to publish a message like this in entry_gate.filter:
{"verb": "register", "parameter": "1"} which means that we want to register the transponder with id 1.

To simulate the behavior of the gate, which opens upon receiving a registered transponder ID, I log 'Gate opened' for registered transponders and 'Gate not opened' for non-registered transponders.

![image](https://github.com/user-attachments/assets/70a53171-2573-48ed-a9b1-cd0de453bfda)
