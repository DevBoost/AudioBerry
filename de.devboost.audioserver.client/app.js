//===============================================
// Websocket Server 0.1
//===============================================

// Websocket-Server
var WebSocketServer = require('ws').Server
var wss = new WebSocketServer({host: '192.168.0.45/de.devboost.audioserver.webapp/websocket',port: 8080});

wss.on('connection', function(ws)
{
    console.log('client verbunden...');

    ws.on('message', function(message)
    {
        console.log('von Client empfangen: ' + message);
        ws.send('von Server empfangen: ' + message);
    });

});