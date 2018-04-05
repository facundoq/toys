'''
Created on 02/07/2013

@author: facuq
'''
from Tkinter import Frame
from cic.observer import Observable
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg, NavigationToolbar2TkAgg

from Tkinter import Tk, Frame, Label, Text, Scrollbar, StringVar, Entry, Button,\
    DoubleVar, IntVar
 
 
class EntryWithLabel( Frame,Observable):
    def __init__(self,master,text,default=None, minimum=-9999999,maximum=-9999999,var= DoubleVar):
        Frame.__init__(self, master)
        Observable.__init__(self)
        self.minimum= minimum
        self.maximum= maximum
        self.label = Label(self, text=text)
        self.label.grid(column=0,row=0)
        self.value= var()
        self.value.set(default if default!= None else self.minimum)
        self.entry= Entry(self,textvariable=self.value)
        self.entry.bind( "<FocusOut>", self.check_range)
        self.entry.grid(column=1,row=0)
    def set(self, value):
        return self.value.set(value)    
    def get(self):
        return self.value.get()
    def check_range(self, event):
        v=self.get()
        v= max(v, self.minimum)
        v= min(v, self.maximum)
        self.value.set(v)  

class FiguresView( Frame,Observable):
    def __init__(self,master):
        Frame.__init__(self, master)
        Observable.__init__(self)
        self.image= FigureView(self)
            
class FigureView( Frame,Observable):
    def __init__(self,master):
        Frame.__init__(self, master)
        Observable.__init__(self)
        self.figure=plt.figure()
        self.figure.set_size_inches(10,2)
        self.canvas = FigureCanvasTkAgg(self.figure, master=self)
        #self.canvas.get_tk_widget().pack(side=Tkinter.TOP, fill=Tkinter.BOTH, expand=1)
        #self.canvas.show()
        self.toolbar = NavigationToolbar2TkAgg( self.canvas, self)
        #toolbar.pack(side=Tkinter.TOP, fill=Tkinter.BOTH, expand=1)
        #toolbar.update()
        self.toolbar.grid(row=1,column=0)
        self.canvas.get_tk_widget().grid(row=0,column=0)