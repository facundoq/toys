#!/usr/bin/python
import sys
import os
import diphone_db
import ttslib
import wavlib
import string_processing

# prints without a newline
def print2(a):
    sys.stdout.write(a)

# checks the argument count
def validate_arguments():
    argument_count=len(sys.argv)
    if ( argument_count != 3 ):
        sys.exit('\n ERROR: Invalid number of arguments (%d).\nUsage: pytts input_string output_file' % argument_count)
    input_string=sys.argv[1]
    output_filepath=sys.argv[2]

    return input_string,output_filepath

# synthesizes a string to speech. the string is received in sys.argv
def main():
    input_string,output_filepath=validate_arguments()

    print2("1) Checking syntax... ")
    if not string_processing.validate_syntax(input_string):
        sys.exit('\n ERROR: Invalid input string syntax \"%s\". Input must be of the form (CV)+ where C is a consonant taken from {m,l,s,p,k} and V a vowel taken from {a,A}.' % input_string)
    print "done, syntax ok."

    diphones_path="diphones"
    print2("2) Loading diphones wav files from folder %s... " % diphones_path)
    diphones_wavs_db=diphone_db.read_diphones(diphones_path)
    print "done, %d diphones loaded." % len(diphones_wavs_db)

    print2("3) Parsing diphones from: \"%s\"... " % input_string)
    diphones=string_processing.tokenize(input_string)
    print "done, diphones to synthesize: %s." % str(diphones)

    print2("4) Synthesizing... ")
    output_wav=ttslib.synthesize(diphones_wavs_db,diphones)
    print "done."

    print2("5) Saving file %s..." % output_filepath)
    wavlib.write_wav(output_wav,output_filepath)
    print "done."


if __name__ == "__main__":
    main()
