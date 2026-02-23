package vswe.stevescarts.module.realtimer;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Note Sequencer Module - Plays musical sequences
 *
 * This module allows the cart to play configurable music tracks using
 * note block sounds. It supports multiple tracks, each with its own
 * instrument and sequence of notes. Playback can be triggered by
 * powered rails.
 *
 * Instruments: Piano, Bass Drum, Snare Drum, Sticks, Bass Guitar
 * Notes: 25 pitches from F#3 to F#5
 * Tracks: Up to 15 per module
 * Notes per track: Up to 4095
 * Speed settings: 7 levels (1-13 tick delay)
 */
public class NoteSequencerModule extends CartModule {
	private final List<Track> tracks = new ArrayList<>();
	private boolean playing = false;
	private int currentTick = 0;
	private int playProgress = 0;
	private int speedSetting = 5;

	private static final int MAX_TRACKS = 15;
	private static final int MAX_NOTES_PER_TRACK = 4095;
	private static final String[] PITCH_NAMES = {
		"F#3", "G3", "G#3", "A3", "A#3", "B3",
		"C4", "C#4", "D4", "D#4", "E4", "F4", "F#4",
		"G4", "G#4", "A4", "A#4", "B4",
		"C5", "C#5", "D5", "D#5", "E5", "F5", "F#5"
	};

	public NoteSequencerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void tick() {
		if (getEntity() == null || getEntity().world == null) {
			return;
		}

		if (!playing) {
			return;
		}

		if (currentTick <= 0) {
			boolean found = false;
			for (Track track : tracks) {
				if (track.notes.size() > playProgress) {
					Note note = track.notes.get(playProgress);
					if (note.pitch >= 0 && note.instrument >= 0) {
						playNote(note, track.volume);
					}
					found = true;
				}
			}

			if (!found) {
				playing = false;
				playProgress = 0;
			} else {
				++playProgress;
			}
			currentTick = getTickDelay();
		} else {
			--currentTick;
		}
	}

	/**
	 * Plays a single note at the cart's position.
	 */
	private void playNote(Note note, int volume) {
		if (getEntity().world.isClient || volume == 0) {
			return;
		}

		float vol = switch (volume) {
			case 1 -> 0.33f;
			case 2 -> 0.67f;
			default -> 1.0f;
		};

		float pitch = (float) Math.pow(2.0, (note.pitch - 12) / 12.0);

		// Select sound based on instrument
		var sound = switch (note.instrument) {
			case 1 -> SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM;
			case 2 -> SoundEvents.BLOCK_NOTE_BLOCK_SNARE;
			case 3 -> SoundEvents.BLOCK_NOTE_BLOCK_HAT;
			case 4 -> SoundEvents.BLOCK_NOTE_BLOCK_BASS;
			default -> SoundEvents.BLOCK_NOTE_BLOCK_HARP;
		};

		getEntity().world.playSound(
			null,
			getEntity().getX(), getEntity().getY(), getEntity().getZ(),
			sound, SoundCategory.RECORDS,
			vol, pitch
		);

		// Spawn note particle on server
		if (getEntity().world instanceof ServerWorld serverWorld) {
			serverWorld.spawnParticles(
				ParticleTypes.NOTE,
				getEntity().getX(), getEntity().getY() + 1.2, getEntity().getZ(),
				1, 0.0, 0.0, 0.0, note.pitch / 24.0
			);
		}
	}

	@Override
	public void onActivate() {
		if (!playing) {
			playing = true;
			playProgress = 0;
			currentTick = 0;
		}
	}

	private int getTickDelay() {
		return switch (speedSetting) {
			case 6 -> 1;
			case 5 -> 2;
			case 4 -> 3;
			case 3 -> 5;
			case 2 -> 7;
			case 1 -> 11;
			case 0 -> 13;
			default -> 2;
		};
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
		if (!playing) {
			playProgress = 0;
		}
	}

	public int getSpeedSetting() {
		return speedSetting;
	}

	public void setSpeedSetting(int speed) {
		this.speedSetting = Math.max(0, Math.min(6, speed));
	}

	public List<Track> getTracks() {
		return tracks;
	}

	/**
	 * Adds a new empty track.
	 */
	public boolean addTrack() {
		if (tracks.size() < MAX_TRACKS) {
			tracks.add(new Track());
			return true;
		}
		return false;
	}

	/**
	 * Removes the last track.
	 */
	public boolean removeTrack() {
		if (!tracks.isEmpty()) {
			tracks.remove(tracks.size() - 1);
			return true;
		}
		return false;
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putBoolean("Playing", playing);
		nbt.putInt("PlayProgress", playProgress);
		nbt.putInt("Speed", speedSetting);

		NbtList trackList = new NbtList();
		for (Track track : tracks) {
			NbtCompound trackNbt = new NbtCompound();
			trackNbt.putInt("Volume", track.volume);

			NbtList noteList = new NbtList();
			for (Note note : track.notes) {
				NbtCompound noteNbt = new NbtCompound();
				noteNbt.putByte("Pitch", (byte) note.pitch);
				noteNbt.putByte("Instrument", (byte) note.instrument);
				noteList.add(noteNbt);
			}
			trackNbt.put("Notes", noteList);
			trackList.add(trackNbt);
		}
		nbt.put("Tracks", trackList);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		playing = nbt.getBoolean("Playing");
		playProgress = nbt.getInt("PlayProgress");
		speedSetting = nbt.getInt("Speed");
		if (speedSetting == 0 && !nbt.contains("Speed")) {
			speedSetting = 5;
		}

		tracks.clear();
		NbtList trackList = nbt.getList("Tracks", 10); // 10 = NbtCompound
		for (int i = 0; i < trackList.size() && i < MAX_TRACKS; i++) {
			NbtCompound trackNbt = trackList.getCompound(i);
			Track track = new Track();
			track.volume = trackNbt.getInt("Volume");

			NbtList noteList = trackNbt.getList("Notes", 10);
			for (int j = 0; j < noteList.size() && j < MAX_NOTES_PER_TRACK; j++) {
				NbtCompound noteNbt = noteList.getCompound(j);
				Note note = new Note();
				note.pitch = noteNbt.getByte("Pitch");
				note.instrument = noteNbt.getByte("Instrument");
				track.notes.add(note);
			}
			tracks.add(track);
		}
		super.readFromNbt(nbt);
	}

	/**
	 * A single music track with a sequence of notes.
	 */
	public static class Track {
		public final List<Note> notes = new ArrayList<>();
		public int volume = 3; // 0=mute, 1=quiet, 2=medium, 3=full

		public boolean addNote(int pitch, int instrument) {
			if (notes.size() < MAX_NOTES_PER_TRACK) {
				Note note = new Note();
				note.pitch = pitch;
				note.instrument = instrument;
				notes.add(note);
				return true;
			}
			return false;
		}
	}

	/**
	 * A single note with pitch (0-24) and instrument (0-4).
	 */
	public static class Note {
		public int pitch = -1;      // -1 = rest/silent
		public int instrument = 0;  // 0=harp, 1=basedrum, 2=snare, 3=hat, 4=bass
	}
}
