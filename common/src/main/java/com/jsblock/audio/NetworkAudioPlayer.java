package com.jsblock.audio;

import com.jsblock.Joban;
import net.minecraft.core.BlockPos;

import javax.sound.sampled.*;
import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.cert.X509Certificate;
import java.util.concurrent.CompletableFuture;

public class NetworkAudioPlayer {
    public static void play(String url, float volume, BlockPos pos) {
        CompletableFuture.runAsync(() -> {
            Path tempFile = null;
            try {
                trust();

                URL audioUrl = new URL(url);
                tempFile = Files.createTempFile("network_audio", ".tmp");
                try (InputStream in = audioUrl.openStream()) {
                    Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
                }

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(tempFile.toFile());
                AudioFormat format = audioStream.getFormat();

                DataLine.Info info = new DataLine.Info(Clip.class, format);
                if (!AudioSystem.isLineSupported(info)) {
                    AudioFormat pcmFormat = new AudioFormat(
                            AudioFormat.Encoding.PCM_SIGNED,
                            format.getSampleRate(),
                            16,
                            format.getChannels(),
                            format.getChannels() * 2,
                            format.getSampleRate(),
                            false);
                    audioStream = AudioSystem.getAudioInputStream(pcmFormat, audioStream);
                    info = new DataLine.Info(Clip.class, pcmFormat);
                }

                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.open(audioStream);

                if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
                    dB = Math.max(gainControl.getMinimum(), Math.min(gainControl.getMaximum(), dB));
                    gainControl.setValue(dB);
                }

                clip.start();

                Path finalTempFile = tempFile;
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                        try { Files.deleteIfExists(finalTempFile); } catch (IOException ignored) {}
                    }
                });

            } catch (UnsupportedAudioFileException e) {
                Joban.LOGGER.warn("Unsupported audio format for URL: " + url);
                e.printStackTrace();
                if (tempFile != null) {
                    try { Files.deleteIfExists(tempFile); } catch (IOException ignored) {}
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (tempFile != null) {
                    try { Files.deleteIfExists(tempFile); } catch (IOException ignored) {}
                }
            }
        });
    }

    private static void trust() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
        };
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }
}