

The files main_facuft_VERSION.m contain sample code to compare the output of MATLAB's FFT default implementation with other pure MATLAB implementations of DFT algorithms ( DFT, FACUFT, FACUFT_ITERATIVE,FACUFT_BASECASE).

These scripts call the functions defined in the following files of the folder "functions":

dft.m
facuft.m
facuft_basecase.m
facuft_iterative.m

These files contain the different implementations of the DFT algorithms. Each defines one function.

The file main_timing.m has a script that performs a comparative analysis of the experimental running time of the previous functions. Other functions defined in the folder "timing" help in this task, but have little importance since they are not employed to calculate the DFT in any way, they are only used to test the implementations.
