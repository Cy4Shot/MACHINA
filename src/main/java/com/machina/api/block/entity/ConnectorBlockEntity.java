package com.machina.api.block.entity;

import com.machina.api.block.ConnectorBlock;
import com.machina.api.block.menu.IDirectionalMenuProvider;
import com.machina.api.cap.IConnectorStorage;
import com.machina.api.cap.sided.ConnectionSide;
import com.machina.api.cap.sided.SidedLazyOptionalCache;
import com.machina.api.client.model.connector.ConnectorModel.ConnectorModelData;
import com.machina.api.item.ConnectorFilterItem;
import com.machina.api.util.block.BlockHelper;
import com.machina.api.util.reflect.QuintFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.client.model.data.ModelData;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ConnectorBlockEntity<U, T extends IConnectorStorage<U>> extends ContainerBlockEntity
        implements IDirectionalMenuProvider {

    protected final int[] roundrobin;
    private int recursionDepth;
    private boolean search = false;

    private final Map<Direction, ConnectionSide> myConnectors = new HashMap<>();
    public List<Connection> connectors = new ArrayList<>();
    public final List<BlockPos> cache = new ArrayList<>();
    public List<Direction> dirs = new ArrayList<>();

    private final SidedLazyOptionalCache<T> cap;

    public ConnectorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.cap = new SidedLazyOptionalCache<>();
        this.roundrobin = new int[Direction.values().length];
        this.reviveCaps();
    }

    public abstract T createStorage(Direction side);

    public abstract int slotsPerSide();

    @Override
    public boolean hasItemIO() {
        return false;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        if (level.isClientSide())
            return;

        ConnectorBlockEntity<?, ?> cbe = (ConnectorBlockEntity<?, ?>) be;

        for (Direction dir : Direction.values()) {
            cbe.cap.get(dir).ifPresent(IConnectorStorage::tick);
        }

        if (cbe.search) {
            cbe.search();
            cbe.search = false;
            cbe.sync();
        }
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player, Direction d) {
        if (this.canOpen(player)) {
            return getMenu().apply(id, getLevel(), getBlockPos(), inv, d);
        }
        return null;
    }

    public abstract QuintFunction<Integer, Level, BlockPos, Inventory, Direction, AbstractContainerMenu> getMenu();

    @SuppressWarnings("unchecked")
    public boolean filter(Direction dir, U resource) {
        ItemStack i = getItem(getSlotForSide(dir, 0));
        if (i.getItem() instanceof ConnectorFilterItem<?, ?> c) {
            return ((ConnectorFilterItem<U, T>) c).filter(i, resource);
        }
        return true;
    }

    public int getRoundRobinIndex(Direction direction) {
        return roundrobin[direction.get3DDataValue()];
    }

    public void setRoundRobinIndex(Direction direction, int value) {
        roundrobin[direction.get3DDataValue()] = value;
    }

    public List<Connection> getSortedConnections() {
        return connectors.stream().sorted(Comparator.comparingInt(Connection::distance))
                .collect(Collectors.toList());
    }

    public void setConnection(Direction dir, ConnectionSide side) {
        if (side == ConnectionSide.NONE)
            return;
        myConnectors.put(dir, side);
        this.sync();
    }

    public ConnectionSide getConnection(Direction dir) {
        return myConnectors.getOrDefault(dir, ConnectionSide.NONE);
    }

    public abstract Capability<?> getCapability();

    public abstract int getRate();

    @Override
    public <C> @NotNull LazyOptional<C> getCapability(@NotNull BlockCapability<C, Direction> cap, Direction d) {
        if (cap == getCapability())
            return this.cap.get(d).cast();

        return super.getCapability(cap, d);
    }

    @Override
    public void invalidateCaps() {
        this.cap.invalidate();
        super.invalidateCaps();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        for (Direction dir : Direction.values()) {
            for (int i = 0; i < slotsPerSide(); i++)
                itemStorage();
            cap.revalidate(dir, s -> true, this::createStorage);
        }
    }

    @Override
    public void setRemoved() {
        this.cap.invalidate();
        super.setRemoved();
    }
    

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, Provider registries) {
        CompoundTag mycons = new CompoundTag();
        for (Direction dir : Direction.values()) {
            if (myConnectors.containsKey(dir)) {
                CompoundTag postag = new CompoundTag();
                myConnectors.get(dir).save(postag, "connector");
                mycons.put(String.valueOf(dir.get3DDataValue()), postag);
            }
        }
        tag.put("my_connectors", mycons);

        ListTag cons = new ListTag();
        this.connectors.forEach(pos -> {
            CompoundTag postag = new CompoundTag();
            postag.put("connector", pos.save());
            cons.add(postag);
        });
        tag.put("connectors", cons);

        ListTag sides = new ListTag();
        this.dirs.forEach(dir -> {
            CompoundTag postag = new CompoundTag();
            postag.putInt("dir", dir.get3DDataValue());
            sides.add(postag);
        });
        tag.put("dirs", sides);
        super.saveAdditional(tag, registries);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, Provider registries) {
        connectors = new ArrayList<>();
        dirs = new ArrayList<>();

        CompoundTag mycons = tag.getCompound("my_connectors");
        for (int j = 0; j < Direction.values().length; j++) {
            String key = Integer.toString(j);
            if (mycons.contains(key)) {
                ConnectionSide con = ConnectionSide.load(mycons.getCompound(key), "connector");
                Direction d = Direction.from3DDataValue(j);
                myConnectors.put(d, con);
            }
        }

        ListTag cons = tag.getList("connectors", Tag.TAG_COMPOUND);
        for (int j = 0; j < cons.size(); j++) {
            connectors.add(Connection.load(cons.getCompound(j).getCompound("connector")));
        }

        ListTag sides = tag.getList("dirs", Tag.TAG_COMPOUND);
        for (int j = 0; j < sides.size(); j++) {
            dirs.add(Direction.from3DDataValue(sides.getCompound(j).getInt("dir")));
        }
        super.loadAdditional(tag, registries);

        if (this.level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
    }

    public void enqueueSearch() {
        this.connectors.clear();
        this.search = true;
    }

    public void search() {
        if (this.level != null) {
            addToCache(this.worldPosition);

            Block b = this.getBlockState().getBlock();
            if (b instanceof ConnectorBlock cb) {
                cb.syncConnections(level, worldPosition);

                dirs.forEach(dir -> connectors.add(new Connection(this.worldPosition, dir, 0)));
                cb.searchConnectors(this.level, this.worldPosition, this, 0);
                dirs.forEach(dir -> {
                    ConnectionSide side = myConnectors.getOrDefault(dir, ConnectionSide.NONE);
                    if (!side.isIO()) {
                        side = ConnectionSide.OUTPUT;
                    }
                    setConnection(dir, side);
                });
            }
        }
        this.cache.clear();
    }

    public int getSlotForSide(Direction side, int slot) {
        return side.get3DDataValue() * slotsPerSide() + slot;
    }

    public void addToCache(BlockPos pos) {
        this.cache.add(pos);
    }

    public boolean isInCache(BlockPos pos) {
        return this.cache.contains(pos);
    }

    public boolean pushRecursion() {
        if (recursionDepth >= 1) {
            return true;
        }
        recursionDepth++;
        return false;
    }

    public void popRecursion() {
        recursionDepth--;
    }

    public record Connection(BlockPos pos, Direction direction, int distance) {

        public ConnectionSide getSide(BlockGetter level) {
            return BlockHelper.getFromTe(level, pos, ConnectorBlockEntity.class, c -> c.getConnection(direction));
        }

        @SuppressWarnings("unchecked")
        public boolean filter(BlockGetter level, Object resource) {
            return Boolean.TRUE.equals(BlockHelper.getFromTe(level, pos, ConnectorBlockEntity.class, c -> c.filter(direction, resource)));
        }

        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.put("CPos", NbtUtils.writeBlockPos(pos));
            tag.putInt("CDir", direction.get3DDataValue());
            tag.putInt("CDis", distance);
            return tag;
        }

        public static Connection load(CompoundTag nbt) {
            BlockPos p = NbtUtils.readBlockPos(nbt, "CPos").orElse(BlockPos.ZERO);
            Direction d = Direction.from3DDataValue(nbt.getInt("CDir"));
            int dist = nbt.getInt("CDis");
            return new Connection(p, d, dist);
        }
    }

    private ConnectionSide connectionData(boolean connected, Direction dir) {
        return connected ? (myConnectors.containsKey(dir) && dirs.contains(dir) ? myConnectors.get(dir) : ConnectionSide.NORMAL)
                : ConnectionSide.NONE;
    }

    private short getPackedModelData() {
        if (this.level == null) {
            return 0;
        }

        ConnectorBlock b = (ConnectorBlock) getBlockState().getBlock();
        boolean[] data = b.getModelData(this.level, getBlockPos());
        ConnectionSide north = connectionData(data[2], Direction.NORTH);
        ConnectionSide east = connectionData(data[5], Direction.EAST);
        ConnectionSide south = connectionData(data[3], Direction.SOUTH);
        ConnectionSide west = connectionData(data[4], Direction.WEST);
        ConnectionSide up = connectionData(data[1], Direction.UP);
        ConnectionSide down = connectionData(data[0], Direction.DOWN);

        short packed = 0;
        packed |= (short) down.ordinal();
        packed |= (short) (up.ordinal() << 2);
        packed |= (short) (north.ordinal() << 4);
        packed |= (short) (south.ordinal() << 6);
        packed |= (short) (west.ordinal() << 8);
        packed |= (short) (east.ordinal() << 10);
        packed |= (short) ((data[6] ? 0 : 1) << 12);
        return packed;
    }

    @Override
    public @NotNull ModelData getModelData() {
        return ModelData.builder().with(ConnectorModelData.PROPERTY, getPackedModelData()).build();
    }

    @Override
    public boolean activeModel() {
        return true;
    }
}
