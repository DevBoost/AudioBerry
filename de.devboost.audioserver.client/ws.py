#!/usr/bin/python
import websocket
import thread
import time

def on_message(ws, message):
    import tempfile
    import subprocess
    import json
    data = json.loads(message);
    print data;
    if data["audio"]:
       tmp=tempfile.NamedTemporaryFile(suffix='.wav')
       tmp.write(data["audio"].decode('base64'))
       tmp.seek(0)
       print tmp.name
       subprocess.call(["aplay", tmp.name])
       tmp.close()

def on_error(ws, error):
    print error

def on_close(ws):
    print "### closed ###"

def on_open(ws):
    print "connected"


if __name__ == "__main__":
    websocket.enableTrace(True)
    ws = websocket.WebSocketApp("ws://devboost.de:8443/de.devboost.audioserver.webapp/websocket",
                                on_message = on_message,
                                on_error = on_error,
                                on_close = on_close)
    ws.on_open = on_open

    ws.run_forever()
