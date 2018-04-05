'''
Created on 02/07/2013

@author: facuq
'''
import matplotlib
from cic.WidgetLibrary import EntryWithLabel, FigureView
from observer import Observable


from numpy import array
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg, NavigationToolbar2TkAgg
import matplotlib.pyplot as plt
import tkFileDialog
import Tkinter
from Tkconstants import END, DISABLED, NORMAL
from Tkinter import Tk, Frame, Label, Text, Scrollbar, StringVar, Entry, Button,\
    DoubleVar, IntVar
 

class ResultsView( Frame,Observable):
    def __init__(self,master):
        Frame.__init__(self, master)
        Observable.__init__(self)
        
        self.t_half_var = StringVar()
        self.t_half= Label(self, textvariable= self.t_half_var)
        self.t_half.grid(column=0,row=0)
        
        self.f_var = StringVar()
        self.f= Label(self, textvariable= self.f_var)
        self.f.grid(column=1,row=0)
        
        self.peaks_var = StringVar()
        self.peaks= Label(self, textvariable= self.peaks_var)
        self.peaks.grid(column=3,row=0)
        
        self.f0_var = StringVar()
        self.f0= Label(self, textvariable= self.f0_var)
        self.f0.grid(column=4,row=0)
        
        self.update_values( "?",  "?", "?", "?","?")
        
    def update_values(self, t_half,f, peaks_max, peaks_min,f0):
        
        self.f_var.set ("F (average):"+str(f))
        self.t_half_var.set("T12:"+str(t_half))
        self.peaks_var.set("Peaks found (Min/Max): "+str(peaks_min)+"/"+str(peaks_max))
        self.f0_var.set("f0:"+str(f0))
        
class PanelView( Frame,Observable):
    def __init__(self,master,view,application):
        Frame.__init__(self, master)
        Observable.__init__(self)
        self.application=application
        self.view=view
        self.generate_control_view()
        self.generate_options_view()
        self.result_view= ResultsView(self)
        self.result_view.grid(column=0,row=2,columnspan=8)
        self.application.subscribe( self,"folder_path_changed", lambda x,y: self.value.set(self.application.folder_path))
    def generate_options_view(self):
        self.options_view=ImageOptionsView(self,self.application)
        self.options_view.grid(column=0,row=1,columnspan=8)
        self.options_view.subscribe(self,"area_changed",lambda x,y: self.view.plot_image(self.application.combined_images))
        self.application.subscribe(self,"area_changed",lambda x,y: self.options_view.update_values(self.application.options))
        

    def generate_control_view(self):
        Label(self, text="Directorio:").grid(row=0,column=0)
        self.value = StringVar()
        self.folder_path = Entry(self,state='readonly',width=100,textvariable=self.value)
        self.folder_path.grid(column=1,row=0,columnspan=8)
        self.button = Button(self, text="Elegir...", fg="black", command=self.choose_folder)
        self.button.grid(column=8,row=0)
    def choose_folder(self):
        message= "Selecciona el directorio donde estan las imagenes:"
        folder_path= tkFileDialog.askdirectory(parent=self.view.root,initialdir="/",title=message)
        self.application.set_folder_path(folder_path)  


class ImageOptionsView( Frame,Observable):
    def __init__(self,master,application):
        Frame.__init__(self, master)
        Observable.__init__(self)
        self.options= application.options
        self.application=application
        self.area_start= self.entry_int("Altura comienzo area :",self.options.area.start,0,0)
        self.area_end= self.entry_int("Altura fin area:",self.options.area.end,1,0)
        self.background_area_start=self.entry_int("Altura comienzo area fondo:",self.options.area.background_start,2,0)
        self.background_area_end=self.entry_int("Altura fin area fondo:",self.options.area.background_end,3,0)
        self.area_button = Button(self, text="Seleccionar area", fg="black", command=self.update_options)
        self.area_button.grid(column=4,row=0)
    def entry_int(self,label,value, column,row):
        entry= EntryWithLabel(self,label,value,0,9999, IntVar)
        #entry.entry.bind( "<FocusOut>", lambda x:self.update_options())
        entry.grid(column=column,row=row)
        return entry
    def update_values(self, options): 
        self.area_end.set(self.options.area.end)
        self.area_start.set(self.options.area.start)
        self.background_area_end.set(self.options.area.background_end)
        self.background_area_start.set(self.options.area.background_start) 
        print "values updated" 
        self.enable()  
    def update_options(self):
        self.options.area.end=self.area_end.get()
        self.options.area.start=self.area_start.get()
        self.options.area.background_end=self.background_area_end.get()
        self.options.area.background_start=self.background_area_start.get() 
        self.application.area_changed()
        self.notify( "area_changed", None)
    def enable(self):
        self.area_button.state= NORMAL
                  
class OptionsView( Frame,Observable):
    def __init__(self,master,application):
        Frame.__init__(self, master)
        Observable.__init__(self)
        self.options= application.options
        self.application=application
        self.window_size= EntryWithLabel(self,"Ventana (suavizado,>=1,1=off):",self.options.window_size,0, 50)
        self.window_size.grid(column=0,row=0)
        self.order = EntryWithLabel(self,"Orden (Suavizado):",self.options.order,0, 49)
        self.order.grid(column=1,row=0)
        self.minimum_dy= EntryWithLabel(self,"Altura minima entre picos:",self.options.minimum_dy,0.001,9999)
        self.minimum_dy.grid(column=2,row=0)
        self.duration= EntryWithLabel(self,"Duracion total:",self.options.duration,0,999999, IntVar)
        self.duration.grid(column=3,row=0)
        self.process_button = Button(self, text="Procesar", fg="black", command=lambda x,y: self.update_options)
        self.process_button.grid(column=4,row=0)
        self.application.subscribe(self,"options_changed",lambda x,y: self.update_values(self.application.options))
        
    def entry_int(self,label,value, column,row):
        entry= EntryWithLabel(self,label,value,0,9999, IntVar)
        #entry.entry.bind( "<FocusOut>", lambda x:self.update_options())
        entry.grid(column=column,row=row)
        return entry
    def enable(self):
        self.process_button.state= NORMAL
    def update_values(self, options):
        self.order.set(self.options.order)
        self.window_size.set(self.options.window_size)
        self.minimum_dy.set(self.options.minimum_dy)
        self.duration.set(self.options.duration)
        self.update_idletasks()
        self.enable()
        
    def update_options(self):
        self.options.order=self.order.get()
        self.options.window_size=self.window_size.get()
        self.options.minimum_dy=self.minimum_dy.get()
        self.options.duration=self.duration.get()
        self.application.options_changed()
        self.notify( "options_changed", None)
        
        

