'''
Created on 28/08/2012

@author: facuq
'''
from observer import Observable
from View import  View
from Processing import Processor, ProcessingOptions, ProcessingArea

class Application(Observable):
    '''
    classdocs
    '''


    def __init__(self):
        Observable.__init__(self) 
        self.options= ProcessingOptions(0,ProcessingArea(246,266,200,300),5,4,1)
        self.folder_path=None
        self.log=None
        self.p= Processor()
        
    def update_folder_path(self):
        self.combined_images, self.images, self.white= self.p.preprocess(self.folder_path)
        self.notify( "image_changed", None)
        duration=len(self.images)*5.22
        center= self.combined_images.shape[0]//2
        area=ProcessingArea(center-5, center+5,center-30,center+30)
        area=ProcessingArea(280,290,260,360)
        self.options.area= area
        self.notify( "area_changed", None)
        self.options.duration=duration
        self.notify( "options_changed", None)
        self.process()
    def area_changed(self):
        self.notify( "area_changed", None)
        self.process()
        
    def options_changed(self):
        self.notify( "options_changed", None)
        self.process()
        
            
    def set_folder_path(self, value):
        self.folder_path = value
        self.notify( "folder_path_changed", None)
        self.update_folder_path()
        
           
    def process(self):
        if (self.folder_path != None and self.options!=None):
            
            self.notify("began_processing",None)
            self.result=self.p.process(self.options,self.combined_images,self.images,self.white)
            self.log=[]
            self.notify("finished_processing",None)
            self.p.save_output(self.result, self.folder_path)
            self.notify("log_changed",None) 
            
        

if __name__ == '__main__':
    
    app = Application()
    view = View(app)
    
        