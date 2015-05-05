from socketIO_client import SocketIO

def filter_server_response(*args):
        print 'Filter Server Response', args

socketIO = SocketIO('10.1.36.163', 8090)
socketIO.on('output', filter_server_response)
socketIO.wait(seconds=5)
socketIO.emit("gateway_req","1,100")
#socketIO.emit('heartbeat', "2,100")
#socketIO.emit('send_sensor_data', "100,12:32:12;1,traffic,10,20,30;2,traffic,30,10,10;3,traffic,44,10,20;4,traffic,5,23,16;5,traffic,90,10,1;6,traffic,55,22,65;7,traffic,45,32,12;8,patient,4;9,patient,3")
#socketIO.emit('send_sensor_data', "100,12:32:12;1,traffic,11,30,43;2,traffic,12,9,40;3,traffic,46,87,30;4,traffic,90,10,67;5,traffic,24,11,54;6,traffic,23,32,84;7,traffic,63,86,50;8,patient,3;9,patient,5")
#socketIO.emit('query_data',"Patient,8");
socketIO.wait(seconds=5)
