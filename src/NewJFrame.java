/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Hamada
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Random;




public class NewJFrame extends javax.swing.JFrame {
        
    static DataInputStream dataInputStream ;
    static DataOutputStream dataOutputStream ;
    
    static ServerSocket serversocket ;
    static Socket socket ;

    
    
     public static String KeyHillCipher ="ddcf"; 
          public  static boolean HillorDES  ;
          
          
               // Initial Permutation table
	private static final byte[] IP = { 
		58, 50, 42, 34, 26, 18, 10, 2,
		60, 52, 44, 36, 28, 20, 12, 4,
		62, 54, 46, 38, 30, 22, 14, 6,
		64, 56, 48, 40, 32, 24, 16, 8,
		57, 49, 41, 33, 25, 17, 9,  1,
		59, 51, 43, 35, 27, 19, 11, 3,
		61, 53, 45, 37, 29, 21, 13, 5,
		63, 55, 47, 39, 31, 23, 15, 7
	};
	
    
    // Permuted Choice 1 table
	private static final byte[] PC1 = {
		57, 49, 41, 33, 25, 17, 9,
		1,  58, 50, 42, 34, 26, 18,
		10, 2,  59, 51, 43, 35, 27,
		19, 11, 3,  60, 52, 44, 36,
		63, 55, 47, 39, 31, 23, 15,
		7,  62, 54, 46, 38, 30, 22,
		14, 6,  61, 53, 45, 37, 29,
		21, 13, 5,  28, 20, 12, 4
	};
    
    
    // Permuted Choice 2 table
	private static final byte[] PC2 = {
		14, 17, 11, 24, 1,  5,
		3,  28, 15, 6,  21, 10,
		23, 19, 12, 4,  26, 8,
		16, 7,  27, 20, 13, 2,
		41, 52, 31, 37, 47, 55,
		30, 40, 51, 45, 33, 48,
		44, 49, 39, 56, 34, 53,
		46, 42, 50, 36, 29, 32
	};
        
        
        // Array to store the number of rotations that are to be done on each round
	private static final byte[] rotations = {
		1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
	};
        
        
        
        // Expansion (aka P-box) table
	private static final byte[] E = {
		32, 1,  2,  3,  4,  5,
		4,  5,  6,  7,  8,  9,
		8,  9,  10, 11, 12, 13,
		12, 13, 14, 15, 16, 17,
		16, 17, 18, 19, 20, 21,
		20, 21, 22, 23, 24, 25,
		24, 25, 26, 27, 28, 29,
		28, 29, 30, 31, 32, 1
	};
        
        
        
        
        // S-boxes (i.e. Substitution boxes)
	private static final byte[][] S = { {
		14, 4,  13, 1,  2,  15, 11, 8,  3,  10, 6,  12, 5,  9,  0,  7,
		0,  15, 7,  4,  14, 2,  13, 1,  10, 6,  12, 11, 9,  5,  3,  8,
		4,  1,  14, 8,  13, 6,  2,  11, 15, 12, 9,  7,  3,  10, 5,  0,
		15, 12, 8,  2,  4,  9,  1,  7,  5,  11, 3,  14, 10, 0,  6,  13
	}, {
		15, 1,  8,  14, 6,  11, 3,  4,  9,  7,  2,  13, 12, 0,  5,  10,
		3,  13, 4,  7,  15, 2,  8,  14, 12, 0,  1,  10, 6,  9,  11, 5,
		0,  14, 7,  11, 10, 4,  13, 1,  5,  8,  12, 6,  9,  3,  2,  15,
		13, 8,  10, 1,  3,  15, 4,  2,  11, 6,  7,  12, 0,  5,  14, 9
	}, {
		10, 0,  9,  14, 6,  3,  15, 5,  1,  13, 12, 7,  11, 4,  2,  8,
		13, 7,  0,  9,  3,  4,  6,  10, 2,  8,  5,  14, 12, 11, 15, 1,
		13, 6,  4,  9,  8,  15, 3,  0,  11, 1,  2,  12, 5,  10, 14, 7,
		1,  10, 13, 0,  6,  9,  8,  7,  4,  15, 14, 3,  11, 5,  2,  12
	}, {
		7,  13, 14, 3,  0,  6,  9,  10, 1,  2,  8,  5,  11, 12, 4,  15,
		13, 8,  11, 5,  6,  15, 0,  3,  4,  7,  2,  12, 1,  10, 14, 9,
		10, 6,  9,  0,  12, 11, 7,  13, 15, 1,  3,  14, 5,  2,  8,  4,
		3,  15, 0,  6,  10, 1,  13, 8,  9,  4,  5,  11, 12, 7,  2,  14
	}, {
		2,  12, 4,  1,  7,  10, 11, 6,  8,  5,  3,  15, 13, 0,  14, 9,
		14, 11, 2,  12, 4,  7,  13, 1,  5,  0,  15, 10, 3,  9,  8,  6,
		4,  2,  1,  11, 10, 13, 7,  8,  15, 9,  12, 5,  6,  3,  0,  14,
		11, 8,  12, 7,  1,  14, 2,  13, 6,  15, 0,  9,  10, 4,  5,  3
	}, {
		12, 1,  10, 15, 9,  2,  6,  8,  0,  13, 3,  4,  14, 7,  5,  11,
		10, 15, 4,  2,  7,  12, 9,  5,  6,  1,  13, 14, 0,  11, 3,  8,
		9,  14, 15, 5,  2,  8,  12, 3,  7,  0,  4,  10, 1,  13, 11, 6,
		4,  3,  2,  12, 9,  5,  15, 10, 11, 14, 1,  7,  6,  0,  8,  13
	}, {
		4,  11, 2,  14, 15, 0,  8,  13, 3,  12, 9,  7,  5,  10, 6,  1,
		13, 0,  11, 7,  4,  9,  1,  10, 14, 3,  5,  12, 2,  15, 8,  6,
		1,  4,  11, 13, 12, 3,  7,  14, 10, 15, 6,  8,  0,  5,  9,  2,
		6,  11, 13, 8,  1,  4,  10, 7,  9,  5,  0,  15, 14, 2,  3,  12
	}, {
		13, 2,  8,  4,  6,  15, 11, 1,  10, 9,  3,  14, 5,  0,  12, 7,
		1,  15, 13, 8,  10, 3,  7,  4,  12, 5,  6,  11, 0,  14, 9,  2,
		7,  11, 4,  1,  9,  12, 14, 2,  0,  6,  10, 13, 15, 3,  5,  8,
		2,  1,  14, 7,  4,  10, 8,  13, 15, 12, 9,  0,  3,  5,  6,  11
	} };
	
        
        
        
        // Permutation table
	private static final byte[] P = {
		16, 7,  20, 21,
		29, 12, 28, 17,
		1,  15, 23, 26,
		5,  18, 31, 10,
		2,  8,  24, 14,
		32, 27, 3,  9,
		19, 13, 30, 6,
		22, 11, 4,  25
	};
	
        
        // Final permutation (aka Inverse permutation) table
	private static final byte[] FP = {
		40, 8, 48, 16, 56, 24, 64, 32,
		39, 7, 47, 15, 55, 23, 63, 31,
		38, 6, 46, 14, 54, 22, 62, 30,
		37, 5, 45, 13, 53, 21, 61, 29,
		36, 4, 44, 12, 52, 20, 60, 28,
		35, 3, 43, 11, 51, 19, 59, 27,
		34, 2, 42, 10, 50, 18, 58, 26,
		33, 1, 41, 9, 49, 17, 57, 25
	};
        public static int[][] subkey = new int[16][48];
        private static int[] C = new int[28];
	private static int[] D = new int[28];
    
    
    
       public static String KeyInput ="ABCDEFGT";
   
     ////////////Counter mode (CTR) for 64 bit PlineTextblock and 64 bit for Counter encryption by 64 bit Key  //////////////////////////
    public static String Counter ="AZSXDCFV";
          

    /**
     * Creates new form NewJFrame
     */
    public NewJFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        TextClient = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TextAreaClient = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 102, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Client", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Tahoma", 1, 24), new java.awt.Color(255, 0, 0))); // NOI18N

        jPanel2.setBackground(new java.awt.Color(51, 153, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Enter here", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));

        TextClient.setBackground(new java.awt.Color(204, 204, 204));

        jButton1.setText("Hill");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("DES");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(TextClient, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TextClient, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TextAreaClient.setBackground(new java.awt.Color(204, 204, 204));
        TextAreaClient.setColumns(20);
        TextAreaClient.setRows(5);
        jScrollPane1.setViewportView(TextAreaClient);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

         private static int[][] getKeyMatrix() { 
     
      //Check key matrix is square matrix or not 
      int sq = KeyHillCipher.length()% 2 ;
     
       if (sq != 0) {  
           JOptionPane.showMessageDialog(null,"Cannot Form a square matrix");
        } 
       
        int len = KeyHillCipher.length() ;
       int[][] keyMatrix = new int[len][len];
       
        int k = 0;  
        for (int i = 0; i < 2; i++)  
        {  
            for (int j = 0; j < 2; j++)  
            {  
              keyMatrix[i][j] = ((int) KeyHillCipher.charAt(k))-97 ; 
         
                k++; 
            }  
        }
        return keyMatrix ; 
      }
         
         
         
           private static void isValidMatrix(int[][] keyMatrix) {  
        int det = keyMatrix[0][0] * keyMatrix[1][1] - keyMatrix[0][1] * keyMatrix[1][0];  
        // If det=0, throw exception and terminate  
        if(det == 0) {  
             JOptionPane.showMessageDialog(null,"Det equals to zero, invalid key matrix!");
          
        }  
    }
           
           
           
        public static String encrypt(String phrase)  
    {   
        
          int phraseToNum[]; 
          int phraseEncoded[] ; 
          int IVxorPhrase[] = new int[phrase.length()]; 
         // int IV[] = new int[phrase.length()]; 
      
        Random R = new Random();
        // Delete all non-english characters, and convert phrase to upper case  
        
        phrase = phrase.replaceAll("[^a-zA-Z]","").toUpperCase();  
 
        // If phrase length is not an even number, add "Q" to make it even  
        if(phrase.length() % 2 == 1) {  
            phrase += "Q";     
        }  
        
        // Get the 2x2 key matrix from sc  
        int[][] keyMatrix1;  
          
        keyMatrix1 = getKeyMatrix(); 
         
      
        // Check if the matrix is valid (det != 0)  
        isValidMatrix(keyMatrix1);  
       
    ///علشان لو دخل رقم فردي ما اكون معرفه حجم المصفوفة قبل لانه لو كان فردي راح يزيد حجمه واحد 
         phraseToNum = new int[phrase.length()];
         phraseEncoded = new int[phrase.length()];
        
         // Convert characters to numbers according to their  
        // place in ASCII table minus 65 positions (A=65 in ASCII table)  
        // If we use A=0 alphabet
        
        for(int i=0; i < phrase.length(); i++) {  
            // enter charcter upper حروف كبيرة  A=65
         
            phraseToNum[i]=(int)phrase.charAt(i) - (65); 
          
        } 
     
       
       
       
        // Find the product per pair of the phrase with the key matrix modulo 26  
       
        for( int i=0; i < phraseToNum.length; i += 2) {  
            int x = (keyMatrix1[0][0] * phraseToNum[i] + keyMatrix1[0][1] * phraseToNum[i+1]) % 26;  
            int y = (keyMatrix1[1][0] * phraseToNum[i] + keyMatrix1[1][1] * phraseToNum[i+1]) % 26;  
            phraseEncoded[i]= x;  
            phraseEncoded[i+1]=y ;  
            
        } 
        
        char c[];
         String arr = "";
      for( int g =0; g < phraseEncoded.length; g += 1) {  
         c=  Character.toChars(phraseEncoded[g] + (65 ));  
        arr += String.valueOf(c);
       
          // Print the result 
      }
     
         JOptionPane.showMessageDialog(null,arr);
    
      return arr;
    }  
    
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         try{
             HillorDES = false;
           dataOutputStream.writeBoolean(HillorDES);
        String msgout = ""  ;
       msgout = ""+  encrypt(TextClient.getText());

        dataOutputStream.writeUTF(msgout); // sending the server message to the client 
    }catch(Exception e){}
    }//GEN-LAST:event_jButton1ActionPerformed
  private static String EncrytionOrDecryption(boolean isDecrypt,String PlainTextInput) {                    
         
         
          
           int inputBits[] = new int[ 8 *PlainTextInput.length()]; 
           for(int i=0 ; i < PlainTextInput.length() ; i++) {
			
			String s = Integer.toBinaryString( (int)PlainTextInput.charAt(i) );
			
			
			while(s.length() < 8) {
				s = "0" + s;
			}
                 
			// Add the 4 bits we have extracted into the array of bits.
			for(int j=0 ; j < 8 ; j++) {
				inputBits[(8*i)+j] = Integer.parseInt(s.charAt(j) + "");
                                
			}
                          
		} 
        
           int keyBits[] = new int[64];
           for(int i=0 ; i < KeyInput.length() ; i++) {
			String s = Integer.toBinaryString( (int)KeyInput.charAt(i) );
			while(s.length() < 8) {
				s = "0" + s;
			}
                        
			for(int j=0 ; j < 8 ; j++) {
				keyBits[(8*i)+j] = Integer.parseInt(s.charAt(j) + "");
			}
                   
		}
              
          
            if(isDecrypt){
       return permute(inputBits, keyBits, true);
            }
            else{
                  
            return permute(inputBits, keyBits, false);
            
            }
            
            
}
 
  private static String permute(int[] inputBits, int[] keyBits , boolean isDecrypt) {
          
             
		// Initial permutation step takes input bits and permutes into the
		// newBits array
               //  JOptionPane.showMessageDialog(null, " YTUY ");
		int newBits[] = new int[inputBits.length];
		for(int i=0 ; i < inputBits.length ; i++) {
			newBits[i] = inputBits[IP[i]-1]; /**************/
		}
		
		   //  JOptionPane.showMessageDialog(null, " GHY ");
		// L and R arrays are created to store the Left and Right halves of the
		// subkey respectively
		int L[] = new int[32];
		int R[] = new int[32];
                // After PC1 the first L and R are ready to be used and hence looping
		// can start once L and R are initialized
		System.arraycopy(newBits, 0, L, 0, 32);
		System.arraycopy(newBits, 32, R, 0, 32);
		
		
	
		
		
		// Permuted Choice 1 is done here
                int i;
		for(i=0 ; i < 28 ; i++) {
			C[i] = keyBits[PC1[i]-1];
		}
		for( ; i < 56 ; i++) {
			D[i-28] = keyBits[PC1[i]-1];
		}
		
		// 16 rounds will start here
		for(int n=0 ; n < 16 ; n++) {
			
			// newR is the new R half generated by the Fiestel function. If it
			// is encrpytion then the KS method is called to generate the
			// subkey otherwise the stored subkeys are used in reverse order
			// for decryption.
			int newR[] = new int[0];
                        
		        if(isDecrypt) {
				newR = fiestel(R, subkey[15-n]);
                                   
                        }
                        else{
			newR = fiestel(R, KR(n, keyBits));
                                }         
			// xor-ing the L and new R gives the new L value. new L is stored
			// in R and new R is stored in L, thus exchanging R and L for the
			// next round.
			int newL[] = xor(L, newR);
			L = R;
			R = newL;
		
		}
                
                // R and L has the two halves of the output before applying the final
		// permutation. This is called the "Preoutput".
		int output[] = new int[64];
		System.arraycopy(R, 0, output, 0, 32);
		System.arraycopy(L, 0, output, 32, 32);
		int finalOutput[] = new int[64];
		// Applying FP table to the preoutput, we get the final output:
		// Encryption => final output is ciphertext
		// Decryption => final output is plaintext
		for(i=0 ; i < 64 ; i++) {
			finalOutput[i] = output[FP[i]-1];
		}
		
		// Since the final output is stored as an int array of bits, we convert
		// it into  string:
		String StrOutput = new String();
		for(i=0 ; i < 8 ; i++) {
			String bin = new String();
			for(int j=0 ; j < 8 ; j++) {
				bin += finalOutput[(8*i)+j];
			}
			int decimal = Integer.parseInt(bin, 2);
                          
			StrOutput += (char)decimal;
		}
                  JOptionPane.showMessageDialog(null,StrOutput);
		   
		return StrOutput;
        }
  
  
    
        private static int[] xor(int[] a, int[] b) {
		// Simple xor function on two int arrays
		int answer[] = new int[a.length];
		for(int i=0 ; i < a.length ; i++) {
			answer[i] = a[i]^b[i];
		}
		return answer;
	}
        
        
 
        
        
        private static int[] KR(int round, int[] key) {
		// The KS (Key Structure) function generates the round keys.
		// C1 and D1 are the new values of C and D which will be generated in
		// this round.
		int C1[] = new int[28];
		int D1[] = new int[28];
		
		// The rotation array is used to set how many rotations are to be done
		int rotationTimes = (int) rotations[round];
		// leftShift() method is used for rotation (the rotation is basically)
		// a left shift operation, hence the name.
		C1 = leftShift(C, rotationTimes);
		D1 = leftShift(D, rotationTimes);
		// CnDn stores the combined C1 and D1 halves
		int CnDn[] = new int[56];
		System.arraycopy(C1, 0, CnDn, 0, 28);
		System.arraycopy(D1, 0, CnDn, 28, 28);
		// Kn stores the subkey, which is generated by applying the PC2 table
		// to CnDn
		int Kn[] = new int[48];
		for(int i=0 ; i < Kn.length ; i++) {
			Kn[i] = CnDn[PC2[i]-1];
		}
		
		// Now we store C1 and D1 in C and D respectively, thus becoming the
		// old C and D for the next round. Subkey is stored and returned.
		subkey[round] = Kn;
                 
		C = C1;
		D = D1;
		return Kn;
	}
        
        
	private static int[] fiestel(int[] R, int[] roundKey) {
		// Method to implement Fiestel function.
		// First the 32 bits of the R array are expanded using E table.
		int expandedR[] = new int[48];
		for(int i=0 ; i < 48 ; i++) {
			expandedR[i] = R[E[i]-1];
		}
		// We xor the expanded R and the generated round key
		int temp[] = xor(expandedR, roundKey);
		// The S boxes are then applied to this xor result and this is the
		// output of the Fiestel function.
		int output[] = sBlock(temp);
		return output;
	}
	
        private static int[] sBlock(int[] bits) {
		// S-boxes are applied in this method.
		int output[] = new int[32];
		// We know that input will be of 32 bits, hence we will loop 32/4 = 8
		// times (divided by 4 as we will take 4 bits of input at each
		for(int i=0 ; i < 8 ; i++) {
			// S-box requires a row and a column, which is found from the
			// input bits. The first and 6th bit of the current iteration
			// (i.e. bits 0 and 5) gives the row bits.
			int row[] = new int [2];
			row[0] = bits[6*i];
			row[1] = bits[(6*i)+5];
			String sRow = row[0] + "" + row[1];
			// Similarly column bits are found, which are the 4 bits between
			// the two row bits (i.e. bits 1,2,3,4)
			int column[] = new int[4];
			column[0] = bits[(6*i)+1];
			column[1] = bits[(6*i)+2];
			column[2] = bits[(6*i)+3];
			column[3] = bits[(6*i)+4];
			String sColumn = column[0] +""+ column[1] +""+ column[2] +""+ column[3];
			// Converting binary into decimal value, to be given into the
			// array as input
			int iRow = Integer.parseInt(sRow, 2);
			int iColumn = Integer.parseInt(sColumn, 2);
			int x = S[i][(iRow*16) + iColumn];
			// We get decimal value of the S-box here, but we need to convert
			// it into binary:
			String s = Integer.toBinaryString(x);
			// Padding is required since Java returns a decimal '5' as '111' in
			// binary, when we require '0111'.
			while(s.length() < 4) {
				s = "0" + s;
			}
			// The binary bits are appended to the output
			for(int j=0 ; j < 4 ; j++) {
				output[(i*4) + j] = Integer.parseInt(s.charAt(j) + "");
			}
		}
		// P table is applied to the output and this is the final output of one
		// S-box round:
		int finalOutput[] = new int[32];
		for(int i=0 ; i < 32 ; i++) {
			finalOutput[i] = output[P[i]-1];
		}
		return finalOutput;
	}
	
	private static int[] leftShift(int[] bits, int n) {
		// Left shifting takes place here, i.e. each bit is rotated to the left
		// and the leftmost bit is stored at the rightmost bit. This is a left
		// shift operation.
		int answer[] = new int[bits.length];
		System.arraycopy(bits, 0, answer, 0, bits.length);
		for(int i=0 ; i < n ; i++) {
			int temp = answer[0];
			for(int j=0 ; j < bits.length-1 ; j++) {
				answer[j] = answer[j+1];
			}
			answer[bits.length-1] = temp;
		}
		return answer;
	}
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try{
            HillorDES = true;
            dataOutputStream.writeBoolean(HillorDES);
            String msgout = ""  ;

            msgout= ""+ (EncrytionOrDecryption(true,EncrytionOrDecryption(false, TextClient.getText() )) );
            //   msgout =EncrytionOrDecryption(false, TextClient.getText() );

            dataOutputStream.writeUTF(msgout); // sending the server message to the client
        }catch(Exception e){}

    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewJFrame().setVisible(true);
            }
        });
         String msgin ="";
        try{
        
              socket = new Socket("127.0.0.1",1201); //server start at 1201 port number
             
             dataInputStream = new DataInputStream(socket.getInputStream());
             dataOutputStream = new DataOutputStream(socket.getOutputStream());
        
               //  msgin =   din.readUTF() ;
           while(!msgin.equals("exit") ){
               
              msgin=dataInputStream.readUTF();
             
    
             TextAreaClient.setText(TextAreaClient.getText().trim() + " \n Server : "+ msgin); //display message form client to server 
              
           }
          
               }catch(Exception e){}
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JTextArea TextAreaClient;
    private javax.swing.JTextField TextClient;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
