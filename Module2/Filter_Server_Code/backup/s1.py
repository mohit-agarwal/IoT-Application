import os
import socketIO_client


def fun(*args):
        print 'hello'

print(os.path.dirname(socketIO_client.__file__))

from socketIO_client import SocketIO, LoggingNamespace

socketIO = SocketIO('localhost', 8090)
socketIO.emit('message','aaa')
socketIO.wait(seconds=20)
socketIO.on('message', fun)
