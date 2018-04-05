'''
Created on 28/08/2012

@author: facuq
'''
import matplotlib
from cic.WidgetLibrary import EntryWithLabel, FigureView
from cic.Widgets import PanelView, OptionsView

matplotlib.use('TkAgg')
from observer import Observable


from numpy import array
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg, NavigationToolbar2TkAgg
import matplotlib.pyplot as plt
import tkFileDialog
import Tkinter
from Tkconstants import END, DISABLED, NORMAL
from Tkinter import Tk, Frame, Label, Text, Scrollbar, StringVar, Entry, Button,\
    DoubleVar, IntVar
 
class View(object):
    '''
    classdocs
    '''

    def __init__(self,application):
        self.application=application;
        self.root = Tk()
        self.root.title("CIC")
        #w, h = self.root.winfo_screenwidth(), self.root.winfo_screenheight()
        #self.root.minsize(w,h)
        #self.root.geometry("%dx%d+0+0" % (w, h))
        self.frame = Frame(self.root)
        self.frame.grid()
        self.widgets()
        
        self.application.subscribe(self,"began_processing",self.began_normalizing)
        self.application.subscribe(self,"area_changed",self.image_changed)
        self.application.subscribe(self,"finished_processing",self.result_changed)
        self.application.subscribe( self,"folder_path_changed", self.folder_path_changed) 
        
        self.root.mainloop()
        
    def widgets(self):     
        self.panel=PanelView(self.frame,self,self.application)
        self.panel.grid(column=0,row=0,columnspan=8)
        self.image_figure=FigureView(self.root)
        self.image_figure.grid(column=0,row=4,columnspan=8)
        self.options_view= OptionsView(self.root, self.application)
        self.options_view.grid(column=0,row=5,columnspan=8)
        self.plot_figure=FigureView(self.root)
        self.plot_figure.grid(column=0,row=6,columnspan=8)
        self.smooth_plot_figure=FigureView(self.root)
        self.smooth_plot_figure.grid(column=0,row=7,columnspan=8)
        
    def message(self, message):
        #self.events.insert(END,  message)
        pass
    def image_changed(self, event, message):
        self.message( "%d Imagenes cargadas\n" % (len(self.application.images)))
        self.plot_image(self.application.combined_images)
        
    def result_changed(self, event, message):
        r=self.application.result
        self.plot_peaks(r)
        self.message( "Procesamiento finalizado")
        self.panel.result_view.update_values(r.t_half, r.f_mean,len(r.peaks.min),len(r.peaks.max),r.f0)
            
    def plot_image(self,image):
        self.image_figure.figure.clear()
        axes=self.image_figure.figure.gca()
        axes.imshow(image)
        x= array([0,image.shape[1]])
        y1= array([self.application.options.area.start,self.application.options.area.start])
        y2= array([self.application.options.area.end,self.application.options.area.end])
        axes.plot(x,y1,'b')
        axes.plot(x,y2,'b')
        y1= array([self.application.options.area.background_start,self.application.options.area.background_start])
        y2= array([self.application.options.area.background_end,self.application.options.area.background_end])
        axes.plot(x,y1,'w')
        axes.plot(x,y2,'w')
        self.image_figure.canvas.draw()
        
    def plot_peaks(self,result):
        self.plot_peaks_figure(result.peaks, result.f,self.plot_figure)
        self.plot_peaks_figure(result.peaks, result.smooth_f,self.smooth_plot_figure)
    def plot_peaks_figure(self,peaks,f, figure):
        figure.figure.clear()
        axes=figure.figure.gca()
        x,y=(f[0,:],f[1,:])
        figure.figure.gca().plot(x,y)
        peaks= peaks
        xm = peaks.x_max()
        ym = peaks.y_max()
        xn = peaks.x_min()
        yn = peaks.y_min()
        for i in range(len(xn)):
            axes.annotate(str(i), xy=(xn[i], yn[i]),size=10)
        figure.figure.gca().plot(xm, ym, 'r+')
        figure.figure.gca().plot(xn, yn, 'g+')
        figure.figure.canvas.draw()
        plt.draw()
      
    def began_normalizing(self,event,message):
       self.message("\nComenzando proceso...")
        
    
    def log_changed(self,event,message):
        pass
      
    def folder_path_changed(self,event,message):
        self.message( "\nCargando carpeta: "+self.application.folder_path+ "\n")


