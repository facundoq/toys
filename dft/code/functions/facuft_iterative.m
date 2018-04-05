function f=facuft_iterative(x)
    f=permutations(x);
    f=combinations(f);
end

function x=permutations(x)
    N=length(x);
    levels=log2(N);
    for l=0:(levels-2)
        x=permutation_for_level(l,x,N);
    end
end

function f=combinations(f)
    N=length(f);
    levels=log2(N);
    for l=(levels-1):-1:0
        f=combinations_for_level(l,f,N);
    end
end

function f=permutation_for_level(l,x,N)
        inputs=2^l;
        input_size=N/inputs;
        output_size=input_size/2;
        f=zeros(size(x));
        for i=0:inputs-1
            input_base_index=i*input_size;
            even= x(input_base_index+1:2:input_base_index+input_size);
            odd = x(input_base_index+2:2:input_base_index+input_size);
            
            f(input_base_index+1:input_base_index+output_size)=even;
            f(input_base_index+1+output_size:input_base_index+output_size*2)=odd;
        end
end

function f=combinations_for_level(l,x,N)
    outputs=2^l;
    output_size=N/outputs;
    input_size=output_size/2;
    Wn=exp(-2*pi*1i/output_size);
    twiddle_factors= Wn.^(0:(input_size-1));
    for o=0:outputs-1
        output_base_index=o*output_size;
        f_even=x(output_base_index+1:output_base_index+input_size);
        f_odd=x(output_base_index+1+input_size:output_base_index+input_size*2);
        tf=twiddle_factors;
        f(output_base_index+1:output_base_index+input_size)=f_even+ tf.* f_odd;
        f(output_base_index+1+input_size:output_base_index+input_size*2)=f_even- tf.* f_odd;
    end
end