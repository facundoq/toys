import soundfile as sf
import numpy as np
import sys
import smoothing
import matplotlib.pyplot as plt


# takes a list of diphones and outputs a wav file with the wavs of each
# diphone (taken from the diphones_path) concatenated
def synthesize(diphones_wavs_db,diphones):
    try:
        diphone_wavs=map(lambda diphone: diphones_wavs_db[diphone],diphones)
    except KeyError, e:
        sys.exit("Diphone %s was not found" % str(e))
    return concat_wav(diphone_wavs)

# concatenate multiple wavs into a single one
# by repeatedly concatenating two wavs
# basically a do fold over wavs with concat2_wav
def concat_wav(wavs):
    output_wav=concat2_wav(wavs[0],wavs[1])
    for i in xrange(2,len(wavs)):
        output_wav=concat2_wav(output_wav,wavs[i])
    return output_wav

# concatenates two sound signals wav1 and wav2
# and smoothes the interval around the union
def concat2_wav(wav1,wav2):
    output_wav=np.hstack([wav1,wav2])
    sigma=10
    desired_radius=50
    position=len(wav1)
    output_wav_smoothed=smoothing.smooth_wav(output_wav,position,sigma,desired_radius)

    # f, axarr = plt.subplots(2,sharex=True,sharey=True)
    # axarr[0].plot(output_wav)
    # axarr[0].set_title('original' )
    #
    # axarr[1].plot(output_wav_smoothed)
    # axarr[1].set_title('smoothed at %d' % position)
    # plt.show()

    return output_wav_smoothed
