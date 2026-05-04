package com.machina.api.cap.steam;

public interface ISteamHandler {
	
	/**
     * Adds steam to the storage. Returns the amount of steam that was accepted.
     *
     * @param toReceive The amount of steam being received.
     * @param simulate  If true, the insertion will only be simulated, meaning {@link #getSteamStored()} will not change.
     * @return Amount of steam that was (or would have been, if simulated) accepted by the storage.
     */
    int receiveSteam(int toReceive, boolean simulate);

    /**
     * Removes energy from the storage. Returns the amount of energy that was removed.
     *
     * @param toExtract The amount of energy being extracted.
     * @param simulate  If true, the extraction will only be simulated, meaning {@link #getSteamStored()} will not change.
     * @return Amount of energy that was (or would have been, if simulated) extracted from the storage.
     */
    int extractSteam(int toExtract, boolean simulate);

    /**
     * Returns the amount of energy currently stored.
     */
    int getSteamStored();

    /**
     * Returns the maximum amount of energy that can be stored.
     */
    int getMaxSteamStored();

    /**
     * Returns if this storage can have energy extracted.
     * If this is false, then any calls to extractSteam will return 0.
     */
    boolean canExtract();

    /**
     * Used to determine if this storage can receive energy.
     * If this is false, then any calls to receiveSteam will return 0.
     */
    boolean canReceive();
}
