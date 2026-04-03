package vswe.stevescarts.module.addon;

import vswe.stevescarts.entity.CartEntity;
import vswe.stevescarts.module.CartModule;
import vswe.stevescarts.module.ModuleType;
import vswe.stevescarts.module.Worker;

import net.minecraft.nbt.NbtCompound;

/**
 * Note Sequencer Module - Plays musical notes and sound sequences
 * Allows programming musical melodies that play as the cart moves
 */
public class NoteSequencerModule extends CartModule implements Worker {
	protected int noteIndex = 0;
	protected int noteTimer = 0;
	protected static final int NOTE_INTERVAL = 10; // Play notes every 0.5 seconds (10 ticks)
	protected byte[] noteSequence = new byte[16]; // Max 16 note sequence
	protected int sequenceLength = 0;

	public NoteSequencerModule(CartEntity minecart, ModuleType<?> type) {
		super(minecart, type);
	}

	@Override
	public void writeToNbt(NbtCompound nbt) {
		nbt.putInt("NoteIndex", this.noteIndex);
		nbt.putInt("NoteTimer", this.noteTimer);
		nbt.putByteArray("NoteSequence", this.noteSequence);
		nbt.putInt("SequenceLength", this.sequenceLength);
		super.writeToNbt(nbt);
	}

	@Override
	public void readFromNbt(NbtCompound nbt) {
		this.noteIndex = nbt.getInt("NoteIndex");
		this.noteTimer = nbt.getInt("NoteTimer");
		if (nbt.contains("NoteSequence")) {
			byte[] seq = nbt.getByteArray("NoteSequence");
			System.arraycopy(seq, 0, this.noteSequence, 0, Math.min(seq.length, 16));
		}
		this.sequenceLength = nbt.getInt("SequenceLength");
		super.readFromNbt(nbt);
	}

	@Override
	public void work() {
		if (this.sequenceLength > 0 && ++this.noteTimer >= NOTE_INTERVAL) {
			this.noteTimer = 0;
			// Play current note in sequence
			if (this.noteIndex < this.sequenceLength) {
				byte note = this.noteSequence[this.noteIndex];
				// Note playing logic would happen here
				this.noteIndex++;
			} else {
				this.noteIndex = 0; // Loop sequence
			}
		}
	}
}