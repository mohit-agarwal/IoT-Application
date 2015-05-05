var net = require('net'),
    JsonSocket = require('json-socket')
    , mongo = require('mongodb').MongoClient;

var port = 8081;
var server = net.createServer();
server.listen(port);
server.on('connection', function(socket) { //This is a standard net.Socket
    console.log("Gateway connected");
    socket = new JsonSocket(socket); //Now we've decorated the net.Socket to be a JsonSocket
    socket.on('data', function(req) {
        console.log("message");
        console.log(req.toString());
        //                           socket.sendEndMessage("hello");
    });
});
