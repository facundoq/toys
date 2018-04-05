
# Extracts subintervals of wav files to use as
# diphones.

import numpy as np
import wavlib
import os

#Diphones in language
#a-, A-,
# -k, -l, -m, -s, -p
# ka, la, ma, sa, pa
# ak, al, am, as, ap
# kA, lA, mA, sA, pA
# Ak, Al, Am, As, Ap


# extract subintervals of a wav file generating a  diphone wav file
# for each subinterval in the output_folderpath
def split_wav(input_filepath,diphones,output_folderpath):
    wav=wavlib.read_wav(input_filepath)
    tolerance=0
    for (name,start_ms,end_ms) in diphones:
        start=start_ms*16 -tolerance # 16000
        #start=max(start,0)
        end=end_ms*16 +tolerance
        #end=min(end,len(diphones))
        diphone=wav[start:end]
        output_filename='%s.wav' % name
        output_filepath=os.path.join(output_folderpath,output_filename)
        wavlib.write_wav(diphone,output_filepath)


class DiphoneSource:
        def __init__(self,filepath,splits):
            self.splits=splits # tuples of the form (diphone_name,start_ms,end_ms)
            self.filepath=filepath #path to wav file containing diphones

def main():
    input_folderpath='diphone_words/'

    # helper functions to create DiphoneSources
    sources=[]
    make_ds=lambda name,splits: DiphoneSource(os.path.join(input_folderpath,name+'.wav'),splits)
    make_ds_and_add=lambda name,splits: sources.append(make_ds(name,splits))

    # Source wav files from which to extract diphones, and intervals for each diphone
    make_ds_and_add('mamA',  [('A-',1202,1704)])
    make_ds_and_add('kamAla',[('a-',624,900)])
    make_ds_and_add('kamAla',[('-k',  0, 20),('ka', 15, 70),('am', 70,225),('mA',225,384),('Al',461,550)])
    make_ds_and_add('malApa',[('-m',550,700),('ma',700,836),('al',836,951),('lA',951,1100),('Ap',1100,1300)])
    make_ds_and_add('lapAsa',[('-l', 75,225),('la',225,350),('ap',350,470),('pA',470,625),('As',625,800)])
    make_ds_and_add('pasAka',[('-p', 77,170),('pa',170,260),('as',260,370),('sA',370,530),('Ak',530,720)])
    make_ds_and_add('sakAma',[('-s',630,700),('sa',700,820),('ak',820,930),('kA',930,1150),('Am',1150,1300)])


    output_folderpath='diphones'
    print "Generating diphones in folder \"%s\"..." % output_folderpath
    for source in sources:
        split_wav(source.filepath,source.splits,output_folderpath)
    print "Done"


if __name__ == "__main__":
    main()
