var fs = require('fs')
, http = require('http')
, socketio = require('socket.io')
, mongo = require('mongodb').MongoClient;

var server = http.createServer(function(req, res) {
        res.writeHead(200, { 'Content-type': 'text/html'});
        res.end(fs.readFileSync(__dirname + '/index.html'));
        }).listen(8090, function() {
            console.log('Listening at: http://localhost:8090');
            });

socketio.listen(server).on('connection', function (socket) {
        mongo.connect('mongodb://127.0.0.1/ias', function(err,db){
            if(err) { throw err; }
            else {
            console.log("Successfully connected to database.");
            }
            
            socket.on('gateway_id', function(gateway_id){
                var repo_collection = db.collection('repositry');
                repo_collection.find({"gateway_id":parseInt(gateway_id)}).toArray(function(err,res){
                    if(err) throw err;
                    console.log(res);
                    socket.emit('output',res);
                });
            });


        
            socket.on('message', function (msg) {
            console.log('Message Received: ', msg);
            socket.broadcast.emit('output', msg);
            });
        
    });
});
