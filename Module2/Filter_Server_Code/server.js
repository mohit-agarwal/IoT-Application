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
        mongo.connect('mongodb://127.0.0.1/iot', function(err,db){
            if(err) { throw err; }
            else {
            console.log("Successfully connected to database.");
            }
        console.log("message");
        console.log("---> " + req.toString());
        req = req.toString();
        var header = req.split(';')[0];
        var req_id = parseInt(header.split(',')[0]);
        if(req_id == 2){
                console.log("[Heartbeat request]");
                var gateway_id = parseInt(header.split(',')[1]);
                console.log("Gateway " + gateway_id + " has sent a heartbeat. ");
                console.log("Gateway " + gateway_id + " is alive.");
        } else if (req_id == 1){
                    console.log("[Gateway ID request]");
                var repo_collection = db.collection('repositry');
                var gateway_id = parseInt(header.split(',')[1]);
                
                repo_collection.find({"GID":gateway_id}).toArray(function(err,res){
                    if(err) throw err;
                    var json_output = "";
                    for(var i=0; i< res.length; i++){
                        var obj = res[i];
                        json_output = json_output + obj["SID"] + "," + obj["sensor_type"] + ";"
                    }
                    console.log("Sending all sensors ids from Repositry for gateway id: "+gateway_id);
                    console.log("Gateway_req Response : " + json_output);
                    //socket.emit('output',json_output);
                });
        } else {
                console.log("[Sensor data request]");
                var reg_collection = db.collection('registry');
                var data = req.split(';');
                var timestamp = data[0].split(',')[2];
                console.log("Receiving data from gateway id: "+ data[0].split(',')[1]);
                for(var i=1; i< data.length; i++){
                    var sdata = data[i].split(',');
                    var sensor_type = sdata[1];
                    //reg_collection.remove({"SID":parseInt(sdata[0])});
                    //console.log("Removing "+sdata[0]);
                    if (sensor_type == "traffic"){
                        console.log("Inserting " + sdata);
                        reg_collection.update({"SID":parseInt(sdata[0])},{"SID":parseInt(sdata[0]),"sensor_type":sdata[1],"v":parseInt(sdata[2]),"d":parseInt(sdata[3]),"q":parseInt(sdata[4]),"timestamp":sdata[5]});
                    } else {
                        //console.log("Inserting " + sdata[0]);
                        console.log("Inserting " + sdata);
                        reg_collection.update({"SID":parseInt(sdata[0])},{"SID":parseInt(sdata[0]),"sensor_type":sdata[1],"health":parseInt(sdata[2]),"timestamp":sdata[3]});
                    }
                }
                console.log("Sensor Data Inserted into registry.");
        }
        //                  var result = message.a + message.b;
        //                           socket.sendEndMessage("hello");
        });
    });
});
