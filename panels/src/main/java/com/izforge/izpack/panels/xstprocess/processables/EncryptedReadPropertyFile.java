/*     */ package com.izforge.izpack.panels.xstprocess.processables;
/*     */ 
/*     */ import com.izforge.izpack.api.data.Variables;
/*     */ import com.izforge.izpack.panels.xstprocess.util.XstPropertyFileNormalizer;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.io.StringReader;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Paths;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.logging.Logger;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.crypto.spec.SecretKeySpec;
/*     */ import org.apache.commons.configuration2.PropertiesConfiguration;
/*     */ 
/*     */ public class EncryptedReadPropertyFile extends ReadPropertyFile
/*     */ {
/*     */   String encryptionKey;
/*     */   
/*     */   public EncryptedReadPropertyFile(String fileName, HashMap<String, String> keyVariables, String encryptionKey)
/*     */   {
/*  26 */     super(fileName, keyVariables);
/*  27 */     this.encryptionKey = encryptionKey;
/*     */   }
/*     */   
/*     */   public boolean run()
/*     */   {
/*  32 */     PropertiesConfiguration propConfig = decryptFile(this.variables, this.fileName, this.encryptionKey);
/*     */     
/*  34 */     if (propConfig == null) {
/*  35 */       return false;
/*     */     }
/*     */     
/*  38 */     setInstallerVariables(propConfig, this.variables);
/*  39 */     return true;
/*     */   }
/*     */   
/*     */   public PropertiesConfiguration decryptFile(Variables variables, String fileName, String encryptionKey)
/*     */   {
/*     */     try
/*     */     {
/*  46 */       fileName = variables.replace(fileName);
/*     */     } catch (Exception ex) {
/*  48 */       ex.printStackTrace();
/*     */     }
/*     */     
/*     */ 
/*  52 */     File propertyFileFile = new File(fileName);
/*  53 */     if (!propertyFileFile.exists()) {
/*  54 */       this.logger.severe("File set to read properties from does not exist: " + fileName);
/*  55 */       return null;
/*     */     }
/*     */     
/*  58 */     encryptionKey = variables.replace(encryptionKey);
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  63 */       byte[] encBytes = Files.readAllBytes(Paths.get(fileName, new String[0]));
/*     */       
/*     */ 
/*  66 */       byte[] key = encryptionKey.getBytes("UTF-8");
/*     */       
/*     */ 
/*  69 */       MessageDigest sha = MessageDigest.getInstance("SHA-1");
/*  70 */       key = sha.digest(key);
/*  71 */       key = Arrays.copyOf(key, 16);
/*     */       
/*  73 */       SecretKey myDesKey = new SecretKeySpec(key, "AES");
/*  74 */       Cipher desCipher = Cipher.getInstance("AES");
/*     */       
/*     */ 
/*     */ 
/*  78 */       desCipher.init(2, myDesKey);
/*     */       
/*     */ 
/*  81 */       byte[] textDecry = desCipher.doFinal(encBytes);
/*  82 */       String unencFile = new String(textDecry, "UTF-8");
/*     */       
/*     */ 
/*  85 */       PropertiesConfiguration propConfig = new PropertiesConfiguration();
/*  86 */       StringReader stringReader = new StringReader(unencFile);
/*     */       
/*  88 */       propConfig.read(stringReader);
/*     */       
/*     */ 
/*  91 */       PropertiesConfiguration propConfigNew = new PropertiesConfiguration();
/*     */       
/*  93 */       Iterator iter = propConfig.getKeys();
/*  94 */       while (iter.hasNext()) {
/*  95 */         String propkey = (String)iter.next();
/*  96 */         String propval = (String)propConfig.getProperty(propkey);
/*     */         
/*  98 */         System.out.println("Key before " + propkey + "   -   Value before: " + propval);
/*  99 */         propval = XstPropertyFileNormalizer.normalizeString(propval);
/* 100 */         System.out.println("Key after " + propkey + "   -   Value after: " + propval);
/*     */         
/* 102 */         propConfigNew.addProperty(propkey, propval);
/*     */         
/* 104 */         System.out.println("has next: " + iter.hasNext());
/*     */       }
/*     */       
/*     */ 
/* 108 */       return propConfigNew;
/*     */     }
/*     */     catch (Exception ex) {
/* 111 */       System.out.println(ex); }
/* 112 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getProcessableName()
/*     */   {
/* 118 */     return "Read Encrypted Property File";
/*     */   }
/*     */ }


/* Location:              D:\stage-installer-01-30-2023\postghost\installer\JCG-Xstore17-InstallerCopy.jar!\com\izforge\izpack\panels\xstprocess\processables\EncryptedReadPropertyFile.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */