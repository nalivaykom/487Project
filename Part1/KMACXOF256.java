import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.*;

//Comment added by Cliff
//Another one
//Another nother one
public class KMACXOF256 {

    private static final long[] roundConstant = {
        0x0000000000000001L, 0x0000000000008082L, 0x800000000000808aL,
        0x8000000080008000L, 0x000000000000808BL, 0x0000000080000001L,
        0x8000000080008081L, 0x8000000000008009L, 0x000000000000008aL,
        0x0000000000000088L, 0x0000000080008009L, 0x000000008000000aL,
        0x000000008000808bL, 0x800000000000008bL, 0x8000000000008089L,
        0x8000000000008003L, 0x8000000000008002L, 0x8000000000000080L,
        0x000000000000800aL, 0x800000008000000aL, 0x8000000080008081L,
        0x8000000000008080L, 0x0000000080000001L, 0x8000000080008008L
    };

    private static final int[][] offset = {
        {25, 39, 3, 10, 43},
        {55, 20, 36, 44, 6},
        {28, 27, 0, 1, 62},
        {56, 14, 18, 2, 61},
        {21, 8, 41, 45, 15}
    };

    private static byte[] KEY = {1, 2, 3, 4};
    private enum ENCODE {Left, Right}
    
    public static void main(String[] args) {
        
        //System.out.println("hello world");
        /*
        String str = "Hi my name is Michael and my partner is Cliff";
        byte[] bArr = str.getBytes();
        int temp = bArr.length;
        int i = 0;
        StringBuilder totalString = new StringBuilder();
        //System.out.println(bArr.length);
        while (temp > 0) {
            System.out.println("bArr[" + i + "]: " + bArr[i]);
            System.out.println("bArr[" + i + "] in binary: " + Integer.toString(bArr[i], 2));
            totalString.append(Integer.toString(bArr[i], 2));
            temp--;
            i++;
        }
        System.out.println("totalString: " + totalString);
        */
        
        /*
        byte[] temp = lrEncode(0, ENCODE.Left);
        byte[] temp2 = leftEncode(0);
        for (int i = 0; i < temp.length; i++) {
            System.out.println("temp[i] = " + temp[i]);
            System.out.println("temp2[i] = " + temp2[i]);
        }
        */

        // String str = "hello cliff";
        // byte[] strBytes =  str.getBytes();
        // for (byte b : strBytes) {
        //     System.out.println(b);
        // }
        
        // byte[] testInt = new byte[8];
        // testInt[0] = 97;
        // testInt[1] = 99;
        // testInt[2] = 99;
        // testInt[3] = 117; //was 117
        // testInt[4] = 114;
        // testInt[5] = 97;
        // testInt[6] = 99;
        // testInt[7] = 121;

        // long temp = bytesToWord(0, testInt);
        // //System.out.println(temp);
        // byte[] temp2 = wordToBytes(temp);
        // for (int i = 0; i < temp2.length; i++) {
        //     System.out.println(i + " = " + temp2[i]);
            
        // }
        // byte[] emptyByte = {};
        // byte[] KMACOutput = KMACXOF256(KEY, testInt, testInt.length * 8, "");
        // for (int i = 0; i < KMACOutput.length; i++) {
        //     System.out.println(KMACOutput[i]);
        // }
        // System.out.println();
        // byte[] SHA3Return = SHA3(testInt, 512);
        // for (int i = 0; i < SHA3Return.length; i++) {
        //     System.out.println(i + " = " + SHA3Return[i]);
        // }
        // System.out.println();
        // byte[] SHA3Return2 = SHA3(SHA3Return, 512);
        // for (int i = 0; i < SHA3Return2.length; i++) {
        //     System.out.println(i + " = " + (SHA3Return2[i] ^ SHA3Return[i]));
        // }

        // 0111100101100011011000010111001001110101011000110110001101100001
        // 0111100101100011011000010111001001110101011000110110001101100001
        // for (int i = 0; i < 150; i++) {
        //     testInt[i] = (byte) (i);
        // }
        // byte[] a = pad10_1(testInt);
        //byte[] b = padTenOne(1088, testInt);

        // for (int i = 0; i < a.length; i++) {
        //     System.out.println("pad101:    " + a[i]);
        //     System.out.println("padTenOne: " + b[i]);
        //     System.out.println();
        // }
        
        
        //long[][] stateTest = byteArrayToStates(testInt, 512);
        //long[] singleState = stateTest[0];
        // long[][][] thetaReturn = theta(singleState);
        // int inc = 0;
        // for (int i = 0; i < stateTest.length; i++) {
        //     for (int j = 0; j < stateTest[0].length; j++) {
        //         System.out.println(inc + ". stateTest[i][j] = " + stateTest[i][j]);
        //         inc++;
        //     }
        // }

        // System.out.println("return of theta method:");
        // int incr = 0;
        // for (int x = 0; x < 5; x++) {
        //     for (int y = 0; y < 5; y++) {
        //         System.out.print(incr + ". ");
        //         for (int z = 0; z < 64; z++) {
        //             System.out.print(thetaReturn[x][y][z]);
        //         }
        //         incr++;
        //         System.out.println();
        //     }
        // }
      
        // 0001101100011010000110010001100000010111000101100001010100010100
        // 0001101100011010000110010001100000010111000101100001010100010100
        // 1000001010000001100000001000000001111111011111100111110101111100
        // 1000001010000001100000001000000001111111011111100111110101111100

        // System.out.println(stateTest[0].length);
        // for (int i = 0; i < stateTest.length; i++) {
        //     for (int j = 0; j < stateTest[0].length; j++) {
        //         System.out.println("stateTest[i][j] = " + stateTest[i][j]);
        //     }
        // }

        //long[] badThetaReturn = badTheta(singleState);
        // long[] rhoPieReturn = rhoPie(singleState);
        // for (int i = 0; i < singleState.length; i++) {
        //     System.out.println("singleStateReturn[" + i + "] = " + singleState[i]);
        //     System.out.println("rhoPieReturn[" + i + "] =    " + rhoPieReturn[i]);
        // }
        

         //int ofs = 1 % 64;
         //System.out.println(1952900979675763988L << ofs | (1952900979675763988L >>>(Long.SIZE - ofs)));
        //                  6220687490169981512
        // 0001101100011010000110010001100000010111000101100001010100010100
        //  0011011000110100001100100011000000101110001011000010101000101000
        // byte[] testInt2 = new byte[8];
        // testInt2[0] = 1;
        // testInt2[1] = 2;
        // testInt2[2] = 3;
        // testInt2[3] = 4;
        // testInt2[4] = 5;
        // testInt2[5] = 6;
        // testInt2[6] = 7;
        // testInt2[7] = 8;

        // byte[] testInt3 = concatArray(testInt, testInt2);

        // for (byte t : testInt3) {
        //     System.out.println(t);
        // }

        // byte[] temp = bytepad(testInt, 136);
        // byte[] temp2 = bytepad2(testInt, 136);
        // for (int i = 0; i < temp2.length; i++) {
        //     System.out.println("temp[i]: " + temp[i] + "  i = " + i);
        //     System.out.println("temp2[i]: " + temp2[i]);
        // }

        
        //long finalThing = bytesToWord(0, testInt);
         
        //  System.out.println(finalThing);
        //  System.out.println(finalThing % 64);
         //System.out.println(finalThing << (3) | finalThing >>>(Long.SIZE - (3)));
         //System.out.println(finalThing);
         //System.out.println(lRotWord(8746942045507248993L, 1));
        
        // 1101100011010000110010001100000010111000101100001010100010100000
        // 0001101100011010000110010001100000010111000101100001010100010100


        // 111100101100011011000010111001001110101011000110110001101100001
        //-110100111001001111010001101100010101001110010011100100111110
        // 7963617275636361
        // F2C6C2E4EAC6C6C2

        // 7963617220636361
        // -D393D1BBF39393E

        // String str = "111100101100011011000010111001001110101011000110110001101100001";
        // System.out.println(str.length());
        
        // System.out.println(7^3);
        //String str = "18446744073709551615";
        //System.out.println(str.length());
        //System.out.println(15 | 7);









        

        
        // byte[] testInt = new byte[10];
        // testInt[0] = 97;
        // testInt[1] = 99;
        // testInt[2] = 99;
        // testInt[3] = 117; //was 117
        // testInt[4] = 114;
        // testInt[5] = 97;
        // testInt[6] = 99;
        // testInt[7] = 121;
        // testInt[8] = 23;

        // for (int i = 0; i < testInt.length; i++) {
        //     testInt[i] = (byte) i;
        // }


        // long temp = bytesToWord(0, testInt);
        // //System.out.println(temp);
        // byte[] temp2 = wordToBytes(temp);
        // for (int i = 0; i < temp2.length; i++) {
        //     System.out.println(i + " = " + temp2[i]);
            
        // }
        // System.out.println();
        // byte[] emptyByte = {};
        // byte[] KMACOutput = KMACXOF256(new byte[] {1,2,3,4}, testInt, testInt.length * 8, "");
        // for (int i = 0; i < KMACOutput.length; i++) {
        //     System.out.println(KMACOutput[i]);
        // }

        // System.out.println();
        // byte[] KMACOutput2 = KMACXOF256(new byte[] {1,2,3,4}, KMACOutput, testInt.length * 8, "");
        // for (int i = 0; i < KMACOutput2.length; i++) {
        //     System.out.println(KMACOutput2[i]);
        // }

        // System.out.println();
        // for (int i = 0; i < KMACOutput.length; i++) {
        //     System.out.println(KMACOutput[i] ^ KMACOutput2[i]);
        // }

        System.out.println();

        long start = System.nanoTime();

       

        Scanner input = new Scanner(System.in);
        System.out.print("Type a message you would like to encrypt: ");
        String userInput = input.nextLine();

        //String userInput = "hello my man, jennifer is an odd human being";
        System.out.println("Message before encryption: " + userInput);

        byte[] testInt = new byte[userInput.length()];

        for (int i = 0; i < userInput.length(); i++) {
            testInt[i] = (byte) userInput.charAt(i);
        }


        // for (int i = 0; i < testInt.length; i++) {
        //     System.out.println("originalMessage[" + i + "]: " + testInt[i]);
        // }
        // System.out.println();


        byte[] key = {1,2,3,4,75};
        byte[] key2 = {1,2,3,4,75};

        byte[][] symmetricCryptogram = encrypt(testInt, key);
        byte[][] decryptedCryptogram = decrypt(key2, symmetricCryptogram[0], symmetricCryptogram[1], symmetricCryptogram[2]);
        byte[] m = decryptedCryptogram[0];
        byte[] t = symmetricCryptogram[2];
        byte[] tPrime = decryptedCryptogram[1];
        boolean acceptMessage = acceptMessage(t, tPrime);
        // System.out.println("Should you accept this message: " + acceptMessage);

        long finish = System.nanoTime();

        long timeToFinish = finish - start;

        //System.out.println("Time to finish: " + timeToFinish / 1000000 + " ms");

        String finalOutput = "";
            try {
                finalOutput = new String(m, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        if (acceptMessage) {
            System.out.println("Congratulations, your message has been successfully decrypted!");
            
            System.out.println("Your message is: " + finalOutput);
        } else {
            System.out.println("Uh Oh, Something went wrong...");
            System.out.println("The message returned was: " + finalOutput);
        }
        System.out.println();

        // 2.2ghz = 44 seconds
        // 3.0ghz = 31 seconds
        // 3.7ghz = 26 seconds (no tb)
        // 3.9ghz = 26 seconds (no tb)
        // 3.7ghz = 26 seconds
        // 3.9ghz = 26 seconds

        // 2.2ghz = 41205 ms
        // 3.0ghz = 29819 ms
        // 3.7ghz = 25009 ms (no tb)
        // 3.9ghz = 25321 ms (no tb)
        // 3.7ghz = 24808 ms
        // 3.9ghz = 24714 ms

        
        // 2.2ghz = 162 ms
        // 3.0ghz = 112 ms
        // 3.7ghz = 91 ms
        // 3.9ghz = 87 ms

        //////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////
        //decrypting      

    }

    public static byte[][] encrypt(byte[] testInt, byte[] key) {
        byte[][] output = new byte[3][];

        SecureRandom random = new SecureRandom();
        byte[] z = new byte[64];
        random.nextBytes(z);

        byte[] z_key = new byte[z.length + key.length]; 
        System.arraycopy(z, 0, z_key, 0, z.length);
        System.arraycopy(key, 0, z_key, z.length, key.length);

        // for (int i = 0; i < z_key.length; i++) {
        //     System.out.println("z_key[" + i + "]: " + z_key[i]);
        // }

        byte[] ke_ka = KMACXOF256(z_key, new byte[] {}, 1024, "S");
        byte[] ke = new byte[ke_ka.length / 2];
        byte[] ka = new byte[ke_ka.length / 2];
        
        System.arraycopy(ke_ka, 0, ke, 0, ke.length);
        System.arraycopy(ke_ka, ke.length, ka, 0, ka.length);

        // for (int i = 0; i < ke_ka.length; i++) {
        //     System.out.println("ke_ka " + i + ": " + ke_ka[i]);
        // }
        
        // for (int i = 0; i < ke.length; i++) {
        //     System.out.println("ke " + i + ": " + ke[i]);
        // }

        // for (int i = 0; i < ka.length; i++) {
        //     System.out.println("ka " + i + ": " + ka[i]);
        // }
        
        
        // System.out.println(ke.length);
        byte[] cTemp = KMACXOF256(ke, new byte[] {}, testInt.length * 8, "SKE");
        byte[] c = new byte[cTemp.length];
       
        // System.out.println("cTemp length: " + cTemp.length);
        // System.out.println("testInt length: " + testInt.length);
        // for (int i = 0; i < testInt.length; i++) {
        //     System.out.println("cTemp[" + i + "]: " + cTemp[i]);
        //     System.out.println("testInt[" + i + "]: " + testInt[i]);

        // }

        for (int i = 0; i < c.length; i++) {
            long cTempLong = (long) cTemp[i];
            long testIntLong = (long) testInt[i];
            c[i] = (byte) (cTempLong ^ testIntLong);
            // System.out.println("c[" + i + "]: " + c[i]);
        }
        // System.out.println();

        byte[] t = KMACXOF256(ka, testInt, 512, "SKA");
        // System.out.println("length of c: " + c.length);
        // System.out.println("length of testInt: " + testInt.length);

        output[0] = z;
        output[1] = c;
        output[2] = t;
        return output;
    }

    public static byte[][] decrypt(byte[] key2, byte[] z, byte[] c, byte[] t) {
        byte[][] output = new byte[2][];

        byte[] z_key2 = new byte[z.length + key2.length]; 
        System.arraycopy(z, 0, z_key2, 0, z.length);
        System.arraycopy(key2, 0, z_key2, z.length, key2.length);

        byte[] ke_ka2 = KMACXOF256(z_key2, new byte[] {}, 1024, "S");
        byte[] ke2 = new byte[ke_ka2.length / 2];
        byte[] ka2 = new byte[ke_ka2.length / 2];
        
        System.arraycopy(ke_ka2, 0, ke2, 0, ke2.length);
        System.arraycopy(ke_ka2, ke2.length, ka2, 0, ka2.length);

        byte[] mTemp = KMACXOF256(ke2, new byte[] {}, c.length * 8, "SKE");
        byte[] m = new byte[mTemp.length];
        for (int i = 0; i < m.length; i++) {
            long mTempLong = (long) mTemp[i];
            long cLong = (long) c[i];
            m[i] = (byte) (mTempLong ^ cLong);
            // System.out.println("m[" + i + "]: " + m[i]);
        }


        // String finalOutput = "";
        // try {
        //     finalOutput = new String(m, "UTF-8");
        // } catch (UnsupportedEncodingException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
        // System.out.println();
        // System.out.print("Message after decryption: ");
        // System.out.println(finalOutput);
        // System.out.println("length of mTemp= " + mTemp.length);
        // System.out.println("length of c= " + c.length);


        byte[] tPrime = KMACXOF256(ka2, m, 512, "SKA");

        System.out.println();

        // boolean testBool = true;
        // for (int i = 0; i < t.length; i++) {
        //     System.out.println("t[" + i + "]: " + t[i]);
        //     System.out.println("tPrime[" + i + "]: " + tPrime[i]);
        //     //System.out.println("ka: " + ka[i]);
        //     //System.out.println("ka2: " + ka2[i]);
        //     if (t[i] != tPrime[i]) {
        //         System.out.println(i);
        //         testBool = false;
        //     }
        // }
        // System.out.println("testBool = " + testBool);

        // for (int i = 0; i < t.length; i++) {
            
        // }  

        output[0] = m;
        output[1] = tPrime;

        return output;

    }

    public static boolean acceptMessage(byte[] t, byte[] tPrime) {
        boolean returnBool = true;
        for (int i = 0; i < t.length; i++) {
            if (t[i] != tPrime[i]) {
                returnBool = false;
            }
        }
        return returnBool;
    }

    public static byte[] KMACXOF256(byte[] key, byte[] input, int outLength, String str) {
        
        byte[] newX = new byte[bytepad(encodeString(key), 136).length + input.length + rightEncode(0).length];
        System.arraycopy(bytepad(encodeString(key), 136), 0, newX, 0, bytepad(encodeString(key), 136).length);
        System.arraycopy(input, 0, newX, bytepad(encodeString(key), 136).length, input.length);
        System.arraycopy(rightEncode(0), 0, newX, bytepad(encodeString(key), 136).length + input.length, rightEncode(0).length);
        //bytepad(encodeString(key), 136) + input + lrEncode(outLength, ENCODE.Right);

        
  
        //byte[] returnArray = {};
        //return returnArray;
        return cShake256(newX, outLength, "KMAC", str);
    }

    

    public static byte[] cShake256(byte[] input, int outLength, String functionName, String customString) {

        byte[] inFun = concatByteArray(encodeString(functionName.getBytes()), encodeString(customString.getBytes()));
        inFun = concatByteArray(bytepad(inFun, 136), input);
        inFun = concatByteArray((inFun), new byte[] {0x04});////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        return sponge(inFun, outLength, 512);
        
        //previously was thinking of returning KECCAK[512](bytepad(encode_string(N) || encode_string(S), 136) || X || 00, L);

    }

    public static byte[] sponge(byte[] input, int outLength, int capacity) {
        //need keccak perm done first //jk keccak is donedone :)
        int rate = 1600 - capacity;
        long[] outputLongArray = {};
        byte[] padded = input;
        if ((padded.length % (rate / 8)) != 0) {
            padded = pad10_1(padded);
        }
        long[][] states = byteArrayToStates(padded, capacity);
        long[] state = new long[25];
        for (int i = 0; i < states.length; i++) {
            state = keccakp(xorLongs(state, states[i]), 1600, 24);
        }
    
        // Arrays.copyOf(original, newLength)

        // for (int i = 0; i )

        

        //return new byte[] {};

        //change//////////////////////////////////////////////////////////////////////////
        long[] out = {};
        int offset = 0;
        do {
            out = Arrays.copyOf(out, offset + rate/64);
            System.arraycopy(state, 0, out, offset, rate/64);
            offset += rate/64;
            state = keccakp(state, 1600, 24);
        } while (out.length*64 < outLength);

        return stateToByteArray(out, outLength);
    }

    
    /**
     * start of keccak function
     */

    public static long[] keccakp(long[] input, int outLength, int rounds) {
        long[] output = input;
        for (int i = 0; i < rounds; i++) {
            output = iota(chi(rhoPie(theta(output))), i);////////////check if we can just do input
        }
        return output;
    }

    public static long[] theta(long[] input) {
        //long[][][] tempInput = new long[5][5][64];
        //long[][][] tripleArrayOutput = new long[5][5][64];
        //String[] stringLanes = new String[25];
        //long[][] C = new long[5][64];
        //long[][] D = new long[5][64];
        long[] C = new long[25];
        long[] D = new long[25];
        long[] output = new long[25];

        for (int x = 0; x < 5; x++) {
            C[x] = input[x] ^ input[x + 5] ^ input[x + 10] ^ input[x + 15] ^ input[x + 20];
        }

        for (int x = 0; x < 5; x++) {
            long tempWord = C[(x + 1) % 5];
            D[x] = C[(x + 4) % 5] ^ (tempWord << 1 | (tempWord >>>(Long.SIZE - 1)));
        }

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                output[x + (5 * y)] = input[x + (5 * y)] ^ D[x];
            }
        }


        // for (int i = 0; i < 25; i++) {
        //     String temp = Long.toBinaryString(input[i]);
        //     while (temp.length() < 64) {
        //         temp = "0" + temp;
        //     }
        //     stringLanes[i] = temp;
        // }

        // int incr = 0;
        // for (int x = 0; x < 5; x++) {
        //     for (int y = 0; y < 5; y++) {
        //         for (int z = 0; z < 64; z++) {
        //             tempInput[x][y][z] = Long.parseLong(Character.toString(stringLanes[incr].charAt(z)));
        //         }
        //         incr++;
        //     }
        // }
        
        // for (int x = 0; x < 5; x++) {
        //     for (int z = 0; z < 64; z++) {
        //         C[x][z] = tempInput[x][0][z] ^ tempInput[x][1][z] ^ tempInput[x][2][z] ^ tempInput[x][3][z] ^ tempInput[x][4][z];
        //     }
        // }
 
        // for (int x = 0; x < 5; x++) {
        //     for (int z = 0; z < 64; z++) {
        //         D[x][z] = C[(x+4) % 5][z] ^ C[(x+1) % 5][(z+63) % 64];
        //     }
        // }

        // for (int x = 0; x < 5; x++) {
        //     for (int y = 0; y < 5; y++) {
        //         for (int z = 0; z < 64; z++) {
        //             tripleArrayOutput[x][y][z] = tempInput[x][y][z] ^ D[x][z];
        //         }
        //     }
        // }
        

        // int incr2 = 0;
        // for (int x = 0; x < 5; x++) {
        //     for (int y = 0; y < 5; y++) {
        //         String tempString = "";
        //         for (int z = 0; z < 64; z++) {
        //             tempString = tripleArrayOutput[x][y][z] + tempString;
        //         }
        //         System.out.println("hello im tempstring " + tempString);
        //         System.out.println("tempstring parsed with radix 2: " + Long.parseLong(tempString, 2));
        //         output[incr2] = Long.parseLong(tempString, 2);
        //     }
        // }        
        // long[][] C = new long[5][64]; 

        // for (int x = 0; x < 5; x++) {
        //     for (int z = 0; z < 64; z++) {
        //         C[x][z] = 
        //     }
        // }

        // for (int x = 0; x < 5; x++) {
        //     C[x] = input[x] ^ input[x + 5] ^ input[x + 10] ^ input[x + 15] ^ input[x + 20];
        // }
        // String temp = Long.toBinaryString(C[0]);
        // char temp2 = temp.charAt(0);
        // long temp3 = Long.parseLong(Character.toString(temp2));
        
        // for (int x = 0; x < 5; x++) {
        //     for (int z = 0; z < 64; z++) {
                
        //     }
        // } 

        return output;
    }

    public static long[] rhoPie(long[] input) {
        long[] output = new long [25];
        
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                output[x + (5 * (((2 * x) + (3 * y)) % 5))] = (input[x + (5 * y)] 
                << offset[x][y] | (input[x + (5 * y)] >>>(Long.SIZE - offset[x][y])));
            }
        }
        return output;
    }    

     public static long[] chi(long[] input) {
        long[] output = new long [25];

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {   
                output[x + (5 * y)] = input[x + (5 * y)] ^ (( ~input[((x + 1) % 5) + (5 * y)]) & input[((x + 2) % 5) + (5 * y)]); 
            }        
        }
        return output;
     }

    public static long[] iota(long[] input, int round) {
        long[] output = new long [25];
        output = input;
        output[0] = output[0] ^ roundConstant[round]; 
        return output;
    }


    /**
    * Apply the NIST bytepad primitive to a byte array X with encoding factor w.
    * @param X the byte array to bytepad
    * @param w the encoding factor (the output length must be a multiple of w)
    * @return the byte-padded byte array X with encoding factor w.
    */
    private static byte[] bytepad(byte[] X, int outLen) {

        if (outLen > 0) {
            //byte[] wenc = leftEncode(outLen);
            byte[] wenc = leftEncode(outLen);
            byte[] z = new byte[outLen*((wenc.length + X.length + outLen - 1)/outLen)];

            System.arraycopy(wenc, 0, z, 0, wenc.length);
            System.arraycopy(X, 0, z, wenc.length, X.length);
            for (int i = wenc.length + X.length; i < z.length; i++) {
                z[i] = (byte)0;
            }
 
            return z;
        }
        return X;
    }

    private static byte[] encodeString(byte[] s) {
        
        byte[] returnArray = new byte[leftEncode(s.length).length + s.length];
        System.arraycopy(leftEncode(s.length), 0, returnArray, 0, leftEncode(s.length).length);
        System.arraycopy(s, 0, returnArray, leftEncode(s.length).length, s.length);

        return returnArray;
    }
    
    private static byte[] leftEncode(long length) {
        if (length == 0) return new byte[] {1, 0};
        
        long lengthCopy = length;
        byte[] tempArray = new byte[8]; 
        int i = 0;
        while (lengthCopy > 0) {
            byte lengthByte = (byte) lengthCopy;
            lengthCopy = lengthCopy>>>8;
            tempArray[7 - i++] = lengthByte;
        }
        byte[] returnArray = new byte[i + 1];
        returnArray[0] = (byte) i;
        System.arraycopy(tempArray, 8 - i, returnArray, 1, i);

        return returnArray;
    }
    
    private static byte[] rightEncode(long length) {

        if (length == 0) return new byte[] {0, 1};
        
        long lengthCopy = length;
        byte[] tempArray = new byte[8]; 
        int i = 0;
        while (lengthCopy > 0) {
            byte lengthByte = (byte) lengthCopy;
            lengthCopy = lengthCopy>>>8;
            tempArray[7 - i++] = lengthByte;
        }
        byte[] returnArray = new byte[i + 1];
        returnArray[returnArray.length - 1] = (byte) i;
        System.arraycopy(tempArray, 8 - i, returnArray, 0, i);

        return returnArray;
    }
    
    public static byte[] concatByteArray(byte[] a1, byte[] a2) {
        byte[] returnArray = Arrays.copyOf(a1, a1.length + a2.length);
        System.arraycopy(a2, 0, returnArray, a1.length, a2.length);
        return returnArray;
    }



    private static long bytesToWord(int offset, byte[] in) {        
        long word = 0L;
        for (int i = 0; i < 8; i++) {
            word += (((long) in[offset + i])) << (8*i);
            // System.out.println("with thing= " + ((((long)in[offset + i]) & 0xff)<<(8*i)));
            // System.out.println("without thing= " + ((((long)in[offset + i]) & 0xff)));
            // System.out.println("without 0xff= " + ((((long)in[offset + i]))<<(8*i)));
        }
        return word;
    }

    private static byte[] wordToBytes(long input) {
        String temp = Long.toBinaryString(input);
        while (temp.length() % 8 != 0) {
            temp = "0" + temp;
        }
        //System.out.println("wordToBytes binarystring temp: " + temp.length());
        String[] stringArr = new String[temp.length() / 8];
        int ofs = 0;
        int i = 0;
        while (ofs < temp.length()) {
            stringArr[i++] = temp.substring(ofs, ofs + 8);
            //System.out.println("stringArr[i] = " + temp.substring(ofs, ofs + 8));
            ofs = ofs + 8;
        }
        byte[] tempArr = new byte[stringArr.length];
        for (int j = 0; j < stringArr.length; j++) {
            tempArr[j] = (byte) Long.parseLong(stringArr[j], 2);
            //System.out.println("output[j] = " + (byte) Long.parseLong(stringArr[j], 2));
        } 
        
        byte[] output = new byte[tempArr.length];
        int k = output.length - 1;
        for (int j = 0; j < tempArr.length; j++) {
            output[j] = tempArr[k--];
        }

        return output;
    }

    public static long[] xorLongs(long[] l1, long[] l2) {
        long[] returnLong = new long[25];
        for (int i = 0; i < l1.length; i++) {
            returnLong[i] = l1[i] ^ l2[i];
        }
        return returnLong;
    }
    
   

    public static long [][] byteArrayToStates(byte[] input, int capacity) {
        long[][] states = new long[(input.length * 8) / (1600 - capacity)][25];
        int ofs = 0;
        for (int i = 0; i < states.length; i++) {
            long[] state = new long[25];
            for (int j = 0; j < (1600 - capacity) / 64; j++) {
                long word = bytesToWord(ofs, input);
                state[j] = word;
                ofs = ofs + 8;
            }
            states[i] = state;
        }
        return states;
    }

    public static byte[] pad10_1(byte[] input) {
        int numToPad = 136 - (input.length % 136);
        byte[] output = new byte[input.length + numToPad];
        System.arraycopy(input, 0, output, 0, input.length);
        for (int i = 0; i < numToPad; i++) {
            output[input.length + i] = 0;
        }
        output[input.length + numToPad - 1] = (byte) 0x80;
        return output;
    }

    // public static byte[] stateToByteArray(long[] input, int outLength) {
    //     byte[] output = new byte[outLength / 8];
    //     // System.out.println();
    //     // System.out.println(outLength/8);
    //     // System.out.println("output length: " + output.length / 8);
    //     // System.out.println();
    //     int index = 0;
    //     int ofs = 0;
    //     while ((index * 64 < outLength)) {
    //         if (index < outLength / 8) {
    //             // System.out.println(ofs < output.length);
    //             long word = input[index];
    //             byte[] temp = wordToBytes(word);
    //             // System.out.println("temp.length: " + temp.length);
    //             System.arraycopy(temp, 0, output, ofs, temp.length);
    //             ofs = ofs + 1;
    //             index++;
    //             // System.out.println("index: " + index);
    //             // System.out.println("ofs: " + ofs);
    //         }
    //     }
    //     return output;
    // }
    private static byte[] stateToByteArray(long[] state, int bitLen) {
        if (state.length*64 < bitLen) throw new IllegalArgumentException("State is of insufficient length to produced desired bit length.");
        byte[] out = new byte[bitLen/8];
        int wrdInd = 0;
        while (wrdInd*64 < bitLen) {
            long word = state[wrdInd++];
            int fill = wrdInd*64 > bitLen ? (bitLen - (wrdInd - 1) * 64) / 8 : 8;
            for (int b = 0; b < fill; b++) {
                byte ubt = (byte) (word>>>(8*b) & 0xFF);
                out[(wrdInd - 1)*8 + b] = ubt;
            }
        } 

        return out;
    }
}