/*    */ package com.izforge.izpack.panels.xstprocess.util;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.File;
/*    */ import java.io.FileReader;
/*    */ import java.io.FileWriter;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class XstPropertyFileNormalizer
/*    */ {
/*    */   public static String normalizeString(String string)
/*    */   {
/* 14 */     char[] charArray = string.toCharArray();
/* 15 */     StringBuilder normalizedString = new StringBuilder();
/*    */     
/*    */ 
/*    */ 
/* 19 */     for (int i = 0; i < charArray.length; i++)
/*    */     {
/*    */ 
/* 22 */       if (i + 1 == charArray.length) {
/* 23 */         normalizedString.append(charArray[i]);
/* 24 */       } else if ((charArray[i] == '\\') && ((charArray[(i + 1)] == '=') || (charArray[(i + 1)] == ':'))) {
/* 25 */         normalizedString.append(charArray[(++i)]);
/*    */       } else {
/* 27 */         normalizedString.append(charArray[i]);
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 32 */     return normalizedString.toString();
/*    */   }
/*    */   
/*    */   public static void normalizeFile(String fileName) throws IOException
/*    */   {
/* 37 */     BufferedReader br = new BufferedReader(new FileReader(fileName));
/*    */     
/* 39 */     StringBuilder sb = new StringBuilder();
/*    */     
/* 41 */     String line = "";
/* 42 */     while ((line = br.readLine()) != null) {
/* 43 */       sb.append(normalizeString(line));
/* 44 */       sb.append('\n');
/*    */     }
/*    */     
/* 47 */     br.close();
/*    */     
/*    */ 
/* 50 */     new File(fileName).delete();
/* 51 */     BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
/* 52 */     bw.write(sb.toString());
/* 53 */     bw.close();
/*    */   }
/*    */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\util\XstPropertyFileNormalizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */