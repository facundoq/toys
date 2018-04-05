'''
Created on 28/08/2012

@author: facuq
'''
from matplotlib.figure import Figure
from matplotlib.backends.backend_gtkagg import FigureCanvasGTKAgg as FigureCanvas
from matplotlib.backends.backend_gtkagg import NavigationToolbar2GTKAgg as NavigationToolbar

import tkFileDialog
import Tkinter
from Tkconstants import END
from Tkinter import Tk, Frame, Label, Text, Scrollbar, StringVar, Entry, Button

class View(object):
    '''
    classdocs
    '''
    

    def __init__(self,application):
        self.application=application;
        self.root = Tk()
        self.root.title("CIC")
        self.root.minsize(300,300)
        self.root.geometry("900x600")
        self.frame = Frame(self.root)
        self.frame.grid()
        
        self.generate_control_view()
        self.generate_event_view()
        self.root.mainloop()

    def generate_event_view(self):
        self.events_label = Label(self.frame, text="Eventos:")
        self.events_label.grid(column=0,row=2)
        self.events = Text(self.frame,  width=100, highlightthickness=2)
        scroll=Scrollbar(self.frame)
        scroll.grid(column=6,row=3,sticky="ns")
        
        scroll.config(command=self.events.yview)
        self.events.configure(yscrollcommand=scroll.set)
        self.events.grid(column=0,row=3,rowspan=1,columnspan=5,sticky="nesw")
        self.events.tag_config("a", wrap=Tkinter.WORD)
        self.application.subscribe(self,"log_changed",self.log_changed)
        self.application.subscribe(self,"began_processing",self.began_normalizing)
        
    def began_normalizing(self,event,message):
        self.events.insert(END,"Comenzando proceso de normalizacion....\n")    
    
    def log_changed(self,event,message):
        event_list= "\n".join(self.application.log)
        self.events.insert(END, event_list)
        
    def generate_control_view(self):
        Label(self.frame, text="Directorio:").grid(row=0,column=0)
        self.value = StringVar()
        self.folder_path = Entry(self.frame,state='readonly',width=100,textvariable=self.value)
        self.folder_path.grid(column=1,row=0,columnspan=5)
        self.button = Button(
            self.frame, text="Elegir...", fg="black", command=self.choose_folder)
        self.button.grid(column=6,row=0)
        self.button = Button(
            self.frame, text="Procesar", fg="black", command=self.application.normalize)
        self.button.grid(column=0,row=1)
        self.application.subscribe( self,"folder_path_changed", self.folder_path_changed) 
        
    def folder_path_changed(self,event,message):
        self.value.set(self.application.folder_path)

        
    def choose_folder(self):
        message= "Selecciona el directorio donde estan las imagenes:"
        folder_path= tkFileDialog.askdirectory(parent=self.root,initialdir="/",title=message)
        self.application.set_folder_path(folder_path)
        