import timeit
import websocket
import cv2
import numpy as np
import tensorflow as tf

ws=websocket.WebSocket();
ws.connect("ws://127.0.0.1:8887")

def open_camera():
    capture=cv2.VideoCapture(0)
    while(True):     
        start = timeit.default_timer() 
        ret, frame = capture.read()     
        cv2.imshow('video', frame) 
        image = np.resize(frame,(224, 224,3))
        image_array = np.asarray(image) 
        normalized_image_array = (image_array.astype(np.float32) / 127.0) - 1
        ws.send('class:'+str(1))
        stop = timeit.default_timer()
        #print('elapsed time:', (stop-start)*1000)
        if cv2.waitKey(2) == 27:
            break
    capture.release()
    cv2.destroyAllWindows()

open_camera()

