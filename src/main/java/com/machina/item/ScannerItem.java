package com.machina.item;

import javax.annotation.Nullable;

import com.machina.item.container.ScannerContainer;
import com.machina.util.text.StringUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class ScannerItem extends Item {

	public ScannerItem(Properties props) {
		super(props);
	}

	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand pHand) {
		if (world.isClientSide())
			return super.use(world, player, pHand);

		RegistryKey<World> dim = player.level.dimension();

		NetworkHooks.openGui((ServerPlayerEntity) player, new INamedContainerProvider() {
			@Override
			public ITextComponent getDisplayName() {
				return StringUtils.EMPTY;
			}

			@Nullable
			@Override
			public Container createMenu(int i, PlayerInventory inv, PlayerEntity player) {
				return new ScannerContainer(i, dim);
			}
		}, (buffer) -> {
			buffer.writeResourceLocation(dim.location());
		});
		return ActionResult.consume(player.getItemInHand(pHand));
	}

}
