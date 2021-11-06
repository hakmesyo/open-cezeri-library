# -*- coding: utf-8 -*-
"""
Created on Thu Aug 19 15:37:07 2021

@author: cezerilab
"""
# import tensorflow as tf
# import keras
# from keras import backend as K
# from keras.layers.core import Dense, Activation
# from keras.optimizers import Adam
# from keras.metrics import categorical_crossentropy
# from keras.preprocessing.image import ImageDataGenerator
# from keras.preprocessing import image
# from keras.models import Model
# from keras.applications import imagenet_utils
# from keras.layers import Dense,GlobalAveragePooling2D
# from keras.applications import MobileNet
# from keras.applications.mobilenet import preprocess_input
# import numpy as np
# from IPython.display import Image
# from keras.optimizers import Adam
# import timeit


# print(tf.__version__)

# mobile = keras.applications.mobilenet.MobileNet()

# def prepare_image(file):
#     img_path = ''
#     img = image.load_img(img_path + file, target_size=(224, 224))
#     img_array = image.img_to_array(img)
#     img_array_expanded_dims = np.expand_dims(img_array, axis=0)
#     return keras.applications.mobilenet.preprocess_input(img_array_expanded_dims)

# start = timeit.default_timer()
# for i in range(50):
#     preprocessed_image = prepare_image('./images/mixed/'+str(i)+'.jpg')
#     predictions = mobile.predict(preprocessed_image)
#     results = imagenet_utils.decode_predictions(predictions)
#     print(results[0][0])

# stop = timeit.default_timer()
# print('Time: ', stop - start,' seconds')  

from tensorflow.keras.models import load_model
from PIL import Image, ImageOps
import numpy as np
import timeit
import threading
import tensorflow as tf

from tensorflow.python.client import device_lib
def get_available_gpus():
    local_device_protos = device_lib.list_local_devices()
    return [x.name for x in local_device_protos if x.device_type == 'GPU']
print(get_available_gpus())
# Load the model
model = load_model('keras_model.h5')

def thr1(n):
    # start = timeit.default_timer()
    for i in range(n):
        start = timeit.default_timer()
        # Create the array of the right shape to feed into the keras model
        # The 'length' or number of images you can put into the array is
        # determined by the first position in the shape tuple, in this case 1.
        data = np.ndarray(shape=(1, 224, 224, 3), dtype=np.float32)
        # Replace this with the path to your image
        image = Image.open('./images/mixed/'+str(i)+'.jpg')
        #resize the image to a 224x224 with the same strategy as in TM2:
        #resizing the image to be at least 224x224 and then cropping from the center
        size = (224, 224)
        image = ImageOps.fit(image, size, Image.ANTIALIAS)
        
        #turn the image into a numpy array
        image_array = np.asarray(image)
        # Normalize the image
        normalized_image_array = (image_array.astype(np.float32) / 127.0) - 1
        # Load the image into the array
        data[0] = normalized_image_array
        
        # run the inference
        prediction = model.predict(data)
        # print(prediction)
        stop = timeit.default_timer()
        print('thr1 Time: ', (stop - start)*1000,' mili seconds')  
    # stop = timeit.default_timer()
    # print('Time: ', stop - start,' seconds')  
    
def thr2(n):
    # start = timeit.default_timer()
    for i in range(n):
        start = timeit.default_timer()
        # Create the array of the right shape to feed into the keras model
        # The 'length' or number of images you can put into the array is
        # determined by the first position in the shape tuple, in this case 1.
        data = np.ndarray(shape=(1, 224, 224, 3), dtype=np.float32)
        # Replace this with the path to your image
        image = Image.open('./images/mixed/'+str(i)+'.jpg')
        #resize the image to a 224x224 with the same strategy as in TM2:
        #resizing the image to be at least 224x224 and then cropping from the center
        size = (224, 224)
        image = ImageOps.fit(image, size, Image.ANTIALIAS)
        
        #turn the image into a numpy array
        image_array = np.asarray(image)
        # Normalize the image
        normalized_image_array = (image_array.astype(np.float32) / 127.0) - 1
        # Load the image into the array
        data[0] = normalized_image_array
        
        # run the inference
        prediction = model.predict(data)
        # print(prediction)
        stop = timeit.default_timer()
        print('thr2 Time: ', (stop - start)*1000,' mili seconds')  
    # stop = timeit.default_timer()
    # print('Time: ', stop - start,' seconds')  

def thr3(n):
    # start = timeit.default_timer()
    for i in range(n):
        start = timeit.default_timer()
        # Create the array of the right shape to feed into the keras model
        # The 'length' or number of images you can put into the array is
        # determined by the first position in the shape tuple, in this case 1.
        data = np.ndarray(shape=(1, 224, 224, 3), dtype=np.float32)
        # Replace this with the path to your image
        image = Image.open('./images/mixed/'+str(i)+'.jpg')
        #resize the image to a 224x224 with the same strategy as in TM2:
        #resizing the image to be at least 224x224 and then cropping from the center
        size = (224, 224)
        image = ImageOps.fit(image, size, Image.ANTIALIAS)
        
        #turn the image into a numpy array
        image_array = np.asarray(image)
        # Normalize the image
        normalized_image_array = (image_array.astype(np.float32) / 127.0) - 1
        # Load the image into the array
        data[0] = normalized_image_array
        
        # run the inference
        prediction = model.predict(data)
        # print(prediction)
        stop = timeit.default_timer()
        print('thr3 Time: ', (stop - start)*1000,' mili seconds')  
    # stop = timeit.default_timer()
    # print('Time: ', stop - start,' seconds')

start = timeit.default_timer()
k=562
# thr1(k)
t1 = threading.Thread(target=thr1, args=(k,))
t2 = threading.Thread(target=thr2, args=(k,))
t3 = threading.Thread(target=thr3, args=(k,))
# t4 = threading.Thread(target=thr3, args=(k,))
# t5 = threading.Thread(target=thr3, args=(k,))

t1.start()
t2.start()
t3.start()
# t4.start()
# t5.start()

t1.join()
t2.join()
t3.join()
# t4.join()
# t5.join()

print("Done")
stop = timeit.default_timer()
print('Time: ', stop - start,' seconds')
