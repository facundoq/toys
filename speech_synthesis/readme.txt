Developed in Python 2.7@lubuntu 14.04

Libraries used:
	pysoundfile
	numpy
	scipy

Install instructions for debian/ubuntu based distros:

1) pysoundfile python sound lib

requires:
	pip (sudo apt-get install python-pip)
	libsndfile (sudo apt-get install libsndfile1)
	numpy (sudo apt-get install python-numpy)
	cffi (sudo apt-get install python-dev && sudo apt-get install python-cffi)

install with:
	pip install pysoundfile

2) numpy: should be installed as a requisite for pysoundfile in step 1.

3) scipy: sudo apt-get install python-scipy

Project Outline

1) Main scripts:
tts.py                Main file. Can be executed with "python tts.py input_string output_file".
test.py               Helper script that splits the diphones, and then calls the main file with many test strings, leaving the resulting synthesized wavs in folder 'test_outputs'.

2) Files with utility functions:
diphone_db.py         Loads .wav files from a directory to a dictionary (ie, the diphone db)
smoothing.py          Helper functions to  smooth a signal with a gaussian kernel in a given interval.
string_processing.py  Helper functions to validate the syntax of the input and tokenize it.
ttslib.py							Main synthethize function. Concatenates wavs and smoothes the unions.
wavlib.py							Small wrapper around pysoundfile

3) Diphone splitting script:
split_diphones.py     Given a set of wav audio files, and annotations that indicate the intervals for diphones in the file, extracts the diphones to separate wav files.

4) Folders:
sample_outputs: Samples given by Gravano.
diphone_words: Wav files with words from where to extract diphones (recorded)
diphones: Diphones extracted to use for Synthesizing (generated with split_diphones.py)
test_outputs: Set of test wavs syntesized by tts.py (generated with test.py)
