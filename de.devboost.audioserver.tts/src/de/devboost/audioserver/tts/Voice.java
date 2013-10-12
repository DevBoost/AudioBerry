package de.devboost.audioserver.tts;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;

public class Voice {

	private com.sun.speech.freetts.Voice systemVoice;
	private String name;

	public Voice(String name) {
		this.name = name;
		this.systemVoice = VoiceManager.getInstance().getVoice(this.name);
		this.systemVoice.allocate();
	}

	public void speakToSpeaker(String thingToSay) {
		systemVoice.speak(thingToSay);
	}

	public byte[] speakToWAV(String thingToSay) {
		Path tmpDir = null;
		try {
			 tmpDir = Files.createTempDirectory("audioberry");
		} catch (IOException e1) {
			return null;
		}
		
		Path tmpFile = Paths.get(tmpDir.toString(), "output");
		
		SingleFileAudioPlayer audioPlayer = new SingleFileAudioPlayer(tmpFile.toString(), javax.sound.sampled.AudioFileFormat.Type.WAVE);
		systemVoice.setAudioPlayer(audioPlayer);
		
		systemVoice.speak(thingToSay);
//		systemVoice.deallocate();
		audioPlayer.close();
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File("output.wav")));
			
			int read;
			byte[] buf = new byte[1024];
			
			while ((read = in.read(buf)) > 0) {
				out.write(buf, 0, read);
			}
			out.flush();
			in.close();
			
			return out.toByteArray();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
}
