#!/usr/bin/env python

'''
Created on 29/08/2012
http://www.py2exe.org/index.cgi/MatPlotLib
http://stackoverflow.com/questions/4787821/bundle-files-1-fails-with-py2exe-using-matplotlib
@author: facuq

Instructions: download and install py2exe in a Windows OS,
Open a console, change directory to the project's location
Execute:  "python setup.py py2exe"
The resulting application can be found in subfolder  "dist"
'''


from distutils.core import setup
import py2exe
import matplotlib
    
setup(console=['Application.py'],    data_files=matplotlib.get_py2exe_datafiles())