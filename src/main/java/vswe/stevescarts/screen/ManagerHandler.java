package vswe.stevescarts.screen;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import io.github.cottonmc.cotton.gui.networking.NetworkSide;
import io.github.cottonmc.cotton.gui.networking.ScreenNetworking;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import vswe.stevescarts.StevesCarts;
import vswe.stevescarts.util.TextHelper;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Abstract base handler for Manager GUIs (Cargo Manager, Liquid Manager).
 * Provides common functionality for transfer direction, colors, return settings.
 */
public abstract class ManagerHandler extends SyncedGuiDescription {
    private static final Identifier PACKET_TOGGLE_DIRECTION = StevesCarts.id("manager_direction");
    private static final Identifier PACKET_CHANGE_COLOR = StevesCarts.id("manager_color");
    private static final Identifier PACKET_CHANGE_AMOUNT = StevesCarts.id("manager_amount");
    private static final Identifier PACKET_TOGGLE_RETURN = StevesCarts.id("manager_return");
    private static final Identifier PACKET_CHANGE_LAYOUT = StevesCarts.id("manager_layout");

    // Transfer settings for 4 slots
    protected final boolean[] toCart = {true, true, false, false};
    protected final int[] color = {1, 2, 3, 4}; // 1-4 = colors, 5 = disabled
    protected final boolean[] doReturn = {false, false, false, false};
    protected int layoutType = 0;
    protected int lastSetting = -1;
    protected int moveProgress = 0;

    protected ManagerHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, 
                            Inventory blockInventory, PropertyDelegate propertyDelegate) {
        super(type, syncId, playerInventory, blockInventory, propertyDelegate);
    }

    /**
     * Sets up the common network handlers for manager GUIs.
     */
    protected void setupManagerNetworkHandlers(BlockEntity blockEntity) {
        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_TOGGLE_DIRECTION, buf -> {
            int id = buf.readByte();
            if (id >= 0 && id < 4) {
                toCart[id] = !toCart[id];
                blockEntity.markDirty();
            }
        });

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_CHANGE_COLOR, buf -> {
            int id = buf.readByte();
            boolean rightClick = buf.readBoolean();
            if (id >= 0 && id < 4) {
                color[id] = rightClick ? (color[id] == 1 ? 5 : color[id] - 1) : (color[id] == 5 ? 1 : color[id] + 1);
                blockEntity.markDirty();
            }
        });

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_TOGGLE_RETURN, buf -> {
            int id = buf.readByte();
            if (id >= 0 && id < 4 && color[id] != 5) {
                doReturn[color[id] - 1] = !doReturn[color[id] - 1];
                blockEntity.markDirty();
            }
        });

        ScreenNetworking.of(this, NetworkSide.SERVER).receive(PACKET_CHANGE_LAYOUT, buf -> {
            layoutType = (layoutType + 1) % 3;
            blockEntity.markDirty();
        });
    }

    /**
     * Adds a direction toggle button to the panel.
     */
    protected WButton addDirectionButton(WPlainPanel panel, int id, int x, int y) {
        WButton button = new WButton(TextHelper.literal(toCart[id] ? "→" : "←"));
        button.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_TOGGLE_DIRECTION, buf -> {
                buf.writeByte(id);
            });
        });
        panel.add(button, x, y, 28, 28);
        return button;
    }

    /**
     * Adds a color picker button to the panel.
     */
    protected WButton addColorButton(WPlainPanel panel, int id, int x, int y) {
        WButton button = new WButton(getColorText(color[id]));
        button.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_CHANGE_COLOR, buf -> {
                buf.writeByte(id);
                buf.writeBoolean(false);
            });
        });
        panel.add(button, x, y, 16, 16);
        return button;
    }

    /**
     * Adds a return toggle button to the panel.
     */
    protected WButton addReturnButton(WPlainPanel panel, int id, int x, int y) {
        WButton button = new WButton(TextHelper.literal(doReturn[id] ? "R" : "-"));
        button.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_TOGGLE_RETURN, buf -> {
                buf.writeByte(id);
            });
        });
        panel.add(button, x, y, 16, 16);
        return button;
    }

    /**
     * Adds a layout toggle button to the panel.
     */
    protected WButton addLayoutButton(WPlainPanel panel, int x, int y, int width, int height) {
        WButton button = new WButton(TextHelper.translatable(getLayoutKey()));
        button.setOnClick(() -> {
            ScreenNetworking.of(this, NetworkSide.CLIENT).send(PACKET_CHANGE_LAYOUT, buf -> {});
        });
        panel.add(button, x, y, width, height);
        return button;
    }

    protected WLabel getColorLabel(int colorId) {
        String colorName = switch (colorId) {
            case 1 -> "§cR"; // Red
            case 2 -> "§9B"; // Blue
            case 3 -> "§eY"; // Yellow
            case 4 -> "§aG"; // Green
            default -> "§7-"; // Disabled
        };
        return new WLabel(TextHelper.literal(colorName));
    }

    /**
     * Returns the color text for a button.
     */
    protected Text getColorText(int colorId) {
        String colorName = switch (colorId) {
            case 1 -> "§cR"; // Red
            case 2 -> "§9B"; // Blue
            case 3 -> "§eY"; // Yellow
            case 4 -> "§aG"; // Green
            default -> "§7-"; // Disabled
        };
        return TextHelper.literal(colorName);
    }

    protected String getColorName(int colorId) {
        return switch (colorId) {
            case 1 -> "Red";
            case 2 -> "Blue";
            case 3 -> "Yellow";
            case 4 -> "Green";
            default -> "Disabled";
        };
    }

    // Abstract methods to be implemented by subclasses
    protected abstract String getLayoutKey();
    protected abstract String getLayoutOption(int layoutType);
    protected abstract String getMaxSizeText(int id);
    protected abstract String getMaxSizeOverlay(int id);

    // Getters
    public boolean isToCart(int id) { return toCart[id]; }
    public int getColor(int id) { return color[id]; }
    public boolean getDoReturn(int colorId) { return doReturn[colorId]; }
    public int getLayoutType() { return layoutType; }
    public int getLastSetting() { return lastSetting; }
    
    public int moveProgressScaled(int scale) {
        return moveProgress * scale / 100;
    }
}
