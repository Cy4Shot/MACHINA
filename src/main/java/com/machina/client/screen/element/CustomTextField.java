package com.machina.client.screen.element;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import com.machina.client.util.UIHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomTextField extends Widget implements IRenderable, IGuiEventListener {
	private final FontRenderer font;
	private String value = "";
	private int maxLength = 32;
	private int frame;
	private boolean bordered = true;
	private boolean canLoseFocus = true;
	private boolean isEditable = true;
	private boolean shiftPressed;
	private int displayPos;
	private int cursorPos;
	private int highlightPos;
	private int textColor = 0xFF_00fefe;
	private int textColorBorder = 0xFF_0e0e0e;
	private String suggestion;
	private Consumer<String> responder;
	private Consumer<String> complete;
	private Predicate<String> filter = Objects::nonNull;

	public CustomTextField(FontRenderer pFont, int pX, int pY, int pWidth, int pHeight, ITextComponent pMessage) {
		this(pFont, pX, pY, pWidth, pHeight, (TextFieldWidget) null, pMessage);
	}

	public CustomTextField(FontRenderer pFont, int pX, int pY, int pWidth, int pHeight,
			@Nullable TextFieldWidget p_i232259_6_, ITextComponent pMessage) {
		super(pX, pY, pWidth, pHeight, pMessage);
		this.font = pFont;
		if (p_i232259_6_ != null) {
			this.setValue(p_i232259_6_.getValue());
		}

	}

	public void setResponder(Consumer<String> pResponder) {
		this.responder = pResponder;
	}

	public void setCompletion(Consumer<String> completion) {
		this.complete = completion;
	}

	public void tick() {
		++this.frame;
	}

	protected IFormattableTextComponent createNarrationMessage() {
		ITextComponent itextcomponent = this.getMessage();
		return new TranslationTextComponent("gui.narrate.editBox", itextcomponent, this.value);
	}

	public void setValue(String pText) {
		if (this.filter.test(pText)) {
			if (pText.length() > this.maxLength) {
				this.value = pText.substring(0, this.maxLength);
			} else {
				this.value = pText;
			}

			this.moveCursorToEnd();
			this.setHighlightPos(this.cursorPos);
			this.onValueChange(pText);
		}
	}

	public String getValue() {
		return this.value;
	}

	public String getHighlighted() {
		int i = this.cursorPos < this.highlightPos ? this.cursorPos : this.highlightPos;
		int j = this.cursorPos < this.highlightPos ? this.highlightPos : this.cursorPos;
		return this.value.substring(i, j);
	}

	public void setFilter(Predicate<String> pValidator) {
		this.filter = pValidator;
	}

	public void insertText(String pTextToWrite) {
		int i = this.cursorPos < this.highlightPos ? this.cursorPos : this.highlightPos;
		int j = this.cursorPos < this.highlightPos ? this.highlightPos : this.cursorPos;
		int k = this.maxLength - this.value.length() - (i - j);
		String s = SharedConstants.filterText(pTextToWrite);
		int l = s.length();
		if (k < l) {
			s = s.substring(0, k);
			l = k;
		}

		String s1 = (new StringBuilder(this.value)).replace(i, j, s).toString();
		if (this.filter.test(s1)) {
			this.value = s1;
			this.setCursorPosition(i + l);
			this.setHighlightPos(this.cursorPos);
			this.onValueChange(this.value);
		}
	}

	private void onValueChange(String pNewText) {
		if (this.responder != null) {
			this.responder.accept(pNewText);
		}

		this.nextNarration = Util.getMillis() + 500L;
	}

	private void deleteText(int p_212950_1_) {
		if (Screen.hasControlDown()) {
			this.deleteWords(p_212950_1_);
		} else {
			this.deleteChars(p_212950_1_);
		}

	}

	public void deleteWords(int pNum) {
		if (!this.value.isEmpty()) {
			if (this.highlightPos != this.cursorPos) {
				this.insertText("");
			} else {
				this.deleteChars(this.getWordPosition(pNum) - this.cursorPos);
			}
		}
	}

	public void deleteChars(int pNum) {
		if (!this.value.isEmpty()) {
			if (this.highlightPos != this.cursorPos) {
				this.insertText("");
			} else {
				int i = this.getCursorPos(pNum);
				int j = Math.min(i, this.cursorPos);
				int k = Math.max(i, this.cursorPos);
				if (j != k) {
					String s = (new StringBuilder(this.value)).delete(j, k).toString();
					if (this.filter.test(s)) {
						this.value = s;
						this.moveCursorTo(j);
					}
				}
			}
		}
	}

	public int getWordPosition(int pNumWords) {
		return this.getWordPosition(pNumWords, this.getCursorPosition());
	}

	private int getWordPosition(int pN, int pPos) {
		return this.getWordPosition(pN, pPos, true);
	}

	private int getWordPosition(int pN, int pPos, boolean pSkipWs) {
		int i = pPos;
		boolean flag = pN < 0;
		int j = Math.abs(pN);

		for (int k = 0; k < j; ++k) {
			if (!flag) {
				int l = this.value.length();
				i = this.value.indexOf(32, i);
				if (i == -1) {
					i = l;
				} else {
					while (pSkipWs && i < l && this.value.charAt(i) == ' ') {
						++i;
					}
				}
			} else {
				while (pSkipWs && i > 0 && this.value.charAt(i - 1) == ' ') {
					--i;
				}

				while (i > 0 && this.value.charAt(i - 1) != ' ') {
					--i;
				}
			}
		}

		return i;
	}

	public void moveCursor(int pDelta) {
		this.moveCursorTo(this.getCursorPos(pDelta));
	}

	private int getCursorPos(int pDelta) {
		return Util.offsetByCodepoints(this.value, this.cursorPos, pDelta);
	}

	public void moveCursorTo(int pPos) {
		this.setCursorPosition(pPos);
		if (!this.shiftPressed) {
			this.setHighlightPos(this.cursorPos);
		}

		this.onValueChange(this.value);
	}

	public void setCursorPosition(int pPos) {
		this.cursorPos = MathHelper.clamp(pPos, 0, this.value.length());
	}

	public void moveCursorToStart() {
		this.moveCursorTo(0);
	}

	public void moveCursorToEnd() {
		this.moveCursorTo(this.value.length());
	}

	public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
		if (!this.canConsumeInput()) {
			return false;
		} else {
			Minecraft mc = Minecraft.getInstance();
			this.shiftPressed = Screen.hasShiftDown();
			if (Screen.isSelectAll(pKeyCode)) {
				this.moveCursorToEnd();
				this.setHighlightPos(0);
				return true;
			} else if (Screen.isCopy(pKeyCode)) {
				mc.keyboardHandler.setClipboard(this.getHighlighted());
				return true;
			} else if (Screen.isPaste(pKeyCode)) {
				if (this.isEditable) {
					this.insertText(mc.keyboardHandler.getClipboard());
				}

				return true;
			} else if (Screen.isCut(pKeyCode)) {
				mc.keyboardHandler.setClipboard(this.getHighlighted());
				if (this.isEditable) {
					this.insertText("");
				}

				return true;
			} else {
				switch (pKeyCode) {
				case 257:
					if (complete != null)
						complete.accept(this.value);
				case 259:
					if (this.isEditable) {
						this.shiftPressed = false;
						this.deleteText(-1);
						this.shiftPressed = Screen.hasShiftDown();
					}

					return true;
				case 260:
				case 264:
				case 265:
				case 266:
				case 267:
				default:
					return false;
				case 261:
					if (this.isEditable) {
						this.shiftPressed = false;
						this.deleteText(1);
						this.shiftPressed = Screen.hasShiftDown();
					}

					return true;
				case 262:
					if (Screen.hasControlDown()) {
						this.moveCursorTo(this.getWordPosition(1));
					} else {
						this.moveCursor(1);
					}

					return true;
				case 263:
					if (Screen.hasControlDown()) {
						this.moveCursorTo(this.getWordPosition(-1));
					} else {
						this.moveCursor(-1);
					}

					return true;
				case 268:
					this.moveCursorToStart();
					return true;
				case 269:
					this.moveCursorToEnd();
					return true;
				}
			}
		}
	}

	public boolean canConsumeInput() {
		return this.isVisible() && this.isFocused() && this.isEditable();
	}

	public boolean charTyped(char pCodePoint, int pModifiers) {
		if (!this.canConsumeInput()) {
			return false;
		} else if (SharedConstants.isAllowedChatCharacter(pCodePoint)) {
			if (this.isEditable) {
				this.insertText(Character.toString(pCodePoint));
			}

			return true;
		} else {
			return false;
		}
	}

	public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
		if (!this.isVisible()) {
			return false;
		} else {
			boolean flag = pMouseX >= (double) this.x && pMouseX < (double) (this.x + this.width)
					&& pMouseY >= (double) this.y && pMouseY < (double) (this.y + this.height);
			if (this.canLoseFocus) {
				this.setFocus(flag);
			}

			if (this.isFocused() && flag && pButton == 0) {
				int i = MathHelper.floor(pMouseX) - this.x;
				if (this.bordered) {
					i -= 4;
				}

				String s = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
				this.moveCursorTo(this.font.plainSubstrByWidth(s, i).length() + this.displayPos);
				return true;
			} else {
				return false;
			}
		}
	}

	public void setFocus(boolean pIsFocused) {
		super.setFocused(pIsFocused);
	}

	public void renderButton(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
		if (this.isVisible()) {
			if (this.isBordered()) {
				int i = this.isFocused() ? -1 : -6250336;
				fill(pMatrixStack, this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, i);
				fill(pMatrixStack, this.x, this.y, this.x + this.width, this.y + this.height, -16777216);
			}

			int j = this.cursorPos - this.displayPos;
			int k = this.highlightPos - this.displayPos;
			String s = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), this.getInnerWidth());
			boolean flag = j >= 0 && j <= s.length();
			boolean flag1 = this.isFocused() && this.frame / 6 % 2 == 0 && flag;
			int l = this.bordered ? this.x + 4 : this.x;
			int i1 = this.bordered ? this.y + (this.height - 8) / 2 : this.y;
			int j1 = l;
			if (k > s.length()) {
				k = s.length();
			}

			if (!s.isEmpty()) {
				String s1 = flag ? s.substring(0, j) : s;
				j1 = UIHelper.drawStringWithBorder(pMatrixStack, s1, (float) l, (float) i1, this.textColor,
						this.textColorBorder);
			}

			boolean flag2 = this.cursorPos < this.value.length() || this.value.length() >= this.getMaxLength();
			int k1 = j1;
			if (!flag) {
				k1 = j > 0 ? l + this.width : l;
			} else if (flag2) {
				k1 = j1 - 1;
				--j1;
			}

			if (!s.isEmpty() && flag && j < s.length()) {
				UIHelper.drawStringWithBorder(pMatrixStack, s.substring(j), (float) j1, (float) i1, this.textColor,
						this.textColorBorder);
			}

			if (!flag2 && this.suggestion != null) {
				UIHelper.drawStringWithBorder(pMatrixStack, this.suggestion, (float) (k1 - 1), (float) i1,
						this.textColor, this.textColorBorder);
			}

			if (flag1) {
				if (flag2) {
					AbstractGui.fill(pMatrixStack, k1, i1 - 1, k1 + 3, i1 + 10, this.textColorBorder);
					AbstractGui.fill(pMatrixStack, k1 + 1, i1, k1 + 2, i1 + 9, this.textColor);
				} else {
					UIHelper.drawStringWithBorder(pMatrixStack, "_", (float) k1, (float) i1, this.textColor,
							this.textColorBorder);
				}
			}

			if (k != j) {
				int l1 = l + this.font.width(s.substring(0, k));
				this.renderHighlight(k1, i1 - 1, l1 - 1, i1 + 1 + 9);
			}

		}
	}

	private void renderHighlight(int pStartX, int pStartY, int pEndX, int pEndY) {
		if (pStartX < pEndX) {
			int i = pStartX;
			pStartX = pEndX;
			pEndX = i;
		}

		if (pStartY < pEndY) {
			int j = pStartY;
			pStartY = pEndY;
			pEndY = j;
		}

		if (pEndX > this.x + this.width) {
			pEndX = this.x + this.width;
		}

		if (pStartX > this.x + this.width) {
			pStartX = this.x + this.width;
		}

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuilder();
		RenderSystem.color4f(0.0F, 0.0F, 255.0F, 255.0F);
		RenderSystem.disableTexture();
		RenderSystem.enableColorLogicOp();
		RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.vertex((double) pStartX, (double) pEndY, 0.0D).endVertex();
		bufferbuilder.vertex((double) pEndX, (double) pEndY, 0.0D).endVertex();
		bufferbuilder.vertex((double) pEndX, (double) pStartY, 0.0D).endVertex();
		bufferbuilder.vertex((double) pStartX, (double) pStartY, 0.0D).endVertex();
		tessellator.end();
		RenderSystem.disableColorLogicOp();
		RenderSystem.enableTexture();
	}

	public void setMaxLength(int pLength) {
		this.maxLength = pLength;
		if (this.value.length() > pLength) {
			this.value = this.value.substring(0, pLength);
			this.onValueChange(this.value);
		}

	}

	private int getMaxLength() {
		return this.maxLength;
	}

	public int getCursorPosition() {
		return this.cursorPos;
	}

	private boolean isBordered() {
		return this.bordered;
	}

	public void setBordered(boolean pEnableBackgroundDrawing) {
		this.bordered = pEnableBackgroundDrawing;
	}

	public void setColor(int tex, int border) {
		this.textColor = tex;
		this.textColorBorder = border;
	}

	public boolean changeFocus(boolean pFocus) {
		return this.visible && this.isEditable ? super.changeFocus(pFocus) : false;
	}

	public boolean isMouseOver(double pMouseX, double pMouseY) {
		return this.visible && pMouseX >= (double) this.x && pMouseX < (double) (this.x + this.width)
				&& pMouseY >= (double) this.y && pMouseY < (double) (this.y + this.height);
	}

	protected void onFocusedChanged(boolean pFocused) {
		if (pFocused) {
			this.frame = 0;
		}

	}

	private boolean isEditable() {
		return this.isEditable;
	}

	public void setEditable(boolean pEnabled) {
		this.isEditable = pEnabled;
	}

	public int getInnerWidth() {
		return this.isBordered() ? this.width - 8 : this.width;
	}

	public void setHighlightPos(int pPosition) {
		int i = this.value.length();
		this.highlightPos = MathHelper.clamp(pPosition, 0, i);
		if (this.font != null) {
			if (this.displayPos > i) {
				this.displayPos = i;
			}

			int j = this.getInnerWidth();
			String s = this.font.plainSubstrByWidth(this.value.substring(this.displayPos), j);
			int k = s.length() + this.displayPos;
			if (this.highlightPos == this.displayPos) {
				this.displayPos -= this.font.plainSubstrByWidth(this.value, j, true).length();
			}

			if (this.highlightPos > k) {
				this.displayPos += this.highlightPos - k;
			} else if (this.highlightPos <= this.displayPos) {
				this.displayPos -= this.displayPos - this.highlightPos;
			}

			this.displayPos = MathHelper.clamp(this.displayPos, 0, i);
		}

	}

	public void setCanLoseFocus(boolean pCanLoseFocus) {
		this.canLoseFocus = pCanLoseFocus;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(boolean pIsVisible) {
		this.visible = pIsVisible;
	}

	public void setSuggestion(@Nullable String pSuggestion) {
		this.suggestion = pSuggestion;
	}

	public int getScreenX(int p_195611_1_) {
		return p_195611_1_ > this.value.length() ? this.x
				: this.x + this.font.width(this.value.substring(0, p_195611_1_));
	}

	public void setX(int pX) {
		this.x = pX;
	}
}
