# Warehouse

Simple application for managing warehouse.
Implemented as an assignment for university course - Mobile Services on the Internet.

The project consists of:
- Android client application
- Back-end server application (written in Java, Spring)

The main topic of this project is tackling the problem of data synchronization between back-end server and mobile client applications running on different devices. The gist of the problem is that users can make operations via the client app not only online but also offline. These operations consist of adding, editing and deleting products, as well as increasing and decreasing quantity of each product in the warehouse. When user does any operation offline, it is saved to device memory and sent to back-end server after the app goes online. The back-end server then tries to apply them to its database. If there are any conflicts (ex. trying to remove 100 units of certain product when the up-to-date quantity in the warehouse is only 10) then the appropriate information about these conflicts is sent back and displayed in client app. For the purpose of giving user an immediate feedback even in offline mode, a copy of products data from back-end server is stored in device memory and updated/synchronized whenever necessary. Besides solving the problem, I've also used this project as a practice of clean coding and organising codebase structure.
