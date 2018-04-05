function plot_results(dft_functions,repetitions)
    handles=[];
    for i=1:length(dft_functions)
        dft_function=dft_functions(i);
        plot_handle=plot(dft_function.n,dft_function.time,'LineWidth',4);
        handles=[handles plot_handle];
        hold all;
    end
    minimum_bits=min([dft_functions.minimum_bits]);
    maximum_bits=max([dft_functions.maximum_bits]);
    legend_names=strrep({dft_functions.name}, '_', ' ');
    legend(handles,legend_names,'interpreter', 'none');
    xlabel('N');
    ylabel('Time (sec)');
    ticks= 2.^(minimum_bits:maximum_bits);
    ticks=dft_functions(2).n;
    set(gca,'XTickLabel',ticks)
    title(sprintf('Average execution time for various dft implementations (%d repetitions for each N and implementation)',repetitions));
end

