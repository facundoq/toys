function f=facuft_iterative(x)
    f=permutations(x); %O(n log(n))
    f=combinations(f); %O(n log(n))
end
function x=permutations(x)
    N=length(x); %O(n)
    levels=log2(N); %O(1)
    for l=0:(levels-2) %O(n log(n))
        x=permutation_for_level(l,x,N); %O(n)
    end
end
function f=combinations(f)
    N=length(f); %O(n)
    levels=log2(N); %O(1)
    for l=(levels-1):-1:0 %O(n log(n))
        f=combinations_for_level(l,f,N); %O(n)
    end
end
