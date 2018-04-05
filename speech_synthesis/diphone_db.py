import soundfile as sf
import numpy as np
import wavlib
import os
import matplotlib.pyplot as plt

def preprocess_diphone(name,diphone):
    #normalize amplitude
    #preprocessed_diphone=diphone/max(diphone)

    # TODO remove
    # f, axarr = plt.subplots(2, sharex=True)
    # axarr[0].plot(diphone)
    # axarr[0].set_title('%s original' % name )
    # diphone[diphone>1]=1
    # axarr[1].plot(diphone)
    # axarr[1].set_title('%s preprocessed' % name )
    # plt.show()

    return diphone

# reads a db of diphones from diphones_path
# returns a dict where the key is the name of the diphone and the value is the
# audio signal, float32 encoded, in a numpy array
def read_diphones(diphones_path):
    diphones_wavs_db={}

    for filename in os.listdir(diphones_path):
        if filename.endswith(".wav"):
            wav = wavlib.read_wav(os.path.join(diphones_path,filename))
            name=os.path.splitext(filename)[0]
            wav=preprocess_diphone(name,wav)
            diphones_wavs_db[name]=wav
    return diphones_wavs_db
