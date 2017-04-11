package caveat.engine.util;

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.util.HashMap;

import caveat.engine.CaveDefaults;

public class CaveSoundBank {

	HashMap<CaveSound,AudioClip> sounds; 

	public void initSounds() {
		sounds = new HashMap<CaveSound, AudioClip>();
		sounds.put(CaveSound.AMBIENT, loadAudioFile(CaveDefaults.MONSTER_GROWL_SOUND_FILENAME));
		sounds.put(CaveSound.WALL_DESTRUCTION_SOUND, loadAudioFile(CaveDefaults.WALL_DESTRUCTION_SOUND));

//		for (CaveSound sound : sounds.keySet()) {
//			playSound(sound);
//			stopSound(sound);
//		}
	}

	public AudioClip loadAudioFile(String filename) {
		URL resourceUrl = getResourceUrl(filename);
		AudioClip newAudioClip = Applet.newAudioClip(resourceUrl);
		return newAudioClip;
	}

	private URL getResourceUrl(String filename) {
		URL url = this.getClass().getResource(filename);
		return url;
	}
	
	public void playSound(CaveSound sound) {
		AudioClip clip = sounds.get(sound);
		clip.play();
	}
	
	public void stopSound(CaveSound sound) {
		sounds.get(sound).stop();
	}

	public void loopSound(CaveSound sound) {
		sounds.get(sound).loop();
	}

}
