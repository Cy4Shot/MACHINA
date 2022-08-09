package com.machina.item;

import com.machina.block.tile.TintedTileEntity;
import com.machina.client.ClientStarchart;
import com.machina.registration.init.AttributeInit;
import com.machina.util.text.StringUtils;
import com.machina.world.data.PlanetData;
import com.machina.world.data.StarchartData;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class TintedItem extends BlockItem {

	private static final String PLANET_ID = "PlanetID";

	public TintedItem(Block pBlock, Properties pProperties) {
		super(pBlock, pProperties);
	}

	@Override
	public ITextComponent getName(ItemStack pStack) {
		String old = StringUtils.translate(this.getDescriptionId(pStack));
		int id = getFromStack(pStack);
		if (id != -1) {
			PlanetData data;
			try {
				data = StarchartData.getDataForDimension(id);
			} catch (Exception e) {
				data = ClientStarchart.getPlanetData(id);
			}
			if (data != null) {
				String name = data.getAttributeFormatted(AttributeInit.PLANET_NAME).trim();
				old = old + " (" + name + ")";
			}
		}
		return StringUtils.toComp(old);
	}

	@Override
	protected boolean updateCustomBlockEntityTag(BlockPos pPos, World pLevel, PlayerEntity pPlayer, ItemStack pStack,
			BlockState pState) {
		super.updateCustomBlockEntityTag(pPos, pLevel, pPlayer, pStack, pState);
		TileEntity tileentity = pLevel.getBlockEntity(pPos);
		if (tileentity != null && tileentity instanceof TintedTileEntity) {
			if (!pLevel.isClientSide && tileentity.onlyOpCanSetNbt()
					&& (pPlayer == null || !pPlayer.canUseGameMasterBlocks())) {
				return false;
			}
			
			TintedTileEntity te = (TintedTileEntity) tileentity;
			te.id = getFromStack(pStack);
			te.sync();

			return true;
		}

		return false;
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = super.getDefaultInstance();
		applyToStack(stack, -1);
		return stack;
	}

	public static void applyToStack(ItemStack stack, int planet) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putInt(PLANET_ID, planet);
	}

	public static int getFromStack(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt.contains(PLANET_ID))
			return nbt.getInt(PLANET_ID);
		else
			return -1;
	}
}