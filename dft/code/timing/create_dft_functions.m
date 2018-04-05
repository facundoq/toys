function dft_functions=create_dft_functions()

dft_functions=struct('name','','handle',[],'minimum_bits',0,'maximum_bits',0,'n',[],'time',[]);

dft_functions(1).name='dft';
dft_functions(1).handle=@dft;
dft_functions(1).maximum_bits=12;

maximum_bits=18;
dft_functions(2).name='fft';
dft_functions(2).handle=@fft;
dft_functions(2).maximum_bits=maximum_bits;

dft_functions(3).name='facuft';
dft_functions(3).handle=@facuft;
dft_functions(3).maximum_bits=maximum_bits;

dft_functions(4).name='facuft_basecase';
dft_functions(4).handle=@facuft_basecase;
dft_functions(4).maximum_bits=maximum_bits;

dft_functions(5).name='facuft_iterative';
dft_functions(5).handle=@facuft_iterative;
dft_functions(5).maximum_bits=maximum_bits;

end