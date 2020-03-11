# -*- coding: utf-8 -*-
"""
Created on Thu Dec 12 15:56:43 2019

@author: DELL LAB
"""

# -*- coding: utf-8 -*-
"""
Created on Tue Nov 26 08:43:25 2019

@author: DELL LAB
"""

import tensorflow.keras
from PIL import Image
import numpy as np
import os
import matplotlib.pyplot as plt
from keras.optimizers import Adam,Adadelta
import timeit
import websocket
import cv2

#model_path='.\..\..\..\models'
#print(os.listdir(model_path))


ws=websocket.WebSocket();
ws.connect("ws://127.0.0.1:8887")


class_names = ['closed','open']
# Disable scientific notation for clarity
np.set_printoptions(suppress=True)

# Load the model
#model = tensorflow.keras.models.load_model('tm_keras_model_big_ds.h5')
#model = tensorflow.keras.models.load_model('keras_model_epoch200.h5')
#model = tensorflow.keras.models.load_model('data_augment_model.h5')
#model = tensorflow.keras.models.load_model('vgg16_model_big_ds.h5')
#model = tensorflow.keras.models.load_model('vgg16_1000_ds.h5')
#model = tensorflow.keras.models.load_model('cnn.h5')
#model = tensorflow.keras.models.load_model('./../../../models/keras_model_pistachio.h5')
model = tensorflow.keras.models.load_model(r'C:\python_data\models\keras_model_musa.h5')
#model = tensorflow.keras.models.load_model(r'C:\python_data\models\cuneyt_mobilenet.h5')
model.summary()

# Create the array of the right shape to feed into the keras model
# The 'length' or number of images you can put into the array is
# determined by the first position in the shape tuple, in this case 1.
data = np.ndarray(shape=(1, 224, 224, 3), dtype=np.float32)

# Replace this with the path to your image

nOpenSuccess=0
nClosedSuccess=0
nTotal=0;
acc=0.0

#path=r"C:\python_data\dataset\pistachio\snapshots"    
#for imge in os.listdir(path): 
#        image = Image.open(os.path.join(path,imge))        
#        # Make sure to resize all images to 224, 224 otherwise they won't fit in the array
#        image = image.resize((224, 224))
#        plt.imshow(image)
#        plt.show()
#        image_array = np.asarray(image)        
#        # Normalize the image
#        normalized_image_array = (image_array.astype(np.float32) / 255.0)        
##        normalized_image_array = (image_array.astype(np.float32) / 127.0) - 1        
#        # Load the image into the array
#        data[0] = normalized_image_array        
#        # run the inference
#        prediction = model.predict(data)
#        nTotal+=1
#        print('acc',acc,' class:',class_names[np.argmax(prediction)])

#for x in class_names:    
#    path=os.path.join(r"C:\python_data\dataset\pistachio\teachable_machine\test",x)    
#    for imge in os.listdir(path): 
#        image = Image.open(os.path.join(path,imge))        
#        # Make sure to resize all images to 224, 224 otherwise they won't fit in the array
#        image = image.resize((224, 224))
##        plt.imshow(image)
##        plt.show()
#        image_array = np.asarray(image)        
#        # Normalize the image
#        normalized_image_array = (image_array.astype(np.float32) / 127.0) - 1        
##        normalized_image_array = (image_array.astype(np.float32) / 255.0)        
#        # Load the image into the array
#        data[0] = normalized_image_array        
#        # run the inference
#        start = timeit.default_timer() 
#        prediction = model.predict(data)
#        stop = timeit.default_timer()
#        nTotal+=1
#        if x=='open' and np.argmax(prediction)==1:            
#            nOpenSuccess+=1            
#        if x=='closed' and np.argmax(prediction)==0:  
#            nClosedSuccess+=1
#        acc=(nOpenSuccess+nClosedSuccess)*1.0/nTotal
#        print('acc',acc,' class:',class_names[np.argmax(prediction)],' elapsed time:', (stop-start))
#        ws.send('class:'+class_names[np.argmax(prediction)])

def open_camera():
    global nTotal;
    global nOpenSuccess;
    global nClosedSuccess;
    global acc;
    x='open'
    
    capture=cv2.VideoCapture(0)
    while(True):     
        start = timeit.default_timer() 
        ret, frame = capture.read()  
        frame=cv2.flip(frame,1)
        cv2.imshow('video', frame) 
        image = np.resize(frame,(224, 224,3))
        image_array = np.asarray(image) 
        normalized_image_array = (image_array.astype(np.float32) / 127.0) - 1
        # Load the image into the array
        data[0] = normalized_image_array        
        # run the inference
        prediction = model.predict(data)
        print(np.argmax(prediction))
        nTotal+=1
        if x=='open' and np.argmax(prediction)==1:            
            nOpenSuccess+=1            
        if x=='closed' and np.argmax(prediction)==0:  
            nClosedSuccess+=1
        acc=(nOpenSuccess+nClosedSuccess)*1.0/nTotal
#        ws.send('class:'+class_names[np.argmax(prediction)])
#        ws.send(np.argmax(prediction))
        stop = timeit.default_timer()
#        print('acc',acc,' class:',class_names[np.argmax(prediction)],' elapsed time:', (stop-start)*1000)
        if cv2.waitKey(2) == 27:
            break
    capture.release()
    cv2.destroyAllWindows()

open_camera()

print('overall accuracy:',acc)
print('hit open :',nOpenSuccess)
print('hit closed :',nClosedSuccess)
print('#samples:',nTotal)