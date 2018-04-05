addpath(genpath('.'));
clc;
clear all;

frequency=200;

bits=13;
N=2^bits;
T=2*pi/N;
Fs=1/T;
x=linspace(0,2*pi,N);

f=sin(2*pi*frequency*x);

y=facuft_iterative(f);
y_fft=fft(f);

y=fftshift(y);
y_fft=fftshift(y_fft);

subplot(3,1,1);
plot(x,f);
period=1/frequency;
xlim([0,2*period]);
title(sprintf('Original function f=sin(2*pi*%d*x), sampling frequency = %.2fhz',frequency,Fs));

frequencies=(-N/2:(N/2-1))*Fs/N;

subplot(3,1,2);
plot(frequencies,abs(real(y)));
title(sprintf('facuft iterative(): abs(real(F)), N=2^{%d}',bits));

subplot(3,1,3);
plot(frequencies,abs(real(y_fft)));
title(sprintf('Matlab´s fft(): abs(real(F)), N=2^{%d}',bits));

set(gcf,'Color','White');
export_fig facuft_iterative.pdf