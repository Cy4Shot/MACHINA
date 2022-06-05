package com.machina.client.screen;

import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

// The same as ContainerScreen, but not scanned by JEI.
public abstract class NoJeiContainerScreen<T extends Container> extends Screen implements IHasContainer<T> {
	/** The location of the inventory background texture */
	public static final ResourceLocation INVENTORY_LOCATION = new ResourceLocation(
			"textures/gui/container/inventory.png");
	/** The X size of the inventory window in pixels. */
	protected int imageWidth = 176;
	/** The Y size of the inventory window in pixels. */
	protected int imageHeight = 166;
	protected int titleLabelX;
	protected int titleLabelY;
	protected int inventoryLabelX;
	protected int inventoryLabelY;
	/** A list of the players inventory slots */
	protected final T menu;
	protected final PlayerInventory inventory;
	/** Holds the slot currently hovered */
	@Nullable
	protected Slot hoveredSlot;
	/** Used when touchscreen is enabled */
	@Nullable
	private Slot clickedSlot;
	@Nullable
	private Slot snapbackEnd;
	@Nullable
	private Slot quickdropSlot;
	@Nullable
	private Slot lastClickSlot;
	/** Starting X position for the Gui. Inconsistent use for Gui backgrounds. */
	protected int leftPos;
	/** Starting Y position for the Gui. Inconsistent use for Gui backgrounds. */
	protected int topPos;
	/** Used when touchscreen is enabled. */
	private boolean isSplittingStack;
	/** Used when touchscreen is enabled */
	private ItemStack draggingItem = ItemStack.EMPTY;
	private int snapbackStartX;
	private int snapbackStartY;
	private long snapbackTime;
	/** Used when touchscreen is enabled */
	private ItemStack snapbackItem = ItemStack.EMPTY;
	private long quickdropTime;
	protected final Set<Slot> quickCraftSlots = Sets.newHashSet();
	protected boolean isQuickCrafting;
	private int quickCraftingType;
	private int quickCraftingButton;
	private boolean skipNextRelease;
	private int quickCraftingRemainder;
	private long lastClickTime;
	private int lastClickButton;
	private boolean doubleclick;
	private ItemStack lastQuickMoved = ItemStack.EMPTY;

	public NoJeiContainerScreen(T pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
		super(pTitle);
		this.menu = pMenu;
		this.inventory = pPlayerInventory;
		this.skipNextRelease = true;
		this.titleLabelX = 8;
		this.titleLabelY = 6;
		this.inventoryLabelX = 8;
		this.inventoryLabelY = this.imageHeight - 94;
	}

	protected void init() {
		super.init();
		this.leftPos = (this.width - this.imageWidth) / 2;
		this.topPos = (this.height - this.imageHeight) / 2;
	}

	public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		int i = this.leftPos;
		int j = this.topPos;
		this.renderBg(pMatrixStack, pPartialTicks, pMouseX, pMouseY);
//	      net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawBackground(this, pMatrixStack, pMouseX, pMouseY));
		RenderSystem.disableRescaleNormal();
		RenderSystem.disableDepthTest();
		super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
		RenderSystem.pushMatrix();
		RenderSystem.translatef((float) i, (float) j, 0.0F);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.enableRescaleNormal();
		this.hoveredSlot = null;
		RenderSystem.glMultiTexCoord2f(33986, 240.0F, 240.0F);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		for (int i1 = 0; i1 < this.menu.slots.size(); ++i1) {
			Slot slot = this.menu.slots.get(i1);
			if (slot.isActive()) {
				this.renderSlot(pMatrixStack, slot);
			}

			if (this.isHovering(slot, (double) pMouseX, (double) pMouseY) && slot.isActive()) {
				this.hoveredSlot = slot;
				RenderSystem.disableDepthTest();
				int j1 = slot.x;
				int k1 = slot.y;
				RenderSystem.colorMask(true, true, true, false);
				int slotColor = this.getSlotColor(i1);
				this.fillGradient(pMatrixStack, j1, k1, j1 + 16, k1 + 16, slotColor, slotColor);
				RenderSystem.colorMask(true, true, true, true);
				RenderSystem.enableDepthTest();
			}
		}

		this.renderLabels(pMatrixStack, pMouseX, pMouseY);
//	      net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.GuiContainerEvent.DrawForeground(this, pMatrixStack, pMouseX, pMouseY));
		PlayerInventory playerinventory = this.minecraft.player.inventory;
		ItemStack itemstack = this.draggingItem.isEmpty() ? playerinventory.getCarried() : this.draggingItem;
		if (!itemstack.isEmpty()) {
			int k2 = this.draggingItem.isEmpty() ? 8 : 16;
			String s = null;
			if (!this.draggingItem.isEmpty() && this.isSplittingStack) {
				itemstack = itemstack.copy();
				itemstack.setCount(MathHelper.ceil((float) itemstack.getCount() / 2.0F));
			} else if (this.isQuickCrafting && this.quickCraftSlots.size() > 1) {
				itemstack = itemstack.copy();
				itemstack.setCount(this.quickCraftingRemainder);
				if (itemstack.isEmpty()) {
					s = "" + TextFormatting.YELLOW + "0";
				}
			}

			this.renderFloatingItem(itemstack, pMouseX - i - 8, pMouseY - j - k2, s);
		}

		if (!this.snapbackItem.isEmpty()) {
			float f = (float) (Util.getMillis() - this.snapbackTime) / 100.0F;
			if (f >= 1.0F) {
				f = 1.0F;
				this.snapbackItem = ItemStack.EMPTY;
			}

			int l2 = this.snapbackEnd.x - this.snapbackStartX;
			int i3 = this.snapbackEnd.y - this.snapbackStartY;
			int l1 = this.snapbackStartX + (int) ((float) l2 * f);
			int i2 = this.snapbackStartY + (int) ((float) i3 * f);
			this.renderFloatingItem(this.snapbackItem, l1, i2, (String) null);
		}

		RenderSystem.popMatrix();
		RenderSystem.enableDepthTest();
	}

	protected void renderTooltip(MatrixStack pPoseStack, int pX, int pY) {
		if (this.minecraft.player.inventory.getCarried().isEmpty() && this.hoveredSlot != null
				&& this.hoveredSlot.hasItem()) {
			this.renderTooltip(pPoseStack, this.hoveredSlot.getItem(), pX, pY);
		}

	}

	/**
	 * Draws an ItemStack.
	 * 
	 * The z index is increased by 32 (and not decreased afterwards), and the item
	 * is then rendered at z=200.
	 */
	private void renderFloatingItem(ItemStack pStack, int pX, int pY, String pAltText) {
		RenderSystem.translatef(0.0F, 0.0F, 32.0F);
		this.setBlitOffset(200);
		this.itemRenderer.blitOffset = 200.0F;
		net.minecraft.client.gui.FontRenderer font = pStack.getItem().getFontRenderer(pStack);
		if (font == null)
			font = this.font;
		this.itemRenderer.renderAndDecorateItem(pStack, pX, pY);
		this.itemRenderer.renderGuiItemDecorations(font, pStack, pX, pY - (this.draggingItem.isEmpty() ? 0 : 8),
				pAltText);
		this.setBlitOffset(0);
		this.itemRenderer.blitOffset = 0.0F;
	}

	protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {
		this.font.draw(pMatrixStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
		this.font.draw(pMatrixStack, this.inventory.getDisplayName(), (float) this.inventoryLabelX,
				(float) this.inventoryLabelY, 4210752);
	}

	protected abstract void renderBg(MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY);

	private void renderSlot(MatrixStack pPoseStack, Slot pSlot) {
		int i = pSlot.x;
		int j = pSlot.y;
		ItemStack itemstack = pSlot.getItem();
		boolean flag = false;
		boolean flag1 = pSlot == this.clickedSlot && !this.draggingItem.isEmpty() && !this.isSplittingStack;
		ItemStack itemstack1 = this.minecraft.player.inventory.getCarried();
		String s = null;
		if (pSlot == this.clickedSlot && !this.draggingItem.isEmpty() && this.isSplittingStack
				&& !itemstack.isEmpty()) {
			itemstack = itemstack.copy();
			itemstack.setCount(itemstack.getCount() / 2);
		} else if (this.isQuickCrafting && this.quickCraftSlots.contains(pSlot) && !itemstack1.isEmpty()) {
			if (this.quickCraftSlots.size() == 1) {
				return;
			}

			if (Container.canItemQuickReplace(pSlot, itemstack1, true) && this.menu.canDragTo(pSlot)) {
				itemstack = itemstack1.copy();
				flag = true;
				Container.getQuickCraftSlotCount(this.quickCraftSlots, this.quickCraftingType, itemstack,
						pSlot.getItem().isEmpty() ? 0 : pSlot.getItem().getCount());
				int k = Math.min(itemstack.getMaxStackSize(), pSlot.getMaxStackSize(itemstack));
				if (itemstack.getCount() > k) {
					s = TextFormatting.YELLOW.toString() + k;
					itemstack.setCount(k);
				}
			} else {
				this.quickCraftSlots.remove(pSlot);
				this.recalculateQuickCraftRemaining();
			}
		}

		this.setBlitOffset(100);
		this.itemRenderer.blitOffset = 100.0F;
		if (itemstack.isEmpty() && pSlot.isActive()) {
			Pair<ResourceLocation, ResourceLocation> pair = pSlot.getNoItemIcon();
			if (pair != null) {
				TextureAtlasSprite textureatlassprite = this.minecraft.getTextureAtlas(pair.getFirst())
						.apply(pair.getSecond());
				this.minecraft.getTextureManager().bind(textureatlassprite.atlas().location());
				blit(pPoseStack, i, j, this.getBlitOffset(), 16, 16, textureatlassprite);
				flag1 = true;
			}
		}

		if (!flag1) {
			if (flag) {
				fill(pPoseStack, i, j, i + 16, j + 16, -2130706433);
			}

			RenderSystem.enableDepthTest();
			this.itemRenderer.renderAndDecorateItem(this.minecraft.player, itemstack, i, j);
			this.itemRenderer.renderGuiItemDecorations(this.font, itemstack, i, j, s);
		}

		this.itemRenderer.blitOffset = 0.0F;
		this.setBlitOffset(0);
	}

	private void recalculateQuickCraftRemaining() {
		ItemStack itemstack = this.minecraft.player.inventory.getCarried();
		if (!itemstack.isEmpty() && this.isQuickCrafting) {
			if (this.quickCraftingType == 2) {
				this.quickCraftingRemainder = itemstack.getMaxStackSize();
			} else {
				this.quickCraftingRemainder = itemstack.getCount();

				for (Slot slot : this.quickCraftSlots) {
					ItemStack itemstack1 = itemstack.copy();
					ItemStack itemstack2 = slot.getItem();
					int i = itemstack2.isEmpty() ? 0 : itemstack2.getCount();
					Container.getQuickCraftSlotCount(this.quickCraftSlots, this.quickCraftingType, itemstack1, i);
					int j = Math.min(itemstack1.getMaxStackSize(), slot.getMaxStackSize(itemstack1));
					if (itemstack1.getCount() > j) {
						itemstack1.setCount(j);
					}

					this.quickCraftingRemainder -= itemstack1.getCount() - i;
				}

			}
		}
	}

	@Nullable
	private Slot findSlot(double pMouseX, double pMouseY) {
		for (int i = 0; i < this.menu.slots.size(); ++i) {
			Slot slot = this.menu.slots.get(i);
			if (this.isHovering(slot, pMouseX, pMouseY) && slot.isActive()) {
				return slot;
			}
		}

		return null;
	}

	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		if (super.mouseClicked(pMouseX, pMouseY, pButton)) {
			return true;
		} else {
			InputMappings.Input mouseKey = InputMappings.Type.MOUSE.getOrCreate(pButton);
			boolean flag = this.minecraft.options.keyPickItem.isActiveAndMatches(mouseKey);
			Slot slot = this.findSlot(pMouseX, pMouseY);
			long i = Util.getMillis();
			this.doubleclick = this.lastClickSlot == slot && i - this.lastClickTime < 250L
					&& this.lastClickButton == pButton;
			this.skipNextRelease = false;
			if (pButton != 0 && pButton != 1 && !flag) {
				this.checkHotbarMouseClicked(pButton);
			} else {
				int j = this.leftPos;
				int k = this.topPos;
				boolean flag1 = this.hasClickedOutside(pMouseX, pMouseY, j, k, pButton);
				if (slot != null)
					flag1 = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
				int l = -1;
				if (slot != null) {
					l = slot.index;
				}

				if (flag1) {
					l = -999;
				}

				if (this.minecraft.options.touchscreen && flag1
						&& this.minecraft.player.inventory.getCarried().isEmpty()) {
					this.minecraft.setScreen((Screen) null);
					return true;
				}

				if (l != -1) {
					if (this.minecraft.options.touchscreen) {
						if (slot != null && slot.hasItem()) {
							this.clickedSlot = slot;
							this.draggingItem = ItemStack.EMPTY;
							this.isSplittingStack = pButton == 1;
						} else {
							this.clickedSlot = null;
						}
					} else if (!this.isQuickCrafting) {
						if (this.minecraft.player.inventory.getCarried().isEmpty()) {
							if (this.minecraft.options.keyPickItem.isActiveAndMatches(mouseKey)) {
								this.slotClicked(slot, l, pButton, ClickType.CLONE);
							} else {
								boolean flag2 = l != -999 && (InputMappings
										.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340)
										|| InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(),
												344));
								ClickType clicktype = ClickType.PICKUP;
								if (flag2) {
									this.lastQuickMoved = slot != null && slot.hasItem() ? slot.getItem().copy()
											: ItemStack.EMPTY;
									clicktype = ClickType.QUICK_MOVE;
								} else if (l == -999) {
									clicktype = ClickType.THROW;
								}

								this.slotClicked(slot, l, pButton, clicktype);
							}

							this.skipNextRelease = true;
						} else {
							this.isQuickCrafting = true;
							this.quickCraftingButton = pButton;
							this.quickCraftSlots.clear();
							if (pButton == 0) {
								this.quickCraftingType = 0;
							} else if (pButton == 1) {
								this.quickCraftingType = 1;
							} else if (this.minecraft.options.keyPickItem.isActiveAndMatches(mouseKey)) {
								this.quickCraftingType = 2;
							}
						}
					}
				}
			}

			this.lastClickSlot = slot;
			this.lastClickTime = i;
			this.lastClickButton = pButton;
			return true;
		}
	}

	private void checkHotbarMouseClicked(int pKeyCode) {
		if (this.hoveredSlot != null && this.minecraft.player.inventory.getCarried().isEmpty()) {
			if (this.minecraft.options.keySwapOffhand.matchesMouse(pKeyCode)) {
				this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 40, ClickType.SWAP);
				return;
			}

			for (int i = 0; i < 9; ++i) {
				if (this.minecraft.options.keyHotbarSlots[i].matchesMouse(pKeyCode)) {
					this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, i, ClickType.SWAP);
				}
			}
		}

	}

	protected boolean hasClickedOutside(double pMouseX, double pMouseY, int pGuiLeft, int pGuiTop, int pMouseButton) {
		return pMouseX < (double) pGuiLeft || pMouseY < (double) pGuiTop
				|| pMouseX >= (double) (pGuiLeft + this.imageWidth) || pMouseY >= (double) (pGuiTop + this.imageHeight);
	}

	public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
		Slot slot = this.findSlot(pMouseX, pMouseY);
		ItemStack itemstack = this.minecraft.player.inventory.getCarried();
		if (this.clickedSlot != null && this.minecraft.options.touchscreen) {
			if (pButton == 0 || pButton == 1) {
				if (this.draggingItem.isEmpty()) {
					if (slot != this.clickedSlot && !this.clickedSlot.getItem().isEmpty()) {
						this.draggingItem = this.clickedSlot.getItem().copy();
					}
				} else if (this.draggingItem.getCount() > 1 && slot != null
						&& Container.canItemQuickReplace(slot, this.draggingItem, false)) {
					long i = Util.getMillis();
					if (this.quickdropSlot == slot) {
						if (i - this.quickdropTime > 500L) {
							this.slotClicked(this.clickedSlot, this.clickedSlot.index, 0, ClickType.PICKUP);
							this.slotClicked(slot, slot.index, 1, ClickType.PICKUP);
							this.slotClicked(this.clickedSlot, this.clickedSlot.index, 0, ClickType.PICKUP);
							this.quickdropTime = i + 750L;
							this.draggingItem.shrink(1);
						}
					} else {
						this.quickdropSlot = slot;
						this.quickdropTime = i;
					}
				}
			}
		} else if (this.isQuickCrafting && slot != null && !itemstack.isEmpty()
				&& (itemstack.getCount() > this.quickCraftSlots.size() || this.quickCraftingType == 2)
				&& Container.canItemQuickReplace(slot, itemstack, true) && slot.mayPlace(itemstack)
				&& this.menu.canDragTo(slot)) {
			this.quickCraftSlots.add(slot);
			this.recalculateQuickCraftRemaining();
		}

		return true;
	}

	public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
		super.mouseReleased(pMouseX, pMouseY, pButton); // Forge, Call parent to release buttons
		Slot slot = this.findSlot(pMouseX, pMouseY);
		int i = this.leftPos;
		int j = this.topPos;
		boolean flag = this.hasClickedOutside(pMouseX, pMouseY, i, j, pButton);
		if (slot != null)
			flag = false; // Forge, prevent dropping of items through slots outside of GUI boundaries
		InputMappings.Input mouseKey = InputMappings.Type.MOUSE.getOrCreate(pButton);
		int k = -1;
		if (slot != null) {
			k = slot.index;
		}

		if (flag) {
			k = -999;
		}

		if (this.doubleclick && slot != null && pButton == 0
				&& this.menu.canTakeItemForPickAll(ItemStack.EMPTY, slot)) {
			if (hasShiftDown()) {
				if (!this.lastQuickMoved.isEmpty()) {
					for (Slot slot2 : this.menu.slots) {
						if (slot2 != null && slot2.mayPickup(this.minecraft.player) && slot2.hasItem()
								&& slot2.isSameInventory(slot)
								&& Container.canItemQuickReplace(slot2, this.lastQuickMoved, true)) {
							this.slotClicked(slot2, slot2.index, pButton, ClickType.QUICK_MOVE);
						}
					}
				}
			} else {
				this.slotClicked(slot, k, pButton, ClickType.PICKUP_ALL);
			}

			this.doubleclick = false;
			this.lastClickTime = 0L;
		} else {
			if (this.isQuickCrafting && this.quickCraftingButton != pButton) {
				this.isQuickCrafting = false;
				this.quickCraftSlots.clear();
				this.skipNextRelease = true;
				return true;
			}

			if (this.skipNextRelease) {
				this.skipNextRelease = false;
				return true;
			}

			if (this.clickedSlot != null && this.minecraft.options.touchscreen) {
				if (pButton == 0 || pButton == 1) {
					if (this.draggingItem.isEmpty() && slot != this.clickedSlot) {
						this.draggingItem = this.clickedSlot.getItem();
					}

					boolean flag2 = Container.canItemQuickReplace(slot, this.draggingItem, false);
					if (k != -1 && !this.draggingItem.isEmpty() && flag2) {
						this.slotClicked(this.clickedSlot, this.clickedSlot.index, pButton, ClickType.PICKUP);
						this.slotClicked(slot, k, 0, ClickType.PICKUP);
						if (this.minecraft.player.inventory.getCarried().isEmpty()) {
							this.snapbackItem = ItemStack.EMPTY;
						} else {
							this.slotClicked(this.clickedSlot, this.clickedSlot.index, pButton, ClickType.PICKUP);
							this.snapbackStartX = MathHelper.floor(pMouseX - (double) i);
							this.snapbackStartY = MathHelper.floor(pMouseY - (double) j);
							this.snapbackEnd = this.clickedSlot;
							this.snapbackItem = this.draggingItem;
							this.snapbackTime = Util.getMillis();
						}
					} else if (!this.draggingItem.isEmpty()) {
						this.snapbackStartX = MathHelper.floor(pMouseX - (double) i);
						this.snapbackStartY = MathHelper.floor(pMouseY - (double) j);
						this.snapbackEnd = this.clickedSlot;
						this.snapbackItem = this.draggingItem;
						this.snapbackTime = Util.getMillis();
					}

					this.draggingItem = ItemStack.EMPTY;
					this.clickedSlot = null;
				}
			} else if (this.isQuickCrafting && !this.quickCraftSlots.isEmpty()) {
				this.slotClicked((Slot) null, -999, Container.getQuickcraftMask(0, this.quickCraftingType),
						ClickType.QUICK_CRAFT);

				for (Slot slot1 : this.quickCraftSlots) {
					this.slotClicked(slot1, slot1.index, Container.getQuickcraftMask(1, this.quickCraftingType),
							ClickType.QUICK_CRAFT);
				}

				this.slotClicked((Slot) null, -999, Container.getQuickcraftMask(2, this.quickCraftingType),
						ClickType.QUICK_CRAFT);
			} else if (!this.minecraft.player.inventory.getCarried().isEmpty()) {
				if (this.minecraft.options.keyPickItem.isActiveAndMatches(mouseKey)) {
					this.slotClicked(slot, k, pButton, ClickType.CLONE);
				} else {
					boolean flag1 = k != -999
							&& (InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 340)
									|| InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), 344));
					if (flag1) {
						this.lastQuickMoved = slot != null && slot.hasItem() ? slot.getItem().copy() : ItemStack.EMPTY;
					}

					this.slotClicked(slot, k, pButton, flag1 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
				}
			}
		}

		if (this.minecraft.player.inventory.getCarried().isEmpty()) {
			this.lastClickTime = 0L;
		}

		this.isQuickCrafting = false;
		return true;
	}

	private boolean isHovering(Slot pSlot, double pMouseX, double pMouseY) {
		return this.isHovering(pSlot.x, pSlot.y, 16, 16, pMouseX, pMouseY);
	}

	protected boolean isHovering(int pX, int pY, int pWidth, int pHeight, double pMouseX, double pMouseY) {
		int i = this.leftPos;
		int j = this.topPos;
		pMouseX = pMouseX - (double) i;
		pMouseY = pMouseY - (double) j;
		return pMouseX >= (double) (pX - 1) && pMouseX < (double) (pX + pWidth + 1) && pMouseY >= (double) (pY - 1)
				&& pMouseY < (double) (pY + pHeight + 1);
	}

	/**
	 * Called when the mouse is clicked over a slot or outside the gui.
	 */
	protected void slotClicked(Slot pSlot, int pSlotId, int pMouseButton, ClickType pType) {
		if (pSlot != null) {
			pSlotId = pSlot.index;
		}

		this.minecraft.gameMode.handleInventoryMouseClick(this.menu.containerId, pSlotId, pMouseButton, pType,
				this.minecraft.player);
	}

	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		InputMappings.Input mouseKey = InputMappings.getKey(pKeyCode, pScanCode);
		if (super.keyPressed(pKeyCode, pScanCode, pModifiers)) {
			return true;
		} else if (this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
			this.onClose();
			return true;
		} else {
			boolean handled = this.checkHotbarKeyPressed(pKeyCode, pScanCode);// Forge MC-146650: Needs to return true
																				// when the key is handled
			if (this.hoveredSlot != null && this.hoveredSlot.hasItem()) {
				if (this.minecraft.options.keyPickItem.isActiveAndMatches(mouseKey)) {
					this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 0, ClickType.CLONE);
					handled = true;
				} else if (this.minecraft.options.keyDrop.isActiveAndMatches(mouseKey)) {
					this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, hasControlDown() ? 1 : 0,
							ClickType.THROW);
					handled = true;
				}
			} else if (this.minecraft.options.keyDrop.isActiveAndMatches(mouseKey)) {
				handled = true; // Forge MC-146650: Emulate MC bug, so we don't drop from hotbar when pressing
								// drop without hovering over a item.
			}

			return handled;
		}
	}

	protected boolean checkHotbarKeyPressed(int pKeyCode, int pScanCode) {
		if (this.minecraft.player.inventory.getCarried().isEmpty() && this.hoveredSlot != null) {
			if (this.minecraft.options.keySwapOffhand.isActiveAndMatches(InputMappings.getKey(pKeyCode, pScanCode))) {
				this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, 40, ClickType.SWAP);
				return true;
			}

			for (int i = 0; i < 9; ++i) {
				if (this.minecraft.options.keyHotbarSlots[i]
						.isActiveAndMatches(InputMappings.getKey(pKeyCode, pScanCode))) {
					this.slotClicked(this.hoveredSlot, this.hoveredSlot.index, i, ClickType.SWAP);
					return true;
				}
			}
		}

		return false;
	}

	public void removed() {
		if (this.minecraft.player != null) {
			this.menu.removed(this.minecraft.player);
		}
	}

	public boolean isPauseScreen() {
		return false;
	}

	public void tick() {
		super.tick();
		if (!this.minecraft.player.isAlive() || this.minecraft.player.removed) {
			this.minecraft.player.closeContainer();
		}

	}

	public T getMenu() {
		return this.menu;
	}

	@javax.annotation.Nullable
	public Slot getSlotUnderMouse() {
		return this.hoveredSlot;
	}

	public int getGuiLeft() {
		return leftPos;
	}

	public int getGuiTop() {
		return topPos;
	}

	public int getXSize() {
		return imageWidth;
	}

	public int getYSize() {
		return imageHeight;
	}

	protected int slotColor = -2130706433;

	public int getSlotColor(int index) {
		return slotColor;
	}

	public void onClose() {
		this.minecraft.player.closeContainer();
		super.onClose();
	}
}
