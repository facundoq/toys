package org.tiledzelda.visualization.graphics;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.protocol.DataSource;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class SoundManager {
	
	//TODO clip pool
	
	public static class Sound{
		byte[] b;
		AudioFormat format;
		long frames;
		public Sound(byte[] b, AudioFormat format, long l) {
			super();
			this.b = b;
			this.format = format;
			this.frames = l;
		}
		
	}
	public HashMap<String, Sound> sounds;
	public String path;
	
	public SoundManager(){
		
		this.path="resources/sound/";
		sounds=new HashMap<String, Sound>();
		
		addSound("attack.wav");
		addSound("hit.wav");
		
		
        
	}
	
	private void addSound(String sound) {
		try {
			AudioInputStream is = AudioSystem.getAudioInputStream(new File(path+sound));
			Sound s= new Sound(IOUtils.toByteArray(is), is.getFormat(), is.getFrameLength());
			sounds.put(sound, s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void play(String sound,float db){
		
		Sound s= sounds.get(sound);
		AudioInputStream is = new AudioInputStream(new ByteArrayInputStream(s.b),s.format,s.frames);
		Clip clip;
		try {
			clip = AudioSystem.getClip();
			clip.open(is);
			FloatControl volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(db);
			clip.start();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	
	
}
