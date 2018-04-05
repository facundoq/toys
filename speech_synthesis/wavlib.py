import numpy as np
import soundfile as sf

def read_wav(filepath):
    f = sf.SoundFile(filepath)
    wav= f.read(filepath)
    assert f.samplerate == 16000
    assert f.channels == 1
    assert f.subtype == 'PCM_16'
    return wav

def write_wav(wav,filepath):
    sf.write(filepath, wav, 16000)
