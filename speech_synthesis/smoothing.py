import numpy as np
from scipy import signal
#import matplotlib.pyplot as plt


# smoothes a wav with a gaussian kernel of std=sigma
# in the interval centered at position with
# radius desired_radius (or a smaller, depending on the lenght of the sequence)
def smooth_wav(wav,position,sigma,desired_radius):
    smoothed_wav=np.copy(wav)
    window=gaussian_window(sigma)
    radius=smoothing_radius(desired_radius,position,wav,len(window))

    smoothed_wav[position]=smooth_position(wav,position,window)
    for i in xrange(radius+1):
        adjusted_sigma = sigma / (((i+1)/2)+1)
        if (adjusted_sigma % 2 == 0):
            adjusted_sigma+=1
        window=gaussian_window(adjusted_sigma)
        smoothed_wav[position+i]=smooth_position(wav,position+i,window)
        smoothed_wav[position-i]=smooth_position(wav,position-i,window)
    return  smoothed_wav

# creates a gaussian window with sigma standard deviation
def gaussian_window(sigma):
    truncate_at_sd=4
    window_size= 2*int(truncate_at_sd * sigma + 0.5)+1
    window = signal.gaussian(window_size, std=sigma)
    window=window/np.sum(window) # normalize window
    return window

# calculates the filtering radius for a desired radius,
# which can result in a smaller radius depending on the position-radius
# and length of the wav
def smoothing_radius(desired_radius,position,wav,window_size):
    n=len(wav)

    #possibly make radius smaller based on position and n
    min_length=min(position-1,n-position)
    radius=min(min_length,desired_radius)
    return radius

# size of window must be odd
def smooth_position(signal,position,window):
    radius=(len(window)-1)/2
    return np.dot(window,signal[(position-radius):(position+radius+1)])
