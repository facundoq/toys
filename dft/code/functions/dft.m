function f = dft(x)
  f=zeros(size(x));
  N=length(x);
  Wn=exp(-2*pi*1i/N);
  for k=1:N
    for n=1:N
      f(k)=f(k)+ x(n) * Wn^((k-1)*(n-1));
    end
  end
end