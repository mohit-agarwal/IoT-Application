#!/usr/bin/python           # This is client.py file

import socket               # Import socket module

s = socket.socket()         # Create a socket object
host = socket.gethostbyname('localhost')     # Get local machine name
port = 8090                # Reserve a port for your service.

print host
s.connect((host, port))
s.send('Thanks for connecting.')
#s.close        
