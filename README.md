
# Description
App A just give me the latest weather description once I click on a button. The App B receives a message from App C which is the service and is received once its bound.
# Functionality Implemented
•	Used the onStart() and onStop to call the binding and unbinding in service

•	Used AsyncTask to thread the data send it to the service

•	Parsed JSON/XML feed, extracting weather description and stored it in List of weather objects

•	Displayed List object data in ListView

•	Implemented getView()

•	Implement a remote bound service that uses a messenger so that it can send a message to the client

•	Implemented a response handler to take in the message and respond to the service

•	Notification sent when service has started

