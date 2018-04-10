from skimage.util import view_as_blocks,view_as_windows
import numpy as np




def calculate_gradient_and_mag(image):
    if len(image.shape)==3:
        image=np.mean(image,axis=2)
    gx,gy=np.gradient(image)
    #angle=np.zeros_like(gx)
    angle=np.abs(np.arctan2(gx,gy))
    mag = np.sqrt(gx**2+gy**2)
    return angle,mag

def calculate_cells(angle,mag,histogram_bins,cell_size):
    
    eps=1e-10
    bins=np.linspace(0,np.pi+eps,histogram_bins)
    
    cs=cell_size
    rows,cols=angle.shape
    
    cells_r,cells_c= ( rows//cs,cols//cs)
    block_angle=view_as_blocks(angle, block_shape=(cs,cs))
    new_shape=(block_angle.shape[0],block_angle.shape[1],cs*cs)
    block_mag=view_as_blocks(mag, block_shape=(cs,cs))
    
    block_angle=block_angle.reshape(new_shape)
    block_mag=block_mag.reshape(new_shape)

    histograms=np.zeros( (block_angle.shape[0],block_angle.shape[1],len(bins)) )

    for i in range(block_angle.shape[0]):
        for j in range(block_angle.shape[1]):
            rangle,rmag=(block_angle[i,j],block_mag[i,j])
            indices=np.digitize(rangle,bins)
            histograms[i,j,indices]+=rmag[indices]
    return histograms

def calculate_blocks(cells,stride,block_size):
    eps=1e-10

    blocks_r,block_c=(cells.shape[0]//stride-block_size[0]+1,cells.shape[1]//stride-block_size[1]+1)
    block_histograms=np.zeros( (blocks_r,block_c,block_size[0]*block_size[1]*cells.shape[2]))
    c=block_size #shorter name
    for i in range(block_histograms.shape[0]):
        ai=i*stride
        for j in range(block_histograms.shape[1]):
            aj=j*stride
            block_histogram=cells[ai:ai+c[0],aj:aj+c[1],:].flatten()
            constant=np.sqrt(np.sum(block_histogram**2))
            if constant>eps:
                block_histogram/=constant
            block_histograms[i,j,:]=block_histogram
    return block_histograms

def calculate_hog(image,cell_size=6,histogram_bins=9,block_strides=1,block_size=(3,3)):
    
    if (image.shape[0] % cell_size) != 0 :
        raise ValueError("The image height must be divisible by the cell_size.")
                         
    if (image.shape[1] % cell_size) != 0 :
        raise ValueError("The image width must be divisible by the cell_size.")
                         
    angle,mag=calculate_gradient_and_mag(image)
    cells=calculate_cells(angle,mag,histogram_bins,cell_size)
    blocks=calculate_blocks(cells,block_strides,block_size)
    descriptor=blocks.flatten()
    return descriptor
