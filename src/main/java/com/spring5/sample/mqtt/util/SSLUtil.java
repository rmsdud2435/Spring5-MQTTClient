package com.spring5.sample.mqtt.util;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;

public class SSLUtil
{
	public static SSLSocketFactory getSocketFactory (final String caCrtFile, final String crtFile, final String keyFile, final String password) throws Exception
	{
		Security.addProvider(new BouncyCastleProvider());
		PEMReader reader = null;
		X509Certificate caCert = null;
		X509Certificate cert = null;
		KeyPair key = null;
		if(caCrtFile != null) {
			// load CA certificate
			try {
				reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(caCrtFile)))));
				caCert = (X509Certificate)reader.readObject();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				reader.close();
			}
		}

		try {
			// load client certificate
			reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(crtFile)))));
			cert = (X509Certificate)reader.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
			
		}

		try {
			// load client private key
			reader = new PEMReader(
					new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(keyFile)))),
					new PasswordFinder() {
						@Override
						public char[] getPassword() {
							return password.toCharArray();
						}
					}
					);
			key = (KeyPair)reader.readObject();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			reader.close();
			
		}
		
		// CA certificate is used to authenticate server
		KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
		caKs.load(null, null);
		caKs.setCertificateEntry("ca-certificate", caCert);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(caKs);

		// client key and certificates are sent to server so it can authenticate us
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setCertificateEntry("certificate", cert);
		ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(), new java.security.cert.Certificate[]{cert});
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(ks, password.toCharArray());

		// finally, create SSL socket factory
		SSLContext context = SSLContext.getInstance("TLSv1.2");
		context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		return context.getSocketFactory();
	}
	
	public static SSLSocketFactory getSSLSocketFactory() {
		SSLContext sc = null;
		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				
//				@SuppressWarnings("unused")
//				public void checkClientTrusted(X509Certificate[] certs,String authType) {
//				}
//				
//				@SuppressWarnings("unused")
//				public void checkServerTrusted(X509Certificate[] certs,String authType) {
//				}
				
				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] arg0,
						String arg1) throws CertificateException {
					// TODO Auto-generated method stub
				}
				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] arg0,
						String arg1) throws CertificateException {
					// TODO Auto-generated method stub
				}
			} };
			
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sc.getSocketFactory();
	}
}
