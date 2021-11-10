package com.cy4.machina.api.client.gui;

public interface IGui {
	
	default int getLeft() {
        if (this instanceof BaseScreen<?, ?>) {
            return ((BaseScreen<?, ?>) this).getGuiLeft();
        }
        return 0;
    }

    default int getTop() {
        if (this instanceof BaseScreen<?, ?>) {
            return ((BaseScreen<?, ?>) this).getGuiTop();
        }
        return 0;
    }

    default int getWidth() {
        if (this instanceof BaseScreen<?, ?>) {
            return ((BaseScreen<?, ?>) this).getXSize();
        }
        return 0;
    }
    
    default int getHeight() {
    	if (this instanceof BaseScreen<?, ?>) {
    		return ((BaseScreen<?, ?>) this).getYSize();
    	}
    	return 0;
    }

}
