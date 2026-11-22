package com.machina.block;

import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.item.context.BlockPlaceContext;

public class ToxShroomBlock extends FlowerBlock {

	public static final BooleanProperty PICKED = BooleanProperty.create("picked");

	public static final MapCodec<ToxShroomBlock> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					EFFECTS_FIELD.forGetter(FlowerBlock::getSuspiciousEffects),
					propertiesCodec()
			).apply(instance, ToxShroomBlock::new)
	);

	private Supplier<Item> sporocarpItem;

	public ToxShroomBlock(SuspiciousStewEffects effects, Properties props) {
		super(effects, props);
		this.registerDefaultState(this.stateDefinition.any().setValue(PICKED, false));
	}

	public ToxShroomBlock(Holder<MobEffect> effect, int duration, Properties props) {
		super(effect, duration, props);
		this.registerDefaultState(this.stateDefinition.any().setValue(PICKED, false));
	}

	public ToxShroomBlock setSporocarp(Supplier<Item> item) {
		this.sporocarpItem = item;
		return this;
	}

	@Override
	public MapCodec<? extends FlowerBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(PICKED);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
		return state.isSolid();
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Player player = context.getPlayer();
		boolean isCreative = player != null && player.getAbilities().instabuild;
		return this.defaultBlockState().setValue(PICKED, !isCreative);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
		if (state.getValue(PICKED)) {
			return InteractionResult.PASS;
		}

		if (sporocarpItem != null && !level.isClientSide) {
			popResource(level, pos, new ItemStack(sporocarpItem.get()));
		}

		level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
		level.setBlock(pos, state.setValue(PICKED, true), 2);
		level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state));

		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return true;
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (state.getValue(PICKED) && random.nextInt(3) == 0) {
			level.setBlock(pos, state.setValue(PICKED, false), 2);
		}
	}
}
