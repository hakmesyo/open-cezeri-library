# -*- coding: utf-8 -*-
"""
Created on Wed Dec 11 12:54:17 2019

@author: BAP1
"""

from websocket_server import WebsocketServer

# Called for every client connecting (after handshake)
def new_client(client, server):
	print("New client connected and was given id %d" % client['id'])
	server.send_message_to_all("Hey all, a new client has joined us")


# Called for every client disconnecting
def client_left(client, server):
	print("Client(%d) disconnected" % client['id'])


# Called when a client sends a message
def message_received(client, server, message):
    server.send_message_to_all(message+" command from java was received")
#    if len(message)>200:
#        message=message[:200]
#    if message=='start' :        
#        startWebSocketClient()
#        open_camera()
#	print("Client(%d) said: %s" % (client['id'], message))


def startWebSocketServer():
    PORT=8888
    server = WebsocketServer(PORT)
    server.set_fn_new_client(new_client)
    server.set_fn_client_left(client_left)
    server.set_fn_message_received(message_received)
    server.run_forever()

startWebSocketServer()
