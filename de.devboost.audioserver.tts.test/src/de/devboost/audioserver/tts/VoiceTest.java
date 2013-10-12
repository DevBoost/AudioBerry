package de.devboost.audioserver.tts;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

public class VoiceTest {

	@Test
	public void test() {
		
		Voice voice = new Voice("kevin16");
		voice.speakToWAV("Hallo Mirko, ich habe nun eine WAV-Datei.");
	}

	@Test
	public void testGetBaseName() {
		final String extension = "ext";
		final String baseName = "file";
		String fullName = baseName + "." + extension;
		
		TestableVoice voice = new TestableVoice(extension);
		
		File file = new File(fullName);
		String actualBaseName = voice.getBaseName(file);
		
		File absoluteFile = new File(file.getAbsolutePath());
		String expectedBaseName = absoluteFile.getParent() + File.separator + baseName;
		assertEquals(expectedBaseName, actualBaseName);
	}
	
	private class TestableVoice extends Voice {
		private String extension;
		
		public TestableVoice(String extension) {
			super("kevin16");
			this.extension = extension;
		}

		@Override
		protected String getExtension() {
			return extension;
		}
		
		@Override
		public String getBaseName(File file) {
			return super.getBaseName(file);
		}
	}
}
