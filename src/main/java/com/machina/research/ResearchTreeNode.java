package com.machina.research;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import net.minecraft.util.math.vector.Vector2f;

public class ResearchTreeNode {
	private final Research research;
	private final ResearchTreeNode parent;
	private final ResearchTreeNode previousSibling;
	private final int childIndex;
	private final List<ResearchTreeNode> children = Lists.newArrayList();
	private ResearchTreeNode ancestor;
	private ResearchTreeNode thread;
	private int x;
	private float y;
	private float mod;
	private float change;
	private float shift;

	public ResearchTreeNode(Research pResearch, @Nullable ResearchTreeNode pParent,
			@Nullable ResearchTreeNode pPreviousSibling, int pChildIndex, int pX) {
		if (pResearch.getLocation() == null) {
			throw new IllegalArgumentException("Can't position an invisible research!");
		} else {
			this.research = pResearch;
			this.parent = pParent;
			this.previousSibling = pPreviousSibling;
			this.childIndex = pChildIndex;
			this.ancestor = this;
			this.x = pX;
			this.y = -1.0F;
			ResearchTreeNode researchtreenode = null;

			for (Research research : pResearch.getChildren()) {
				researchtreenode = this.addChild(research, researchtreenode);
			}

		}
	}

	@Nullable
	private ResearchTreeNode addChild(Research pResearch, @Nullable ResearchTreeNode pPrevious) {
		if (pResearch.getLocation() != null) {
			pPrevious = new ResearchTreeNode(pResearch, this, pPrevious, this.children.size() + 1, this.x + 1);
			this.children.add(pPrevious);
		} else {
			for (Research research : pResearch.getChildren()) {
				pPrevious = this.addChild(research, pPrevious);
			}
		}

		return pPrevious;
	}

	private void firstWalk() {
		if (this.children.isEmpty()) {
			if (this.previousSibling != null) {
				this.y = this.previousSibling.y + 1.0F;
			} else {
				this.y = 0.0F;
			}

		} else {
			ResearchTreeNode researchtreenode = null;

			for (ResearchTreeNode researchtreenode1 : this.children) {
				researchtreenode1.firstWalk();
				researchtreenode = researchtreenode1
						.apportion(researchtreenode == null ? researchtreenode1 : researchtreenode);
			}

			this.executeShifts();
			float f = ((this.children.get(0)).y + (this.children.get(this.children.size() - 1)).y) / 2.0F;
			if (this.previousSibling != null) {
				this.y = this.previousSibling.y + 1.0F;
				this.mod = this.y - f;
			} else {
				this.y = f;
			}

		}
	}

	private float secondWalk(float pOffsetY, int pColumnX, float pSubtreeTopY) {
		this.y += pOffsetY;
		this.x = pColumnX;
		if (this.y < pSubtreeTopY) {
			pSubtreeTopY = this.y;
		}

		for (ResearchTreeNode researchtreenode : this.children) {
			pSubtreeTopY = researchtreenode.secondWalk(pOffsetY + this.mod, pColumnX + 1, pSubtreeTopY);
		}

		return pSubtreeTopY;
	}

	private void thirdWalk(float pY) {
		this.y += pY;

		for (ResearchTreeNode researchtreenode : this.children) {
			researchtreenode.thirdWalk(pY);
		}

	}

	private void executeShifts() {
		float f = 0.0F;
		float f1 = 0.0F;

		for (int i = this.children.size() - 1; i >= 0; --i) {
			ResearchTreeNode researchtreenode = this.children.get(i);
			researchtreenode.y += f;
			researchtreenode.mod += f;
			f1 += researchtreenode.change;
			f += researchtreenode.shift + f1;
		}

	}

	@Nullable
	private ResearchTreeNode previousOrThread() {
		if (this.thread != null) {
			return this.thread;
		} else {
			return !this.children.isEmpty() ? this.children.get(0) : null;
		}
	}

	@Nullable
	private ResearchTreeNode nextOrThread() {
		if (this.thread != null) {
			return this.thread;
		} else {
			return !this.children.isEmpty() ? this.children.get(this.children.size() - 1) : null;
		}
	}

	private ResearchTreeNode apportion(ResearchTreeNode pNode) {
		if (this.previousSibling == null) {
			return pNode;
		} else {
			ResearchTreeNode researchtreenode = this;
			ResearchTreeNode researchtreenode1 = this;
			ResearchTreeNode researchtreenode2 = this.previousSibling;
			ResearchTreeNode researchtreenode3 = this.parent.children.get(0);
			float f = this.mod;
			float f1 = this.mod;
			float f2 = researchtreenode2.mod;

			float f3;
			for (f3 = researchtreenode3.mod; researchtreenode2.nextOrThread() != null
					&& researchtreenode.previousOrThread() != null; f1 += researchtreenode1.mod) {
				researchtreenode2 = researchtreenode2.nextOrThread();
				researchtreenode = researchtreenode.previousOrThread();
				researchtreenode3 = researchtreenode3.previousOrThread();
				researchtreenode1 = researchtreenode1.nextOrThread();
				researchtreenode1.ancestor = this;
				float f4 = researchtreenode2.y + f2 - (researchtreenode.y + f) + 1.0F;
				if (f4 > 0.0F) {
					researchtreenode2.getAncestor(this, pNode).moveSubtree(this, f4);
					f += f4;
					f1 += f4;
				}

				f2 += researchtreenode2.mod;
				f += researchtreenode.mod;
				f3 += researchtreenode3.mod;
			}

			if (researchtreenode2.nextOrThread() != null && researchtreenode1.nextOrThread() == null) {
				researchtreenode1.thread = researchtreenode2.nextOrThread();
				researchtreenode1.mod += f2 - f1;
			} else {
				if (researchtreenode.previousOrThread() != null && researchtreenode3.previousOrThread() == null) {
					researchtreenode3.thread = researchtreenode.previousOrThread();
					researchtreenode3.mod += f - f3;
				}

				pNode = this;
			}

			return pNode;
		}
	}

	private void moveSubtree(ResearchTreeNode pNode, float pShift) {
		float f = (float) (pNode.childIndex - this.childIndex);
		if (f != 0.0F) {
			pNode.change -= pShift / f;
			this.change += pShift / f;
		}

		pNode.shift += pShift;
		pNode.y += pShift;
		pNode.mod += pShift;
	}

	private ResearchTreeNode getAncestor(ResearchTreeNode pSelf, ResearchTreeNode pOther) {
		return this.ancestor != null && pSelf.parent.children.contains(this.ancestor) ? this.ancestor : pOther;
	}

	private void finalizePosition() {
		if (this.research.getLocation() != null) {
			this.research.setLocation(new Vector2f((float) this.x, this.y));
		}

		if (!this.children.isEmpty()) {
			for (ResearchTreeNode researchtreenode : this.children) {
				researchtreenode.finalizePosition();
			}
		}

	}

	public static void run(Research pRoot) {
		if (pRoot.getLocation() == null) {
			throw new IllegalArgumentException("Can't position children of an invisible root!");
		} else {
			ResearchTreeNode researchtreenode = new ResearchTreeNode(pRoot, (ResearchTreeNode) null,
					(ResearchTreeNode) null, 1, 0);
			researchtreenode.firstWalk();
			float f = researchtreenode.secondWalk(0.0F, 0, researchtreenode.y);
			if (f < 0.0F) {
				researchtreenode.thirdWalk(-f);
			}

			researchtreenode.finalizePosition();
		}
	}
}
