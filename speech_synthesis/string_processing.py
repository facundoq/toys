

# validates the syntax of the input string to check it only contains
# Consonant-Vowel syllables
def validate_syntax(input_string):
    vowels=['a','A']
    consonants=['k','l','m','s','p']
    n=len(input_string)
    if n == 0:
        return False
    if n % 2 != 0:
        return False
    pairs= n / 2
    for i in xrange(pairs):
        consonant=input_string[2*i]
        vowel=input_string[2*i+1]
        if not (vowel in vowels) or  not(consonant in consonants):
            return False
    return True

# receives an input string with valid syntax
# returns the list of diphones the string represents
# as a list of strings
def tokenize(input_string):
    n=len(input_string)
    pairs= n / 2
    diphone_tuples=zip(input_string,input_string[1:])
    f = lambda (consonant,vowel): consonant+vowel
    diphones=map(f,diphone_tuples)
    diphones.insert(0,'-'+input_string[0])
    diphones.append(input_string[-1]+'-')
    return diphones
