
#splits diphones, generating the diphone lib,
# and then synthesizes the strings in the variable input_strings

import os
import sys



os.system('python split_diphones.py')

output_folderpath='test_outputs/'
input_strings = ['kamAla','lapAsa','malApa','mamA','pasAka','sakAma']
input_strings+= ['kala','laka','mama','papa','papA','sAla','kasa','sakA']
input_strings+= ['mamAsalAlapApa','papAsakAlakAma',]

if not os.path.exists(output_folderpath):
        os.makedirs(output_folderpath)

for input_string in input_strings:
    output_filepath=os.path.join(output_folderpath,input_string+'.wav')
    os.system('python tts.py %s %s' % (input_string,output_filepath))
