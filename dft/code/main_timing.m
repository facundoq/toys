clear;
clc;
addpath(genpath('.'));

fprintf('Creating dft functions..\n');
dft_functions=create_dft_functions();
fprintf('Executing warmup..\n');
dft_experiment(dft_functions,1); %warmup
repetitions=10;
fprintf('Executing dft experiments..\n');
dft_functions=dft_experiment(dft_functions,repetitions);
fprintf('Plotting results..\n');
plot_results(dft_functions,repetitions);
plot_results(dft_functions(1:5),repetitions);
set(gcf,'Color','White');
export_fig running_time_iterative.pdf