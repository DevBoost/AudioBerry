package de.devboost.audioserver.tts;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFileFormat.Type;

import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.SingleFileAudioPlayer;

public class Voice {

	private com.sun.speech.freetts.Voice systemVoice;
	private String name;

	private static final Type audioType = AudioFileFormat.Type.WAVE;

	public Voice(String name) {
		this.name = name;
		this.systemVoice = VoiceManager.getInstance().getVoice(this.name);
		this.systemVoice.allocate();

	}

	public void speakToSpeaker(String thingToSay) {
		systemVoice.speak(thingToSay);
	}

	public byte[] speakToWAV(String thingToSay) {
		File tmpFile;
		try {
			tmpFile = createTempWAVFile();
		} catch (IOException e1) {
			throw new RuntimeException("Huih, cannot create temporary file.");
		}

		SingleFileAudioPlayer audioPlayer;
		audioPlayer = new SingleFileAudioPlayer(getBaseName(tmpFile), audioType);

		systemVoice.setAudioPlayer(audioPlayer);
		systemVoice.speak(thingToSay);

		audioPlayer.close();

		return writeFileToByteArray(tmpFile);
	}

	private byte[] writeFileToByteArray(File tmpFile) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(tmpFile));

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

	private File createTempWAVFile() throws IOException {
		String extension = "." + getExtension();
		return File.createTempFile("audioberry", extension);
	}

	protected String getBaseName(File file) {
		String filePath = file.getAbsolutePath();
		int lastIndex = filePath.indexOf(getExtension());

		return filePath.substring(0, lastIndex-1);
	}

	protected String getExtension() {
		return audioType.getExtension();
	}

}
