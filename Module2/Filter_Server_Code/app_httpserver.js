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

var roads = {1:[2,3], 2:[4], 3:[5,7], 4:[5,6]};

socketio.listen(server).on('connection', function (socket) {
        mongo.connect('mongodb://127.0.0.1/iot', function(err,db){
            if(err) { throw err; }
            else {
            console.log("Successfully connected to database.");
            }
            
            socket.on('gateway_req', function(req){
                var repo_collection = db.collection('repositry');
                var gateway_id = parseInt(req.split(',')[1]);
                
                repo_collection.find({"GID":gateway_id}).toArray(function(err,res){
                    if(err) throw err;
                    var json_output = "";
                    for(var i=0; i< res.length; i++){
                        var obj = res[i];
                        json_output = json_output + obj["SID"] + "," + obj["sensor_type"] + ";"
                    }
                    console.log("Sending all sensors ids from Repositry for gateway id: "+gateway_id);
                    console.log("Gateway_req Response : " + json_output);
                    socket.emit('output',json_output);
                });
            });

            socket.on('heartbeat', function(req){
                var repo_collection = db.collection('repositry');
                var gateway_id = parseInt(req.split(',')[1]);
                console.log("Gateway " + gateway_id + " has sent a heartbeat. ");
                console.log("Gateway " + gateway_id + " is alive.");
            });

            socket.on('send_sensor_data', function(req){
                var reg_collection = db.collection('registry');
                var data = req.split(';');
                var timestamp = data[0].split(',')[1];
                console.log("Receiving data from gateway id: "+ data[0].split(',')[0]);
                for(var i=1; i< data.length; i++){
                    var sdata = data[i].split(',');
                    var sensor_type = sdata[1];
                    //reg_collection.remove({"SID":parseInt(sdata[0])});
                    //console.log("Removing "+sdata[0]);
                    if (sensor_type == "traffic"){
                        console.log("Inserting " + sdata);
                        reg_collection.update({"SID":parseInt(sdata[0])},{"SID":parseInt(sdata[0]),"sensor_type":sdata[1],"v":parseInt(sdata[2]),"d":parseInt(sdata[3]),"q":parseInt(sdata[4]),"timestamp":timestamp});
                    } else {
                        //console.log("Inserting " + sdata[0]);
                        console.log("Inserting " + sdata);
                        reg_collection.update({"SID":parseInt(sdata[0])},{"SID":parseInt(sdata[0]),"sensor_type":sdata[1],"health":parseInt(sdata[2]),"timestamp":timestamp});
                    }
                }
                console.log("Sensor Data Inserted into registry.");
            });
        

            socket.on('message', function(req){
                var reg_collection = db.collection('registry');
                var data  = req.split(',');
                var patient_id = parseInt(data[1]);
                if (data.length == 4){ // Acknowledgement
                        reg_collection.update({"SID":patient_id},{$set:{"RID": parseInt(data[2])}});
                } else {
                    var current_road, best_road;
                    var patient_data;
                    reg_collection.find({"SID":patient_id, "RID" : {$exists:true}}).toArray(function(err,patient_data){
                        if(err) throw err;
                        console.log(patient_data);
                        if (patient_data[0] != null)
                            current_road = patient_data[0]["RID"];
                        else
                            current_road = 1;
                        console.log("Current Road is " + String(current_road));

                        best_road = current_road;
                        var traffic_collection = db.collection('registry');
                        var max_q = -1;

                        for(var key in roads){
                        console.log("key is "+key);
                            if(current_road == key){
                                for (j in roads[key]) {
                                var dest_road = roads[key][j];
                                console.log("dest_road is " + dest_road);
                                traffic_collection.find({"sensor_type":"traffic", "SID":dest_road}).toArray(function(err,res){
                                    var record = res[0];
                                    console.log("q is " + record["q"]);
                                    if(record["q"] > max_q){
                                        max_q = record["q"];
                                        best_road = record["SID"];
                                        console.log("Updating max_q " + max_q);
                                        console.log("best_road " + best_road);
                                        socket.emit('road_data', best_road);
                                        reg_collection.update({"SID":patient_id},{$set:{"RID": parseInt(best_road)}});
                                    }    
                                });
                                }
                            }
                        }
                    });
                    //console.log("Best road is " + best_road);
                    //socket.emit('output', String(best_road));
                }
            });
            
            
  //          socket.on('message', function (msg) {
  //          console.log('Message Received: ', msg);
   //         console.log('Recieved query from the user for patient id ' + msg);
  //          socket.broadcast.emit('message', msg);
  //          });
        
    });
});
