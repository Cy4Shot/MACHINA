package com.machina.block.tile;

import com.machina.block.CargoCrateBlock;
import com.machina.registration.init.TileEntityTypesInit;
import com.machina.util.nbt.ItemStackUtil;
import com.machina.util.text.TextComponentHelper;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class CargoCrateTileEntity extends LockableLootTileEntity implements IAnimatable {

	public static int slots = 1;
	protected NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);

	private final AnimationFactory manager = new AnimationFactory(this);

	public CargoCrateTileEntity(TileEntityType<?> type) {
		super(type);
	}

	public CargoCrateTileEntity() {
		this(TileEntityTypesInit.CARGO_CRATE.get());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <E extends TileEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
		AnimationController controller = event.getController();
		controller.transitionLengthTicks = 0;

		if (event.getAnimatable().getBlockState().getValue(CargoCrateBlock.OPEN).booleanValue()) {
			controller.setAnimation(new AnimationBuilder().addAnimation("animation.cargo_crate.open", false)
					.addAnimation("animation.cargo_crate.stay_open", true));
		} else {
			controller.setAnimation(new AnimationBuilder().addAnimation("animation.cargo_crate.stay_closed", true));
		}

		return PlayState.CONTINUE;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void registerControllers(AnimationData data) {
		data.addAnimationController(new AnimationController(this, "controller", 0, this::predicate));
	}

	@Override
	public AnimationFactory getFactory() {
		return this.manager;
	}

	@Override
	public int getContainerSize() {
		return slots;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return this.items;
	}

	@Override
	protected void setItems(NonNullList<ItemStack> pItems) {
		this.items = pItems;
		this.syncClients();
	}
	
	@Override
	public void setItem(int pIndex, ItemStack pStack) {
		super.setItem(pIndex, pStack);
		this.syncClients();
	}
	
	public void syncClients() {
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 2);
		this.setChanged();
	}
	
	@Override
	protected ITextComponent getDefaultName() {
		return TextComponentHelper.translate("machina.container.cargo_crate");
	}

	public ItemStack getItem(int id) {
		return this.items.get(id);
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		if (!this.trySaveLootTable(compound)) {
			ItemStackUtil.saveAllItems(compound, this.items, "I");
		}
		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
		if (!this.tryLoadLootTable(compound)) {
			ItemStackUtil.loadAllItems(compound, this.items, "I");
		}
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return this.save(new CompoundNBT());
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = new CompoundNBT();
		this.save(nbt);
		return new SUpdateTileEntityPacket(this.getBlockPos(), 0, nbt);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		this.load(this.getBlockState(), pkt.getTag());

	}

	@Override
	protected Container createMenu(int pId, PlayerInventory pPlayer) {
		return null;
	}

	@Override
	public boolean canOpen(PlayerEntity pPlayer) {
		return false;
	}
}
