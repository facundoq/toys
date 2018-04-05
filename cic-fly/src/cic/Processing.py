'''
Created on 14/05/2013

@author: facuq
'''
from numpy import *

import os;

import matplotlib.pyplot as plt
from PeakDetector import peakdetect
from ImageReader import imsave, ImageReader
import itertools
from matplotlib.pyplot import figure
from Smoothing import savitzky_golay
   
class Peak(object):
    (Maximum,Minimum) = (0,1)
    def __init__(self,x,y, type):
        self.x=x
        self.y=y
        self.type= type
           
class Peaks(object):
    
    def __init__(self,x,y,minimum_dy):
        _max,_min = peakdetect(y, x, 10, minimum_dy)
        self.min=_min
        self.max=_max
    def x_min(self):
        return [p[0] for p in self.min]
    def y_min(self):
        return [p[1] for p in self.min]
    def x_max(self):
        return [p[0] for p in self.max]
    def y_max(self):
        return [p[1] for p in self.max]
    def detect(self,y,x):
        pass
    def all_in_one(self):
        minimum= map(lambda p: Peak(p[0],p[1], Peak.Minimum),self.min)
        maximum= map(lambda p: Peak(p[0],p[1], Peak.Maximum),self.max)
        return sorted(minimum+maximum,key= lambda p: p.x)
class ProcessingResult(object):
    def __init__(self,options,image_count, using_white,combined_image,f,smooth_f, peaks,t_half,f_mean,f0):
        self.f=f
        self.f0=f0
        self.smooth_f= smooth_f
        self.combined_image=combined_image
        self.options=options
        self.peaks=peaks
        self.t_half=t_half
        self.f_mean=f_mean
        self.using_white= using_white
        self.image_count= image_count
    def output_name(self):
        return     "output-duration-%d.txt" % (self.options.duration)
class ProcessingArea(object):
    def __init__(self,start,end,background_start,background_end):
        self.start=start
        self.end=end
        self.background_start=background_start
        self.background_end=background_end    
class ProcessingOptions(object):
    def __init__(self,duration,area,minimum_dy,  window_size, order):
        self.duration=duration
        self.area=area
        self.minimum_dy=minimum_dy
        self.window_size= window_size
        self.order= order

class Processor(object):
    '''
    classdocs
    '''


    def __init__(self):
        '''
        Constructor
        '''
    def process(self,options,combined_images, images, white):
        result=self.extract_information(combined_images,len(images), white!= None, options)
        return result
    
    def preprocess(self, folder_path):
        images,white= ImageReader().read_all(folder_path)
        if white!= None:
            images=self.remove_white(images,white)
        return (self.combine_images(images), images, white)
        
    def remove_white(self, images, white):
        for i in images:
            i=i-white
        return images
            
    def combine_images(self, images):
        return concatenate(images,axis=1)
    def smooth(self,y,window_size, order):
        if (window_size<=1 or order>= window_size):
            return y
        if window_size %2==0:
            window_size+=1
        return savitzky_golay(y, window_size=window_size, order=order)
        
    def extract_information(self, combined_images,image_count, using_white, options):
        data_points,f0= self.extract_data_points(combined_images,options)
        time=linspace(0,options.duration,len(data_points))
        smooth_data_points= self.smooth(data_points, options.window_size, options.order)
        f= array([time,data_points])
        smooth_f=array([time,smooth_data_points])
        peaks=Peaks(time,smooth_data_points,  options.minimum_dy)
        mean_f=mean(peaks.y_max())
        t_half=self.calculate_t_half(peaks)
        return ProcessingResult(options,image_count, using_white,combined_images,f,smooth_f,peaks,t_half,mean_f,f0) 
    def extract_data_points(self,combined_images,options):
        
        f0=average(combined_images[:options.area.background_start,:,:])
        f0+= average(combined_images[options.area.background_end:,:,:])
        
        data = combined_images[options.area.start:options.area.end,:,:]
        avg_data=average(data, (2))
        avg_data=average(avg_data, (0))
        avg_data-=f0
        return (avg_data,f0)
    def save_output(self,result, folder_path):
        
        folder_path+= "/result"
        if not os.path.exists(folder_path):
            os.mkdir(folder_path)
        #path=os.path.join(folder_path,result.output_name())
        path= folder_path+ "/"+result.output_name()
        savetxt(path, transpose(result.f), "%10.3f", ",",'\n')
        path= folder_path+ "/"+'total.png'
        #path=os.path.join(folder_path,'total.png')
        imsave(path,result.combined_image)
        
        
    def calculate_t_half (self,peaks):
        all= peaks.all_in_one()
        all=list(itertools.dropwhile(lambda p: p.type== Peak.Minimum,all))
        result=[]
        i=0
        while i<len(all):
            maximum=None
            minimum=None
            while i<len(all) and all[i].type== Peak.Maximum:
                if maximum==None or all[i].y> maximum.y:
                    maximum=all[i]
                i+=1
            while i<len(all) and all[i].type== Peak.Minimum :
                if minimum==None or all[i].y< minimum.y:
                    minimum=all[i]
                i+=1 
            if maximum!= None  and minimum!= None:
                result+= [minimum.x- maximum.x]
        return mean(result)
    
def plot_image(image):
    figure()
    plt.imshow(image)
    plt.show()

def plot_peaks(result):
    figure()
    x,y=(result.f[0,:],result.f[1,:])
    plt.plot(x,y)
    peaks= result.peaks
    xm = peaks.x_max()
    ym = peaks.y_max()
    xn = peaks.x_min()
    yn = peaks.y_min()
    
    plt.plot(xm, ym, 'r+')
    plt.plot(xn, yn, 'g+')
    plt.show()

def main():
    folder_path='C:\\Users\\facuq\\Desktop\\img\\test'
    p=Processor()
    combined_images, images, white= p.preprocess(folder_path)
    plot_image(combined_images)
    options= ProcessingOptions(len (images)*5.2,ProcessingArea(280, 290),10,0.3)
    result=p.process(options,combined_images, images, white)
    p.save_output(result, folder_path)
    plot_peaks(result)
    print  "f %f - T12  %f"  % (result.f_mean, result.t_half)
    
if __name__ == '__main__':
    main()
    