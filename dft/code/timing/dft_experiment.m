function dft_functions=dft_experiment(dft_functions,repetitions)

frequency=100;
minimum_bits=ceil( log(frequency));
fn=length(dft_functions);
for i=1:fn
    dft_function=dft_functions(i);
    dft_function.minimum_bits=minimum_bits;
    fprintf('%d/%d Running experiment with %s..\n',i,fn,dft_function.name);
    for bits=minimum_bits:dft_function.maximum_bits
        N=2^bits;
        %T=2*pi/N;
        %Fs=1/T;
        x=linspace(0,2*pi,N);
        y=sin(2*pi*frequency*x);
        tic;
        for j=1:repetitions
            f=dft_function.handle(y);
        end
        t=toc;
        dft_function.n=[dft_function.n N];
        dft_function.time=[dft_function.time t/repetitions];
    end
    dft_functions(i)=dft_function;
end