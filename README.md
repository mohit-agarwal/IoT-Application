# IoT-Application
Internet of Things(IoT) based Plaform for Ambulance Routing Use case. 

The project consists of 3 modules:

**Module 1 - Gateway Sensor Network**

Here different types of sensors collect data from their surroundings and repeatedly send this collected information to the gateway it is connected to. The Gateways are written in Android. This communication between the sensor devices and the Gateway server takes place through an API.

**Module 2 - Filter Server and Database (Repository and Registry)**

The Database is implemented in mongoDB which is the Core of the project where all the important information is being stored and all the three major parts , the Gateway server , the Filter server and the Logic/App server communicate through a common API to fetch and gain control of the information for their central functioning and also for storing the updates made.

**Module 3 - Logic Server and App Engine**

For gaining access to  processed and filtered information the Logic-server / App-engine has to register itself with the Filter-Server but for this purpose it first needs to gain control over the sensors that are specific to the type of information needed by the App-engine and that too within a certain range.
 
