# IMPORTANT install packages from command line
# pip install numpy scikit-video scikit-image 

import skvideo.io
import numpy as np
from skimage import transform
import skimage.io
import os

# read original image
filepath = "miau.jpg"
original_image = skimage.io.imread(filepath)

# centrar transformacion para que las rotaciones y escaladossean desde el centro
def center_transformation(transformation:transform.AffineTransform,image_size:(int,int)):
    h,w=image_size
    shift_y, shift_x = (h- 1) / 2., (w- 1) / 2.
    shift = transform.AffineTransform(translation=[-shift_x, -shift_y])
    shift_inv = transform.AffineTransform(translation=[shift_x, shift_y])
    return shift + (transformation+ shift_inv)


#https://scikit-image.org/docs/dev/api/skimage.transform.html#skimage.transform.warp

# funcion para transformar imagen 
def transform_image_center(image:np.ndarray,
rotation:float,
translation:(int,int),
scale=(float,float), mode="reflect"):
    
    tform = transform.AffineTransform(
                rotation=rotation,
                translation = translation,
                scale=scale,
                )

    h,w,c=image.shape
    tform=center_transformation(tform, (h,w))
    return transform.warp(image,tform.inverse,mode=mode)

n_frames=300
h,w,c=original_image.shape
fps = 20
quality = 3000000
writer = skvideo.io.FFmpegWriter("outputvideo.mp4",
 outputdict={'-r': str(fps),'-vcodec': 'libx264', '-b': str(quality)})
# transformar a float de 0 a 1 para procesar
original_image=original_image/255
for i in range(n_frames):
    r=0.1*i /(2*np.pi)
    # transformar imagen
    s=(0.99  )**i
    transformed_image=transform_image_center(original_image,r,(0,0),(s,s))
    #transformar a uint8 de 0 a 255 para guardar
    image_uint8=(transformed_image*255).astype(np.uint8)
    # grabar frame en el video
    writer.writeFrame(image_uint8)
writer.close()