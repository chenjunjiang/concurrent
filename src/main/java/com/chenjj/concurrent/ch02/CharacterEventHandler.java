package com.chenjj.concurrent.ch02;

import java.util.Vector;

public class CharacterEventHandler {

	private Vector listeners = new Vector();

	public void addCharacterListener(CharacterListener characterListener) {
		listeners.add(characterListener);
	}

	public void removeCharacterListener(CharacterListener characterListener) {
		listeners.remove(characterListener);
	}

	public void fireNewCharacter(CharacterSource characterSource, int character) {
		CharacterEvent characterEvent = new CharacterEvent(characterSource, character);
		CharacterListener[] characterListeners = (CharacterListener[]) listeners.toArray();
		for (int i = 0, len = characterListeners.length; i < len; i++) {
			characterListeners[i].newCharacter(characterEvent);
		}
	}
}
