package com.machina.registration.init;

import com.machina.Machina;
import com.machina.world.structure.CityVaultStructure;
import com.machina.world.structure.vault.VaultCorridorPiece;
import com.machina.world.structure.vault.VaultEntrancePiece;
import com.machina.world.structure.vault.VaultMainChamberPiece;
import com.machina.world.structure.vault.VaultSideRoomPiece;
import com.machina.world.structure.vault.VaultStaircasePiece;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class StructureInit {

	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
		DeferredRegister.create(Registries.STRUCTURE_TYPE, Machina.MOD_ID);

	public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES =
		DeferredRegister.create(Registries.STRUCTURE_PIECE, Machina.MOD_ID);

	public static final Supplier<StructureType<CityVaultStructure>> CITY_VAULT =
		STRUCTURE_TYPES.register("city_vault", () -> () -> CityVaultStructure.CODEC);

	public static final Supplier<StructurePieceType> VAULT_MAIN_CHAMBER =
		STRUCTURE_PIECE_TYPES.register("vault_main_chamber", () -> VaultMainChamberPiece::new);

	public static final Supplier<StructurePieceType> VAULT_STAIRCASE =
		STRUCTURE_PIECE_TYPES.register("vault_staircase", () -> VaultStaircasePiece::new);

	public static final Supplier<StructurePieceType> VAULT_ENTRANCE =
		STRUCTURE_PIECE_TYPES.register("vault_entrance", () -> VaultEntrancePiece::new);

	public static final Supplier<StructurePieceType> VAULT_CORRIDOR =
		STRUCTURE_PIECE_TYPES.register("vault_corridor", () -> VaultCorridorPiece::new);

	public static final Supplier<StructurePieceType> VAULT_SIDE_ROOM =
		STRUCTURE_PIECE_TYPES.register("vault_side_room", () -> VaultSideRoomPiece::new);
}
