'''
Created on 14/05/2013

@author: facuq
'''
from numpy import *
from scipy import ndimage

import os;
from Utils import is_hidden
from matplotlib.image import imread
from PIL import TiffImagePlugin


def imsave(fname, arr, vmin=None, vmax=None, cmap=None, format=None,
           origin=None, dpi=100):
    """
    Saves a 2D :class:`numpy.array` as an image with one pixel per element.
    The output formats available depend on the backend being used.

    Arguments:
      *fname*:
        A string containing a path to a filename, or a Python file-like object.
        If *format* is *None* and *fname* is a string, the output
        format is deduced from the extension of the filename.
      *arr*:
        A 2D array.
    Keyword arguments:
      *vmin*/*vmax*: [ None | scalar ]
        *vmin* and *vmax* set the color scaling for the image by fixing the
        values that map to the colormap color limits. If either *vmin* or *vmax*
        is None, that limit is determined from the *arr* min/max value.
      *cmap*:
        cmap is a colors.Colormap instance, eg cm.jet.
        If None, default to the rc image.cmap value.
      *format*:
        One of the file extensions supported by the active
        backend.  Most backends support png, pdf, ps, eps and svg.
      *origin*
        [ 'upper' | 'lower' ] Indicates where the [0,0] index of
        the array is in the upper left or lower left corner of
        the axes. Defaults to the rc image.origin value.
      *dpi*
        The DPI to store in the metadata of the file.  This does not affect the
        resolution of the output image.
    """
    from matplotlib.backends.backend_agg import FigureCanvasAgg as FigureCanvas
    from matplotlib.figure import Figure

    figsize = [x / float(dpi) for x in (arr.shape[1], arr.shape[0])]
    fig = Figure(figsize=figsize, dpi=dpi, frameon=False)
    canvas = FigureCanvas(fig)
    im = fig.figimage(arr, cmap=cmap, vmin=vmin, vmax=vmax, origin=origin)
    fig.savefig(fname, dpi=dpi, format=format)
    
class ImageReader(object):
    '''
    classdocs
    '''


    def __init__(self):
        '''
        Constructor
        '''
    def read_all(self,folder_path):    
        images= self.read_images(folder_path);
        white=self.read_white(folder_path);
        return (images,white)


    def is_image(self,filepath):
        
        path,ext= os.path.splitext(filepath)
        filename=path.split('\\')[-1]
        ext=ext.lower()
        return filename.find("total")==-1 and filename.find("~")==-1 and not is_hidden(filepath) and ['.tif','.png'].count(ext)>0
    def obtain_filenames(self,path):
        def image_file(filename,filepath):
            return filename.find("blanco")==-1 and self.is_image( filepath)

        files = [os.path.join(path, f) for f in os.listdir(path) if image_file(f, os.path.join(path, f))]
        return files
    def obtain_white_filename(self,path):
        def image_file(filename,filepath):
            return filename.find("blanco")!=-1 and filename.find("~")==-1 and not is_hidden(filepath)
    
        files = [os.path.join(path, f) for f in os.listdir(path) if image_file(f, os.path.join(path, f))]
        if len(files)==0:
            return None
        else:
            return files[0]
    def read_image(self,path):   
        print path
        return ndimage.rotate(imread(path),-90)[:,1:-1,:]
    def read_images(self, folder_path):
        files=self.obtain_filenames(folder_path)
        
        return  map(self.read_image,files )
        
    def read_white(self, folder_path):
        file_name=self.obtain_white_filename(folder_path)
        if file_name==None:
            return None
        else:
            return transpose(imread(file_name))


if __name__ == '__main__':
    pass