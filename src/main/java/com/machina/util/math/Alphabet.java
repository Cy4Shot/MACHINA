package com.machina.util.math;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//Modified from: https://github.com/mimno/Mallet
@SuppressWarnings("rawtypes")
public class Alphabet implements Serializable {
	HashMap<Object, Integer> map;
	ArrayList entries;
	volatile boolean growthStopped = false;
	Class entryClass = null;
	VMID instanceId = new VMID();

	private transient ReadWriteLock lock = new ReentrantReadWriteLock();

	public Alphabet(int capacity, Class entryClass) {
		this.map = new HashMap<Object, Integer>(capacity);
		this.entries = new ArrayList(capacity);
		this.entryClass = entryClass;

		deserializedEntries.putIfAbsent(instanceId, this);
	}

	public Alphabet(Class entryClass) {
		this(8, entryClass);
	}

	public Alphabet(int capacity) {
		this(capacity, null);
	}

	public Alphabet() {
		this(8, null);
	}

	public Alphabet(Object[] entries) {
		this(entries.length);
		for (Object entry : entries)
			this.lookupIndex(entry);
	}

	@SuppressWarnings("unchecked")
	public Object clone() {
		lock.readLock().lock();
		try {
			Alphabet ret = new Alphabet();
			ret.map = (HashMap<Object, Integer>) map.clone();
			ret.entries = (ArrayList) entries.clone();
			ret.growthStopped = growthStopped;
			ret.entryClass = entryClass;
			return ret;
		} finally {
			lock.readLock().unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public int lookupIndex(Object entry, boolean addIfNotPresent) {
		if (entry == null)
			throw new IllegalArgumentException("Can't lookup \"null\" in an Alphabet.");
		if (entryClass == null) {
			entryClass = entry.getClass();
		} else {

			if (entry.getClass() != entryClass)
				throw new IllegalArgumentException(
						"Non-matching entry class, " + entry.getClass() + ", was " + entryClass);
		}

		lock.readLock().lock();
		try {
			if (map.containsKey(entry)) {
				return map.get(entry);
			}
		} finally {
			lock.readLock().unlock();
		}

		if (!growthStopped && addIfNotPresent) {
			lock.writeLock().lock();
			try {
				int retIndex = entries.size();
				map.put(entry, retIndex);
				entries.add(entry);
				return retIndex;

			} finally {
				lock.writeLock().unlock();
			}
		}
		return -1;
	}

	public int lookupIndex(Object entry) {
		return lookupIndex(entry, true);
	}

	public Object lookupObject(int index) {
		lock.readLock().lock();
		try {
			return entries.get(index);
		} finally {
			lock.readLock().unlock();
		}
	}

	public Object[] toArray() {
		lock.readLock().lock();
		try {
			return entries.toArray();
		} finally {
			lock.readLock().unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public Object[] toArray(Object[] in) {
		lock.readLock().lock();
		try {
			return entries.toArray(in);
		} finally {
			lock.readLock().unlock();
		}
	}

	@SuppressWarnings("unchecked")
	public Iterator iterator() {
		lock.readLock().lock();
		try {
			ArrayList copy = new ArrayList();
			copy.addAll(entries);
			return copy.iterator();
		} finally {
			lock.readLock().unlock();
		}
	}

	public Object[] lookupObjects(int[] indices) {
		lock.readLock().lock();
		try {
			Object[] ret = new Object[indices.length];
			for (int i = 0; i < indices.length; i++)
				ret[i] = entries.get(indices[i]);
			return ret;
		} finally {
			lock.readLock().unlock();
		}
	}

	public Object[] lookupObjects(int[] indices, Object[] buf) {
		lock.readLock().lock();
		try {
			for (int i = 0; i < indices.length; i++)
				buf[i] = entries.get(indices[i]);
			return buf;
		} finally {
			lock.readLock().unlock();
		}
	}

	public int[] lookupIndices(Object[] objects, boolean addIfNotPresent) {
		int[] ret = new int[objects.length];
		for (int i = 0; i < objects.length; i++)
			ret[i] = lookupIndex(objects[i], addIfNotPresent);
		return ret;
	}

	public int size() {
		lock.readLock().lock();
		try {
			return entries.size();
		} finally {
			lock.readLock().unlock();
		}
	}

	public void stopGrowth() {
		growthStopped = true;
	}

	public void startGrowth() {
		growthStopped = false;
	}

	public boolean growthStopped() {
		return growthStopped;
	}

	public Class entryClass() {
		return entryClass;
	}

	public String toString() {
		lock.readLock().lock();
		try {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < entries.size(); i++) {
				sb.append(entries.get(i).toString());
				sb.append('\n');
			}
			return sb.toString();
		} finally {
			lock.readLock().unlock();
		}
	}

	public void dump() {
		dump(System.out);
	}

	public void dump(PrintStream out) {
		dump(new PrintWriter(new OutputStreamWriter(out), true));
	}

	public void dump(PrintWriter out) {
		lock.readLock().lock();
		try {
			for (int i = 0; i < entries.size(); i++) {
				out.println(i + " => " + entries.get(i));
			}
		} finally {
			lock.readLock().unlock();
		}
	}

	public static boolean alphabetsMatch(AlphabetCarrying object1, AlphabetCarrying object2) {
		Alphabet[] a1 = object1.getAlphabets();
		Alphabet[] a2 = object2.getAlphabets();
		if (a1.length != a2.length)
			return false;
		for (int i = 0; i < a1.length; i++) {
			if (a1[i] == a2[i])
				continue;
			if (a1[i] == null || a2[i] == null)
				return false;
			if (!a1[i].equals(a2[i]))
				return false;
		}
		return true;
	}

	public VMID getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(VMID id) {
		this.instanceId = id;
	}

	private static final long serialVersionUID = 1;
	private static final int CURRENT_SERIAL_VERSION = 1;

	private void writeObject(ObjectOutputStream out) throws IOException {
		lock.readLock().lock();
		try {
			out.writeInt(CURRENT_SERIAL_VERSION);
			out.writeInt(entries.size());
			for (int i = 0; i < entries.size(); i++)
				out.writeObject(entries.get(i));
			out.writeBoolean(growthStopped);
			out.writeObject(entryClass);
			out.writeObject(instanceId);
		} finally {
			lock.readLock().unlock();
		}
	}

	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		lock = new ReentrantReadWriteLock();
		lock.writeLock().lock();
		try {
			int version = in.readInt();
			int size = in.readInt();
			entries = new ArrayList(size);
			map = new HashMap<Object, Integer>(size);
			for (int i = 0; i < size; i++) {
				Object o = in.readObject();
				map.put(o, i);
				entries.add(o);
			}
			growthStopped = in.readBoolean();
			entryClass = (Class) in.readObject();
			if (version > 0) {
				instanceId = (VMID) in.readObject();
			}
		} finally {
			lock.writeLock().unlock();
		}
	}

	private transient static ConcurrentMap<VMID, Object> deserializedEntries = new ConcurrentHashMap<VMID, Object>();

	public Object readResolve() throws ObjectStreamException {
		Object previous = deserializedEntries.get(instanceId);
		if (previous != null) {

			return previous;
		}
		if (instanceId != null) {
			Object prev = deserializedEntries.putIfAbsent(instanceId, this);
			if (prev != null) {
				return prev;
			}
		}

		return this;
	}

	public interface AlphabetCarrying {
		Alphabet getAlphabet();

		Alphabet[] getAlphabets();
	}

}
