module Encoding where

import Debug.Trace
import System.Random
import Number
--import Data.BitString
import Data.BitVector -- cabal install bv-0.1.0
import Data.Char


--decode :: Modulo -> [Integer] -> String
decode n ints   = map ( chr . fromIntegral . nat ) bitsSplit
            where  bits = integersToBits n ints 
                   bitsSplit = splitBits bits charSize

decodeAndUnpad n ints = map ( chr . fromIntegral . nat ) bitsSplit
            where  bits = integersToBits n ints 
                   unpaddedBits = unpad n bits charSize
                   bitsSplit = splitBits (concatBitVector unpaddedBits) charSize

encodeAndPad  n s  = map nat ints
            where
                  bits = stringToBits s charSize
                  paddedBits = pad n bits charSize
                  ints = splitBits (concatBitVector paddedBits) n

encode n s = map nat ints
            where bits = stringToBits s charSize
                  ints = splitBits bits n



-- segmentSize must be a multiple of elementSize, and size of bits must be a multiple of elementSize too
pad segmentSize bits elementSize = bitsFilled
        where   fill = fillForBits elementSize -- generate the fill pattern
                bitsSplit = splitBits bits elementSize -- split the bit array into a list of elementSize elements
                bitsReplaced = duplicate bitsSplit fill -- duplicate the fill pattern
                bitsReplacedConcat = concatBitVector bitsReplaced -- get all the bits concat to take the size
                bitsInLastSegment = mod (size bitsReplacedConcat) segmentSize -- calculate how many bits in the last segment
                missingBits = if (bitsInLastSegment > 0 )  then segmentSize - bitsInLastSegment else 0-- calculate how many bits must be added to the last segment to complete it (this is a multiple of elementSize)
                missingElements = div missingBits elementSize -- calculate how many extra elements 
                bitsToAdd =  (take missingElements $ repeat fill)
                bitsFilled = bitsReplaced ++ bitsToAdd

unpad segmentSize bits elementSize = bitsReplaced
        where   fill = fillForBits elementSize -- generate the fill pattern
                bitsSplit = splitBits bits elementSize -- split the bit array into a list of elementSize
                bitsWithoutFill = removeEnding fill bitsSplit  -- remove the fill at the end
                bitsReplaced = removeDuplicates bitsWithoutFill fill -- remove the fill pattern duplicates

charSize = 8 ::Int -- in bits


-- BitRequirements
bitsToStore 0 = 1
bitsToStore n = (ceiling $ logBase 2 (fromIntegral (n+1)))


bitsToStoreNMultipleOf n d =  nearestUpperMultiple (bitsToStore n) d -- d must be a divisor of n
nearestUpperMultiple n d | r==0 = n
                         | otherwise = n + missing
                        where missing = d - r
                              r = mod n d


-- BitVectorUtility
concatBitVector [] = bitVec 0 0
concatBitVector xs = foldr1 (#) xs


fillToMultipleOf multiple bits fill = bits # finalFill
                                      where extra=mod (size bits) (fromIntegral multiple)
                                            fills= div extra (size fill)
                                            finalFill = concatBitVector (take fills $ repeat fill)

--toBits :: String -> [Integer]
fillForBits charSize = (zeros (fromIntegral charSize))+( fromIntegral $ ord 'a')

-- size of BITS must be divisible by segmentSize
splitBits bits segmentSize | sizeOfBits == 0 = []
                           | otherwise = mostSignificant : (splitBits lessSignificant $  segmentSize)
                        where mostSignificant = most (segmentSize) bits
                              lessSignificant = least ( (sizeOfBits-ss)) bits
                              sizeOfBits = size bits
                              ss =  segmentSize

-- BitVector Conversion
stringToIntegers s = map ord s

integersToBits n ints = concatBitVector bitList
            where bitList = map (bitVec (fromIntegral n)) ints -- list of bits of size n


-- Can i create a bitvector with 0 elements?
stringToBits s segmentSize = concatBitVector bitSegments
                            where ints = stringToIntegers s
                                  bitSegments = map (bitVec (fromIntegral segmentSize)) ints

bitsToString bits segmentSize = map (chr . fromIntegral) (bitsToIntegers bits segmentSize)

bitsToIntegers bits segmentSize = map nat bitList
                                where bitList = splitBits bits segmentSize


-- ListUtility

duplicate [] e = []
duplicate (b:bs) e | b == e = e:(e: call)
                   | otherwise = b : call
                    where call = (duplicate bs e)

removeDuplicates [] e = []
removeDuplicates [x] e = [x]
removeDuplicates (x1:(x2:xs)) e | x1 == x2 && x1 == e = e : (removeDuplicates xs e)
                                | otherwise = x1 : (removeDuplicates (x2:xs) e)

removeEnding e xs  = reverse (removeBeginning e (reverse xs))
removeBeginning e xs = dropWhile (==e) xs