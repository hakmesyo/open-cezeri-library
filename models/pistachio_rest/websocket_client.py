# -*- coding: utf-8 -*-
"""
Created on Fri Sep 17 08:09:38 2021

@author: cezerilab
"""

import websocket

ws=websocket.WebSocket();
ws.connect('ws://127.0.0.1:8887')
ws.send('merhaba from python nasılsın')


# import nest_asyncio
# nest_asyncio.apply()
# import asyncio
# import websockets

# async def echo(websocket, path):
#     async for message in websocket:
#         await websocket.send(message)

# asyncio.get_event_loop().run_until_complete(
#     websockets.serve(echo, 'localhost', 8765))
# asyncio.get_event_loop().run_forever()

# import socket
# serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# # serversocket.bind((socket.gethostname(), 8887))
# serversocket.bind(('localhost', 8886))
# print(serversocket.getsockname())
# serversocket.listen(10)
# print("{}:{} dinleniyor...".format(*serversocket.getsockname()))

# while True:
#     try:
#         client, address = serversocket.accept()
#         print("{}:{} bağlandı".format(*address))
#         print (client.recv(4096))
#         client.send("Isleyen kod bug tutmaz :)")
#         client.close()
#     except KeyboardInterrupt:
#         break

# serversocket.close()